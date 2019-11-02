import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.BalancingPool;
import scala.concurrent.duration.Duration;

public class RouterActor extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final ActorRef storeActor;
    private final ActorRef testPool;
    private static SupervisorStrategy strategy =
            new OneForOneStrategy(10,
                    Duration.create("1 minute"),
                    DeciderBuilder.
                            matchAny(o -> SupervisorStrategy.escalate()).build());

    public RouterActor() {
        super();
        this.storeActor = getContext().actorOf(StoreActor.props(), "second");
        this.testPool = getContext().actorOf(
                new BalancingPool(5)
                        .withSupervisorStrategy(strategy)
                        .props(TestActor.props()),
                "routerForTests"
        );
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestRequest.class,
                        req -> {
                            for (TestCase testCase: req.getTestCases())
                                testPool.tell(new TestCaseMsg(testCase, req), storeActor);
                        })
                .match(StoreActor.GetResultMessage.class,
                        req -> storeActor.forward(req, getContext()))
                .matchAny(o -> log.info(o.toString() + o.getClass()))
                .build();
    }

    static Props props() {
        return Props.create(RouterActor.class);
    }
}

package Actors;

import Messages.TestResult;
import Messages.TestMetaInfo;
import Messages.TestResultMsg;
import Messages.GetResMsg;
import Messages.ResMsg;
import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.BalancingPool;
import scala.concurrent.duration.Duration;

public class RouterActor extends AbstractActor {
    final private static int TESTING_WORKERS_AM = 5;
    final private static int MAX_NUM_OF_RETRIES = 5;
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final ActorRef storeActor;
    private final ActorRef testPool;
    private static SupervisorStrategy strategy =
            new OneForOneStrategy(MAX_NUM_OF_RETRIES,
                    Duration.create("1 minute"),
                    DeciderBuilder.
                            matchAny(o -> SupervisorStrategy.escalate()).build());

    public RouterActor() {
        super();
        this.storeActor = getContext().actorOf(StoreActor.props(), "second");
        this.testPool = getContext().actorOf(
                new BalancingPool(TESTING_WORKERS_AM)
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
                            for (TestCase testCase : req.getTestCases())
                                testPool.tell(new TestCaseMsg(testCase, new TestMetaInfo(req)), storeActor);
                        })
                .match(GetResMsg.class,
                        req -> storeActor.forward(req, getContext()))
                .matchAny(o -> log.info(o.toString() + o.getClass()))
                .build();
    }

    static Props props() {
        return Props.create(RouterActor.class);
    }
}

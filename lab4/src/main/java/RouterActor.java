import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;

public class RouterActor extends AbstractActor {
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
                new RoundRobinPool(5)
                        .withSupervisorStrategy(strategy)
                        .props(TestActor.props()),
                "routerForTests"
        );
    }


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestActor.ResultMessage.class, req -> {
                    
                    if (!store.containsKey(m.getUUID()))
                        store.put(m.getUUID(), new ArrayList<>());
                    System.out.println("received result: res: " + m.getActualRes() + ", expected:" + m.getExpectedRes().toString());
                    ArrayList<StoreActor.TestResult> res = store.get(m.getUUID());
                    res.add(new StoreActor.TestResult(m));
                })
                .match(StoreActor.GetResultMessage.class, req -> {
                    if (!store.containsKey(req.getUUID())){
                        sender().tell(
                                new StoreActor.GetResultResponse(), self()
                        );
                        return;
                    }
                    System.out.println(store.get(req.getUUID()));
                    sender().tell(
                            new StoreActor.GetResultResponse(store.get(req.getUUID())), self()
                    );
                })
                .build();
    }
}

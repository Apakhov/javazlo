import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class RouterActor extends AbstractActor {
    private final ActorRef storeActor;
    private final ActorRef testPool;

    public RouterActor() {
        super();
        this.storeActor = getContext().actorOf(StoreActor.props(), "second");
        this.testPool = getContext().actorOf(
                new RoundRobinPool(5)
                        .withSupervisorStrategy(strategy)
                        .props(TestActor.p)),
                "routerForTests"
        );
    }

    @Override
    public void preStart() {
        storeActor =
    }


}

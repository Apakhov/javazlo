import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class RouterActor extends AbstractActor {
    private final ActorRef storeActor;

    @Override
    public void preStart() {
        storeActor = getContext().actorOf(StoreActor.props(), "second");
    }

    testPerformerActor = getContext().actorOf(
new RoundRobinPool(5)
.withSupervisorStrategy(strategy)
.props(Props.create(TestPerformerActor.class, logResultsActor)),
            "routerForTests"
            );
}

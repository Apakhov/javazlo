import akka.actor.AbstractActor;

public class RouterActor extends AbstractActor {
    @Override
    public void preStart() {
        getContext().actorOf(StoreActor.props(), "second");
    }

    @Override
    public void postStop() {
        System.out.println("first stopped");
    }
}

import akka.actor.AbstractActor;

public class RouterActor extends AbstractActor {
    @Override
    public void preStart() {
        getContext().actorOf(StoreActor.props(), "second");
    }

    
}

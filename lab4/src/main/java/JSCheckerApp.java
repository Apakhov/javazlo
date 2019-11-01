import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;

import java.util.concurrent.CompletionStage;

public class JSCheckerApp {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("test");
        ActorRef storeActor = system.actorOf(
                Props.create(StoreActor.class)
        );
        storeActor.tell(
                new StoreActor.StoreMessage("test", "test"),
                ActorRef.noSender()
        );
        storeActor.tell(
                new StoreActor.StoreMessage("test1", "test1"),
                ActorRef.noSender()
        );
        storeActor.tell(
                new StoreActor.StoreMessage("test2", "test2"),
                ActorRef.noSender()
        );
        storeActor.tell(
                new StoreActor.StoreMessage("test3", "test3"),
                ActorRef.noSender()
        );
    }
}

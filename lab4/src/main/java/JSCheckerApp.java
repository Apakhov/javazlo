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
        ActorRef testActor = system.actorOf(
                Props.create(TestActor.class)
        );
        testActor.tell(new TestActor.TestMessage(
                "test", "", "",String[]{""}, ""
        ),
                ActorRef.noSender());
    }
}

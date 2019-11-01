import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.pattern.Patterns;
import akka.pattern.PatternsCS;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class JSCheckerApp {
    public static void main(String[] args) throws IOException {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create("routes");
        ActorRef storeActor = system.actorOf(
                Props.create(StoreActor.class)
        );
        Patterns.ask(storeActor, new StoreActor.CreateStoreMessage(2), );
        storeActor.tell(
                new StoreActor.StoreMessage("test", "test"),
                ActorRef.noSender()
        );


//        final Http http = Http.get(system);
//        final ActorMaterializer materializer = ActorMaterializer.create(system);
//
//        //In order to access all directives we need an instance where the routes are define.
//        HttpRouterActor app = new HttpRouterActor();
//
//        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
//        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
//                ConnectHttp.toHost("localhost", 8080), materializer);
//
//        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
//        System.in.read(); // let it run until user presses return
//
//        binding
//                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
//                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }
}

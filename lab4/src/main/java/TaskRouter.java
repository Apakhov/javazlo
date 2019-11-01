import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.util.concurrent.CompletionStage;

public class TaskRouter extends AbstractActor {
    ActorRef storeActor;

    {
        storeActor = getContext().actorOf(
                Props.create(StoreActor.class)
        );
    }

    

    @Override
    public Receive createReceive() {

    }
}
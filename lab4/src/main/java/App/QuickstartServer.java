import Actors.RouterActor;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

//#main-class
public class QuickstartServer extends AllDirectives {

    // set up ActorSystem and other dependencies here
    private final HTTPRoutes userRoutes;

    public QuickstartServer(ActorSystem system, ActorRef routerActor) {
        userRoutes = new HTTPRoutes(system, routerActor);
    }
    //#main-class

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("testServer");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        ActorRef routerActor = system.actorOf(RouterActor.props(), "router");

        QuickstartServer app = new QuickstartServer(system, routerActor);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/");
    }

    protected Route createRoute() {
        return userRoutes.routes();
    }
}
//#main-class



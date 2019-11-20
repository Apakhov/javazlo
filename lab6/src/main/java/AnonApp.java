import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.setup.ActorSystemSetup;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.*;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class AnonApp {
    public static void main(String[] args) throws IOException {
        System.out.println("start!");
        ActorSystem system = ActorSystem.create("routes", ActorSystemSetup.empty());
        final Http http = Http.get(system);
        final ActorMaterializer materializer =
                ActorMaterializer.create(system);
        ActorRef reqConv = system.actorOf(RequestConverter.props(), "reqConv");
        Patterns.ask(reqConv, new ConverterConfig("localhost"), Duration.ofSeconds(5L));

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = Flow.of(HttpRequest.class)
                .map(req -> { //takes out url and count
                    System.out.println("request come:" + req.getUri());
                    String url = req.getUri().query().get("url").orElse("");
                    String rawCount = req.getUri().query().get("count").orElse("1");
                    int count = 0;
                    if (rawCount.matches("-?\\d+")) {
                        count = Integer.parseInt(rawCount);
                    }
                    return new TestRequest(url, count);
                }).mapAsync(1, testRequest -> { //request to actor for url
                    return Patterns
                            .ask(reqConv, testRequest, Duration.ofSeconds(5L))
                            .thenCompose(nextUrl ->
                                    nextUrl == "" ?
                                            CompletableFuture.completedFuture(HttpResponse.create().withStatus(404).withEntity("invalid url")) :
                                            http.singleRequest(HttpRequest.create((String) nextUrl))
                            );
                })
                .map(response -> //processes response from next server
                        response
                );
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer
        );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate()); // and shutdownwhen done
    }
}

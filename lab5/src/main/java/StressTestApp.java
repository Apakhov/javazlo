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
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.*;
import jdk.internal.util.xml.impl.Pair;
import org.asynchttpclient.Request;
import org.asynchttpclient.*;

import static org.asynchttpclient.Dsl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

public class StressTestApp {
    public static void main(String[] args) throws IOException {
        System.out.println("start!");
        ActorSystem system = ActorSystem.create("routes");
        final Http http = Http.get(system);
        final ActorMaterializer materializer =
                ActorMaterializer.create(system);
        ActorRef store = system.actorOf(StoreActor.props());
        AsyncHttpClient httpClient = asyncHttpClient();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = Flow.of(HttpRequest.class)
                .map(req -> {
                    String url = req.getUri().query().get("testURL").orElse("");
                    String rawCount = req.getUri().query().get("count").orElse("1");
                    int count = 1;
                    if (rawCount.matches("-?\\d+")) {
                        count = Integer.parseInt(rawCount);
                    }
                    return new TestRequest(url, count);
                }).mapAsync(1, p -> {
                    CompletionStage<Object> result = Patterns
                            .ask(routerActor, new GetResMsg(uuid), timeout);

                    Flow<TestRequest, Long, NotUsed> flow = Flow.<TestRequest>create()
                            .mapConcat(t -> {
                                List<String> myList = new ArrayList<>();
                                for (int i = 0; i < t.count; i++) {
                                    myList.add(p.url);
                                }
                                return myList;
                            })
                            .mapAsync(1, url -> {
                                long start = System.nanoTime();
                                return  httpClient
                                        .prepareGet(url)
                                        .execute()
                                        .toCompletableFuture()
                                        .thenCompose(response ->
                                                CompletableFuture.completedFuture(System.nanoTime() - start));
                            });
                    Sink<Long, CompletionStage<Long>> fold = Sink.fold(0L, Long::sum);
                    Sink<TestRequest, CompletionStage<Long>> sink = flow.toMat(fold, Keep.right());
                    Source<TestRequest, NotUsed> source = Source.from(Collections.singletonList(p));
                    RunnableGraph<CompletionStage<Long>> r = source.toMat(sink, Keep.right());
                    return r.run(materializer);
                }).map(l -> {
                    return HttpResponse.create().withStatus(200).withEntity(l.toString());
                });
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

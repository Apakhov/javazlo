import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
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
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = Flow.of(HttpRequest.class)
                .map(req -> {
                    String url = req.getUri().query().get("testURL").orElse("");
                    String rawCount = req.getUri().query().get("count").orElse("1");
                    int count = 1;
                    if (rawCount.matches("-?\\d+")){
                        count = Integer.parseInt(rawCount);
                    }

                    return new TestRequest(url,count);
                }).mapAsync(1, p ->
                     Source.from(Collections.singletonList(p))
                            .toMat(Flow.<TestRequest>create()
                                    .mapConcat(t -> {
                                        List<String> myList = new ArrayList<>();
                                        for (int i = 0; i < t.count; i++) {
                                            myList.add(p.url);
                                        }
                                        return myList;
                                    })
                                    .mapAsync(1, url -> {
                                        AsyncHttpClient httpClient = asyncHttpClient();
                                        long start = System.nanoTime();
                                        CompletableFuture<Long> f = httpClient
                                                .prepareGet(url)
                                                .execute()
                                                .toCompletableFuture()
                                                .thenCompose(response ->
                                                        CompletableFuture.completedFuture(System.nanoTime() - start));
                                        return f;
                                    }).toMat(Sink.fold( 0, (agg, next) -> agg + next
                                            , Keep.right() )), Keep.right()).run(materializer)
                ).map();
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

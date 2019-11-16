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
import akka.japi.Pair;
import org.asynchttpclient.Request;
import org.asynchttpclient.*;

import static org.asynchttpclient.Dsl.*;

import java.io.IOException;
import java.time.Duration;
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


        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = Flow.of(HttpRequest.class)
                .map(req -> {
                    String url = req.getUri().query().get("testURL").orElse("");
                    String rawCount = req.getUri().query().get("count").orElse("1");
                    int count = 1;
                    if (rawCount.matches("-?\\d+")) {
                        count = Integer.parseInt(rawCount);
                    }
                    return new TestRequest(url, count);
                }).mapAsync(1, testRequest -> {
                    CompletionStage<Object> result = Patterns
                            .ask(store, testRequest, Duration.ofSeconds(5L));

                    return result.thenCompose(v -> {
                        StoreResp resp = (StoreResp) v;
                        if (resp.hasInfo) {
                            return CompletableFuture.completedFuture(new TestResult(testRequest, resp.timing));
                        }
                        Source<TestRequest, NotUsed> source = Source.from(Collections.singletonList(testRequest));
                        RunnableGraph<CompletionStage<TestResult>> r = source.toMat(testSink, Keep.right());
                        return r.run(materializer);
                    });
                }).map(l -> {
                    store.tell(l, ActorRef.noSender());
                    return HttpResponse.create().withStatus(200).withEntity(l + " ns");
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

    static final AsyncHttpClient httpClient = asyncHttpClient();

    static final Sink<TestRequest, CompletionStage<TestResult>> testSink = Flow.<TestRequest>create()
            .mapConcat(t -> {
                List<TestRequest> myList = new ArrayList<>();
                for (int i = 0; i < t.count; i++) {
                    myList.add(t);
                }
                return myList;
            })
            .mapAsync(1, request -> {
                long start = System.nanoTime();
                return httpClient
                        .prepareGet(request.url)
                        .execute()
                        .toCompletableFuture()
                        .thenCompose(response ->
                                CompletableFuture.completedFuture( new Pair<TestRequest, Long>(request, System.nanoTime() - start)));
            }).toMat(Sink.fold(new Pair<TestRequest, Long>(new TestRequest("", 0), 0L), (list, p) -> {
                return list;
            }), Keep.right());
}

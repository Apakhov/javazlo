package App;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import Messages.GetResMsg;
import Messages.ResMsg;
import Messages.TestRequest;
import Messages.TestResponse;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class HTTPRoutes extends AllDirectives {
    final private static String SUBMIT_PREF = "submit";
    final private static String UUID_PARAM = "uuid";

    final private LoggingAdapter log;
    final private ActorRef routerActor;


    public HTTPRoutes(ActorSystem system, ActorRef routerActor) {
        this.routerActor = routerActor;
        log = Logging.getLogger(system, this);
    }


    private final static Duration timeout = Duration.ofSeconds(5l);


    public Route routes() {
        return concat(
                getResult(),
                pathPrefix(SUBMIT_PREF, this::submitTests)
        );
    }

    private Route getResult() {
        return get(() ->
                parameter(UUID_PARAM, (uuidStr) -> {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(uuidStr);
                    } catch (Exception e) {
                        return complete(StatusCodes.BAD_REQUEST, "uuid `" + uuidStr + "` is not valid", Jackson.marshaller());
                    }
                    CompletionStage<Object> result = Patterns
                            .ask(routerActor, new GetResMsg(uuid), timeout);
                    return onSuccess(() -> result,
                            p -> {
                                ResMsg res = (ResMsg) p;
                                if (res.result == null) {
                                    return complete(StatusCodes.NOT_FOUND, "tests with this uuid doesnt exist or in progress", Jackson.marshaller());
                                }
                                return complete(StatusCodes.OK, new TestResponse(res), Jackson.marshaller());
                            }
                    );
                }));
    }

    private Route submitTests() {
        return pathEnd(() ->
                route(
                        post(() ->
                                entity(Jackson.unmarshaller(TestRequest.class), r -> {
                                    UUID uuid = UUID.randomUUID();
                                    r.setUUID(uuid);
                                    routerActor.tell(r, ActorRef.noSender());
                                    return complete(
                                            StatusCodes.CREATED,
                                            uuid, Jackson.marshaller()
                                    );
                                }))
                )
        );
    }
}

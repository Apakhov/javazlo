import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Routes can be defined in separated classes like shown in here
 */
//#user-routes-class
public class UserRoutes extends AllDirectives {
    //#user-routes-class
    final private ActorRef userRegistryActor;
    final private LoggingAdapter log;
    final private ActorRef routerActor;


    public UserRoutes(ActorSystem system, ActorRef userRegistryActor, ActorRef routerActor) {
        this.userRegistryActor = userRegistryActor;
        this.routerActor = routerActor;
        log = Logging.getLogger(system, this);
    }

    // Required by the `ask` (?) method below
    Duration timeout = Duration.ofSeconds(5l); // usually we'd obtain the timeout from the system's configuration

    /**
     * This method creates one route (of possibly many more that will be part of your Web App)
     */
    //#all-routes
    //#users-get-delete
    public Route routes() {
        return concat(
                getResult(),
                pathPrefix("submit", this::submitTests)
        );
    }

    private Route getResult() {
        return get(() ->
                parameter("uuid", (uuidStr) -> {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(uuidStr);
                    } catch (Exception e) {
                        uuid = UUID.randomUUID();
                    }
                    CompletionStage<Object> result = Patterns
                            .ask(routerActor, new StoreActor.GetResultMessage(uuid), timeout);
                    return onSuccess(() -> result,
                            performed -> {
                                log.info("res" + result);
                                log.info("perf" + performed);
                                return complete(StatusCodes.OK, performed, Jackson.marshaller());
                            }
                    );
                }));
    }

    public class Submit {
        public String code;

        public void setCode(String code) {
            this.code = code;
        }

        Submit() {
            this.code = "";
        }

        Submit(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }



    private Route submitTests() {
        return pathEnd(() ->
                route(
                        post(() ->
                                entity(Jackson.unmarshaller(TestRequest.class), r -> {
                                    UUID uuid = UUID.randomUUID();
                                    log.info("generated:" + uuid);
                                    return complete(
                                            StatusCodes.CREATED,
                                            "Tests started!", Jackson.marshaller()
                                    );
                                }))
                )
        );
    }
    //#all-routes

    //#users-get-delete

    //#users-get-delete

}

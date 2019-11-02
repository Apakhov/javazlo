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
                                log.info("res"+result);
                                log.info("perf"+performed);
                                    return complete(StatusCodes.OK, performed, Jackson.marshaller());
                            }
                    );
                }));
    }

    private class Submit {



    }

    private Route submitTests() {
        return post(() -> entity(Jackson.unmarshaller(Submit.class),
                content ->{
                    UUID uuid = UUID.randomUUID();
                    log.info("generated:"+uuid);
                    return complete(StatusCodes.OK, uuid, Jackson.marshaller());
                })
        );
    }
    //#all-routes

    //#users-get-delete

    //#users-get-delete
    private Route getUser(String name) {
        return get(() -> {
            // #retrieve-user-info
            CompletionStage<Optional<UserRegistryActor.User>> maybeUser = Patterns
                    .ask(userRegistryActor, new UserRegistryMessages.GetUser(name), timeout)
                    .thenApply(Optional.class::cast);

            return onSuccess(() -> maybeUser,
                    performed -> {
                        if (performed.isPresent())
                            return complete(StatusCodes.OK, performed.get(), Jackson.marshaller());
                        else
                            return complete(StatusCodes.NOT_FOUND);
                    }
            );
            //#retrieve-user-info
        });
    }

    private Route deleteUser(String name) {
        return
                //#users-delete-logic
                delete(() -> {
                    CompletionStage<UserRegistryMessages.ActionPerformed> userDeleted = Patterns
                            .ask(userRegistryActor, new UserRegistryMessages.DeleteUser(name), timeout)
                            .thenApply(UserRegistryMessages.ActionPerformed.class::cast);

                    return onSuccess(() -> userDeleted,
                            performed -> {
                                log.info("Deleted user [{}]: {}", name, performed.getDescription());
                                return complete(StatusCodes.OK, performed, Jackson.marshaller());
                            }
                    );
                });
        //#users-delete-logic
    }
    //#users-get-delete

    //#users-get-post
    private Route getOrPostUsers() {
        return pathEnd(() ->
                route(
                        get(() -> {
                            CompletionStage<UserRegistryActor.Users> futureUsers = Patterns
                                    .ask(userRegistryActor, new UserRegistryMessages.GetUsers(), timeout)
                                    .thenApply(UserRegistryActor.Users.class::cast);
                            return onSuccess(() -> futureUsers,
                                    users -> complete(StatusCodes.OK, users, Jackson.marshaller()));
                        }),
                        post(() ->
                                entity(
                                        Jackson.unmarshaller(UserRegistryActor.User.class),
                                        user -> {
                                            CompletionStage<UserRegistryMessages.ActionPerformed> userCreated = Patterns
                                                    .ask(userRegistryActor, new UserRegistryMessages.CreateUser(user), timeout)
                                                    .thenApply(UserRegistryMessages.ActionPerformed.class::cast);
                                            return onSuccess(() -> userCreated,
                                                    performed -> {
                                                        log.info("Created user [{}]: {}", user.getName(), performed.getDescription());
                                                        return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
                                                    });
                                        }))
                )
        );
    }

    //#users-get-post
}

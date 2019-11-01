import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StoreActor extends AbstractActor {
    public static class CreateStoreMessage {

    }

    public static class CreateStoreResponse {
        private final UUID uuid;

        public CreateStoreResponse(UUID uuid) {
            this.uuid = uuid;
        }

        public UUID getUuid() {
            return uuid;
        }
    }

    private Map<UUID, ArrayList<TestActor.ResultMessage>> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestActor.ResultMessage.class, m -> {
                    ArrayList<TestActor.ResultMessage> res = store.get(m.getUUID());
                    res.add(m);
                    System.out.println("receive message! " + m.toString());
                })
                .match(CreateStoreMessage.class, req -> {
                            UUID uuid = UUID.randomUUID();
                            store.put(uuid, new ArrayList<>());
                            sender().tell(
                                    new CreateStoreResponse(uuid), self()
                            );
                        }
                ).build();
    }
}
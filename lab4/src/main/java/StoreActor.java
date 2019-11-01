import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreActor extends AbstractActor {
    public static class CreateStoreMessage {

    }

    public static class CreateStoreResponse {
        private final String uuid;

        public CreateStoreResponse(String uuid) {
            this.uuid = uuid;
        }

        public String getUuid() {
            return uuid;
        }
    }

    private Map<String, ArrayList<TestActor.ResultMessage>> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestActor.ResultMessage.class, m -> {
                    ArrayList<TestActor.ResultMessage> res = store.get(m.getUUID());
                    res.add(m);
                    System.out.println("receive message! " + m.toString());
                })
                .match(CreateStoreMessage.class, req -> {
                    
                            return sender().tell(
                                    new StoreMessage(req.getKey(), store.get(req.getKey())), self()
                            );
                        }
                ).build();
    }
}
import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import java.util.HashMap;
import java.util.Map;
public class StoreActor extends AbstractActor {
    public static class StoreMessage {
        public StoreMessage(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "StoreMessage{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }

        private final String key;
        private final String value;
    }
    public static class GetMessage {
        public GetMessage(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "GetMessage{" +
                    "key='" + key + '\'' +
                    '}';
        }

        public String getKey() {
            return key;
        }


        private final String key;
    }

    private Map<String, String> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreMessage.class, m -> {
                    store.put(m.getKey(), m.getValue());
                    System.out.println("receive message! "+m.toString());
                })
                .match(GetMessage.class, req -> sender().tell(
                        new StoreMessage(req.getKey(), store.get(req.getKey())), self())
                ).build();
    }
}
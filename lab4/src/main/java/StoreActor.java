import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import java.util.HashMap;
import java.util.Map;
public class StoreActor extends AbstractActor {
    public static class StoreMessage {

    }
    public static class GetMessage {

    }

    private Map<String, TestActor.ResultMessage[]> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestActor.ResultMessage.class, m -> {
                    TestActor.ResultMessage[] res = store.get(m.getUUID());
                    res.
                    System.out.println("receive message! "+m.toString());
                })
                .match(GetMessage.class, req -> sender().tell(
                        new StoreMessage(req.getKey(), store.get(req.getKey())), self())
                ).build();
    }
}
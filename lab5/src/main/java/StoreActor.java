import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StoreActor extends AbstractActor {
    private Map<String, Pair<Integer, Long>> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestRequest.class, m -> {
                    if (!store.containsKey(m.url) && store.get(m.url).first() > m.count)
                        sender().tell(StoreResp.withInfo(store.get(m.url).second()), self());
                    else
                        sender().tell(StoreResp.noInfo(), self());
                })
                .match(TestResult.class, m -> {
                    if (!store.containsKey(m.req.url) || store.get(m.req.url).first() <= m.req.count)
                        store.put(m.req.url, new Pair<>(m.req.count, m.timing / m.req.count));
                })
                .build();
    }

    static Props props() {
        return Props.create(StoreActor.class);
    }
}
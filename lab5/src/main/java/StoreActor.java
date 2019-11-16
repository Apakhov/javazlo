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
                    System.out.println(m.url + ":::" + m.count);
                    if (store.containsKey(m.url)
                            && store.get(m.url).first() >= m.count) {
                        System.out.println("found");
                        sender().tell(StoreResp.withInfo(store.get(m.url).second()), self());
                    } else {
                        System.out.println("not found");
                        sender().tell(StoreResp.noInfo(), self());
                    }
                })
                .match(TestResult.class, m -> {
                    System.out.println(m.req.url + "!!!" + m.req.count);
                    System.out.println("{}{}"+store.containsKey(m.req.url));
                    if (!store.containsKey(m.req.url) || store.get(m.req.url).first() <= m.req.count)
                        store.put(m.req.url, new Pair<>(m.req.count, m.timing));
                    System.out.println("LLLLL"+store);
                })
                .build();
    }

    static Props props() {
        return Props.create(StoreActor.class);
    }
}
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
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
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
                .match(GetResMsg.class, req -> {
                    log.info("store get res with: " + req.uuid);
                    if (!store.containsKey(req.uuid)) {
                        sender().tell(
                                new ResMsg(), self()
                        );
                        return;
                    }
                    log.info(req.uuid.toString());
                    sender().tell(
                            new ResMsg(store.get(req.uuid)), self()
                    );
                })
                .build();
    }

    static Props props() {
        return Props.create(StoreActor.class);
    }
}
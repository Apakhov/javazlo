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
    private Map<UUID, Pair<TestMetaInfo, ArrayList<TestResult>>> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestResultMsg.class, m -> {
                    if (!store.containsKey(m.uuid))
                        store.put(m.uuid, new Pair<>(
                                m.
                                new ArrayList<>());
                    log.info("received result: res: " + m.getActualRes() + ", expected:" + m.getExpectedRes().toString());
                    ArrayList<TestResult> res = store.get(m.getUUID());
                    res.add(new TestResult(m));
                })
                .match(GetResultMessage.class, req -> {
                    log.info("store: "+req.getUUID().toString());
                    log.info("store i: "+self());
                    if (!store.containsKey(req.getUUID())){
                        sender().tell(
                                new GetResultResponse(), self()
                        );
                        return;
                    }
                    log.info(req.getUUID().toString());
                    sender().tell(
                            new GetResultResponse(store.get(req.getUUID())), self()
                    );
                })
                .build();
    }

    static Props props() {
        return Props.create(StoreActor.class);
    }
}
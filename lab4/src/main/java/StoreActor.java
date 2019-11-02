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
                                m.testMetaInfo,
                                new ArrayList<>()));
                    log.info("received result: res: " + m.testResult.getActualResult() + ", expected:" + m.testResult.testCase.getExpectedResult());
                    ArrayList<TestResult> res = store.get(m.uuid).second();
                    res.add(m.testResult);
                    log.info("current state: "+ store);
                })
                .match(GetResMsg.class, req -> {
                    log.info("store get res with: "+req.uuid);
                    if (!store.containsKey(req.getUUID())){
                        sender().tell(
                                new ResMsg(), self()
                        );
                        return;
                    }
                    log.info(req.uuid.toString());
                    sender().tell(
                            new ResMsg(store.get(req.getUUID())), self()
                    );
                })
                .build();
    }

    static Props props() {
        return Props.create(StoreActor.class);
    }
}
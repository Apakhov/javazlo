import akka.actor.AbstractActor;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StoreActor extends AbstractActor {
    public static class CreateStoreMessage {
        private final Integer testsAmount;

        public CreateStoreMessage(Integer testsAmount) {

            this.testsAmount = testsAmount;
        }

        public Integer getTestsAmount() {
            return testsAmount;
        }
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

    public static class GetResultMessage {
        private final UUID uuid;

        public GetResultMessage(UUID uuid) {
            this.uuid = uuid;
        }

        public UUID getUuid() {
            return uuid;
        }
    }

    public static class GetResultResponse {
        private final Integer testsAmount;
        private final ArrayList<TestResult> results;

        public GetResultResponse(Integer testsAmount, ArrayList<TestResult> results){
            this.testsAmount = testsAmount;
            this.results = results;
        }

        public Integer getTestsAmount() {
            return testsAmount;
        }

        public ArrayList<TestResult> getResults() {
            return results;
        }
    }

    public static class TestResult {
        private final TestActor.ResultMessage m;
        public TestResult(TestActor.ResultMessage m){
            this.m = m;
        }

        public String getExpectedRes() {
            return m.getExpectedRes();
        }

        public String getActualRes() {
            return m.getActualRes();
        }

        public boolean isOK() {
            return m.isOK();
        }

        public String getError() {
            return m.getError().toString();
        }
    }

    private Map<UUID, Pair<Integer, ArrayList<TestResult>>> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestActor.ResultMessage.class, m -> {
                    Pair<Integer, ArrayList<TestResult>> res = store.get(m.getUUID());
                    res.second().add(new TestResult(m));
                })
                .match(CreateStoreMessage.class, req -> {
                    UUID uuid = UUID.randomUUID();
                    store.put(uuid, new Pair<>(req.getTestsAmount(), new ArrayList<>()));
                    sender().tell(
                            new CreateStoreResponse(uuid), self()
                    );
                })
                .match(GetResultMessage.class, req -> {
                    Pair<Integer, ArrayList<TestResult>> res = store.get(req.getUuid());
                    sender().tell(
                            new GetResultResponse(res.first(), res.second()), self()
                    );
                })
                .build();
    }
}
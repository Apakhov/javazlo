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

        public UUID getUUID() {
            return uuid;
        }
    }

    public static class GetResultMessage {
        private final UUID uuid;

        public GetResultMessage(UUID uuid) {
            this.uuid = uuid;
        }

        public UUID getUUID() {
            return uuid;
        }
    }

    public static class GetResultResponse {
        private final ArrayList<TestResult> results;

        @Override
        public String toString() {
            return "GetResultResponse{" +
                    "results=" + results +
                    '}';
        }

        public GetResultResponse(ArrayList<TestResult> results) {
            this.results = results;
        }

        public GetResultResponse() {
            this.results = new ArrayList<>();
        }

        public ArrayList<TestResult> getResults() {
            return results;
        }
    }

    public static class TestResult {
        private final TestActor.ResultMessage m;

        public TestResult(TestActor.ResultMessage m) {
            this.m = m;
        }

        @Override
        public String toString() {
            return "TestResult{" +
                    "m=" + m +
                    '}';
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

    private Map<UUID, ArrayList<TestResult>> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestActor.ResultMessage.class, m -> {
                    if (!store.containsKey(m.getUUID()))
                        store.put(m.getUUID(), new ArrayList<>());
                    System.out.println("received result: res: " + m.getActualRes() + ", expected:" + m.getExpectedRes().toString());
                    ArrayList<TestResult> res = store.get(m.getUUID());
                    res.add(new TestResult(m));
                })
                .match(GetResultMessage.class, req -> {
                    if (!store.containsKey(req.getUUID())){
                        sender().tell(
                                new GetResultResponse(), self()
                        );
                        return;
                    }
                    System.out.println(store.get(req.getUUID()));
                    sender().tell(
                            new GetResultResponse(store.get(req.getUUID())), self()
                    );
                })
                .build();
    }
}
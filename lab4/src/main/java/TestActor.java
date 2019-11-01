import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class TestActor extends AbstractActor {
    public static class TestMessage {
        public String getSourceCode() {
            return sourceCode;
        }

        public String getFuncName() {
            return funcName;
        }

        public String[] getArgs() {
            return args;
        }

        public String getExpectedRes() {
            return expectedRes;
        }

        private final String sourceCode;
        private final String funcName;
        private final String[] args;
        private final String expectedRes;

        public TestMessage(String sourceCode, String funcName, String[] args, String expectedRes) {
            this.sourceCode = sourceCode;
            this.funcName = funcName;
            this.args = args;
            this.expectedRes = expectedRes;
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestMessage.class, m -> {
                    store.put(m.getKey(), m.getValue());
                    System.out.println("receive message! "+m.toString());
                })
                .build();
    }
}

import akka.actor.AbstractActor;

public class TestActor extends AbstractActor {
    public static class TestMessage {
        public TestMessage(String key, String value) {
            this.sourceCode = key;
            this.value = value;
        }

        public String getSourceCode() {
            return sourceCode;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "StoreMessage{" +
                    "key='" + sourceCode + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }

        private final String sourceCode;
        private final String value;
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}

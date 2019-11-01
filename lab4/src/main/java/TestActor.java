import akka.actor.AbstractActor;

public class TestActor extends AbstractActor {
    public static class TestMessage {



        private final String sourceCode;
        private final String funcName;
        private final String[] args;
        private final String expectedRes;
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}

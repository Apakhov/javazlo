import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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

    public static class ResultMessage {
        public ResultMessage(String expectedRes, String actualRes, boolean isOK) {
            this.expectedRes = expectedRes;
            this.actualRes = actualRes;
            this.isOK = isOK;
        }

        public String getExpectedRes() {
            return expectedRes;
        }

        public String getActualRes() {
            return actualRes;
        }

        public boolean isOK() {
            return isOK;
        }

        private final String expectedRes;
        private final String actualRes;
        private final boolean isOK;
    }

    private static ResultMessage test(TestMessage m){

    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestMessage.class, m -> sender().tell({
                    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
                    engine.eval(m.getSourceCode());
                    Invocable invocable = (Invocable) engine;
                     invocable.invokeFunction(m.getFuncName(), (Object[]) m.getArgs());
                })
                .build();
    }
}

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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
        public ResultMessage(String expectedRes, String actualRes) {
            this.expectedRes = expectedRes;
            this.actualRes = actualRes;
            this.isOK = expectedRes.equals(actualRes);
            this.error = null;
        }

        public ResultMessage(Exception error, String expectedRes) {
            this.error = error;
            this.expectedRes = expectedRes;
            this.actualRes = "";
            this.isOK = false;
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

        public Exception getError() {
            return error;
        }

        private final String expectedRes;
        private final String actualRes;
        private final boolean isOK;
        private final Exception error;
    }

    private static ResultMessage test(TestMessage m){
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(m.getSourceCode());
            Invocable invocable = (Invocable) engine;
            invocable.invokeFunction(m.getFuncName(), (Object[]) m.getArgs());
        } catch (Exception e){
            return new ResultMessage(e, m.getExpectedRes());
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestMessage.class, m -> sender().tell({

                })
                .build();
    }
}

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

        public String getUuid() {
            return uuid;
        }

        private final String uuid;
        private final String sourceCode;
        private final String funcName;
        private final String[] args;
        private final String expectedRes;

        public TestMessage(String uuid, String sourceCode, String funcName, String[] args, String expectedRes) {
            this.uuid = uuid;
            this.sourceCode = sourceCode;
            this.funcName = funcName;
            this.args = args;
            this.expectedRes = expectedRes;
        }
    }

    public static class ResultMessage {
        public ResultMessage(String uuid, String expectedRes, String actualRes) {
            this.uuid = uuid;
            this.expectedRes = expectedRes;
            this.actualRes = actualRes;
            this.isOK = expectedRes.equals(actualRes);
            this.error = null;
        }

        public ResultMessage(String uuid, String expectedRes, Exception error) {
            this.uuid = uuid;
            this.error = error;
            this.expectedRes = expectedRes;
            this.actualRes = "";
            this.isOK = false;
        }
        public String getUuid() {
            return uuid;
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

        private final String uuid;
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
            Object[] args = m.getArgs();
            String res = invocable.invokeFunction(m.getFuncName(), args).toString();
            return new ResultMessage(m.getUuid(), m.getExpectedRes(),res);
        } catch (Exception e){
            return new ResultMessage(m.getUuid(), m.getExpectedRes(), e);
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

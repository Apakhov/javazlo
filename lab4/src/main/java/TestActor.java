import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TestActor extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    private TestResultMsg test(TestCaseMsg m) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(m.testMetaInfo.getSourceCode());
            Invocable invocable = (Invocable) engine;
            Object[] args = m.testCase.getArgs();
            String res = invocable.invokeFunction(m.testMetaInfo.getFuncName(), args).toString();
            log.info("Successful test: res: " + res.toString() + ", expected:" + m.testCase.getExpectedResult());
            return new TestResultMsg(m.testMetaInfo.getUUID(),
                    new TestResult(null, res, m.testCase));
        } catch (Exception e) {
            System.out.println("exception occurred: " + e.toString());
            return new TestResultMsg(m.testMetaInfo.getUUID(),
                    new TestResult(e.toString(), "", m.testCase));
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestCaseMsg.class, m -> {
                    log.info("on testing" + m.toString());
                    sender().tell(
                            test(m), self()
                    );
                })
                .build();
    }

    static Props props() {
        return Props.create(TestActor.class);
    }
}

import java.util.UUID;

public class TestResultMsg {
    public final UUID uuid;
    public final TestResult testResult;

    TestResultMsg(UUID uuid, TestResult testResult){
        this.uuid = uuid;
        this.testResult = testResult;
    }
}

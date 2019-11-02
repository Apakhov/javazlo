import java.util.UUID;

public class TestResulMsg {
    public final UUID uuid;
    public final TestResult testResult;

    TestResulMsg(UUID uuid, TestResult testResult){
        this.uuid = uuid;
        this.testResult = testResult;
    }
}

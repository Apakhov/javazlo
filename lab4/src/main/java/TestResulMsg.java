import java.util.UUID;

public class TestResulMsg {
    public final TestResult testResult;
    public final UUID uuid;

    TestResulMsg( UUID uuid, TestResult testResult){
        this.testResult = testResult;
        this.uuid = uuid;
    }
}

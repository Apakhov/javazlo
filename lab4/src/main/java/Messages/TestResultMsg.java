package Messages;

import java.util.UUID;

public class TestResultMsg {
    public final UUID uuid;
    public final TestResult testResult;
    public final TestMetaInfo testMetaInfo;

    TestResultMsg(UUID uuid, TestResult testResult, TestMetaInfo testMetaInfo) {
        this.uuid = uuid;
        this.testResult = testResult;
        this.testMetaInfo = testMetaInfo;
    }
}

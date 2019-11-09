package Messages;

public class TestCaseMsg {
    public final TestCase testCase;
    public final TestMetaInfo testMetaInfo;

    public TestCaseMsg(TestCase testCase, TestMetaInfo testMetaInfo) {
        this.testCase = testCase;
        this.testMetaInfo = testMetaInfo;
    }
}

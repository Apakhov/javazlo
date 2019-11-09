package Messages;

public class TestResponse extends TestMetaInfo {
    private TestResult[] testResults;

    TestResponse(ResMsg msg) {
        super(msg.result.first());
        testResults = new TestResult[msg.result.second().size()];
        msg.result.second().toArray(testResults);
    }

    public TestResult[] getTestResults() {
        return testResults;
    }
}

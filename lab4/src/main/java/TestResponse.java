public class TestResponse extends TestMetaInfo {
    private TestResult[] testResults;

    TestResponse(ResMsg msg){
        super(msg.result.first());
        msg.result.second().toArray(testResults);
    }
}

public class TestResult {
    public final TestRequest req;
    public final Long timing;

    public TestResult(TestRequest req, Long timing) {
        this.req = req;
        this.timing = timing;
    }
}

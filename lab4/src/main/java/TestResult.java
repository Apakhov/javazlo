public class TestResult {
    private String error;
    private boolean ok;
    private String actualResult;
    private TestCase testCase;

    public TestResult(String error, String actualResult, TestCase testCase) {
        this.error = error;
        this.ok = error != null && actualResult.equals(testCase.getExpectedResult());
        this.actualResult = actualResult;
        this.testCase = testCase;
    }

    public String getError() {
        return error;
    }

    public boolean isOK() {
        return ok;
    }

    public String getActualResult() {
        return actualResult;
    }

    public TestCase getTestCase() {
        return testCase;
    }
}
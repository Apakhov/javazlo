package Messages;

public class TestResult {
    public final String error;
    public final boolean ok;
    public final String actualResult;
    public final TestCase testCase;

    public TestResult(String error, String actualResult, TestCase testCase) {
        this.error = error;
        this.ok = error == null && actualResult.equals(testCase.getExpectedResult());
        this.actualResult = actualResult;
        this.testCase = testCase;
    }
}
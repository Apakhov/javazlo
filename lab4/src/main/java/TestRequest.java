import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestRequest extends TestMetaInfo {
    private TestCase[] testCases;

    public TestRequest() {
        super();
        this.testCases = new TestCase[]{};
    }

    public TestRequest(String packageID, String jsCode, String functionName, TestCase[] testCases) {
        super(packageID, jsCode, functionName);
        this.testCases = testCases;
    }

    public TestCase[] getTestCases() {
        return testCases;
    }
}
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestRequest extends TestMetaInfo {
    private Test[] tests;

    public TestRequest() {
        super();
        this.tests = new Test[]{};
    }

    public TestRequest(String packageID, String jsCode, String functionName, Test[] tests) {
        super(packageID, jsCode, functionName);
        this.tests = tests;
    }

    public Test[] getTests() {
        return tests;
    }
}
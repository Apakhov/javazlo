package Messages;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestCase {
    private String testName;
    private String expectedResult;
    private Object[] args;

    public TestCase() {
        this.testName = "";
        this.expectedResult = "";
        this.args = new Object[]{};
    }

    public TestCase(String name, String expectedResult, Object[] args) {
        this.testName = name;
        this.expectedResult = expectedResult;
        this.args = args;
    }

    public String getTestName() {
        return testName;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public Object[] getArgs() {
        return args;
    }
}
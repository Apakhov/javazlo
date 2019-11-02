import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestCase {
    private UUID uuid;
    private String testName;
    private String expectedResult;
    private Object[] params;

    public TestCase() {
        this.uuid = null;
        this.testName = "";
        this.expectedResult = "";
        this.params = new Object[]{};
    }

    public TestCase(String name, String expectedResult, Object[] params) {
        this.uuid = null;
        this.testName = name;
        this.expectedResult = expectedResult;
        this.params = params;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTestName() {
        return testName;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public Object[] getParams() {
        return params;
    }
}
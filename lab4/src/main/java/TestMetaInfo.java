import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestMetaInfo {
    private String packageID;
    private String jsCode;
    private String functionName;

    public TestMetaInfo() {
        this.packageID = "";
        this.jsCode = "";
        this.functionName = "";
    }

    public TestMetaInfo(TestMetaInfo e){
        this.packageID = e.packageID;
        this.jsCode = e.jsCode;
        this.functionName = e.functionName;
    }

    public TestMetaInfo(String packageID, String jsCode, String functionName) {
        this.packageID = packageID;
        this.jsCode = jsCode;
        this.functionName = functionName;
    }

    public String getPackageID() {
        return packageID;
    }

    public String getJsCode() {
        return jsCode;
    }

    public String getFunctionName() {
        return functionName;
    }
}
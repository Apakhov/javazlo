import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestMetaInfo {
    private UUID uuid;
    private String sourceCode;
    private String funcName;

    public TestMetaInfo() {
        this.sourceCode = "";
        this.funcName = "";
    }

    public TestMetaInfo( String sourceCode, String funcName) {
        this.sourceCode = sourceCode;
        this.funcName = funcName;
    }

    public TestMetaInfo(TestMetaInfo info){
        this.sourceCode = info.sourceCode;
        this.funcName = info.funcName;
        this.uuid = info.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getFuncName() {
        return funcName;
    }
}
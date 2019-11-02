import java.util.UUID;

public static class TestMessage {
    public String getSourceCode() {
        return sourceCode;
    }

    public String getFuncName() {
        return funcName;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getExpectedRes() {
        return expectedRes;
    }

    public UUID getUuid() {
        return uuid;
    }

    private final UUID uuid;
    private final String sourceCode;
    private final String funcName;
    private final Object[] args;
    private final String expectedRes;

    public TestMessage(UUID uuid, String sourceCode, String funcName, Object[] args, String expectedRes) {
        this.uuid = uuid;
        this.sourceCode = sourceCode;
        this.funcName = funcName;
        this.args = args;
        this.expectedRes = expectedRes;
    }
}
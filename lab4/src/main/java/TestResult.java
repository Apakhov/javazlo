public class ResultMessage {
    @Override
    public String toString() {
        return "ResultMessage{" +
                "expectedRes='" + expectedRes + '\'' +
                ", actualRes='" + actualRes + '\'' +
                ", isOK=" + isOK +
                ", error=" + error +
                '}';
    }

    ResultMessage(UUID uuid, String expectedRes, String actualRes) {
        this.uuid = uuid;
        this.expectedRes = expectedRes;
        this.actualRes = actualRes;
        this.isOK = expectedRes.equals(actualRes);
        this.error = null;
    }

    ResultMessage(UUID uuid, String expectedRes, Exception error) {
        this.uuid = uuid;
        this.error = error;
        this.expectedRes = expectedRes;
        this.actualRes = "";
        this.isOK = false;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getExpectedRes() {
        return expectedRes;
    }

    public String getActualRes() {
        return actualRes;
    }

    public boolean isOK() {
        return isOK;
    }

    public Exception getError() {
        return error;
    }

    private final UUID uuid;
    private final String expectedRes;
    private final String actualRes;
    private final boolean isOK;
    private final Exception error;
}
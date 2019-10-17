import java.io.Serializable;

public class AirportInfo implements Serializable {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public AirportInfo(String description, String code) {
        this.description = description;
        this.code = code;
    }
}

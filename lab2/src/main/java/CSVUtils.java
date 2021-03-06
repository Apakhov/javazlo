import org.apache.commons.lang.StringUtils;

public class CSVUtils {
    public static String first(String fields){
        return fields.toString().split(",", 2)[0];
    }

    public static String second(String fields){
        return fields.toString().split(",", 2)[1];
    }

    public static String[] parseFields(String fields){
        return fields.split(",");
    }

    public static String stripUtSymb(String field){
        return StringUtils.strip(field, "\"");
    }
}

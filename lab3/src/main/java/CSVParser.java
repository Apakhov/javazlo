import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;

public class CSVParser {
    String[] fields;

    CSVParser(){

    }

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

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser {
    private String[] fields;

    CSVParser(String... fields){
        this.fields = stripUtSymb(fields);
    }

    public CSVRow Parse(String values){
        return new CSVRow(this.fields, stripUtSymb());
    }

    public static String stripUtSymb(String field){
        return StringUtils.strip(field, "\"");
    }

    public static String[] stripUtSymb(String[] fields){
        return (String[]) Arrays.stream(fields).map(CSVParser::stripUtSymb).toArray();
    }
}

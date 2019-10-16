import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser {
    private String[] fields;

    CSVParser(String... fields){
        this.fields = fields;
    }

    public CSVRow Parse(String... values){
        return new CSVRow(this.fields, (String[]) Arrays.stream(values).map(CSVParser::stripUtSymb).toArray());
    }

    public static String stripUtSymb(String field){
        return StringUtils.strip(field, "\"");
    }
}

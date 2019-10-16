import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CSVParser {
    private String[] fields;

    CSVParser(String... fields){
        this.fields = fields;
    }

    public CSVRow Parse(String... values){
        return new CSVRow(this.fields, new ArrayList<String>(values));
    }
}

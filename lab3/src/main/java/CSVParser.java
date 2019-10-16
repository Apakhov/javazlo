import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser {
    private String[] fields;

    CSVParser(String... fields){
        this.fields = stripUtSymb(fields);
    }

    public CSVRow Parse(String raw){
        ArrayList<String> values = new ArrayList<>();
        int beg = 0;
        boolean inCitation = false;
        for (int cur = 0; cur < raw.length(); cur++){
            if(raw.charAt(cur) == '"'){
                inCitation = !inCitation;
            }
            if(!inCitation && raw.charAt(cur) == ','){
                values.add(raw.substring(beg, cur));
                beg = cur++;
            }
        }

        return new CSVRow(this.fields, (String[]) values.toArray());
    }

    public static String stripUtSymb(String field){
        return StringUtils.strip(field, "\"");
    }

    public static String[] stripUtSymb(String[] fields){
        return (String[]) Arrays.stream(fields).map(CSVParser::stripUtSymb).toArray();
    }
}

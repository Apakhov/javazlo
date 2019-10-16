import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser implements Serializable {
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
                values.add(stripUtSymb(raw.substring(beg, cur)));
                beg = cur+1;
            }
        }
        System.out.println("lasagna"+ values.toString());

        return new CSVRow(this.fields, values.toArray(new String[values.size()]));
    }

    public static String stripUtSymb(String field){
        return StringUtils.strip(field, "\"");
    }

    public static String[] stripUtSymb(String[] fields){
        return Arrays.stream(fields).map(CSVParser::stripUtSymb).toArray(String[]::new);
    }
}

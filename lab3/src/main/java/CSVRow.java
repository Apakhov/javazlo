import java.util.HashMap;

public class CSVRow {
    private HashMap<String, String> map;

    public CSVRow(String[] fields, String[] values) {
        map = new HashMap<String, String>();
        int len = Math.min(fields.length, values.length);
        for (int i = 0; i < len; i++){
            map.put(fields[i], values[i]);
        }
    }

    public String get(String field) {
        return map.get(field);
    }
}

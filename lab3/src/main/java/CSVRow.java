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

    public float asFloat(String field) {
        return Float.parseFloat(get(field));
    }

    public boolean asBool(String field) {
        try {
            return Float.parseFloat(map.get(field)) != 0;
        } catch (NumberFormatException e){
            return false;
        }
    }
}

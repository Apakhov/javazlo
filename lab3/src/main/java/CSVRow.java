import java.util.HashMap;

public class CSVRow {
    private HashMap<String, String> map;

    public CSVRow(String[] fields, String[] values) throws IllegalArgumentException {

        if (fields.length != values.length) {
            throw new IllegalArgumentException("expected same length for fields and values");
        }
        map = new HashMap<String, String>();
        int len = fields.length;
        for (int i = 0; i < len; i++){
            map.put(fields[i], values[i]);
        }
    }

    public String get(String field) {

    }
}

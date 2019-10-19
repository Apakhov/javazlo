import java.util.HashMap;

public class CSVRow {
    private final HashMap<String, String> map;


    public CSVRow(String[] fields, String[] values) {
        map = new HashMap<String, String>();
        int len = Math.min(fields.length, values.length);
        for (int i = 0; i < len; i++){
            map.put(fields[i], values[i]);
        }
    }

    public String get(String field) throws Exception {
        String e = map.get(field);
        if (e == null){
            throw new Exception(map.toString());
        }
        return e;
    }

    public float asFloat(String field) throws Exception {
        try {
            return Float.parseFloat(get(field));
        }catch (NumberFormatException ignored){}
        return 0;
    }

    public boolean asBool(String field) throws Exception {
        try {
            return Float.parseFloat(map.get(field)) != 0;
        } catch (NumberFormatException ignored){}
        return false;
    }
}

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class StatReducer extends Reducer<AirportFlightComparator, AirportID, Text, Text> {
    @Override
    protected void reduce(AirportFlightComparator key, Iterable<AirportID> values, Context context)
            throws IOException, InterruptedException {
        String sum = "";
        int cnt = 0;
        int all = 0;
        for (AirportID value : values) {
            if (value instanceof Airport){
                sum += "0";
                continue;
            }
            if (value instanceof Flight){
                sum += "1";
                continue;
            }
            if (value != null){
                sum += "2";
            }
            if (value == null){
                throw new IOException("garbage");
            }
        }
        //throw new IOException(sum +" "+ String.valueOf(cnt) +" "+ String.valueOf(all));
        context.write(key.getAirportID(), new Text(sum));
    }
}
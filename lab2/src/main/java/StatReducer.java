import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class StatReducer extends Reducer<AirportFlightComparator, Text, Text, Text> {
    @Override
    protected void reduce(AirportFlightComparator key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        String sum = "";
        int cnt = 0;
        int all = 0;
        for (Text value : values) {
            sum += value.toString();
        }
        sum += "\n\n";
        throw new IOException(String.valueOf(sum.length())+" "+ String.valueOf(cnt) +" "+ String.valueOf(all));
        //context.write(key.getAirportID(), new Text(sum));
    }
}
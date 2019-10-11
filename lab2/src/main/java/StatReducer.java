import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class StatReducer extends Reducer<AirportFlightComparator, Text, Text, Text> {
    @Override
    protected void reduce(AirportFlightComparator key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        int cnt = 0;
        String first = values.iterator().next().toString();
        for (Text value : values) {
            sum += value.toString();
        }
        sum += "\n\n";
        //throw new IOException(sum.length())+" "+ String.valueOf(cnt) +" "+ String.valueOf(all));
        context.write(key.getAirportID(), new Text(fi));
    }
}
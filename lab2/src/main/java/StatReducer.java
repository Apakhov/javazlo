import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class StatReducer extends Reducer<AirportFlightComparator, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(AirportFlightComparator key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        context.write(key.getAirportID(), new IntWritable(sum));
    }
}
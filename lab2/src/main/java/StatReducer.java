import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class StatReducer extends Reducer<Text, AirportFlightComparator, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<AirportFlightComparator> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (AirportFlightComparator value : values) {
            sum += String.atoivalue.getAirportID().toString();
        }
        context.write(key, new IntWritable(sum));
    }
}
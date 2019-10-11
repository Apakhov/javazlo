import org.apache.hadoop.io.IntWritable;
        import org.apache.hadoop.io.LongWritable;
        import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
        import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportFlightComparator, Writable>  {
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        AirportID v = new AirportID(value);
        AirportFlightComparator k = new AirportFlightComparator(v.getAirportID(), 1);
        context.write(k, v);
    }
}
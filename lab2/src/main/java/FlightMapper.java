import org.apache.hadoop.io.IntWritable;
        import org.apache.hadoop.io.LongWritable;
        import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
        import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportFlightComparator, Text>  {
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] fields = value.toString().split(",");
        Text v = new Text(fields[14]+","+fields[18]);
        AirportFlightComparator k = new AirportFlightComparator(new Text(fields[14]), 1);
        context.write(k, v);
    }
}
import org.apache.hadoop.io.IntWritable;
        import org.apache.hadoop.io.LongWritable;
        import org.apache.hadoop.io.Text;
        import org.apache.hadoop.mapreduce.Mapper;
        import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportFlightComparator, IntWritable>  {
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        for (String v: value.toString().split(",")){
            context.write(new AirportFlightComparator("test1", 1), new IntWritable(1));
        }
    }
}
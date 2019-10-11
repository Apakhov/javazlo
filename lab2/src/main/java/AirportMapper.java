import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportFlightComparator, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        for (String v: value.toString().split("[^\\p{L}\\p{N}-']")){
            context.write(new AirportFlightComparator("test1", 0), new IntWritable(1));
        }
    }
}
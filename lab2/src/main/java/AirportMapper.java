import org.apache.avro.generic.GenericData;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.lang.reflect.Array;

public class AirportMapper extends Mapper<LongWritable, Text, AirportFlightComparator, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] fields = value.toString().(",");
        if (fields[0].equals("Code")){
            return;
        }
        Text v = new Text(fields[0]+","+fields[1]);
        //throw new IOException(v.toString());
        AirportFlightComparator k = new AirportFlightComparator(new Text(fields[0]), 0);
        context.write(k, v);
    }
}
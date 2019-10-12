import org.apache.avro.generic.GenericData;
import org.apache.commons.lang.StringUtils;
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

        String[] fields = value.toString().split(",", 2);
        String id = StringUtils.strip(fields[0], "\"");
        String name = fields[1];
        if (id.equals("Code")){
            return;
        }
        Text v = new Text(id+":"+name);
        //throw new IOException(v.toString());
        AirportFlightComparator k = new AirportFlightComparator(new Text(id), 0);
        context.write(k, v);
    }
}
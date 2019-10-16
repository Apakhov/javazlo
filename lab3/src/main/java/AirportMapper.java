import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportFlightComp, Text> {
    private static final int ID_ROW = 0;
    private static final int NAME_ROW = 1;


    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String id = CSVUtils.stripUtSymb(CSVUtils.first(value.toString()));
        String name = CSVUtils.second(value.toString());
        if (id.equals("Code")){
            return;
        }
        Text v = new Text(id+":"+name);
        AirportFlightComp k = new AirportFlightComp(new Text(id), 0);
        context.write(k, v);
    }
}
import org.apache.hadoop.io.IntWritable;
        import org.apache.hadoop.io.LongWritable;
        import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
        import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportFlightComparator, Text>  {
    private static final int rowsAm = 23;
    private static final int yearRow = 0;
    private static final int idRow = 14;
    private static final int delayRow = 18;

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] fields = value.toString().split(",");
        if (fields.length != rowsAm){
            throw new IOException("wrong amount of data: "+value);
        }
        if (fields[yearRow].equals("\"YEAR\"") || fields[delayRow].equals("0.00") || fields[delayRow].isEmpty()){
            return;
        }
        Text v = new Text(fields[idRow]);
        AirportFlightComparator k = new AirportFlightComparator(new Text(fields[idRow]), 1);
        context.write(k, v);
    }
}
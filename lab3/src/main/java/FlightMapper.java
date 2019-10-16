import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportFlightComp, Text>  {
    private static final int ROWS_AM = 23;
    private static final int YEAR_ROW = 0;
    private static final int ID_ROW = 14;
    private static final int DELAY_ROW = 18;
    private static final String YEAR_IDENT = "\"YEAR\"";
    private static final String ZERO_FLOAT = "0.00";

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] fields = CSVUtils.parseFields(value.toString());
        if (fields.length != ROWS_AM){
            throw new IOException("wrong amount of data: "+value);
        }
        if (fields[YEAR_ROW].equals(YEAR_IDENT) || fields[DELAY_ROW].equals(ZERO_FLOAT) || fields[DELAY_ROW].isEmpty()){
            return;
        }
        Text v = new Text(fields[ID_ROW]);
        AirportFlightComp k = new AirportFlightComp(new Text(fields[ID_ROW]), 1);
        context.write(k, v);
    }
}
import com.sun.org.apache.xpath.internal.operations.;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.Formatter;

public class StatReducer extends Reducer<AirportFlightComparator, Text, Text, Text> {
    @Override
    protected void reduce(AirportFlightComparator key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        Float min = null;
        Float max = null;
        Float sum = null;
        int cnt = 0;
        String first = values.iterator().next().toString();
        for (Text value : values) {
            float cur = Float.parseFloat(value.toString());
            if (cnt == 0){
                min = max = sum = cur;
            }
            min = Math.min(min, cur);
            max = Math.max(max, cur);
            sum += cur;
            cnt++;
        }
        //throw new IOException(sum.length())+" "+ String.valueOf(cnt) +" "+ String.valueOf(all));
        StringBuilder sbuf = new StringBuilder();
        Formatter fmt = new Formatter(sbuf);
        if (cnt == 0){
            context.write(new Text(first), new Text(fmt.toString()));
        }else {

        }
    }
}
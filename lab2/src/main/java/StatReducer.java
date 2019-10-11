import com.sun.org.apache.xpath.internal.operations.String;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

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
        if (cnt == 0){
            context.write(first, new Text(String.format("%s = %d", "joe", 35)));
        }else {

        }
    }
}
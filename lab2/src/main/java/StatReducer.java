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
            throw new IOException("not now "+first);
//            float cur = Float.parseFloat(value.toString());
//            if (cnt == 0){
//                min = new Float(cur);
//                max = new Float(cur);
//                sum = new Float(cur);
//                throw new IOException("not now");
//                //continue;
//            }
//            min = Math.min(min, cur);
//            max = Math.max(max, cur);
//            sum += cur;
//            cnt++;
        }
//        //throw new IOException(sum.length())+" "+ String.valueOf(cnt) +" "+ String.valueOf(all));
//        StringBuilder sbuf = new StringBuilder();
//        Formatter fmt = new Formatter(sbuf);
//        if (cnt == 0){
//            fmt.format(
//                    "cnt: %s, max: %f, min: %f, avg: %s",
//                    cnt, max, min, sum/cnt
//            );
//        }else {
//            fmt.format("always on time!");
//        }
//        context.write(new Text(first), new Text(fmt.toString()));
    }
}
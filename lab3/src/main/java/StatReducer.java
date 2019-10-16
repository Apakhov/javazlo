import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Formatter;

public class StatReducer extends Reducer<AirportFlightComp, Text, Text, Text> {
    @Override
    protected void reduce(AirportFlightComp key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {


        float min = 0;
        float max = 0;
        float sum = 0;
        int cnt = 0;
        String first = values.iterator().next().toString();
        for (Text value : values) {
            cnt++;
            if(value.toString().isEmpty()){
                value = new Text("0.00");
            }
            float cur = Float.parseFloat(value.toString());
            if (cnt == 1){
                min = cur;
                max = cur;
                sum = cur;
                continue;
            }
            min = Math.min(min, cur);
            max = Math.max(max, cur);
            sum += cur;
        }

        StringBuilder sbuf = new StringBuilder();
        Formatter fmt = new Formatter(sbuf);
        if (cnt != 0){
            fmt.format(
                    "cnt: %s, max: %f, min: %f, avg: %s",
                    cnt, max, min, sum/cnt
            );
        }else {
            return;
        }
        context.write(new Text(first), new Text(fmt.toString()));
    }
}
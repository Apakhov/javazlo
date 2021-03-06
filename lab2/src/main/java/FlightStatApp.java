import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FlightStatApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: FlightStatApp <input path flights> <input path airport> <output path>");
            System.exit(-1);
        }
        Job job = Job.getInstance();

        job.setJarByClass(FlightStatApp.class);
        job.setJobName("Flight stat count");
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, FlightMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, AirportMapper.class);
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        job.setNumReduceTasks(2);

        job.setMapOutputKeyClass(AirportFlightComp.class);
        job.setMapOutputValueClass(Text.class);

        job.setPartitionerClass(StatPartitioner.class);
        job.setGroupingComparatorClass(StatGroupingComparator.class);

        job.setReducerClass(StatReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
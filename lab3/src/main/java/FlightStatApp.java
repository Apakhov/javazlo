import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class FlightStatApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: FlightStatApp <input path flights> <input path airport> <output path>");
            System.exit(-1);
        }
        SparkConf conf = new SparkConf( ).setAppName( "example");
        JavaSparkContext sc = new JavaSparkContext( conf);
        JavaRDD<String> distFile = sc.textFile( "war-and-peace-1.txt");
    }
}
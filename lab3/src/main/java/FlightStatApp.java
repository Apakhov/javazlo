import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class FlightStatApp {
    public static void main(String[] args) throws Exception {
//        if (args.length != 3) {
//            System.err.println("Usage: FlightStatApp <input path flights> <input path airport> <output path>");
//            System.exit(-1);
//        }
//        JavaRDD<String> airportsFile = sc.textFile("airports.csv");
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);
        CSVParser airportParser = new CSVParser("Code","Description");
        JavaRDD<String> flightsFile = sc.textFile("flights.csv");
        JavaPairRDD splitted = flightsFile.mapToPair(
                s -> {
                    CSVRow row = airportParser.Parse(s);
                    return new Tuple2<>(row.get("Code")+"A", row.get("Description")+"B");
                }
        );
//        JavaPairRDD<String, Long> wordsWithCount =
//                splitted.mapToPair(
//                        s -> new Tuple2<>( s, 1L)
//                );
//        JavaPairRDD<String, Long> collectedWords = wordsWithCount.reduceByKey (
//                Long::sum
//        );
//        JavaRDD<String> dictionaryFile = sc.textFile( "russian.txt");
//        JavaPairRDD<String, Long> dictionary =
//                dictionaryFile.mapToPair(
//                        s -> new Tuple2<>( s, 1L)
//                );
//        JavaPairRDD<String, Tuple2<Long, Long>> joinValue = dictionary.join( collectedWords);
        System.out.println( "BEGIN--------------------------------------------------------------------");
        System.out.println( "result="+splitted.toString());
        System.out.println( "END----------------------------------------------------------------------");
    }
}
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
    private static final String[] FLIGHT_FIELDS = {"YEAR","QUARTER","MONTH",
            "DAY_OF_MONTH","DAY_OF_WEEK","FL_DATE","UNIQUE_CARRIER",
            "AIRLINE_ID","CARRIER","TAIL_NUM","FL_NUM","ORIGIN_AIRPORT_ID",
            "ORIGIN_AIRPORT_SEQ_ID","ORIGIN_CITY_MARKET_ID","DEST_AIRPORT_ID",
            "WHEELS_ON","ARR_TIME","ARR_DELAY","ARR_DELAY_NEW","CANCELLED",
            "CANCELLATION_CODE","AIR_TIME","DISTANCE"};

    public static void main(String[] args) throws Exception {
//        if (args.length != 3) {
//            System.err.println("Usage: FlightStatApp <input path flights> <input path airport> <output path>");
//            System.exit(-1);
//        }
//        JavaRDD<String> airportsFile = sc.textFile("airports.csv");
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);
        CSVParser flightParser = new CSVParser(FLIGHT_FIELDS);
        JavaRDD<String> flightsFile = sc.textFile("flights.csv");
        JavaPairRDD<Tuple2<String, String>, AirportPairStat> splitted = flightsFile.mapToPair(
                s -> {
                    CSVRow row = flightParser.Parse(s);
                    return new Tuple2<>(
                            new Tuple2<>(row.get("ORIGIN_AIRPORT_ID"), row.get("DEST_AIRPORT_ID")),
                            new AirportPairStat(row.asFloat("ARR_DELAY_NEW"), row.asBool("CANCELLED"))
                    );
                }
        );
        JavaPairRDD<Tuple2<String, String>, AirportPairFinalStat> t = splitted.foldByKey(
                new AirportPairFinalStat(), (p, s) -> p. .getDelay()
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
        System.out.println( "result="+splitted.collect());
        System.out.println( "END----------------------------------------------------------------------");
    }
}
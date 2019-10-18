import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class FlightStatApp {
    public enum Constants {
        NAME_1("Value1"),
        NAME_2("Value2"),
        NAME_3("Value3");

        private String value;

        Constants(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
    private static final CSVParser flightParser = new CSVParser(FLIGHT_FIELDS);

    private static final String[] AIRPORT_FIELDS = {"Code","Description"};
    private static final CSVParser airportParser = new CSVParser(AIRPORT_FIELDS);


    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: FlightStatApp <input path flights> <input path airport> <output path>");
            System.exit(-1);
        }
        String flightsPath = args[0];
        String airportsPath = args[1];
        String outputPath = args[2];

        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flightsFile = sc.textFile(flightsPath);
        JavaRDD<String> airportsFile = sc.textFile(airportsPath);

        JavaPairRDD<Tuple2<String, String>, AirportPairStat> splitted = flightsFile.mapToPair(
                s -> {
                    CSVRow row = flightParser.Parse(s);
                    return new Tuple2<>(
                            new Tuple2<>(
                                   row.get("ORIGIN_AIRPORT_ID"),
                                   row.get("DEST_AIRPORT_ID")
                            ),
                            new AirportPairStat(row.asFloat("ARR_DELAY_NEW"), row.asBool("CANCELLED"))
                    );
                }
        );
        JavaPairRDD<Tuple2<String, String>, AirportPairStat> reduced = splitted.reduceByKey(
                AirportPairStat::add
        );

        Map<String, String> stringAirportDataMap = airportsFile.mapToPair(
                s -> {
                    CSVRow row = airportParser.Parse(s);
                    return new Tuple2<>(
                            row.get("Code"), "\""+row.get("Description")+"\""
                    );
                }
        ).collectAsMap();

        final Broadcast<Map<String, String>> airportsBroadcasted =
                sc.broadcast(stringAirportDataMap);

        JavaRDD<String> res = reduced.map(
                p -> airportsBroadcasted.value().get(p._1._1) +
                        " -> " +
                        airportsBroadcasted.value().get(p._1._2) + ": " +
                        p._2.toString()
        );
        res.saveAsTextFile(outputPath);


        System.out.println( "result="+res.collect());
    }
}
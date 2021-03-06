import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class FlightStatApp {
    private static final String[] FLIGHT_FIELDS = {"YEAR","QUARTER","MONTH",
            "DAY_OF_MONTH","DAY_OF_WEEK","FL_DATE","UNIQUE_CARRIER",
            "AIRLINE_ID","CARRIER","TAIL_NUM","FL_NUM","ORIGIN_AIRPORT_ID",
            "ORIGIN_AIRPORT_SEQ_ID","ORIGIN_CITY_MARKET_ID","DEST_AIRPORT_ID",
            "WHEELS_ON","ARR_TIME","ARR_DELAY","ARR_DELAY_NEW","CANCELLED",
            "CANCELLATION_CODE","AIR_TIME","DISTANCE"};
    private static final String ORIGIN_AIRPORT_ID = "ORIGIN_AIRPORT_ID";
    private static final String DEST_AIRPORT_ID = "DEST_AIRPORT_ID";
    private static final String ARR_DELAY_NEW = "ARR_DELAY_NEW";
    private static final String CANCELLED = "CANCELLED";
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
                                    row.get(ORIGIN_AIRPORT_ID),
                                    row.get(DEST_AIRPORT_ID)
                            ),
                            new AirportPairStat(row.asFloat(ARR_DELAY_NEW), row.asBool(CANCELLED))
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
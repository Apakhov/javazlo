import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Partitioner;

public class StatPartitioner extends Partitioner<AirportFlightComparator, Writable> {

    @Override
    public int getPartition(AirportFlightComparator airportFlightComparator, Writable writable, int i) {
        return 0;
    }
}

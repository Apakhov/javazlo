import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Partitioner;

public class StatPartitioner implements Partitioner<AirportFlightComparator, Writable> {
    @Override
    public int getPartition(AirportFlightComparator airportFlightComparator, Writable writable, int i) {
        return 0;
    }
}

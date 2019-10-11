import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Partitioner;

public class StatPartitioner implements Partitioner {

    @Override
    public int getPartition(AirportFlightComparator airportFlightComparator, Writable writable, int i) {
        return 0;
    }

    @Override
    public int getPartition(Object o, Object o2, int i) {
        return 0;
    }
}

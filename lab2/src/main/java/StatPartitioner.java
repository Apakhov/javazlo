import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Partitioner;

public class StatPartitioner extends Partitioner<AirportFlightComparator, Writable> {

    @Override
    public int getPartition(AirportFlightComparator k, Writable v, int i) {
        return k.hashCode() % i;
    }
}

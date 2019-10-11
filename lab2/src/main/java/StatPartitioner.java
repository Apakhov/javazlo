import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Partitioner;

public class StatPartitioner extends Partitioner<AirportFlightComparator, Text> {

    @Override
    public int getPartition(AirportFlightComparator k, Text v, int i) {
        return k.hashCode() % i;
    }
}

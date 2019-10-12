import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class StatPartitioner extends Partitioner<AirportFlightComp, Text> {

    @Override
    public int getPartition(AirportFlightComp k, Text v, int i) {
        return k.getAirportID().hashCode() % i;
    }
}

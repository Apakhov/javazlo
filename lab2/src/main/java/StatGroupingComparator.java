import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class StatGroupingComparator extends WritableComparator {

    @Override
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        // per your desired no-sort logic
        return 0;
    }

    @Override
    public int compare(WritableComparable o1, WritableComparable o2) {
        if (o1 instanceof AirportFlightComparator && o2 instanceof AirportFlightComparator){
            return ((AirportFlightComparator) o1).getAirportID().compareTo(((AirportFlightComparator) o2).getAirportID());
        }
        return 0;
    }
}

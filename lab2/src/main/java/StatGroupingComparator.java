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
    public int compare(Object o1, Object o2) {
        return ((AirportFlightComparator)o1).getAirportID().compareTo(((AirportFlightComparator)o2).getAirportID());
    }
}

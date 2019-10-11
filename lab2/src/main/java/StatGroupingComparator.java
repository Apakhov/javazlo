import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class StatGroupingComparator extends WritableComparator {



    @Override
    public int compare(WritableComparable o1, WritableComparable o2) {
        if (o1 instanceof AirportFlightComparator && o2 instanceof AirportFlightComparator){
            return ((AirportFlightComparator) o1).getAirportID().compareTo(((AirportFlightComparator) o2).getAirportID());
        }
        return 0;
    }
}

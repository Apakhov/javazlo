import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class StatGroupingComparator extends WritableComparator {

    @Override
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        return 0;
    }


    public int compare(AirportFlightComparator o1, AirportFlightComparator o2) throws Exception{
        throw new Exception("ss");
        //return ((AirportFlightComparator)o1).getAirportID().compareTo(((AirportFlightComparator)o2).getAirportID());
    }
}

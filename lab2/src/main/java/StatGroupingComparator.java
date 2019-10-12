import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class StatGroupingComparator extends WritableComparator {

    public StatGroupingComparator(){
        super(AirportFlightComparator.class, true);
    }

    @Override
    public int compare(WritableComparable o1, WritableComparable o2){
        return ((AirportFlightComparator)o1).getAirportID().compareTo(((AirportFlightComparator)o2).getAirportID());
    }
}

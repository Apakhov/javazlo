import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class StatGroupingComparator extends WritableComparator {

    public StatGroupingComparator(){
        super(AirportFlightComp.class, true);
    }

    @Override
    public int compare(WritableComparable o1, WritableComparable o2){
        return ((AirportFlightComp)o1).getAirportID().compareTo(((AirportFlightComp)o2).getAirportID());
    }
}

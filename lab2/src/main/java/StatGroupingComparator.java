import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class StatGroupingComparator extends WritableComparator {

    public StatGroupingComparator(){
        super(AirportFlightComparator.class, true);
    }

//    @Override
//    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
//        return 0;
//    }

    @Override
    public int compare(WritableComparable o1, WritableComparable o2){
        //throw new Exception("ss");
        return ((AirportFlightComparator)o1).getAirportID().compareTo(((AirportFlightComparator)o2).getAirportID());
    }
}

import org.apache.hadoop.io.*;

public class StatGroupingComparator extends WritableComparator {

    public StatGroupingComparator(){
        super(StatGroupingComparator.class, true);
    }

    @Override
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        return 0;
    }


    public int compare(Text o1, Text o2) throws Exception{
        throw new Exception("ss");
        //return ((AirportFlightComparator)o1).getAirportID().compareTo(((AirportFlightComparator)o2).getAirportID());
    }
}

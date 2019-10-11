import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class AirportFlightComparator implements  WritableComparable<AirportFlightComparator> {
    private BigInteger airportID;
    private enum type{
        AIRPORT,
        FLIGHT,
    }

    @Override
    public void readFields(DataInput in) throws IOException {

    }

    @Override
    public void write(DataOutput out) throws IOException {

    }

    @Override
    public int compareTo(AirportFlightComparator to) {
        return  0;
    }

    @Override
    public int hashCode(){
        return 0;
    }
}

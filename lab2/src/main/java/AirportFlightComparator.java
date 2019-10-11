import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class AirportFlightComparator implements  WritableComparable<AirportFlightComparator> {
    public static enum Type{
        AIRPORT,
        FLIGHT,
    }

    private Text airportID;
    private Type type;

    public AirportFlightComparator(Text airportID, Type type) {
        this.airportID = airportID;
        this.type = type;
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

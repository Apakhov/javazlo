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

    public Text getAirportID() {
        return airportID;
    }

    public void setAirportID(Text airportID) {
        this.airportID = airportID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

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

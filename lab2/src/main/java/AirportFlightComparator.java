import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

public class AirportFlightComparator implements  WritableComparable<AirportFlightComparator> {
    private Text airportID;
    private IntWritable type;

    public Text getAirportID() {
        return airportID;
    }

    public void setAirportID(Text airportID) {
        this.airportID = airportID;
    }

    public IntWritable getType() {
        return type;
    }

    public void setType(IntWritable type) {
        this.type = type;
    }

    public AirportFlightComparator(Text airportID, IntWritable type) {
        this.airportID = airportID;
        this.type = type;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        airportID.readFields(in);
        type.readFields(in);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.write(airportID);
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

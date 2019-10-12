import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

public class AirportFlightComp implements WritableComparable<AirportFlightComp> {
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

    public AirportFlightComp(){
        this.airportID = new Text();
        this.type = new IntWritable();
    }

    public AirportFlightComp(Text airportID, int type) {
        this.airportID = new Text(airportID);
        this.type = new IntWritable(type);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        airportID.readFields(in);
        type.readFields(in);
    }

    @Override
    public void write(DataOutput out) throws IOException {
       airportID.write(out);
       type.write(out);
    }

    @Override
    public int compareTo(AirportFlightComp to) {
        int idComp = this.airportID.compareTo(to.airportID);
        if (idComp != 0){
            return  idComp;
        }
        return this.type.compareTo(to.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AirportFlightComp)) return false;
        AirportFlightComp that = (AirportFlightComp) o;
        return Objects.equals(airportID, that.airportID) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportID, type);
    }
}

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AirportID implements Writable {
    private Text airportID;

    public AirportID(Text t){
        String[] fields = t.toString().split(",");
        airportID = new Text(fields[0]);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        airportID.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        airportID.readFields(dataInput);
    }

    public Text getAirportID() {
        return airportID;
    }

    public void setAirportID(Text airportID) {
        this.airportID = airportID;
    }
}

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Flight implements Writable {
    private Text destID;

    public Flight(Text t){
        String[] fields = t.toString().split(",");
        destID = new Text(fields[14]);
    }
    
    @Override
    public void write(DataOutput dataOutput) throws IOException {

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }

    public Text getDestID() {
        return destID;
    }

    public void setDestID(Text destID) {
        this.destID = destID;
    }
}

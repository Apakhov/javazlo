import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Writable;

public class StatGroupingComparator implements RawComparator<AirportFlightComparator> {

    @Override
    public int compare(byte[] bytes, int i, int i1, byte[] bytes1, int i2, int i3) {
        return 0;
    }

    @Override
    public int compare(AirportFlightComparator o1, AirportFlightComparator o2) {
        return 0;
    }
}

import java.io.Serializable;
import java.math.BigInteger;

public class AirportPairFinalStat implements Serializable {
    private float maxDelay;
    private BigInteger amount;
    private BigInteger delayedAmount;
    private BigInteger canceledAmount;


    public AirportPairFinalStat() {
    }
}

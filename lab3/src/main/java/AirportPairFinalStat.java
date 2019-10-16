import java.io.Serializable;
import java.math.BigInteger;

public class AirportPairFinalStat implements Serializable {
    private float maxDelay;
    private BigInteger amount;
    private BigInteger delayedAmount;
    private BigInteger canceledAmount;


    public AirportPairFinalStat() {
        maxDelay = 0;
        amount = BigInteger.ZERO;
        delayedAmount = BigInteger.ZERO;
        canceledAmount = BigInteger.ZERO;
    }

    public void add(float delay, boolean canceled) {
        maxDelay = Math.max(maxDelay, delay);
        amount = amount.add(BigInteger.ONE);
        if (delay > 0) {
            delayedAmount = delayedAmount.add(BigInteger.ONE);
        }
        if (canceled) {
            canceledAmount = canceledAmount.add(BigInteger.ONE);
        }
    }

}

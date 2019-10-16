import java.io.Serializable;
import java.math.BigInteger;

public class AirportPairFinalStat implements Serializable {
    private float maxDelay = 0;
    private BigInteger amount = BigInteger.ZERO;
    private BigInteger delayedAmount = BigInteger.ZERO;
    private BigInteger canceledAmount = BigInteger.ZERO;


    public AirportPairFinalStat() {
    }

    public AirportPairFinalStat(float delay, boolean isCanceled) {
        maxDelay = delay;
        amount = BigInteger.ONE;
        if (delay > 0)
            delayedAmount = BigInteger.ONE;
        if (isCanceled)
            canceledAmount = BigInteger.ONE;
    }

    public void add(AirportPairFinalStat s) {
        maxDelay = Math.max(maxDelay, s.maxDelay);
        amount = amount.add(s.amount);
        delayedAmount = delayedAmount.add(s.delayedAmount);
        canceledAmount = canceledAmount.add(s.canceledAmount);
    }

}

import java.io.Serializable;
import java.math.BigInteger;

public class AirportPairStat implements Serializable {
    private float maxDelay = 0;
    private BigInteger amount = BigInteger.ZERO;
    private BigInteger delayedAmount = BigInteger.ZERO;
    private BigInteger canceledAmount = BigInteger.ZERO;


    public AirportPairStat() {
    }

    public AirportPairStat(float delay, boolean isCanceled) {
        maxDelay = delay;
        amount = BigInteger.ONE;
        if (delay > 0)
            delayedAmount = BigInteger.ONE;
        if (isCanceled)
            canceledAmount = BigInteger.ONE;
    }

    public AirportPairStat add(AirportPairStat s) {
        maxDelay = Math.max(maxDelay, s.maxDelay);
        amount = amount.add(s.amount);
        delayedAmount = delayedAmount.add(s.delayedAmount);
        canceledAmount = canceledAmount.add(s.canceledAmount);
        return this;
    }

    @Override
    public String toString() {
        return "Stat: " +
                "maxDelay=" + maxDelay +
                ", amount=" + amount +
                ", delayedAmount=" + delayedAmount +
                ", canceledAmount=" + canceledAmount;
    }
}

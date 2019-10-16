import java.io.Serializable;

public class AirportPairStat implements Serializable {
    private float delay;
    private boolean isCanceled;


    public AirportPairStat(float delay, boolean isCanceled) {
        this.delay = delay;
        this.isCanceled = isCanceled;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
}

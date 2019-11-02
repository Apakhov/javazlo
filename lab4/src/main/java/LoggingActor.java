import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LoggingActor {
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
}

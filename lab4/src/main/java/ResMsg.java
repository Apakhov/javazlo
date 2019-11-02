import java.util.ArrayList;
import akka.japi.Pair;

public class ResMsg {
    public final Pair<TestMetaInfo, ArrayList<TestResult>> result;

    public ResMsg(Pair<TestMetaInfo, ArrayList<TestResult>> result) {
        this.result = result;
    }

    public ResMsg() {
        this.result = null;
    }
}

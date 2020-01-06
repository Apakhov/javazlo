import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.Arrays;
import java.util.HashMap;

public class Cache {
    private static final Long TIMEOUT = 1000L;
    private Long lastHB;
    private Integer cacheStart;
    private Integer cacheEnd;
    private ZMQ.Socket proxy;
    private HashMap<Integer, String> cache = new HashMap<>();

    Cache(Integer cacheStart, Integer cacheEnd, ZMQ.Socket proxy) {
        this.cacheStart = cacheStart;
        this.cacheEnd = cacheEnd;
        this.proxy = proxy;
    }

    public void notifyProxy() {
        lastHB = System.currentTimeMillis();
        System.out.println("notifiing");
        proxy.send("notify" + cacheStart + " " + cacheEnd);
        String recv = proxy.recvStr(0);
        System.out.println(recv);
        System.out.println("notified");
    }

    public void sendHB() {
        Long t = System.currentTimeMillis();
        if (lastHB < t -TIMEOUT) {
            lastHB = t;
            proxy.send("HB");
        }
    }

    public void handleRequest() {
        ZMsg msg = ZMsg.recvMsg(proxy, false);
        if (msg == null) return;
        System.out.println("handeling");
        String cmdMsg = msg.getLast().getString(ZMQ.CHARSET);
        MSGCommand cmd = MSGCommand.ParseLine(cmdMsg);
        System.out.println("parsed " + cmd);
        String response = "";
        if (!(cacheStart < cmd.getSpace() && cmd.getSpace() < cacheEnd)) {
            response = "not mine";
        } else
            switch (cmd.getCmd()) {
                case "GET":
                    response = cache.getOrDefault(cmd.getSpace(), "");
                    break;
                case "SET":
                    System.out.println("SET !");
                    if (cmd.getVal() == null) {
                        response = "no val on set";
                        break;
                    }
                    System.out.println("has value");
                    System.out.println("has value" + cmd.getSpace() + " " + cmd.getVal());
                    cache.put(cmd.getSpace(), cmd.getVal());
                    System.out.println("got value");

                    response = "roger";
                    break;
                default:
                    response = "bad cmd";
            }
        System.out.println("RESPONSE: " + response + " " + cmd);
        msg.getLast().reset("CMD" + response);
        msg.send(proxy);
    }
}

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.HashMap;

public class CacheApp {
    private static final String PROXY_ADDR = "tcp://localhost:5560";

    public static void main(String[] args) throws InterruptedException {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket proxy = context.socket(SocketType.DEALER);
        proxy.connect(args.length > 1 ? args[1] : PROXY_ADDR);
        ZMQ.Poller items = context.poller(1);
        items.register(proxy, ZMQ.Poller.POLLIN);

        Cache cache = new Cache(0, 199, proxy);
        cache.notifyProxy();
        System.out.println("notified ready for work");

        System.out.println("ready");
        try {
            while (true) {
                cache.handleRequest();
                cache.sendHB();
            }
        } finally {
            context.close();
        }

    }


}
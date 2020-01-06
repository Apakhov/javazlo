import javafx.util.Pair;
import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProxyApp {
    private static final String CLIENT_ADDRESS = "tcp://localhost:5555";
    private static final String CACHE_ADDRESS = "tcp://localhost:5560";
    private static final Long TIMEOUT = 1000L;


    public static void main(String[] args) {
        ZMQ.Context ct = ZMQ.context(1);
        ZMQ.Socket client = router(ct, args.length > 1 ? args[1] : CLIENT_ADDRESS);
        ZMQ.Socket cache = router(ct, args.length > 2 ? args[2] : CACHE_ADDRESS);
        ZMQ.Poller items = ct.poller(2);
        items.register(client, ZMQ.Poller.POLLIN);
        items.register(cache, ZMQ.Poller.POLLIN);
        boolean more = false;

        HashMap<byte[], ArrayList<Long>> caches = new HashMap<>();
        try {
            System.out.println("bind!");
            while (!Thread.currentThread().isInterrupted()) {
                items.poll(TIMEOUT);
                if (items.pollin(0)) {
                    ZMsg msg = ZMsg.recvMsg(client);
                    String command = msg.getLast().getString(ZMQ.CHARSET);

                    System.out.println("msg: " + command);
                    MSGCommand msgCommand = MSGCommand.ParseLine(command);
                    AtomicBoolean sent = new AtomicBoolean(false);
                    caches.forEach((id, l) -> {
                        System.out.println("IN");
                        if (l.get(0) <= msgCommand.getSpace() && msgCommand.getSpace() <= l.get(1)) {
                            System.out.println("sending to" + Arrays.toString(id) + " " + l);
                            cache.send(id, ZFrame.MORE + ZFrame.REUSE);
                            msg.send(cache, false);
                            sent.set(true);
                        }
                    });
                    if (!sent.get()){
                        client.send(msg.getFirst().getData(),  ZMQ.SNDMORE );
                        client.send(new byte[0], ZMQ.SNDMORE);
                        client.send("value not in cache range");
                    }
                    System.out.println("and now...");
                }
                if (items.pollin(1)) {
                    System.out.println("pollin 1");

                    ZMsg msg = ZMsg.recvMsg(cache);
                    String command = msg.getLast().getString(ZMQ.CHARSET);
                    System.out.println("1111"+ Arrays.toString(msg.getFirst().getData()) );
                    if (hasPrefix(command, "notify")) {
                        command = trimPrefix(command, "notify");
                        System.out.println("3333");
                        Pair<Long, Long> lims = parseCacheConf(command);
                        ArrayList<Long> conf = new ArrayList<>();
                        conf.add(lims.getKey());
                        conf.add(lims.getValue());
                        conf.add(System.currentTimeMillis());
                        caches.put(msg.getFirst().getData(), conf);
                        System.out.println("tutting");
                        System.out.println("->");
                        System.out.println(Arrays.toString(msg.getFirst().getData()));
                        cache.sendMore(msg.getFirst().getData());
                        cache.send("okey ");
                        System.out.println("to work");
                    } else if (hasPrefix(command, "CMD")) {
                        command = trimPrefix(command, "CMD");
                        System.out.println("translating");
                        msg.removeFirst();
                        msg.getLast().reset(command);
                        msg.send(client);
                        System.out.println("translated");
                    } else if (hasPrefix(command, "HB")) {
                        System.out.println("HB");
                        if(caches.containsKey(msg.getFirst().getData())){
                            caches.get(msg.getFirst().getData()).set(2, System.currentTimeMillis());
                        }
                    } else {
                        System.out.println("unknown cmd " + command);
                    }
                }

                Long curr = System.currentTimeMillis();
                ArrayList<byte[]> remove = new ArrayList<>();
                caches.forEach((id, l) -> {
                    System.out.println("removed");
                    if (l.get(2) < curr - 10 * TIMEOUT){
                        System.out.println("removed! " + l.get(2) + " " + (curr - 10 * TIMEOUT));
                        remove.add(id);
                    }
                });
                remove.forEach(caches::remove);
            }

        } finally {
            ct.close();
        }
    }

    private static boolean hasPrefix(String s, String pref){
        return s.length() >= pref.length() && s.substring(0, pref.length()).equals(pref);
    }

    private static String trimPrefix(String s, String pref){
        boolean has = s.length() >= pref.length() && s.substring(0, pref.length()).equals(pref);
        if (has)
            s = s.substring(pref.length());
        return s;
    }

    private static Pair<Long, Long> parseCacheConf(String conf) {
        String[] is = conf.split(" ");
        return new Pair<>(Long.parseLong(is[0]), Long.parseLong(is[1]));
    }

    private static ZMQ.Socket router(ZMQ.Context context, String addr) {
        ZMQ.Socket socket = context.socket(SocketType.ROUTER);
        socket.bind(addr);
        return socket;
    }
}

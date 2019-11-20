import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestConverter extends AbstractActor {
    private Map<String, Pair<Integer, Long>> store = new HashMap<>();


    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ConverterConfig.class, conf -> {
                    ZooKeeper(String connectString,
                    int sessionTimeout,
                    Watcher watcher
)
                    sender().tell(true, self());
                })
                .match(TestRequest.class, req -> {
                    String uuid = UUID.randomUUID().toString();
                    System.out.println("["+uuid+"]"+"request:{"+req+"}");
                    String nextUrl = req.url;
                    if (req.count > 0) {
                        nextUrl = "http://localhost:8080/?url="+req.url+"&count="+(req.count-1);
                    }
                    System.out.println("["+uuid+"]"+"nextURL:{"+nextUrl+"}");
                    sender().tell(nextUrl, self());
                })
                .build();
    }

    static Props props() {
        return Props.create(RequestConverter.class);
    }
}
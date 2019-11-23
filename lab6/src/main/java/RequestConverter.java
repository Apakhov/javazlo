import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.util.*;

public class RequestConverter extends AbstractActor {
    private Map<String, Pair<Integer, Long>> store = new HashMap<>();
    private final UUID uuid = UUID.randomUUID();
    private ZKConnection zoo = new ZKConnection();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ConverterConfig.class, conf -> {
                    zoo.connect("localhost");
                    System.out.println("connected");
                    try {
                        zoo.path(CreateMode.PERSISTENT,"servers", uuid.toString());
                        zoo.set(CreateMode.EPHEMERAL, "/servers/"+uuid, conf.host);

                    } catch (Exception e){
                        System.out.println("exception:"+e);
                    }
                    System.out.println("exception??");
                })
                .match(TestRequest.class, req -> {
                    String uuid = UUID.randomUUID().toString();
                    System.out.println("["+uuid+"]"+"request:{"+req+"}");
                    String nextUrl = req.url;
                    if (req.count > 0) {
                        ArrayList<String> nodes = zoo.getChildrenData("/servers", this.uuid.toString());
                        int rnd = new Random().nextInt(nodes.size());
                        nextUrl = nodes.get(rnd)+"/?url="+req.url+"&count="+(req.count-1);
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
package Actors;

import Messages.ConverterConfig;
import Messages.NodesMsg;
import Messages.TestRequest;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;

import java.util.*;
import Main.ZKConnection;

public class RequestConverter extends AbstractActor {
    private Map<String, Pair<Integer, Long>> store = new HashMap<>();
    private final UUID uuid = UUID.randomUUID();
    private ZKConnection zoo;
    private ArrayList<String> nodes;

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ConverterConfig.class, conf ->
                        zoo = new ZKConnection(conf.host, "/servers", uuid.toString(), self()))
                .match(TestRequest.class, req -> {
                    String nextUrl = req.url;
                    if (req.count > 0) {
                        int rnd = new Random().nextInt(nodes.size());
                        nextUrl = nodes.get(rnd) + "/?url=" + req.url + "&count=" + (req.count - 1);
                    }
                    sender().tell(nextUrl, self());
                })
                .match(NodesMsg.class, m -> {
                    nodes = m.nodes;
                })
                .build();
    }

    public static Props props() {
        return Props.create(RequestConverter.class);
    }
}
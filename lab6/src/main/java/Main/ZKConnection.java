package Main;

import Messages.NodesMsg;
import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;

public class ZKConnection {
    private static final int SESSION_TIMEOUT = 2000;
    private static final String REG_NODE_S = "/reg";
    private ZooKeeper zoo;
    private String host;
    private String path;
    private ActorRef reqConv;

    public ZKConnection(String host, String path, String name, ActorRef reqConv)
            throws IOException, KeeperException, InterruptedException {
        this.host = host;
        this.path = path;
        this.reqConv = reqConv;

        reconnect();
        zoo.create(
                path + REG_NODE_S,
                name.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
        System.out.println("connected !!");
    }

    public void reconnect() throws IOException {
        zoo = new ZooKeeper(host, SESSION_TIMEOUT, we -> {
            if (we.getState() == Watcher.Event.KeeperState.Expired
                    || we.getState() == Watcher.Event.KeeperState.Disconnected) {
                try {
                    reconnect();
                    System.out.println("reconnected !!");
                } catch (IOException e) {
                    System.out.println("got state watch error: " + e);
                }
            }
        });

        watchNodes();
    }

    private void watchNodes() {
        try {
            ArrayList<String> serverNodeNames = new ArrayList<>(zoo.getChildren(path, we -> {
                if (we.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNodes();
                }
            }));

            ArrayList<String> addresses = new ArrayList<>();
            for (String nodeName : serverNodeNames) {
                byte[] addr = zoo.getData(path + "/" + nodeName, false, null);
                addresses.add(new String(addr));
            }

            reqConv.tell(new NodesMsg(addresses), ActorRef.noSender());
        } catch (KeeperException | InterruptedException e) {
            System.out.println("got getChildren error:" + e);
        }
    }

    public void close() {
        try {
            zoo.close();
        } catch (InterruptedException e) {
            System.out.println("can not close zookeeper connection: " + e);
        }
    }
}
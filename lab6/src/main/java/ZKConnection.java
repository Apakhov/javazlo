import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class ZKConnection {
    private ZooKeeper zoo;
    CountDownLatch connectionLatch = new CountDownLatch(1);

    public ZooKeeper connect(String host)
            throws IOException,
            InterruptedException {
        zoo = new ZooKeeper(host, 2000, we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectionLatch.countDown();
            }
        });

        connectionLatch.await();
        System.out.println("connected !!");

        return zoo;
    }

    public void path(String... path) throws KeeperException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        for (String p :
                path) {
            builder.append("/").append(p);
            System.out.println("setting path:" + builder.toString());
            set(builder.toString(), "");
        }
    }

    public void set(String path, String data) throws KeeperException, InterruptedException {
        Stat stat = zoo.exists(path, true);
        System.out.println("PATH" + path);
        if (stat == null)
            zoo.create(
                    path,
                    data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL);
        System.out.println("definetly created path: " + path + ", data: " + data);
        zoo.setData(path, data.getBytes(), zoo.exists(path, true).getVersion());
    }

    public ArrayList<String> getChildrenData(String path, String except)
            throws KeeperException,
            InterruptedException, UnsupportedEncodingException {
        ArrayList<String> nodes = new ArrayList<>();
        AtomicReference<KeeperException> ke = new AtomicReference<>();
        AtomicReference<InterruptedException> ie = new AtomicReference<>();
        AtomicReference<UnsupportedEncodingException> uee = new AtomicReference<>();
        System.out.println("FUCK" + path);
        zoo.getChildren(path, true).forEach(x -> {
            try {
                String res = new String(zoo.getData(path + "/" + x, true, null), "UTF-8");
                if (!res.equals(except))
                    nodes.add(res);
            } catch (InterruptedException e) {
                ie.set(e);
            } catch (KeeperException e) {
                ke.set(e);
            } catch (UnsupportedEncodingException e) {
                uee.set(e);
            }
        });
        if (ie.get() != null){
            throw ie.get();
        }
        if (ke.get() != null){
            throw ke.get();
        }
        if (uee.get() != null){
            throw uee.get();
        }

        System.out.println("data: " + nodes);
        return nodes;
    }

    public void close() throws InterruptedException {
        zoo.close();
    }
}
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKConnection {
    private ZooKeeper zoo;
    CountDownLatch connectionLatch = new CountDownLatch(1);

    // ...

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

    public void set(String path, String data) throws KeeperException, InterruptedException {
        Stat stat = zoo.exists(path, true);

        if (stat == null)
            zoo.create(
                    path,
                    data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        System.out.println("definetly created");
        zoo.setData(path, data.getBytes(), zoo.exists(path, true).getVersion());
    }

    public Object getZNodeData(String path, boolean watchFlag)
            throws KeeperException,
            InterruptedException {

        byte[] b = null;
        zoo.getChildren(path, true).
        b = zoo.getData(path, null, null);
        return new String(b, "UTF-8");
    }

    public void close() throws InterruptedException {
        zoo.close();
    }
}
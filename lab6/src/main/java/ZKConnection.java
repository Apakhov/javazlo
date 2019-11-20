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

    public void close() throws InterruptedException {
        zoo.close();
    }
}
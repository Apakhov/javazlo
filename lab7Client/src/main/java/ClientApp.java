import com.sun.istack.internal.Nullable;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class ClientApp {
    private static final String PROXY_ADDR = "tcp://localhost:5555";

    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(SocketType.REQ);
        socket.connect(args.length > 1 ? args[1] : PROXY_ADDR);
        try {
            System.out.println("connected!");

            Scanner scanner = new Scanner(System.in);

            String line;
            MSGCommand cmd;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                System.out.println("line: " + line);
                cmd = MSGCommand.ParseLine(line);
                System.out.println("cmd: " + cmd);
                assert cmd != null;
                cmd.send(socket);
                System.out.println("sent, receiving");
                String reply = socket.recvStr(0);
                System.out.println(reply);
            }
        } finally {
            context.close();
        }
    }
}

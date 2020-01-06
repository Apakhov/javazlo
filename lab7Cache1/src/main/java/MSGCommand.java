import org.zeromq.ZMQ;

public class MSGCommand {
    private String cmd = "";
    private Integer space;
    private String val = null;

    private MSGCommand(String cmd, int space, String val) {
        this.cmd = cmd;
        this.space = space;
        this.val = val;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    static public MSGCommand ParseLine(String s) {
        String[] lines = s.split(" ");

        if (lines.length == 3)
            return new MSGCommand(lines[0], Integer.parseInt(lines[1]), lines[2]);
        else if (lines.length == 2)
            return new MSGCommand(lines[0], Integer.parseInt(lines[1]), null);

        return null;
    }

    public void send(ZMQ.Socket socket) {
        socket.send(cmd + " " + space + (val != null ? " " + val : ""), 0);
    }

    @Override
    public String toString() {
        return super.toString() + "{" + cmd + ", " + space + ", " + val + "}";
    }
}

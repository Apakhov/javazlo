package Messages;

public class TestRequest {
    public final String url;
    public final int count;


    public TestRequest(String url, int count) {
        this.url = url;
        this.count = count;
    }

    @Override
    public String toString() {
        return "URL:"+url
                + " COUNT:"+count;
    }
}

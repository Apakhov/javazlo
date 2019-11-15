public class StoreResp {
    public final boolean hasInfo;
    public final Long timing;


    private StoreResp(boolean hasInfo, Long timing) {
        this.hasInfo = hasInfo;
        this.timing = timing;
    }

    public static StoreResp noInfo(){
        return new StoreResp(false, 0L);
    }

    public static StoreResp withInfo(Long t){
        return new StoreResp(false, t);
    }
}

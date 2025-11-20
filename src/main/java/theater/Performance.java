package theater;

/**
 * Class representing a performance of a play..
 */
public class Performance {

    private String playID;
    private int audience;

    public Performance(String playID, int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    public int getAudience() {
        return this.audience;
    }

    public String getPlayID() {
        return this.playID;
    }
}

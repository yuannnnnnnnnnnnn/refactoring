package theater;

/**
 * Class representing a play..
 */

public class Play {

    private String name;
    private String type;

    public Play(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

}

package kingdom.gem;

public class Diamond implements Gem {
    @Override
    public String getName() {
        return "Diamond";
    }

    @Override
    public int getValue() {
        return 100;
    }
}

package kingdom.gem;

public class Jewel implements Gem {
    @Override
    public String getName() {
        return "Jewel";
    }

    @Override
    public int getValue() {
        return 60;
    }
}

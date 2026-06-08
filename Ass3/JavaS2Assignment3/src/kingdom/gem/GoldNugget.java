package kingdom.gem;

public class GoldNugget implements Gem {
    @Override
    public String getName() {
        return "Gold Nugget";
    }

    @Override
    public int getValue() {
        return 45;
    }
}

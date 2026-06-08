package kingdom.gem;

public class WoodenCoin implements Gem {
    @Override
    public String getName() {
        return "Wooden Coin";
    }

    @Override
    public int getValue() {
        return 10;
    }
}

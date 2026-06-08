package kingdom.gem;

public class SlowMiningStrategy implements MineStrategy {
    @Override
    public Gem mine(GemMine mine) {
        return mine.getRandomGem();
    }

    @Override
    public long getDelayMillis() {
        return 420;
    }
}

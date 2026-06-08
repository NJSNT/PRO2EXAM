package kingdom.gem;

public interface MineStrategy {
    Gem mine(GemMine mine);

    long getDelayMillis();
}

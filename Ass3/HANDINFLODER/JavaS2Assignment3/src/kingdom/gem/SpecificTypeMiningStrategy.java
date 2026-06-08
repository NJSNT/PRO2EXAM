package kingdom.gem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpecificTypeMiningStrategy implements MineStrategy {
    private final List<String> allowedTypes;
    private final Random random = new Random();

    public SpecificTypeMiningStrategy(List<String> allowedTypes) {
        this.allowedTypes = new ArrayList<>(allowedTypes);
    }

    @Override
    public Gem mine(GemMine mine) {
        if (allowedTypes.isEmpty()) {
            return mine.getRandomGem();
        }
        String type = allowedTypes.get(random.nextInt(allowedTypes.size()));
        Gem gem = mine.getGem(type);
        if (gem == null) {
            return mine.getRandomGem();
        }
        return gem;
    }

    @Override
    public long getDelayMillis() {
        return 260;
    }
}

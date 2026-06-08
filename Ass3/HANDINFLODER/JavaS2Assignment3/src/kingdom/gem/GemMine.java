package kingdom.gem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GemMine {
    private final Map<String, Gem> gemMap;
    private final List<String> keys;
    private final Random random;

    public GemMine() {
        gemMap = new HashMap<>();
        random = new Random();
        gemMap.put("Diamond", new Diamond());
        gemMap.put("Gold Nugget", new GoldNugget());
        gemMap.put("Jewel", new Jewel());
        gemMap.put("Ruby", new Ruby());
        gemMap.put("Wooden Coin", new WoodenCoin());
        gemMap.put("Cow", new Cow());
        keys = new ArrayList<>(gemMap.keySet());
        Collections.sort(keys);
    }

    public Gem getGem(String type) {
        return gemMap.get(type);
    }

    public Gem getRandomGem() {
        if (keys.isEmpty()) {
            return null;
        }
        String key = keys.get(random.nextInt(keys.size()));
        return gemMap.get(key);
    }

    public Gem getGemByIndex(int index) {
        if (index < 0 || index >= keys.size()) {
            return null;
        }
        return gemMap.get(keys.get(index));
    }

    public List<String> getGemTypes() {
        return new ArrayList<>(keys);
    }
}

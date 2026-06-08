package kingdom.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kingdom.gem.Gem;
import kingdom.room.TreasureRoomDoor;
import kingdom.util.Catalogue;

public class King implements Runnable {
    private final String name;
    private final TreasureRoomDoor treasureRoomDoor;
    private final Random random;

    public King(String name, TreasureRoomDoor treasureRoomDoor) {
        this.name = name;
        this.treasureRoomDoor = treasureRoomDoor;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            int target = 50 + random.nextInt(101);
            Catalogue.getInstance().log(name + " wants to host a party costing " + target + " value.");
            treasureRoomDoor.acquireWriteAccess(name);
            try {
                List<Gem> selected = new ArrayList<>();
                int total = 0;
                Gem gem;
                while (total < target && (gem = treasureRoomDoor.retrieveValuable()) != null) {
                    selected.add(gem);
                    total += gem.getValue();
                    Catalogue.getInstance().log(name + " inspected " + gem.getName() + " worth " + gem.getValue() + " (running total " + total + ").");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (total < target) {
                    Catalogue.getInstance().log(name + " could not meet the party cost and cancels the party. Returning " + selected.size() + " gem(s).");
                    for (Gem returnGem : selected) {
                        treasureRoomDoor.addValuable(returnGem);
                    }
                } else {
                    Catalogue.getInstance().log(name + " held a party and spent " + total + " worth of gems.");
                }
            } finally {
                treasureRoomDoor.releaseWriteAccess(name);
            }

            try {
                Thread.sleep(800 + random.nextInt(401));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

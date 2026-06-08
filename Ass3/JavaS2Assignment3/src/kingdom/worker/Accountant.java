package kingdom.worker;

import java.util.List;
import java.util.Random;

import kingdom.gem.Gem;
import kingdom.room.TreasureRoomDoor;
import kingdom.util.Catalogue;

public class Accountant implements Runnable {
    private final String name;
    private final TreasureRoomDoor treasureRoomDoor;
    private final Random random;

    public Accountant(String name, TreasureRoomDoor treasureRoomDoor) {
        this.name = name;
        this.treasureRoomDoor = treasureRoomDoor;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            treasureRoomDoor.acquireReadAccess(name);
            try {
                List<Gem> gems = treasureRoomDoor.lookAtAllGems();
                Thread.sleep(150 + random.nextInt(201));
                int total = 0;
                for (Gem gem : gems) {
                    total += gem.getValue();
                }
                Catalogue.getInstance().log(name + " counted total treasure worth " + total + " from " + gems.size() + " gems.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                treasureRoomDoor.releaseReadAccess(name);
            }
            try {
                Thread.sleep(400 + random.nextInt(301));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

package kingdom.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kingdom.gem.Gem;
import kingdom.room.TreasureRoomDoor;
import kingdom.storage.GemDeposit;
import kingdom.util.Catalogue;

public class GemTransporter implements Runnable {
    private final String name;
    private final GemDeposit deposit;
    private final TreasureRoomDoor treasureRoomDoor;
    private final Random random;

    public GemTransporter(String name, GemDeposit deposit, TreasureRoomDoor treasureRoomDoor) {
        this.name = name;
        this.deposit = deposit;
        this.treasureRoomDoor = treasureRoomDoor;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            int target = 50 + random.nextInt(151);
            int current = 0;
            List<Gem> carried = new ArrayList<>();
            Catalogue.getInstance().log(name + " set a target worth of " + target + " before moving gems.");

            while (current < target) {
                Gem gem = deposit.take();
                if (gem == null) {
                    break;
                }
                carried.add(gem);
                current += gem.getValue();
                Catalogue.getInstance().log(name + " collected " + gem.getName() + " worth " + gem.getValue() + " (total " + current + ").");
            }

            if (!carried.isEmpty()) {
                treasureRoomDoor.acquireWriteAccess(name);
                for (Gem gem : carried) {
                    treasureRoomDoor.addValuable(gem);
                    Catalogue.getInstance().log(name + " transported " + gem.getName() + " into the TreasureRoom.");
                }
                treasureRoomDoor.releaseWriteAccess(name);
                carried.clear();
            }

            try {
                Thread.sleep(200 + random.nextInt(301));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

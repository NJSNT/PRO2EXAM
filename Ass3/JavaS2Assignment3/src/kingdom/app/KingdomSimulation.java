package kingdom.app;

import java.util.Arrays;
import java.util.List;

import kingdom.gem.FastMiningStrategy;
import kingdom.gem.GemMine;
import kingdom.room.TreasureRoom;
import kingdom.room.TreasureRoomGuard;
import kingdom.storage.GemDeposit;
import kingdom.util.Catalogue;
import kingdom.worker.Accountant;
import kingdom.worker.GemMineWorker;
import kingdom.worker.GemTransporter;
import kingdom.worker.King;
import kingdom.gem.SpecificTypeMiningStrategy;

public class KingdomSimulation {
    public static void main(String[] args) {
        Catalogue.getInstance().log("Kingdom simulation starting.");

        GemMine mine = new GemMine();
        GemDeposit deposit = new GemDeposit();
        TreasureRoom treasureRoom = new TreasureRoom();
        TreasureRoomGuard guard = new TreasureRoomGuard(treasureRoom);

        Thread worker1 = new Thread(new GemMineWorker("Miner A", mine, deposit, new FastMiningStrategy()));
        Thread worker2 = new Thread(new GemMineWorker("Miner B", mine, deposit, new SpecificTypeMiningStrategy(Arrays.asList("Diamond", "Ruby"))));
        Thread transporter1 = new Thread(new GemTransporter("Transporter A", deposit, guard));
        Thread transporter2 = new Thread(new GemTransporter("Transporter B", deposit, guard));
        Thread accountant = new Thread(new Accountant("Accountant", guard));
        Thread king = new Thread(new King("King", guard));

        List<Thread> threads = Arrays.asList(worker1, worker2, transporter1, transporter2, accountant, king);
        threads.forEach(thread -> {
            thread.setDaemon(false);
            thread.start();
        });

        try {
            while (true) {
                Thread.sleep(10_000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

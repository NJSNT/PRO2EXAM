package kingdom.worker;

import kingdom.gem.Gem;
import kingdom.gem.GemMine;
import kingdom.gem.MineStrategy;
import kingdom.storage.GemDeposit;
import kingdom.util.Catalogue;

public class GemMineWorker implements Runnable {
    private final String name;
    private final GemMine mine;
    private final GemDeposit deposit;
    private final MineStrategy strategy;

    public GemMineWorker(String name, GemMine mine, GemDeposit deposit, MineStrategy strategy) {
        this.name = name;
        this.mine = mine;
        this.deposit = deposit;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        while (true) {
            Gem gem = strategy.mine(mine);
            if (gem == null) {
                Catalogue.getInstance().log(name + " could not mine a gem.");
            } else {
                deposit.put(gem);
                Catalogue.getInstance().log(name + " produced " + gem.getName() + " (" + gem.getValue() + ")");
            }
            try {
                Thread.sleep(strategy.getDelayMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

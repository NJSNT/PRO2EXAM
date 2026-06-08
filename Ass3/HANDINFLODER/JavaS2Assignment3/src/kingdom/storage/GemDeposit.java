package kingdom.storage;

import MyArrayList.MyArrayList;
import kingdom.gem.Gem;
import kingdom.util.Catalogue;

public class GemDeposit {
    private final MyArrayList<Gem> buffer;

    public GemDeposit() {
        buffer = new MyArrayList<>();
    }

    public synchronized void put(Gem gem) {
        buffer.add(gem);
        Catalogue.getInstance().log("GemDeposit received gem: " + gem.getName() + " (" + gem.getValue() + ")");
        notifyAll();
    }

    public synchronized Gem take() {
        while (buffer.isEmpty()) {
            try {
                Catalogue.getInstance().log("GemDeposit is empty, transporter is waiting.");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        Gem gem = buffer.remove(0);
        Catalogue.getInstance().log("GemDeposit released gem: " + gem.getName() + " (" + gem.getValue() + ")");
        notifyAll();
        return gem;
    }

    public synchronized int size() {
        return buffer.size();
    }
}

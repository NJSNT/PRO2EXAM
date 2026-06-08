package kingdom.room;

import java.util.List;

import kingdom.gem.Gem;
import kingdom.util.Catalogue;

public class TreasureRoomGuard implements TreasureRoomDoor {
    private final TreasureRoom treasureRoom;
    private int readers;
    private int writers;
    private int writeRequests;

    public TreasureRoomGuard(TreasureRoom treasureRoom) {
        this.treasureRoom = treasureRoom;
    }

    @Override
    public synchronized void acquireReadAccess(String actorName) {
        Catalogue.getInstance().log(actorName + " is requesting guarded read access.");
        while (writers > 0 || writeRequests > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        readers++;
        Catalogue.getInstance().log(actorName + " acquired guarded read access. Readers=" + readers);
    }

    @Override
    public synchronized void acquireWriteAccess(String actorName) {
        Catalogue.getInstance().log(actorName + " is requesting guarded write access.");
        writeRequests++;
        while (readers > 0 || writers > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                writeRequests--;
                return;
            }
        }
        writeRequests--;
        writers++;
        Catalogue.getInstance().log(actorName + " acquired guarded write access. Writers=" + writers);
    }

    @Override
    public synchronized void releaseReadAccess(String actorName) {
        readers--;
        Catalogue.getInstance().log(actorName + " released guarded read access. Readers=" + readers);
        notifyAll();
    }

    @Override
    public synchronized void releaseWriteAccess(String actorName) {
        writers--;
        Catalogue.getInstance().log(actorName + " released guarded write access. Writers=" + writers);
        notifyAll();
    }

    @Override
    public Gem retrieveValuable() {
        return treasureRoom.retrieveValuable();
    }

    @Override
    public void addValuable(Gem v) {
        treasureRoom.addValuable(v);
    }

    @Override
    public List<Gem> lookAtAllGems() {
        return treasureRoom.lookAtAllGems();
    }
}

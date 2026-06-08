package kingdom.room;

import java.util.ArrayList;
import java.util.List;

import kingdom.gem.Gem;
import kingdom.util.Catalogue;

public class TreasureRoom implements TreasureRoomDoor {
    private final List<Gem> gems;

    public TreasureRoom() {
        gems = new ArrayList<>();
    }

    @Override
    public void acquireReadAccess(String actorName) {
        Catalogue.getInstance().log(actorName + " is requesting read access to the TreasureRoom.");
    }

    @Override
    public void acquireWriteAccess(String actorName) {
        Catalogue.getInstance().log(actorName + " is requesting write access to the TreasureRoom.");
    }

    @Override
    public void releaseReadAccess(String actorName) {
        Catalogue.getInstance().log(actorName + " has released read access to the TreasureRoom.");
    }

    @Override
    public void releaseWriteAccess(String actorName) {
        Catalogue.getInstance().log(actorName + " has released write access to the TreasureRoom.");
    }

    @Override
    public synchronized Gem retrieveValuable() {
        Gem v = null;
        if (!gems.isEmpty()) {
            v = gems.remove(0);
            Catalogue.getInstance().log("TreasureRoom removed gem: " + v.getName() + " (" + v.getValue() + ")");
        } else {
            Catalogue.getInstance().log("TreasureRoom is empty when trying to retrieve a gem.");
        }
        return v;
    }

    @Override
    public synchronized void addValuable(Gem v) {
        gems.add(v);
        Catalogue.getInstance().log("TreasureRoom added gem: " + v.getName() + " (" + v.getValue() + ")");
    }

    @Override
    public synchronized List<Gem> lookAtAllGems() {
        Catalogue.getInstance().log("TreasureRoom is being inspected. Gem count: " + gems.size());
        return new ArrayList<>(gems);
    }
}

package spaceraze.world;

public class PlayerSpaceshipType extends SpaceshipImprovements {

    private boolean availableToBuild;
    private int nrProduced = 0;

    public PlayerSpaceshipType(String typeId, boolean availableToBuild){
        super(typeId);
        this.availableToBuild = availableToBuild;
    }

    public boolean isAvailableToBuild() {
        return availableToBuild;
    }

    public void setAvailableToBuild(boolean availableToBuild) {
        this.availableToBuild = availableToBuild;
    }

    public int getNrProduced() {
        return nrProduced;
    }

    public int updateNrProduced(){
        return ++nrProduced;
    }
}

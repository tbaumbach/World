package spaceraze.world;

public class PlayerSpaceshipImprovement extends SpaceshipImprovements {

    private boolean availableToBuild;
    private int nrProduced = 0;

    public PlayerSpaceshipImprovement(String typeId, boolean availableToBuild){
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

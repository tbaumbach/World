package spaceraze.world;

public class PlayerTroopImprovement extends TroopImprovements {

    private boolean availableToBuild;
    private int nrProduced = 0;

    public PlayerTroopImprovement(String typeId, boolean availableToBuild){
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

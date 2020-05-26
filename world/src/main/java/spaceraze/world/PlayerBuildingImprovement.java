package spaceraze.world;

public class PlayerBuildingImprovement extends BuildingImprovement {

    private boolean developed;
    private int nrProduced = 0;

    public PlayerBuildingImprovement(String name, boolean availableToBuild){
        super(name);
        this.developed = availableToBuild;
    }

    public boolean isDeveloped() {
        return developed;
    }

    public void setDeveloped(boolean developed) {
        this.developed = developed;
    }

    public int getNrProduced() {
        return nrProduced;
    }

    public int updateNrProduced(){
        return ++nrProduced;
    }
}

package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "PLAYER_BUILDING_IMPROVEMENT")
public class PlayerBuildingImprovement extends BuildingImprovement {

    @ManyToOne
    @JoinColumn(name = "FK_PLAYER")
    private Player player;
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

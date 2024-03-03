package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "PLAYER_TROOP_IMPROVEMENT")
public class PlayerTroopImprovement extends TroopImprovements {

    @ManyToOne
    @JoinColumn(name = "FK_PLAYER")
    private Player player;
    private boolean availableToBuild;
    private int nrProduced = 0;

    public PlayerTroopImprovement(String troopTypeUuid, boolean availableToBuild){
        super(troopTypeUuid);
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

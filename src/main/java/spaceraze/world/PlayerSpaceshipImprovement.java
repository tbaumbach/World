package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "PLAYER_SPACESHIP_IMPROVEMENT")
public class PlayerSpaceshipImprovement extends SpaceshipImprovements {

    @ManyToOne
    @JoinColumn(name = "FK_PLAYER")
    private Player player;
    private boolean availableToBuild;
    private int nrProduced = 0;

    public PlayerSpaceshipImprovement(String typeUuid, boolean availableToBuild){
        super(typeUuid);
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

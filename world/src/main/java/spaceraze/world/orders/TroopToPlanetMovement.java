package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Troop;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "TROOP_PLANET_MOVMENT")
public class TroopToPlanetMovement implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

    private String troopKey;
    private String planetName;
    private int turn;

    public TroopToPlanetMovement(Troop troop, Planet destination, int turn) {
        this.troopKey = troop.getKey();
        this.planetName = destination.getName();
        this.turn = turn;
    }

    public String getDestinationName() {
        return planetName;
    }

    public Planet getDestination(Galaxy aGalaxy) {
        return aGalaxy.getPlanet(planetName);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
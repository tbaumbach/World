package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Troop;
import spaceraze.world.TurnInfo;

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

    private int troopId;
    private String planetName;
    private int turn;

    public TroopToPlanetMovement(Troop theTroop, Planet destination, int turn) {
        this.troopId = theTroop.getUniqueId();
        this.planetName = destination.getName();
        this.turn = turn;
    }


    public void performMove(TurnInfo ti, Galaxy aGalaxy) {
        Troop aTroop = aGalaxy.findTroop(troopId);
        Planet aPlanet = aGalaxy.getPlanet(planetName);
        if (aTroop == null || aPlanet == null) {
            Logger.severe("performMove Error: troopId= " + troopId + " planetName= " + planetName);
        } else {
            Logger.finest("performMove: " + aTroop.getUniqueName() + " destination: " + aPlanet.getName());
            aTroop.move(aPlanet, ti);
            aTroop.setLastPlanetMoveTurn(turn);
        }
    }

    public String getDestinationName() {
        return planetName;
    }

    public Planet getDestination(Galaxy aGalaxy) {
        return aGalaxy.getPlanet(planetName);
    }

    public int getTroopId() {
        return troopId;
    }

    public Troop getTroop(Galaxy aGalaxy) {
        return aGalaxy.findTroop(troopId);
    }

    public String getText(Galaxy aGalaxy) {
        Troop aTroop = aGalaxy.findTroop(troopId);
        return "Move " + aTroop.getUniqueName() + " from " + aTroop.getShipLocation().getName() + " to " + getDestinationName() + ".";
    }

    public boolean isThisTroop(Troop aTroop) {
        return aTroop.getUniqueId() == troopId;
    }

    public boolean isThisDestination(Planet aPlanet) {
        return aPlanet.getName().equalsIgnoreCase(planetName);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
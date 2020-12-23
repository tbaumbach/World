package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Spaceship;
import spaceraze.world.Troop;
import spaceraze.world.TurnInfo;
import spaceraze.world.VIP;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "VIP_MOVMENT")
public class VIPMovement implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

    private String vipId;
    private String planetDestination = null;
    private String shipDestination = null;
    private int troopDestination;

    public VIPMovement(VIP aVIP, Planet planetDestination) {
        this.vipId = aVIP.getUniqueId();
        this.planetDestination = planetDestination.getName();
    }

    public VIPMovement(VIP aVIP, Spaceship shipDestination) {
        this.vipId = aVIP.getUniqueId();
        this.shipDestination = shipDestination.getName();
    }

    public VIPMovement(VIP aVIP, Troop troopDestination) {
        this.vipId = aVIP.getUniqueId();
        this.troopDestination = troopDestination.getUniqueId();
    }


    public String getPlanetDestinationName() {
        return planetDestination;
    }

    public boolean isPlanetMove() {
        return planetDestination != null;
    }

    public boolean isShipMove() {
        return shipDestination != null;
    }

    public void performMove(TurnInfo ti, Galaxy aGalaxy) {
        VIP tempVIP = aGalaxy.findVIP(vipId);
        if (isPlanetMove()) {
            tempVIP.moveVIP(aGalaxy.getPlanet(planetDestination), ti);
        } else if (isShipMove()) {
            tempVIP.moveVIP(aGalaxy.findSpaceship(shipDestination, tempVIP.getBoss()), ti);
        } else { // troop move
            tempVIP.moveVIP(aGalaxy.findTroop(troopDestination), ti);
        }
    }

    public String getDestinationName(boolean longName, Galaxy aGalaxy) {
        String returnValue = "";
        if (isPlanetMove()) {
            returnValue = planetDestination;
        } else if (isShipMove()) {
            if (longName) {
                returnValue = aGalaxy.findSpaceship(shipDestination, aGalaxy.findVIP(vipId).getBoss()).getUniqueName();
            } else {
                returnValue = aGalaxy.findSpaceship(shipDestination, aGalaxy.findVIP(vipId).getBoss()).getShortName();
            }
        } else { // troop
            if (longName) {
                returnValue = aGalaxy.findTroop(troopDestination).getName();
            } else {
                returnValue = aGalaxy.findTroop(troopDestination).getShortName();
            }

        }
        return returnValue;
    }

    /*
      public int getShipDestinationId(){
        return shipDestination.getId();
      }
    */
    public String getVIPId() {
        return vipId;
    }

    public int getTroopId() {
        return troopDestination;
    }

    public String getText(Galaxy aGalaxy) {
        return "Move " + aGalaxy.findVIP(vipId).getName() + " from " + aGalaxy.findVIP(vipId).getLocationString() + " to " + getDestinationName(true, aGalaxy) + ".";
    }

    public boolean isThisVIP(VIP tempVIP) {
        return this.vipId.equals(tempVIP.getUniqueId());
    }
}

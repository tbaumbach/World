//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Galaxy;
import spaceraze.world.Spaceship;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "SHIP_CARRIER_MOVE")
public class ShipToCarrierMovement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

    private String spaceShipUniqueId;
    private String destinationCarrier;
    //Spaceship ss;
    //Spaceship destinationCarrier;

    public ShipToCarrierMovement(Spaceship ss, Spaceship destinationCarrier) {
        this.spaceShipUniqueId = ss.getUniqueId();
        this.destinationCarrier = destinationCarrier.getUniqueId();
    }

    public String getDestinationCarrierId() {
        return destinationCarrier;
    }

    public Spaceship getDestinationCarrier(Galaxy aGalaxy) {
        Spaceship aSpaceshipCarrier = aGalaxy.findSpaceshipByUniqueId(destinationCarrier);
        return aSpaceshipCarrier;
    }

    public String getSpaceshipId() {
        return spaceShipUniqueId;
    }

  /* beh�vs ej???
  public String getSpaceshipUniqueName(){
    return ss.getUniqueName();
  }
  */

    public String getText(Galaxy aGalaxy) {
        Spaceship aSpaceship = aGalaxy.findSpaceshipByUniqueId(spaceShipUniqueId);
        Spaceship aSpaceshipCarrier = aGalaxy.findSpaceshipByUniqueId(destinationCarrier);

        String retStr = null;
        if (aSpaceship.getLocation() != null) {
            retStr = "Move " + aSpaceship.getName() + " from " + aSpaceship.getLocation().getName() + " to " + aSpaceshipCarrier.getName() + ".";
        } else {
            retStr = "Move " + aSpaceship.getName() + " from " + aSpaceship.getCarrierLocation().getName() + " to " + aSpaceshipCarrier.getName() + ".";
        }
        return retStr;
    }

    public boolean isThisShip(Spaceship sSpaceship) {
        return sSpaceship.getUniqueId() == spaceShipUniqueId;
    }

    public boolean isThisDestination(Spaceship aCarrier) {
        return aCarrier.getUniqueId() == destinationCarrier;
    }
}
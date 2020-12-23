//Title:        SpaceRaze
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
import spaceraze.world.Troop;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "TROOP_CARRIER_MOVMENT")
public class TroopToCarrierMovement implements Serializable{
  private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

  int troopId;
  String destinationCarrierId;
  
  //Troop troop;
  //Spaceship destinationCarrier;

  public TroopToCarrierMovement(Troop theTroop, Spaceship destinationCarrier){
    this.troopId = theTroop.getUniqueId();
    this.destinationCarrierId = destinationCarrier.getKey();
  }

  public String getDestinationCarrierId(){
    return destinationCarrierId;
  }

  public Spaceship getDestinationCarrier(Galaxy aGalaxy){
	  return aGalaxy.findSpaceshipByUniqueId(destinationCarrierId);
  }

  public int getTroopId(){
	  return troopId;
  }

  public String getText(Galaxy aGalaxy){
	  Troop aTroop = aGalaxy.findTroop(troopId);
	  Spaceship destinationCarrier = aGalaxy.findSpaceshipByUniqueId(destinationCarrierId);
	  String retStr = null;
	  if (aTroop.getPlanetLocation() != null){
		  retStr = "Move " + aTroop.getName() + " from " + aTroop.getPlanetLocation().getName() + " to " + destinationCarrier.getName() + ".";
	  }else{ // move from ship to ship
		  retStr = "Move " + aTroop.getName() + " from " + aTroop.getShipLocation().getName() + " to " + destinationCarrier.getName() + ".";
	  }
    return retStr;
  }

  public boolean isThisTroop(Troop aTroop){
    return aTroop.getUniqueId() == troopId;
  }

  public boolean isThisDestination(Spaceship aCarrier){
	  return aCarrier.getKey() == destinationCarrierId;
  }
}
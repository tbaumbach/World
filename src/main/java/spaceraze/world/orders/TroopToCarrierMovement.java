//Title:        SpaceRaze
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world.orders;
import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

  String troopKey;
  String destinationCarrierKey;

  public TroopToCarrierMovement(Troop theTroop, Spaceship destinationCarrier){
    this.troopKey = theTroop.getUuid();
    this.destinationCarrierKey = destinationCarrier.getUuid();
  }

  public String getDestinationCarrierKey(){
    return destinationCarrierKey;
  }

  public String getTroopKey(){
	  return troopKey;
  }

}
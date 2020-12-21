package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Spaceship;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "ORDERS")
public class ShipMovement implements Serializable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_ORDERS")
	private Orders orders;

	private String planetName;
	private String owner;
	private String spaceShipID;
	
	public ShipMovement(Spaceship ss, Planet destination) {
		this.spaceShipID = ss.getKey();
		this.planetName = destination.getName();
		this.owner = ss.getOwner().getName();
	}

	/*
	 * // anropas vid uppdatering av en spelares orders public
	 * ShipMovement(ShipMovement oldShipMove,Galaxy newGalaxy){ destination =
	 * newGalaxy.findPlanet(oldShipMove.getDestinationName()); ss =
	 * newGalaxy.findSpaceship(oldShipMove.getSpaceshipId()); }
	 */


	/*
	 * public int getSpaceshipId(){ return ss.getId(); }
	 */

	/*
	 * beh�vs ej??? public String getSpaceshipUniqueName(){ return
	 * ss.getUniqueName(); }
	 */

	public String getText(Galaxy aGalaxy) {
		String spaceShipname = aGalaxy.findSpaceshipByUniqueId(spaceShipID).getName();
		return "Move " + spaceShipname + " from "
				+ aGalaxy.findSpaceship(spaceShipname, aGalaxy.getPlayer(owner)).getLocation().getName() + " to "
				+ planetName + ".";
	}

	public boolean isThisShip(Spaceship sSpaceship) {
		return sSpaceship.getKey() == spaceShipID;
	}

	public String getDestinationName() {
		return planetName;
	}

	public String getSpaceShipID() {
		return spaceShipID;
	}

	public String getOwner() {
		return owner;
	}

}
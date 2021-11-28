package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Planet;
import spaceraze.world.Spaceship;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "SHIP_MOVMENT")
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
	private String spaceshipKey;
	
	public ShipMovement(Spaceship ss, Planet destination) {
		this.spaceshipKey = ss.getKey();
		this.planetName = destination.getName();
		this.owner = ss.getOwner().getName();
	}

	public String getDestinationName() {
		return planetName;
	}

	public String getSpaceshipKey() {
		return spaceshipKey;
	}

	public String getOwner() {
		return owner;
	}

}
package spaceraze.world.mapinfo;

import java.io.Serializable;

import lombok.*;
import spaceraze.util.general.Logger;
import spaceraze.world.PlanetConnection;

import javax.persistence.*;

/**
 * Används för att hantera kart-informationen för en enskild starport-koppling mellan två planeter för ett visst drag för en viss spelare. Bara egna kopplingar till att börja med
 *
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "MAP_CONNECTION_INFO")
public class MapConnectionInfo implements Serializable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_MAP_INFO_TURN")
	private MapInfoTurn mapInfoTurn;

	//@Column(name = "Planet_ONE")
	private String planet1;
	//@Column(name = "PLANET TWO")
	private String planet2;

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Starport connection: planet1: " + planet1);
		sb.append(", ");
		sb.append("planet2: " + planet2);
		return sb.toString();
	}
	
	/**
	 * Skapa info om kopplingen mellan tv� planeter s� som spelaren player k�nner till det just nu, f�r att beskriva hur
	 * player:s karta kommer att se ut d� player g�r sitt n�sta drag.
	 */
	public MapConnectionInfo(PlanetConnection planetConnection){
		planet1 = planetConnection.getPlanetOne().getName();
		planet2 = planetConnection.getPlanetTwo().getName();
		Logger.finer("MapConnectionInfo creator, planet1: " + planet1 + ", planet2: " + planet2);
	}
	
	public boolean isStarPortConnection(String aPlanetName1, String aPlanetName2){
		boolean isStarPortConnection = false;
		if ((aPlanetName1.equals(planet1)) & (aPlanetName2.equals(planet2))){
			isStarPortConnection = true;
		}else
		if ((aPlanetName1.equals(planet2)) & (aPlanetName2.equals(planet1))){
			isStarPortConnection = true;
		}
		return isStarPortConnection;
	}

}

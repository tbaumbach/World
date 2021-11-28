package spaceraze.world.mapinfo;

import java.io.Serializable;
import java.util.*;

import lombok.*;
import spaceraze.world.Planet;

import javax.persistence.*;

/**
 * Håller all kart-information för planeter för en spelare för ett specifikt drag
 * 
 * @author Paul Bodin
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "MAP_INOF_TURN")
public class MapInfoTurn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mapInfoTurn")
	@Builder.Default
	private List<MapPlanetInfo> allPlanetInfos = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mapInfoTurn")
	@Builder.Default
	private List<MapConnectionInfo> starPortConnections = new ArrayList<>();

	public MapPlanetInfo getMapPlanetInfo(Planet planet) {
		MapPlanetInfo mapPlanetInfo = allPlanetInfos.stream().filter(mapPlanetInfo1 -> mapPlanetInfo1.getPlanetName().equals(planet.getName())).findFirst().orElseThrow();
		return mapPlanetInfo;
	}

	public boolean isStarPortConnection(String aPlanetName1, String aPlanetName2){
		boolean found = false;
		int i = 0;
		while ((!found) & (i < starPortConnections.size())){
			MapConnectionInfo aMapConnectionInfo = starPortConnections.get(i);
			if (aMapConnectionInfo.isStarPortConnection(aPlanetName1, aPlanetName2)){
				found = true;
			}else{
				i++;
			}
		}
		return found;
	}
	
}

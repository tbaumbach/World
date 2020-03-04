package spaceraze.world.mapinfo;

import java.io.Serializable;

import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.PlanetConnection;
import spaceraze.world.Player;

/**
 * Anv�nds f�r att hantera kart-informationen f�r en enskild starport-koppling mellan tv� planeter f�r ett visst drag f�r en viss spelare
 * 
 * @author Paul Bodin
 *
 */
public class MapConnectionInfo implements Serializable {
	static final long serialVersionUID = 1L;
	
	private String planet1,planet2;

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
		planet1 = planetConnection.getPlanet1().getName();
		planet2 = planetConnection.getPlanet2().getName();
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

    public static boolean isStarPortConnections(PlanetConnection planetConnection, Player player){
    	boolean haveStarPort = false;
    	Galaxy g = player.getGalaxy();
        Planet p1 = planetConnection.getPlanet1();
        Planet p2 = planetConnection.getPlanet2();
      	// check if starport make this connection short range
      	if ((p1.getPlayerInControl() != null) & (p2.getPlayerInControl() != null)){ // none of the planets are neutral
  			if ((p1.hasSpacePort()) & (p2.hasSpacePort())){ // both have a spacestation
  				if (g.getDiplomacy().friendlySpaceports(p1.getPlayerInControl(),p2.getPlayerInControl())){
      				if (g.getDiplomacy().friendlySpaceports(player,p1.getPlayerInControl())){
          				if (g.getDiplomacy().friendlySpaceports(player,p2.getPlayerInControl())){
          					haveStarPort = true;
          				}	
          			}
          		}
      		}
      	}
      	return haveStarPort;
    }

}

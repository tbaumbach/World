/*
 * Created on 2005-aug-26
 */
package spaceraze.world.comparator;

import java.util.Comparator;

import spaceraze.world.Spaceship;

/**
 * @author WMPABOD
 *
 * Compares two spaceships, biggest/most expensive first
 */
public class SpaceshipComparator implements Comparator<Spaceship> {
    static final long serialVersionUID = 1L;

	public int compare(Spaceship ss1, Spaceship ss2) {
		int diff = ss2.getTonnage() - ss1.getTonnage();
		if (diff == 0){
			diff = ss2.getSpaceshipType().getBuildCost(null) - ss1.getSpaceshipType().getBuildCost(null);
		}
		if (diff == 0){
			diff = ss1.getSpaceshipType().getName().compareTo(ss2.getSpaceshipType().getName());
		}
		return diff;
	}

}

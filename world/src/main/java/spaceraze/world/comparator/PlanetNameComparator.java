/*
 * Created on 2005-aug-26
 */
package spaceraze.world.comparator;

import java.util.Comparator;

import spaceraze.world.Planet;

/**
 * @author WMPABOD
 *
 * Compares two planets alfanumerically
 */
public class PlanetNameComparator<T extends Planet> implements Comparator<T> {

	public int compare(T p1, T p2) {
		return p1.getName().compareTo(p2.getName());
	}

}

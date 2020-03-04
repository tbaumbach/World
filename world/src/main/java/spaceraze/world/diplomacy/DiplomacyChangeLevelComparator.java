/*
 * Created on 2005-aug-26
 */
package spaceraze.world.diplomacy;

import java.util.Comparator;

/**
 * @author WMPABOD
 *
 * Compares two diplomacy changes according to their level (low first)
 */
public class DiplomacyChangeLevelComparator<T extends DiplomacyChange> implements Comparator<T> {
    static final long serialVersionUID = 1L;

	public int compare(T dc1, T dc2) {
		return dc1.getNewLevel().ordinal() - dc2.getNewLevel().ordinal();
	}

}

/*
 * Created on 2005-aug-26
 */
package spaceraze.world.comparator;

import java.util.Comparator;

import spaceraze.world.Troop;
import spaceraze.world.comparator.trooptype.TroopTypeComparator;
import spaceraze.world.enums.BattleGroupPosition;

/**
 * @author WMPABOD
 *
 * Compares two spaceships, biggest/most expensive first
 */
public class TroopTypeAndBuildCostComparator implements Comparator<Troop> {
    static final long serialVersionUID = 1L;

	public int compare(Troop t1, Troop t2) {
		int diff = 0;
		if (t1.isSpaceshipTravel() & !t2.isSpaceshipTravel()){
			diff = -1;
		}else
		if (!t1.isSpaceshipTravel() & t2.isSpaceshipTravel()){
			diff = 1;
		}else
		if ((t1.getPosition() == BattleGroupPosition.SUPPORT) & (t2.getPosition() != BattleGroupPosition.SUPPORT)){
			diff = 1;
		}else
		if ((t1.getPosition() != BattleGroupPosition.SUPPORT) & (t2.getPosition() == BattleGroupPosition.SUPPORT)){
			diff = -1;
		}else
		if (!t1.isArmor() & t2.isArmor()){
			diff = 1;
		}else
		if (t1.isArmor() & !t2.isArmor()){
			diff = -1;
		}else{
			TroopTypeComparator ttc = new TroopTypeComparator();
			diff = ttc.compare(t1.getTroopType(), t2.getTroopType());
		}
		return diff;
	}

}

/*
 * Created on 2005-aug-26
 */
package spaceraze.world.comparator.trooptype;

import java.util.Comparator;

import spaceraze.world.TroopType;
import spaceraze.world.enums.BattleGroupPosition;

/**
 * @author WMPABOD
 *
 * Compares two TroopType.
 */
public class TroopTypeTypeAndBuildCostComparator implements Comparator<TroopType> {
    static final long serialVersionUID = 1L;

	public int compare(TroopType t1, TroopType t2) {
		int diff = 0;
		if (t1.isSpaceshipTravel() & !t2.isSpaceshipTravel()){
			diff = 1;
		}else
		if (!t1.isSpaceshipTravel() & t2.isSpaceshipTravel()){
			diff = -1;
		}else
		if ((t1.getDefaultPosition() == BattleGroupPosition.SUPPORT) & (t2.getDefaultPosition() != BattleGroupPosition.SUPPORT)){
			diff = -1;
		}else
		if ((t1.getDefaultPosition() != BattleGroupPosition.SUPPORT) & (t2.getDefaultPosition() == BattleGroupPosition.SUPPORT)){
			diff = 1;
		}else
		if (!t1.isArmor() & t2.isArmor()){
			diff = -1;
		}else
		if (t1.isArmor() & !t2.isArmor()){
			diff = 1;
		}else{
			TroopTypeComparator ttc = new TroopTypeComparator();
			diff = ttc.compare(t1, t2);
		}
		return diff;
	}

}

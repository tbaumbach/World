package spaceraze.world.orders;

import spaceraze.world.Galaxy;
import spaceraze.world.Troop;
import spaceraze.world.enums.BattleGroupPosition;

public class TroopBattlePositionChange {
	private int troop;
	private BattleGroupPosition newPosition;
	
	public TroopBattlePositionChange(Troop aTroop, BattleGroupPosition aNewPosition){
		this.troop = aTroop.getUniqueId();
		this.newPosition = aNewPosition;
	}
	
	public Troop getTroop(Galaxy aGalaxy){
		return aGalaxy.findTroop(troop);
	}
	
	public int getTroopID(){
		return troop;
	}
	
	public boolean isTroop(Troop aTroop){
		return aTroop.getUniqueId() == troop;
	}
	
	public BattleGroupPosition getNewPosition(){
		return newPosition;
	}

	public String getText(Galaxy aGalaxy){
		return "Change battle position for troop " + aGalaxy.findTroop(troop).getUniqueName() + " from " +  aGalaxy.findTroop(troop).getPosition() + " to " + newPosition + ".";
	}

}

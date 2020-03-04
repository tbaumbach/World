package spaceraze.world.report.landbattle;

import java.io.Serializable;


public abstract class TroopAttack implements Serializable {
	static final long serialVersionUID = 1L;
	
	private final String typeName;
	private final int currentDamageCapacity;
	private final int damageCapacity;
	private final boolean artillery; //(aPosition == BattleGroupPosition.SUPPORT) & (aTroop.getTroop().getAttackArtillery() > 0)
	
	public TroopAttack(String typeName, int damageCapacity, int currentDamageCapacity, boolean isArtillery) {
		this.typeName = typeName;
		this.damageCapacity = damageCapacity;
		this.currentDamageCapacity = currentDamageCapacity;
		this.artillery = isArtillery;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public int getCurrentDamageCapacity() {
		return currentDamageCapacity;
	}

	public int getDamageCapacity() {
		return damageCapacity;
	}
	
	public boolean isArtillery() {
		return artillery;
	}

	public abstract String getName();

}

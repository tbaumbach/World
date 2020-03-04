package spaceraze.world.report.landbattle;

import java.io.Serializable;

import spaceraze.world.Troop;

public abstract class TroopTarget implements Serializable {
	static final long serialVersionUID = 1L;
	
	private final String typeName;
	private final int currentDamageCapacity;
	private final int damageCapacity;
	
	public TroopTarget(String typeName, int damageCapacity, int currentDamageCapacity) {
		this.typeName = typeName;
		this.damageCapacity = damageCapacity;
		this.currentDamageCapacity = currentDamageCapacity;
	}
	
	public TroopTarget(Troop troop) {
		this(troop.getTroopType().getUniqueName(), troop.getMaxDC(), troop.getCurrentDC());
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
	
	public abstract String getName();

}

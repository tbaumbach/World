package spaceraze.world.report.landbattle;

public class EnemyTroopTarget extends TroopTarget {
	
	
	public EnemyTroopTarget(String typeName, int damageCapacity, int currentDamageCapacity) {
		super(typeName, damageCapacity, currentDamageCapacity);
	}

	private static final long serialVersionUID = 8113634300201962026L;

	@Override
	public String getName() {
		return getTypeName();
	}

}

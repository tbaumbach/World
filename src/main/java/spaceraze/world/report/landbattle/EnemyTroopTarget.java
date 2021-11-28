package spaceraze.world.report.landbattle;

//TODO 2020-11-15 Remove, changed to use TroopTarget.
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

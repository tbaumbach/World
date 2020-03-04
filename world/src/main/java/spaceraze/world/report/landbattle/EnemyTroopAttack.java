package spaceraze.world.report.landbattle;

public class EnemyTroopAttack extends TroopAttack {
	private static final long serialVersionUID = -1296705424853938150L;

	public EnemyTroopAttack(String typeName, int damageCapacity, int currentDamageCapacity, boolean isArtillery) {
		super(typeName, damageCapacity, currentDamageCapacity, isArtillery);
	}

	@Override
	public String getName() {
		return getTypeName();
	}

}

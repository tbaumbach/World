package spaceraze.world.report.landbattle;

public class OwnTroopAttack extends TroopAttack {
	private static final long serialVersionUID = 1L;
	
	final String name;

	public OwnTroopAttack(String name, String typeName, int damageCapacity, int currentDamageCapacity, boolean isArtillery) {
		super(typeName, damageCapacity, currentDamageCapacity, isArtillery);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	
}

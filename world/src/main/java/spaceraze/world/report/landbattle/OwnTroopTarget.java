package spaceraze.world.report.landbattle;

public class OwnTroopTarget extends TroopTarget {
	private static final long serialVersionUID = 8113634300201962026L;
	
	final String name;
	
	public OwnTroopTarget(String name, String typeName, int damageCapacity, int currentDamageCapacity) {
		super(typeName, damageCapacity, currentDamageCapacity);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}

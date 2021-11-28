package spaceraze.world.report.landbattle;

//TODO 2020-11-15 Remove, changed to use TroopTarget.
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

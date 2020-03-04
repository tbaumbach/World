package spaceraze.world.report.spacebattle;

public class OwnSpaceshipAttack extends SpaceshipAttack {
	private static final long serialVersionUID = 1L;
	
	final String name;

	public OwnSpaceshipAttack(String name, String typeName, boolean scuttled, String retretsToPlanetName) {
		super(typeName, scuttled, retretsToPlanetName);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	

}

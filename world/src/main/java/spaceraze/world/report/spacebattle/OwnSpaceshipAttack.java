package spaceraze.world.report.spacebattle;

//TODO 2020-11-15 Remove, changed to use SpaceshipAttack.
public class OwnSpaceshipAttack extends SpaceshipAttack {
	private static final long serialVersionUID = 1L;
	
	final String name;

	public OwnSpaceshipAttack(String name, String typeName, boolean scuttled, String retretsToPlanetName) {
		super(typeName, scuttled, retretsToPlanetName, true);
		this.name = name;
	}

	@Override
	public String getSpaceshipName() {
		return name;
	}
	
	

}

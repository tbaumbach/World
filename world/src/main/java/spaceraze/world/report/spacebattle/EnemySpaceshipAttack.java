package spaceraze.world.report.spacebattle;

//TODO 2020-11-15 Remove, changed to use SpaceshipAttack.
public class EnemySpaceshipAttack extends SpaceshipAttack {
	private static final long serialVersionUID = 1L;

	public EnemySpaceshipAttack(String name, boolean scuttled) {
		super(name, scuttled, null, false);
	}

	@Override
	public String getSpaceshipName() {
		return getTypeName();
	}

}

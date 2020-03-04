package spaceraze.world.report.spacebattle;

public class EnemySpaceshipAttack extends SpaceshipAttack {
	private static final long serialVersionUID = 1L;

	public EnemySpaceshipAttack(String name, boolean scuttled) {
		super(name, scuttled, null);
	}

	@Override
	public String getName() {
		return getTypeName();
	}

}

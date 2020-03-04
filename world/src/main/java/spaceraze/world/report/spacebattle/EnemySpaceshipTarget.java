package spaceraze.world.report.spacebattle;


import spaceraze.world.Spaceship;

public class EnemySpaceshipTarget extends SpaceshipTarget {
	private static final long serialVersionUID = 1L;
	
	public EnemySpaceshipTarget(Spaceship spaceship) {
		super(spaceship);
	}

	public String getName() {
		return getTypeName();
	}
}

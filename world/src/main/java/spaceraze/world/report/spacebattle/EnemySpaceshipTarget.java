package spaceraze.world.report.spacebattle;


import spaceraze.world.Spaceship;

//TODO 2020-11-15 Remove, changed to use SpaceshipTarget.
public class EnemySpaceshipTarget extends SpaceshipTarget {
	private static final long serialVersionUID = 1L;
	
	public EnemySpaceshipTarget(Spaceship spaceship) {
	}

	public String getName() {
		return getTypeName();
	}
}

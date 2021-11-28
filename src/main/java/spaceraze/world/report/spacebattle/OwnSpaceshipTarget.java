package spaceraze.world.report.spacebattle;


import spaceraze.world.Spaceship;

//TODO 2020-11-15 Remove, changed to use SpaceshipTarget.
public class OwnSpaceshipTarget extends SpaceshipTarget {
	private static final long serialVersionUID = 1L;
	
	final String name;
	
	public OwnSpaceshipTarget(Spaceship spaceship) {
		this.name = spaceship.getName();
	}

	public String getName() {
		return name;
	}
}

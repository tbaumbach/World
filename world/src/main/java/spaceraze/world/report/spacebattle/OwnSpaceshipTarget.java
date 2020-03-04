package spaceraze.world.report.spacebattle;


import spaceraze.world.Spaceship;

public class OwnSpaceshipTarget extends SpaceshipTarget {
	private static final long serialVersionUID = 1L;
	
	final String name;
	
	public OwnSpaceshipTarget(Spaceship spaceship) {
		super(spaceship);
		this.name = spaceship.getName();
	}

	public String getName() {
		return name;
	}
}

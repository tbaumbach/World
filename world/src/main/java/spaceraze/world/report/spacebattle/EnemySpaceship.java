package spaceraze.world.report.spacebattle;

import java.io.Serializable;

public class EnemySpaceship extends SpaceshipState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String type;
	private final boolean screend;
	
	public EnemySpaceship(String type, boolean screend, int hullstatus) {
		super(hullstatus);
		this.type = type;
		this.screend = screend;
	}
	
	public EnemySpaceship(String type, boolean screend) {
		this(type, screend, 100);
	}
	
	public String getType() {
		return type;
	}

	public boolean isScreend() {
		return screend;
	}

}

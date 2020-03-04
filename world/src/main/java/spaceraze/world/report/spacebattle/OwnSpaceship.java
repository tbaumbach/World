package spaceraze.world.report.spacebattle;

import java.io.Serializable;

public class OwnSpaceship extends SpaceshipState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final String type;
	private final boolean screend;
	
	public OwnSpaceship(String name, String type, boolean screend, int hullstatus) {
		super(hullstatus);
		this.name = name;
		this.type = type;
		this.screend = screend;
	}
	
	public OwnSpaceship(String name, String type, boolean screend) {
		this(name, type, screend, 100);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isScreend() {
		return screend;
	}
	
}

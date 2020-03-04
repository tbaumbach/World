package spaceraze.world.report.spacebattle;

import java.io.Serializable;

public abstract class SpaceshipAttack implements Serializable {
	static final long serialVersionUID = 1L;

	final String typeName;
	final boolean scuttled;
	final String retretsToPlanetName;

	public SpaceshipAttack(String typeName, boolean scuttled, String retretsToPlanetName) {
		this.typeName = typeName;
		this.scuttled = scuttled;
		this.retretsToPlanetName = retretsToPlanetName;
	}

	public String getTypeName() {
		return typeName;
	}

	public abstract String getName();

	public boolean isScuttled() {
		return scuttled;
	}

	public String getRetretsToPlanetName() {
		return retretsToPlanetName;
	}

}

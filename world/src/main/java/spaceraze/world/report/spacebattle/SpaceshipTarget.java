package spaceraze.world.report.spacebattle;

import java.io.Serializable;

import spaceraze.world.Spaceship;

public abstract class SpaceshipTarget implements Serializable {
	static final long serialVersionUID = 1L;
	
	final String typeName;
	final int currentShield;
	final int shield; // TODO debugga detta, detta ska vara maximala skölden, d.v.s. kills ska vara medräknat.
	final int currentDamageCapacity;
	final int damageCapacity;
	
	private SpaceshipTarget(String typeName, int currentShield, int shield, int currentDamageCapacity, int damageCapacity) {
		this.typeName = typeName;
		this.currentShield = currentShield;
		this.shield = shield;
		this.currentDamageCapacity = currentDamageCapacity;
		this.damageCapacity = damageCapacity;
	}
	
	public SpaceshipTarget(Spaceship spaceship) {
		this(spaceship.getSpaceshipType().getName(), spaceship.getCurrentShields(), spaceship.getShields(), spaceship.getCurrentDc(), spaceship.getDamageCapacity());
	}

	public String getTypeName() {
		return typeName;
	}

	public abstract String getName();

	public int getCurrentShield() {
		return currentShield;
	}

	public int getShield() {
		return shield;
	}

	public int getCurrentDamageCapacity() {
		return currentDamageCapacity;
	}

	public int getDamageCapacity() {
		return damageCapacity;
	}

}

package spaceraze.world;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "TROOP")
public class Troop implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_GALAXY")
	private Galaxy galaxy;

	private String name;
	private String shortName;
	private String key;
	private String typeKey;
	private int currentDamageCapacity;
	private int damageCapacity;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLAYER", insertable = false, updatable = false)
	private Player owner = null;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLANET_LOCATION", insertable = false, updatable = false)
	private Planet planetLocation;
	private int lastPlanetMoveTurn;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_SPACESHIP_LOCATION", insertable = false, updatable = false)
	private Spaceship shipLocation;
	private int kills;
	private int uniqueId;
	private int techWhenBuilt; // needed for land battle sim
	// attack values
	private int attackInfantry;
	private int attackArmored;
	private int attackArtillery;
	private boolean spaceshipTravel;
	private boolean visible;
	private int upkeep;

	// used when moving
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLANET_OLD_LOCATION", insertable = false, updatable = false)
	private Planet oldPlanetLocation;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_SPACESHIP_OLD_LOCATION", insertable = false, updatable = false)
	private Spaceship oldShipLocation;

	public Troop(TroopType troopType, int aNrProduced, int aTotalTechBonus, int anUniqueId ){
		this.key = UUID.randomUUID().toString();
		this.typeKey = troopType.getKey();
		this.name = troopType.getName() + " - " + aNrProduced;
		this.shortName = troopType.getShortName() + " - " + aNrProduced;
		this.uniqueId = anUniqueId;
		setData(aTotalTechBonus, troopType);
	}
	
	public Troop clone(){
		Troop aTroop = null;
		try {
			aTroop = (Troop)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return aTroop;
	}
	
	private void setData(int aTotalTechBonus, TroopType troopType) {
		techWhenBuilt = aTotalTechBonus;
		int tech = 100 + aTotalTechBonus;
		double techBonus = tech / 100.0;

		attackArmored = (int) Math.round(troopType.getAttackArmored() * techBonus);
		attackArtillery = (int) Math.round(troopType.getAttackArtillery() * techBonus);
		attackInfantry = (int) Math.round(troopType.getAttackInfantry() * techBonus);

		damageCapacity = (int) Math.round(troopType.getDamageCapacity() * techBonus);;
		currentDamageCapacity = damageCapacity;
		spaceshipTravel = troopType.isSpaceshipTravel();
		visible = troopType.isVisible();
		upkeep = troopType.getUpkeep();
	}

	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public Planet getPlanetLocation() {
		return planetLocation;
	}

	public void setPlanetLocation(Planet planetLocation) {
		this.planetLocation = planetLocation;
	}

	public Spaceship getShipLocation() {
		return shipLocation;
	}

	public void setShipLocation(Spaceship shipLocation) {
		this.shipLocation = shipLocation;
	}

	public int getTechWhenBuilt() {
		return techWhenBuilt;
	}

	public void setTechWhenBuilt(int techWhenBuilt) {
		this.techWhenBuilt = techWhenBuilt;
	}

	public void setAttackArmored(int attackArmored) {
		this.attackArmored = attackArmored;
	}

	public void setAttackArtillery(int attackArtillery) {
		this.attackArtillery = attackArtillery;
	}

	public void setAttackInfantry(int attackInfantry) {
		this.attackInfantry = attackInfantry;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setDestroyed() {
		currentDamageCapacity = 0;
	}

	public int getLastPlanetMoveTurn() {
		return lastPlanetMoveTurn;
	}

	public void setLastPlanetMoveTurn(int lastPlanetMoveTurn) {
		this.lastPlanetMoveTurn = lastPlanetMoveTurn;
	}

}

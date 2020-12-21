package spaceraze.world;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.enums.SpaceShipSize;
import spaceraze.world.enums.SpaceshipRange;
import spaceraze.util.general.Logger;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "SPACESHIP")
public class Spaceship implements Serializable, ShortNameable, Cloneable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_GALAXY")
	private Galaxy galaxy;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLAYER", insertable = false, updatable = false)
	private Player owner;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLANET_LOCATION", insertable = false, updatable = false)
	private Planet location;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLANET_OLD_LOCATION", insertable = false, updatable = false)
	private Planet oldLocation;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLANET_RUNNING_FROM", insertable = false, updatable = false)
	private Planet runningFrom; // original planet where the ship was forced to start running from
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLANET_RUNNING_TO", insertable = false, updatable = false)
	private Planet runningTo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_SPACESHIP_CARRIER", insertable = false, updatable = false)
	private Spaceship carrierLocation;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_SPACESHIP_OLD_CARRIER", insertable = false, updatable = false)
	private Spaceship oldCarrierLocation;

	private String name;
	private String uniqueName;
	private String uniqueShortName;
	@Enumerated(EnumType.STRING)
	private SpaceShipSize size;
	private int shields;
	private int damagecapacity;
	private int currentdc;
	private int currentShields;
	private String key;
	private String typeKey;
	private int kills;
	private boolean screened = false;
	private boolean retreating = false;
	private int weaponsStrengthSmall;
	private int weaponsStrengthMedium;
	private int weaponsStrengthLarge;
	private int weaponsStrengthHuge;
	private int weaponsStrengthSquadron;
	private int weaponsSalvoesMedium;
	private int weaponsSalvoesLarge;
	private int weaponsSalvoesHuge; // if Integer.MAX then treat as infinite
	private int weaponsMaxSalvosMedium;
	private int weaponsMaxSalvosLarge;
	private int weaponsMaxSalvosHuge; // if Integer.MAX then treat as infinite
	private double armorSmall;
	private double armorMedium;
	private double armorLarge;
	private double armorHuge;
	private int techWhenBuilt;
	private int upkeep;
	private int buildCost;
	private int bombardment;
	private boolean noRetreat = false;
	private int psychWarfare;
	@Enumerated(EnumType.STRING)
	private SpaceshipRange range;
	private boolean initSupport = false;
	private int increaseInitiative;
	private int initDefence;
	private int incEnemyClosedBonus;
	private int incNeutralClosedBonus;
	private int incFriendlyClosedBonus;
	private int incOwnClosedBonus;
	private int incEnemyOpenBonus;
	private int incNeutralOpenBonus;
	private int incFriendlyOpenBonus;
	private int incOwnOpenBonus;
	private boolean canAttackScreenedShips;
	private boolean canBlockPlanet;
	private boolean visibleOnMap;
	private boolean lookAsCivilian;
	private int troopCapacity;
	private int squadronCapacity;
	@Enumerated(EnumType.STRING)
	private SpaceShipSize supply;

	// construktorn skall ej anropas direkt, utan spaceshiptype.getShip skall
	// användas istället
	public Spaceship(SpaceshipType sst, String name,
			int nrProduced, VIP vipWithBonus, int factionTechBonus,
			int buildingBonus) {

		this.typeKey = sst.getKey();
		this.size = sst.getSize();
		uniqueName = sst.getName() + " - " + nrProduced;
		uniqueShortName = sst.getShortName() + " - " + nrProduced;
		this.key = UUID.randomUUID().toString();
		kills = 0;
		if (name != null) {
			this.name = name;
		} else {
			this.name = uniqueName;
		}
		this.screened = sst.isScreened();
		setData(sst, vipWithBonus, factionTechBonus, buildingBonus);
	}

	private void setData(SpaceshipType sst, VIP vipWithBonus, int factionTechBonus, int buildingBonus) {
		int tech = 100 + factionTechBonus;
		tech = tech + buildingBonus;
		if (vipWithBonus != null) {
			tech = tech + vipWithBonus.getTechBonus();
//			LoggingHandler.fine( this, null, "setData","vipWithBonus.getTechBonus(): " + vipWithBonus.getTechBonus());
		}
		techWhenBuilt = tech - 100;
		double techBonus = tech / 100.0;
		// int tonnage = sst.getTonnage();
//		LoggingHandler.fine( this, null, "setData", techBonus + "");
		// weapons =
		// (int)Math.round(powTonnage(tonnage,2.2)*sst.getWeapons()/sst.getTotal()/10);
		weaponsStrengthSmall = (int) Math.round(sst.getWeaponsStrengthSmall() * techBonus);
		weaponsStrengthMedium = (int) Math.round(sst.getWeaponsStrengthMedium()	* techBonus);
		weaponsStrengthLarge = (int) Math.round(sst.getWeaponsStrengthLarge() * techBonus);
		weaponsStrengthHuge = (int) Math.round(sst.getWeaponsStrengthHuge()	* techBonus);
		weaponsStrengthSquadron = (int) Math.round(sst.getWeaponsStrengthSquadron() * techBonus);

//		LoggingHandler.fine( this, null, "setData", "weaponsStrengthSmall: " + weaponsStrengthSmall);

		weaponsSalvoesMedium = sst.getWeaponsMaxSalvoesMedium();
		weaponsSalvoesLarge = sst.getWeaponsMaxSalvoesLarge();
		weaponsSalvoesHuge = sst.getWeaponsMaxSalvoesHuge();
		weaponsMaxSalvosMedium = sst.getWeaponsMaxSalvoesMedium();
		weaponsMaxSalvosLarge = sst.getWeaponsMaxSalvoesLarge();
		weaponsMaxSalvosHuge = sst.getWeaponsMaxSalvoesHuge();

		shields = (int) Math.round(sst.getShields() * techBonus);

		currentShields = shields;
		Logger.finest( "currentShields & shields: " + currentShields + " " + shields);
		damagecapacity = sst.getHits();

		currentdc = damagecapacity;

		armorSmall = sst.getArmorSmall() / 100.0d;
		armorMedium = sst.getArmorMedium() / 100.0d;
		armorLarge = sst.getArmorLarge() / 100.0d;
		armorHuge = sst.getArmorHuge() / 100.0d;

		this.upkeep = sst.getUpkeep();
		this.bombardment = sst.getBombardment();
		this.noRetreat = sst.isNoRetreat();
		this.psychWarfare = sst.getPsychWarfare();
		this.buildCost = sst.getBuildCost();
		this.range = sst.getRange();
		this.initSupport = sst.isInitSupport();
		this.increaseInitiative = sst.getIncreaseInitiative();
		this.initDefence = sst.getInitDefence();
		this.incEnemyClosedBonus = sst.getIncEnemyClosedBonus();
		this.incNeutralClosedBonus = sst.getIncNeutralClosedBonus();
		this.incFriendlyClosedBonus = sst.getIncFriendlyClosedBonus();
		this.incOwnClosedBonus = sst.getIncOwnClosedBonus();
		this.incEnemyOpenBonus = sst.getIncEnemyOpenBonus();
		this.incNeutralOpenBonus = sst.getIncNeutralOpenBonus();
		this.incFriendlyOpenBonus = sst.getIncFriendlyOpenBonus();
		this.incOwnOpenBonus = sst.getIncOwnOpenBonus();
		this.canBlockPlanet = sst.isCanBlockPlanet();
		this.visibleOnMap = sst.isVisibleOnMap();
		this.lookAsCivilian = sst.isLookAsCivilian();
		this.troopCapacity = sst.getTroopCapacity();
		this.squadronCapacity = sst.getSquadronCapacity();
		this.supply = sst.getSupply();
	}

	public boolean isDestroyed() {
		return currentdc == 0;
	}

	public void clearRunningFrom() {
		runningFrom = null;
	}

	public void setLocation(Planet newLocation) {
		location = newLocation;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return uniqueShortName;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public Planet getLocation() {
		return location;
	}

	public int getDamageCapacity() {
		return damagecapacity;
	}

	public int getCurrentDc() {
		return currentdc;
	}
	
	public int setCurrentDc(int inCurrentdc) {
		return currentdc = inCurrentdc;
	}

	public int getCurrentShields() {
		return currentShields;
	}

	public void setOwner(Player newOwner) {
		this.owner = newOwner;
	}

	public Player getOwner() {
		return owner;
	}

	public void addKill() {
		kills++;
	}

	public void setKills(int newKills) {
		kills = newKills;
	}

	public int getKills() {
		return kills;
	}

	public Planet getOldLocation() {
		return oldLocation;
	}

	public Planet getRetreatingTo() {
		return runningTo;
	}

	public Planet getRetreatingFrom() {
		return runningFrom;
	}

	public void setOldLocation(Planet newOldLocation) {
		oldLocation = newOldLocation;
	}

	public boolean isRetreating() {
		return retreating;
	}

	public void setRetreating(boolean isRetreating){
		this.retreating = isRetreating;
	}

	public double getArmorHuge() {
		return armorHuge;
	}

	public double getArmorLarge() {
		return armorLarge;
	}

	public double getArmorMedium() {
		return armorMedium;
	}

	public double getArmorSmall() {
		return armorSmall;
	}

	public Spaceship getCarrierLocation() {
		return carrierLocation;
	}

	public void setCarrierLocation(Spaceship carrier) {
		this.carrierLocation = carrier;
	}

	public Spaceship getOldCarrierLocation() {
		return oldCarrierLocation;
	}

	public void setOldCarrierLocation(Spaceship carrier){
		this.oldCarrierLocation = carrier;
	}

	public void setDestroyed() {
		currentdc = 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ShortName: " + uniqueShortName);
		sb.append(" Location: " + location);
		sb.append(" Carrier location: " + carrierLocation);
		sb.append(" Owner: " + owner);
		return sb.toString();
	}
	
	@Override
	public Spaceship clone(){
		Spaceship clonedShip = null;
		try {
			clonedShip = (Spaceship)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clonedShip;
	}

	public int getTechWhenBuilt() {
		return techWhenBuilt;
	}

	public void setRunningTo(Planet runningTo) {
		this.runningTo = runningTo;
	}

	public void setRunningFrom(Planet runningFrom) {
		this.runningFrom = runningFrom;
	}

}

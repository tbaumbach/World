package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import spaceraze.world.enums.TypeOfTroop;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "BUILDNING_TYPE")
public class BuildingType implements Serializable, Cloneable{
	static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String key;

	@Column(length = 4000)
	private String description;
	private String shortName;

	@Column(length = 4000)
	private String advantages;
	private boolean inOrbit = false;
	private boolean autoDestructWhenConquered;
	private boolean selfDestructible = true;
	private boolean developed= true;
	private int openPlanetBonus = 0;
	private int closedPlanetBonus = 0;
	private int techBonus = 0;
	private int wharfSize = 0; // if = 0 cannot build ships
	private int troopSize = 0; // if = 0 cannot build troops
	// worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player, planetUnigue = one at planet.
	private boolean worldUnique = false;
	private boolean factionUnique = false;
	private boolean playerUnique = false;
	private boolean planetUnique = false;
	private int buildCost = 0;
	private boolean spaceport;
	//  kan nog vara en ide...  om byggnaden ligger i orbit så fungerar det som för skepp men om byggnaden är på planeten så syns den inte fören en fi har trupper på planeten.
	private boolean visibleOnMap = true;
	
	// war buildings. shieldCapacity= ???, CannonDamage =  damge against enemy ships(one shot), cononRateOfFire(number of shot/turn)
	private int resistanceBonus = 0;
	private int shieldCapacity = 0;
	private int cannonDamage = 0;
	private int cannonRateOfFire = 0;
	private int cannonHitChance = 50;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "BUILDNING_TYPE_TO_VIP_TYPE",
			joinColumns = @JoinColumn(name = "BUILDNING_ID"),
			inverseJoinColumns = @JoinColumn(name = "VIP_ID"))
	@Builder.Default
	private List<VIPType> buildVIPTypes = new ArrayList<>();

	@ElementCollection // TODO do we need to use (fetch = FetchType.EAGER) ?
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private List<TypeOfTroop> typeOfTroop = new ArrayList<>(); // infantry, armored or support
	private String parentBuildingTypeName;
	
	private boolean alienKiller;
	private int counterEspionage = 0;
	private int exterminator = 0;


	public BuildingType(BuildingType original, PlayerBuildingImprovement improvement){
		this.key = original.getKey();
		this.buildVIPTypes = new ArrayList<>();
		this.typeOfTroop = new ArrayList<>();
		this.setName(original.getName());
		this.setDescription(original.getDescription());
		this.setShortName(original.getShortName());
		this.setAdvantages(original.getAdvantages());
		this.setInOrbit(original.isInOrbit());
		this.setAutoDestructWhenConquered(original.isAutoDestructWhenConquered());
		this.setSelfDestructible(original.isSelfDestructible());
		this.setDeveloped(improvement.isDeveloped());
		this.worldUnique = original.isWorldUnique();
		this.factionUnique = original.isFactionUnique();
		this.playerUnique = original.isPlayerUnique();
		this.planetUnique = original.isPlanetUnique();

		this.setOpenPlanetBonus(original.getOpenPlanetBonus() + improvement.getOpenPlanetBonus());
		this.setClosedPlanetBonus(original.getClosedPlanetBonus() + improvement.getClosedPlanetBonus());
		this.setTechBonus(original.getTechBonus() + improvement.getTechBonus());
		this.setWharfSize(improvement.getWharfSize() > 0 ? improvement.getWharfSize() : original.getWharfSize());
		this.setTroopSize(improvement.getTroopSize() > 0 ? improvement.getTroopSize() : original.getTroopSize());
		this.setBuildCost(improvement.getBuildCost() > 0 ? improvement.getBuildCost() : original.getBuildCost());
		this.setSpaceport(improvement.isChangeSpaceport() ? improvement.isSpaceport() : original.isSpaceport());
		this.setVisibleOnMap(improvement.isChangeVisibleOnMap() ? improvement.isVisibleOnMap() : original.isVisibleOnMap());

		this.setResistanceBonus(original.getResistanceBonus() + improvement.getResistanceBonus());
		this.setShieldCapacity(original.getShieldCapacity() + improvement.getShieldCapacity());
		this.setCannonDamage(original.getCannonDamage() + improvement.getCannonDamage());
		this.setCannonRateOfFire(original.getCannonRateOfFire() + improvement.getCannonRateOfFire());
		this.setCannonHitChance(original.getCannonHitChance());

		this.setBuildVIPTypes(original.getBuildVIPTypes());
		this.getTypeOfTroop().addAll(original.getTypeOfTroop());
		this.setParentBuildingTypeName(original.getParentBuildingName());

		this.setAlienKiller(improvement.isChangeAlienKiller() ? improvement.isAlienKiller() : original.isAlienKiller());
		this.setCounterEspionage(original.getCounterEspionage() + improvement.getCounterEspionage());
		this.setExterminator(original.getExterminator() + improvement.getExterminator());


	}
	
	public BuildingType(String name, String shortName, int buildCost){
		this.key = UUID.randomUUID().toString();
		this.buildVIPTypes = new ArrayList<>();
		this.typeOfTroop = new ArrayList<>();
		setName(name);
		setShortName(shortName);
		setBuildCost(buildCost);
	}

	public void setParentBuildingTypeName(String parentBuildingTypeName){
		this.parentBuildingTypeName = parentBuildingTypeName;
	}

	public int getClosedPlanetBonus() {
		return closedPlanetBonus;
	}

	public void setClosedPlanetBonus(int closedPlanetBonus) {
		this.closedPlanetBonus = closedPlanetBonus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdvantages() {
		return advantages;
	}

	public void setAdvantages(String advantages) {
		this.advantages = advantages;
	}
	
	public boolean isDeveloped() {
		return developed;
	}

	public void setDeveloped(boolean developed) {
		this.developed = developed;
	}

	public boolean isInOrbit() {
		return inOrbit;
	}

	public void setInOrbit(boolean inOrbit) {
		this.inOrbit = inOrbit;
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

	public void setShortName(String name) {
		this.shortName = name;
	}

	public int getOpenPlanetBonus() {
		return openPlanetBonus;
	}

	public void setOpenPlanetBonus(int openPlanetBonus) {
		this.openPlanetBonus = openPlanetBonus;
	}

	public String getParentBuildingName() {
		return parentBuildingTypeName;
	}

	public int getResistanceBonus() {
		return resistanceBonus;
	}

	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}

	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}

	public int getTechBonus() {
		return techBonus;
	}

	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}

		public int getWharfSize() {
			return wharfSize;
		}

		public void setWharfSize(int wharfSize) {
			this.wharfSize = wharfSize;
		}

		public boolean isSpaceport() {
			return spaceport;
		}

		public void setSpaceport(boolean spaceport) {
			this.spaceport = spaceport;
		}

	public boolean isAutoDestructWhenConquered() {
		return autoDestructWhenConquered;
	}

	public void setAutoDestructWhenConquered(boolean autoDestructWhenConquered) {
		this.autoDestructWhenConquered = autoDestructWhenConquered;
	}

	@JsonIgnore
	public List<VIPType> getBuildVIPTypes() {
		return buildVIPTypes;
	}

	public void setBuildVIPTypes(List<VIPType> buildVIPTypes) {
		this.buildVIPTypes = buildVIPTypes;
	}
	
	public void addBuildVIPType(VIPType buildVIPType) {
		this.buildVIPTypes.add(buildVIPType);
	}

	public boolean isFactionUnique() {
		return factionUnique;
	}

	public boolean isPlanetUnique() {
		return planetUnique;
	}

	public void setPlanetUnique(boolean planetUnique) {
		this.planetUnique = planetUnique;
	}

	public boolean isPlayerUnique() {
		return playerUnique;
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.visibleOnMap = visibleOnMap;
	}

	public boolean isWorldUnique() {
		return worldUnique;
	}

	public void setWorldUnique(boolean worldUnique) {
		this.worldUnique = worldUnique;
	}

	public int getShieldCapacity() {
		return shieldCapacity;
	}

	public void setShieldCapacity(int shieldCapacity) {
		this.shieldCapacity = shieldCapacity;
	}

	public int getTroopSize() {
		return troopSize;
	}

	public void setTroopSize(int troopSize) {
		this.troopSize = troopSize;
	}

	public List<TypeOfTroop> getTypeOfTroop() {
		return typeOfTroop;
	}
	
	public void addTypeOfTroop(TypeOfTroop typeOfTroop) {
		this.typeOfTroop.add(typeOfTroop);
	}

	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}

	public boolean isAlienKiller() {
		return alienKiller;
	}

	public void setAlienKiller(boolean ailienkiller) {
		this.alienKiller = ailienkiller;
	}

	public int getCounterEspionage() {
		return counterEspionage;
	}

	public void setCounterEspionage(int counterEspionage) {
		this.counterEspionage = counterEspionage;
	}

	public int getExterminator() {
		return exterminator;
	}

	public void setExterminator(int exterminator) {
		this.exterminator = exterminator;
	}

	public int getCannonHitChance() {
		return cannonHitChance;
	}

	public void setCannonHitChance(int iCannonHitChance) {
		this.cannonHitChance = iCannonHitChance;
	}
	
	public int getCannonDamage() {
		return cannonDamage;
	}

	public void setCannonDamage(int CannonDamage) {
		this.cannonDamage = CannonDamage;
	}

	public int getCannonRateOfFire() {
		return cannonRateOfFire;
	}

	public void setCannonRateOfFire(int CannonRateOfFire) {
		this.cannonRateOfFire = CannonRateOfFire;
	}

	public boolean isSelfDestructible() {
		return selfDestructible;
	}

	public void setSelfDestructible(boolean selfDestructible) {
		this.selfDestructible = selfDestructible;
	}


}


package spaceraze.world;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.enums.BlackMarketFrequency;
import spaceraze.world.enums.SpaceShipSize;
import spaceraze.world.enums.SpaceshipRange;
import spaceraze.world.enums.SpaceshipTargetingType;

import javax.persistence.*;

/**
 * Instances of this class represent one spaceship type, from which 
 * spaceships can be built.
 * 
 * @author wmpabod
 *
 */
@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "SPACESHIP_TYPE")
public class SpaceshipType implements Serializable{
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_GAME_WORLD")
    private GameWorld gameWorld;

    private String uuid;
    private String name;
    private String shortName;

	@Enumerated(EnumType.STRING)
    private SpaceShipSize size;
    int shields;
    int upkeep;
    int buildCost;
    int hits;
    int bombardment;
    int increaseInitiative;
    int initDefence;

	@Enumerated(EnumType.STRING)
    private SpaceshipRange range;
    int psychWarfare;
    boolean noRetreat = false;
    boolean initSupport = false;
    private int weaponsStrengthSmall;
    private int weaponsStrengthMedium;
    private int weaponsStrengthLarge;
    private int weaponsStrengthHuge;
    private int weaponsMaxSalvosMedium; // if Integer.MAX then treat as infinite
    private int weaponsMaxSalvosLarge;
    private int weaponsMaxSalvosHuge;
    private int armorSmall;
    private int armorMedium;
    private int armorLarge;
    private int armorHuge;

	@Enumerated(EnumType.STRING)
    private SpaceShipSize supply = SpaceShipSize.NONE; //max size of ship that can be resupplied
    private boolean canAppearOnBlackMarket = true;
	@Enumerated(EnumType.STRING)
    private BlackMarketFrequency blackMarketFrequency = BlackMarketFrequency.COMMON;
    private int blackmarketFirstTurn = 0;
    private int bluePrintFirstTurn = 0;
	@Enumerated(EnumType.STRING)
    private BlackMarketFrequency bluePrintFrequency = BlackMarketFrequency.COMMON;
    
    private boolean planetarySurvey;
    private boolean canAttackScreenedShips;
    private int weaponsStrengthSquadron;
    private int squadronCapacity;
	@Enumerated(EnumType.STRING)
    private SpaceshipTargetingType targetingType = null;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;

    @Column(name = "HISTORY", length = 4000)
	private String history;
    private String shortDescription;
    private String advantages;
    private String disadvantages;
    private boolean civilian = false;
    private boolean lookAsCivilian = false;
    private boolean canBlockPlanet = true;
    private boolean visibleOnMap = true;
    private boolean availableToBuild = true;
    private boolean alwaysRetreat;
	private int incEnemyClosedBonus;
    private int incNeutralClosedBonus;
    private int incFriendlyClosedBonus;
    private int incOwnClosedBonus;
    private int incEnemyOpenBonus;
    private int incNeutralOpenBonus;
    private int incFriendlyOpenBonus;
    private int incOwnOpenBonus;
    private int aimBonus; // increases the chance to fire against the most damage ship.
    private int troopCapacity;

    private boolean screened = false;

//  worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player.
	private boolean worldUnique=  false, factionUnique = false, playerUnique =  false;

	public SpaceshipType(String name, String shortName, SpaceShipSize size, int shields, int hits, SpaceshipRange range, int upkeep, int buildCost, int weaponsStrengthSmall){
    	this(name,shortName, size,shields,hits,range,upkeep,buildCost, weaponsStrengthSmall,0);
    }
    
    /**
     * Constructor for civilian ships
     * @param name
     * @param shortName
     * @param size
     * @param range
     * @param upkeep
     * @param buildCost
     */
    public SpaceshipType(String name, String shortName, SpaceShipSize size, SpaceshipRange range, int upkeep, int buildCost){
    	this(name,shortName, size,0,1,range,upkeep,buildCost, 0,0);
    	setCivilian(true);
    	setLookAsCivilian(true);
    	setCanBlockPlanet(false);
    	this.armorSmall = 0;
    	this.armorMedium = 0;
    	this.armorLarge = 0;
    	this.armorHuge = 0;
    }
	
    public SpaceshipType(String name, String shortName, SpaceShipSize size, int shields, int hits, SpaceshipRange range, int upkeep, int buildCost, int weaponsStrengthSmall, int weaponsStrengthSquadron){
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.shortName = shortName;
        this.size = size;
        this.range = range;
        this.shields = shields;
        this.upkeep = upkeep;
        this.buildCost = buildCost;
        this.hits = hits;
        this.weaponsStrengthSmall = weaponsStrengthSmall;
        this.weaponsStrengthSquadron = weaponsStrengthSquadron;
        // default armor
        if (size == SpaceShipSize.MEDIUM){
            armorSmall = 25;
        }else
        if (size == SpaceShipSize.LARGE){
            armorSmall = 50;
            armorMedium = 25;
        }else
        if (size == SpaceShipSize.HUGE){
            armorSmall = 75;
            armorMedium = 50;
            armorLarge = 25;
        }
        // Default (almost) infinite number of salvoes
        this.weaponsMaxSalvosMedium = Integer.MAX_VALUE;
        this.weaponsMaxSalvosLarge = Integer.MAX_VALUE;
        this.weaponsMaxSalvosHuge = Integer.MAX_VALUE;
        // default targeting type
        targetingType = SpaceshipTargetingType.ANTIMBU;
        
        if(size == SpaceShipSize.SMALL){
        	blackmarketFirstTurn = 1;
        	bluePrintFirstTurn =  1;
        }else if(size == SpaceShipSize.MEDIUM){
        	blackmarketFirstTurn = 6;
        	bluePrintFirstTurn =  6;
        }else if(size == SpaceShipSize.LARGE){
        	blackmarketFirstTurn = 9;
        	bluePrintFirstTurn =  12;
        }else{//HUGE
        	blackmarketFirstTurn = 15;
        	bluePrintFirstTurn =  20;
        }
        
        
    }

    /**
     * Used to get players SpaceshipType updated by PlayerSpaceshipType.
     */
    public SpaceshipType(SpaceshipType originSpaceshipType, PlayerSpaceshipImprovement playerSpaceshipImprovement){
        this.uuid = originSpaceshipType.uuid;
        this.name = originSpaceshipType.getName();
        this.shortName = originSpaceshipType.getShortName();
        this.size = originSpaceshipType.getSize();
        this.range = playerSpaceshipImprovement.getRange() != null ? playerSpaceshipImprovement.getRange() : originSpaceshipType.getRange();
        this.shields = originSpaceshipType.getShields() + playerSpaceshipImprovement.getShields();
        this.upkeep = originSpaceshipType.getUpkeep() + playerSpaceshipImprovement.getUpkeep();
        this.buildCost = originSpaceshipType.getBuildCost() + playerSpaceshipImprovement.getBuildCost();
        this.bombardment = originSpaceshipType.getBombardment() + playerSpaceshipImprovement.getBombardment();
        this.noRetreat = playerSpaceshipImprovement.isNoRetreat();

        //Why can't we research hitpoints?
        this.hits = originSpaceshipType.getHits();

        this.setInitSupport(originSpaceshipType.isInitSupport());
        this.increaseInitiative = originSpaceshipType.getIncreaseInitiative() + playerSpaceshipImprovement.getIncreaseInitiative();
        this.initDefence = originSpaceshipType.getInitDefence() + playerSpaceshipImprovement.getInitDefence();
        this.weaponsStrengthSquadron = originSpaceshipType.getWeaponsStrengthSquadron() + playerSpaceshipImprovement.getWeaponsStrengthSquadron();
        this.weaponsStrengthSmall = originSpaceshipType.getWeaponsStrengthSmall() + playerSpaceshipImprovement.getWeaponsStrengthSmall();
        this.weaponsStrengthMedium = originSpaceshipType.getWeaponsStrengthMedium() + playerSpaceshipImprovement.getWeaponsStrengthMedium();
        this.weaponsStrengthLarge = originSpaceshipType.getWeaponsStrengthLarge() + playerSpaceshipImprovement.getWeaponsStrengthLarge();
        this.weaponsStrengthHuge = originSpaceshipType.getWeaponsStrengthHuge() + playerSpaceshipImprovement.getWeaponsStrengthHuge();
        this.weaponsMaxSalvosMedium = originSpaceshipType.getWeaponsMaxSalvosMedium() + playerSpaceshipImprovement.getWeaponsMaxSalvosMedium();
        this.weaponsMaxSalvosLarge = originSpaceshipType.getWeaponsMaxSalvosLarge() + playerSpaceshipImprovement.getWeaponsMaxSalvosLarge();
        this.weaponsMaxSalvosHuge = originSpaceshipType.getWeaponsMaxSalvosHuge() + playerSpaceshipImprovement.getWeaponsMaxSalvosHuge();
        this.supply = playerSpaceshipImprovement.getSupply()  != null ? playerSpaceshipImprovement.getSupply() : originSpaceshipType.getSupply();
        this.armorSmall = originSpaceshipType.getArmorSmall() + playerSpaceshipImprovement.getArmorSmall();
        this.armorMedium = originSpaceshipType.getArmorMedium() + playerSpaceshipImprovement.getArmorMedium();
        this.armorLarge = originSpaceshipType.getArmorLarge() + playerSpaceshipImprovement.getArmorLarge();
        this.armorHuge = originSpaceshipType.getArmorHuge() + playerSpaceshipImprovement.getArmorHuge();
        this.planetarySurvey = playerSpaceshipImprovement.isChangePlanetarySurvey() ? playerSpaceshipImprovement.isPlanetarySurvey() : originSpaceshipType.isPlanetarySurvey();
//        this.siegeBonus = oldsst.getSiegeBonus();
//        this.troops = oldsst.getTroops();
        this.psychWarfare = originSpaceshipType.getPsychWarfare() + playerSpaceshipImprovement.getPsychWarfare();
        this.targetingType = originSpaceshipType.getTargetingType();
        this.squadronCapacity = originSpaceshipType.getSquadronCapacity() + playerSpaceshipImprovement.getSquadronCapacity();
        this.description = playerSpaceshipImprovement.getDescription() != null ? playerSpaceshipImprovement.getDescription() : originSpaceshipType.getDescription();
        this.history = playerSpaceshipImprovement.getHistory() != null ? playerSpaceshipImprovement.getHistory() : originSpaceshipType.getHistory();
        this.incEnemyClosedBonus = originSpaceshipType.incEnemyClosedBonus + playerSpaceshipImprovement.getIncEnemyClosedBonus();
        this.incEnemyOpenBonus = originSpaceshipType.incEnemyOpenBonus + playerSpaceshipImprovement.getIncEnemyOpenBonus();
        this.incFriendlyClosedBonus = originSpaceshipType.incFriendlyClosedBonus + playerSpaceshipImprovement.getIncFriendlyClosedBonus();
        this.incFriendlyOpenBonus = originSpaceshipType.incFriendlyOpenBonus + playerSpaceshipImprovement.getIncFriendlyOpenBonus();
        this.incNeutralClosedBonus = originSpaceshipType.incNeutralClosedBonus + playerSpaceshipImprovement.getIncNeutralClosedBonus();
        this.incNeutralOpenBonus = originSpaceshipType.incNeutralOpenBonus + playerSpaceshipImprovement.getIncNeutralOpenBonus();
        this.incOwnClosedBonus = originSpaceshipType.incOwnClosedBonus + playerSpaceshipImprovement.getIncOwnClosedBonus();
        this.incOwnOpenBonus = originSpaceshipType.incOwnOpenBonus + playerSpaceshipImprovement.getIncOwnOpenBonus();
        this.canAttackScreenedShips = playerSpaceshipImprovement.isChangeCanAttackScreenedShips() ? playerSpaceshipImprovement.isCanAttackScreenedShips() : originSpaceshipType.canAttackScreenedShips;
        this.civilian = originSpaceshipType.civilian;
        this.lookAsCivilian = playerSpaceshipImprovement.isChangeLookAsCivilian() ? playerSpaceshipImprovement.isLookAsCivilian() : originSpaceshipType.lookAsCivilian;
        this.canBlockPlanet = playerSpaceshipImprovement.isChangeCanBlockPlanet() ? playerSpaceshipImprovement.isCanBlockPlanet() : originSpaceshipType.canBlockPlanet;
        this.visibleOnMap = playerSpaceshipImprovement.isChangeVisibleOnMap() ? playerSpaceshipImprovement.isVisibleOnMap() : originSpaceshipType.isVisibleOnMap();
        this.availableToBuild = playerSpaceshipImprovement.isAvailableToBuild();
        this.troopCapacity = originSpaceshipType.getTroopCapacity() + playerSpaceshipImprovement.getTroopCarrier();
        this.worldUnique = originSpaceshipType.isWorldUnique();
        this.factionUnique = originSpaceshipType.isFactionUnique();
        this.playerUnique = originSpaceshipType.isPlayerUnique();
        this.alwaysRetreat = originSpaceshipType.isAlwaysRetreat();
        this.screened = originSpaceshipType.isScreened();

        this.advantages = originSpaceshipType.getAdvantages();
        this.disadvantages = originSpaceshipType.getDisadvantages();
        this.canAppearOnBlackMarket = originSpaceshipType.isCanAppearOnBlackMarket();
        this.blackMarketFrequency = originSpaceshipType.getBlackMarketFrequency();
        this.blackmarketFirstTurn = originSpaceshipType.getBlackmarketFirstTurn();
        this.bluePrintFirstTurn = originSpaceshipType.getBluePrintFirstTurn();
        this.bluePrintFrequency = originSpaceshipType.getBluePrintFrequency();
    }

    public int getBombardment(){
      return bombardment;
    }

    public int getPsychWarfare(){
      return psychWarfare;
    }

    public int getUpkeep(){
      return upkeep;
    }

    public void setBuildCost(int buildCost){
    	this.buildCost = buildCost;
    }

    @JsonIgnore
    public Spaceship getShip(int vipTechBonus, int factionTechBonus, int buildingBonus, int uniqueId){
      return new Spaceship(this, null, 0, vipTechBonus, factionTechBonus,buildingBonus);
    }

    public SpaceShipSize getSize(){
      return size;
    }

    public SpaceshipRange getRange(){
      return range;
    }

    public int getHits(){
      return hits;
    }

    public int getShields(){
      return shields;
    }

    public String getName(){
      return name;
    }

    public String getShortName(){
      return shortName;
    }

    public int getInitDefence(){
        return initDefence;
      }

    public void setNoRetreat(boolean newValue){
      noRetreat = newValue;
    }

	public int getWeaponsStrengthHuge() {
		return weaponsStrengthHuge;
	}
	
	public int getWeaponsStrengthLarge() {
		return weaponsStrengthLarge;
	}
	
	public int getWeaponsStrengthMedium() {
		return weaponsStrengthMedium;
	}
	
	public int getWeaponsStrengthSmall() {
		return weaponsStrengthSmall;
	}

	public boolean isCanAppearOnBlackMarket() {
		return canAppearOnBlackMarket;
	}

	public void setCanAppearOnBlackMarket(boolean canAppearOnBlackMarket) {
		this.canAppearOnBlackMarket = canAppearOnBlackMarket;
	}

	public int getArmorHuge() {
		return armorHuge;
	}

	public void setArmorHuge(int armorHuge) {
		this.armorHuge = armorHuge;
	}

	public int getArmorLarge() {
		return armorLarge;
	}

	public void setArmorLarge(int armorLarge) {
		this.armorLarge = armorLarge;
	}

	public int getArmorMedium() {
		return armorMedium;
	}

	public void setArmorMedium(int armorMedium) {
		this.armorMedium = armorMedium;
	}

	public int getArmorSmall() {
		return armorSmall;
	}

	public void setArmorSmall(int armorSmall) {
		this.armorSmall = armorSmall;
	}

	public boolean isPlanetarySurvey() {
		return planetarySurvey;
	}

	public void setPlanetarySurvey(boolean planetarySurvey) {
		this.planetarySurvey = planetarySurvey;
	}
/*
	public int getSiegeBonus() {
		return siegeBonus;
	}

	public void setSiegeBonus(int siegeBonus) {
		this.siegeBonus = siegeBonus;
	}
*/
	public SpaceShipSize getSupply() {
		return supply;
	}

	public void setSupply(SpaceShipSize supply) {
		this.supply = supply;
	}

	public void setBombardment(int bombardment) {
		this.bombardment = bombardment;
	}

	public void setIncreaseInitiative(int increaseInitiative) {
		this.increaseInitiative = increaseInitiative;
	}
	
	public int getIncreaseInitiative() {
		return this.increaseInitiative;
	}

	public void setInitDefence(int initDefence) {
		this.initDefence = initDefence;
	}

	public void setInitSupport(boolean initSupport) {
		this.initSupport = initSupport;
	}

	public void setTroops(int newPsychWarfare) {
		this.psychWarfare = newPsychWarfare;
	}

	public void setWeaponsMaxSalvosHuge(int weaponsMaxSalvosHuge) {
		this.weaponsMaxSalvosHuge = weaponsMaxSalvosHuge;
	}

	public void setWeaponsMaxSalvosLarge(int weaponsMaxSalvosLarge) {
		this.weaponsMaxSalvosLarge = weaponsMaxSalvosLarge;
	}

	public void setWeaponsMaxSalvosMedium(int weaponsMaxSalvosMedium) {
		this.weaponsMaxSalvosMedium = weaponsMaxSalvosMedium;
	}

	public void setWeaponsStrengthHuge(int weaponsStrengthHuge) {
		this.weaponsStrengthHuge = weaponsStrengthHuge;
	}

	public void setWeaponsStrengthLarge(int weaponsStrengthLarge) {
		this.weaponsStrengthLarge = weaponsStrengthLarge;
	}

	public void setWeaponsStrengthMedium(int weaponsStrengthMedium) {
		this.weaponsStrengthMedium = weaponsStrengthMedium;
	}
	
	public void setWeaponsStrengthSmall(int weaponsStrengthSmall) {
		this.weaponsStrengthSmall = weaponsStrengthSmall;
	}

	public int getSquadronCapacity() {
		return squadronCapacity;
	}

	public void setSquadronCapacity(int squadronCapacity) {
		this.squadronCapacity = squadronCapacity;
	}

	public int getWeaponsStrengthSquadron() {
		return weaponsStrengthSquadron;
	}

	public void setWeaponsStrengthSquadron(int weaponsStrengthSquadron) {
		this.weaponsStrengthSquadron = weaponsStrengthSquadron;
	}
	
	public void setTargetingType(SpaceshipTargetingType aTargetingType){
		targetingType = aTargetingType;
	}

	public SpaceshipTargetingType getTargetingType(){
		return targetingType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public int getIncEnemyClosedBonus() {
		return incEnemyClosedBonus;
	}

	public void setIncEnemyClosedBonus(int incEnemyClosedBonus) {
		this.incEnemyClosedBonus = incEnemyClosedBonus;
	}

	public int getIncEnemyOpenBonus() {
		return incEnemyOpenBonus;
	}

	public void setIncEnemyOpenBonus(int incEnemyOpenBonus) {
		this.incEnemyOpenBonus = incEnemyOpenBonus;
	}

	public int getIncFriendlyClosedBonus() {
		return incFriendlyClosedBonus;
	}

	public void setIncFriendlyClosedBonus(int incFriendlyClosedBonus) {
		this.incFriendlyClosedBonus = incFriendlyClosedBonus;
	}

	public int getIncFriendlyOpenBonus() {
		return incFriendlyOpenBonus;
	}

	public void setIncFriendlyOpenBonus(int incFriendlyOpenBonus) {
		this.incFriendlyOpenBonus = incFriendlyOpenBonus;
	}

	public int getIncNeutralClosedBonus() {
		return incNeutralClosedBonus;
	}

	public void setIncNeutralClosedBonus(int incNeutralClosedBonus) {
		this.incNeutralClosedBonus = incNeutralClosedBonus;
	}

	public int getIncNeutralOpenBonus() {
		return incNeutralOpenBonus;
	}

	public void setIncNeutralOpenBonus(int incNeutralOpenBonus) {
		this.incNeutralOpenBonus = incNeutralOpenBonus;
	}

	public int getIncOwnClosedBonus() {
		return incOwnClosedBonus;
	}

	public void setIncOwnClosedBonus(int incOwnClosedBonus) {
		this.incOwnClosedBonus = incOwnClosedBonus;
	}

	public int getIncOwnOpenBonus() {
		return incOwnOpenBonus;
	}

	public void setIncOwnOpenBonus(int incOwnOpenBonus) {
		this.incOwnOpenBonus = incOwnOpenBonus;
	}

	public boolean isCanAttackScreenedShips() {
		return canAttackScreenedShips;
	}

	public void setCanAttackScreenedShips(boolean canAttackScreenedShips) {
		this.canAttackScreenedShips = canAttackScreenedShips;
	}

	public boolean isCanBlockPlanet() {
		return canBlockPlanet;
	}

	public void setCanBlockPlanet(boolean canBlockPlanet) {
		this.canBlockPlanet = canBlockPlanet;
	}

	public boolean isCivilian() {
		return civilian;
	}

	public void setCivilian(boolean civilian) {
		this.civilian = civilian;
	}

	public boolean isLookAsCivilian() {
		return lookAsCivilian;
	}

	public void setLookAsCivilian(boolean lookAsCivilian) {
		this.lookAsCivilian = lookAsCivilian;
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.visibleOnMap = visibleOnMap;
	}

	public boolean isAvailableToBuild() {
		return availableToBuild;
	}

	public void setAvailableToBuild(boolean availableToBuild) {
		this.availableToBuild = availableToBuild;
	}
	public void setShields(int inShield){
		shields = inShield;
	}

	public void setUpkeep(int upkeep) {
		this.upkeep = upkeep;
	}
	
	public void setSpaceshipRange(SpaceshipRange range) {
		this.range = range;
	}
	
	public SpaceshipRange getSpaceshipRange() {
		return range;
	}

	public int getAimBonus() {
		return aimBonus;
	}

	public void setAimBonus(int aimBonus) {
		this.aimBonus = aimBonus;
	}

	public int getTroopCapacity() {
		return troopCapacity;
	}

	public void setTroopCapacity(int troopCapacity) {
		this.troopCapacity = troopCapacity;
	}

	public boolean isWorldUnique() {
		return worldUnique;
	}

	public void setWorldUnique(boolean worldUnique) {
		this.worldUnique = worldUnique;
	}
	
	public boolean isFactionUnique() {
		return factionUnique;
	}

	public void setFactionUnigue(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}
	
	public boolean isPlayerUnique() {
		return playerUnique;
	}

	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}

	public void setPsychWarfare(int newPsychWarfare) {
		psychWarfare = newPsychWarfare;
	}

	public int getBlackmarketFirstTurn() {
		return blackmarketFirstTurn;
	}

	public void setBlackmarketFirstTurn(int blackmarketFirstTurn) {
		this.blackmarketFirstTurn = blackmarketFirstTurn;
	}

	public int getBluePrintFirstTurn() {
		return bluePrintFirstTurn;
	}

	public void setBluePrintFirstTurn(int bluePrintFirstTurn) {
		this.bluePrintFirstTurn = bluePrintFirstTurn;
	}

	public BlackMarketFrequency getBluePrintFrequency() {
		return bluePrintFrequency;
	}

	public void setBluePrintFrequency(BlackMarketFrequency bluePrintFrequency) {
		this.bluePrintFrequency = bluePrintFrequency;
	}

	public String getAdvantages() {
		return advantages;
	}

	public void setAdvantages(String advantages) {
		this.advantages = advantages;
	}
	
	public BlackMarketFrequency getBlackMarketFrequency() {
		return blackMarketFrequency;
	}

	public void setBlackMarketFrequency(BlackMarketFrequency blackMarketFrequency) {
		this.blackMarketFrequency = blackMarketFrequency;
	}

    public boolean isAlwaysRetreat() {
		return alwaysRetreat;
	}

	public void setAlwaysRetreat(boolean alwaysRetreat) {
		this.alwaysRetreat = alwaysRetreat;
	}

	public boolean isScreened() {
		return screened;
	}

	public void setScreened(boolean screened) {
		this.screened = screened;
	}

}

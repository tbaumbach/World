
package spaceraze.world;

import java.io.Serializable;

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

    @ManyToOne
    @JoinColumn(name = "FK_GAME_WORLD")
    private GameWorld gameWorld;

    private String name;
    private String shortName;
    private SpaceShipSize size;
    int shields;
    int upkeep;
    int buildCost;
    int hits;
    int bombardment;
    int increaseInitiative;
    int initDefence;

    private SpaceshipRange range;
    int psychWarfare;
    boolean noRetreat = false;
    boolean initSupport = false;
    private int weaponsStrengthSmall;
    private int weaponsStrengthMedium;
    private int weaponsStrengthLarge;
    private int weaponsStrengthHuge;
    private int weaponsMaxSalvoesMedium; // if Integer.MAX then treat as infinite
    private int weaponsMaxSalvoesLarge;
    private int weaponsMaxSalvoesHuge;
    private int armorSmall;
    private int armorMedium;
    private int armorLarge;
    private int armorHuge;
    private SpaceShipSize supply = SpaceShipSize.NONE; //max size of ship that can be resupplied
    private boolean canAppearOnBlackMarket = true;
    private BlackMarketFrequency blackMarketFrequency = BlackMarketFrequency.COMMON;
    private int blackmarketFirstTurn = 0;
    private int bluePrintFirstTurn = 0;
    private BlackMarketFrequency bluePrintFrequency = BlackMarketFrequency.COMMON;
    
    private boolean planetarySurvey;
    private boolean canAttackScreenedShips;
    private int weaponsStrengthSquadron;
    private int squadronCapacity;
    private SpaceshipTargetingType targetingType = null;
    private boolean squadron = false;
    private String description,history;
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
    
//    private int weaponsAirToGround;
//    private int nrAirToGoundAttacks = 3;
//  worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player.
	private boolean worldUnique=  false, factionUnique = false, playerUnique =  false;
	/*
	public int getNrAirToGoundAttacks() {
		return nrAirToGoundAttacks;
	}

	public void setNrAirToGoundAttacks(int nrAirToGoundAttacks) {
		this.nrAirToGoundAttacks = nrAirToGoundAttacks;
	}

	public int getWeaponsAirToGround() {
		return weaponsAirToGround;
	}

	public void setWeaponsAirToGround(int weaponsAirToGround) {
		this.weaponsAirToGround = weaponsAirToGround;
	}*/

	public SpaceshipType(String name, String shortName, SpaceShipSize size, int shields, int hits, SpaceshipRange range, int upkeep, int buildCost, int weaponsStrengthSmall){
    	this(name,shortName, size,shields,hits,range,upkeep,buildCost, weaponsStrengthSmall,0);
    }
	
	@JsonIgnore
    public String getIncomeOpenString(){
    	StringBuffer sb = new StringBuffer();
        if (getIncOwnOpenBonus() > 0){
            sb.append(getIncOwnOpenBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncFriendlyOpenBonus() > 0){
            sb.append(getIncFriendlyOpenBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncNeutralOpenBonus() > 0){
            sb.append(getIncNeutralOpenBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncEnemyOpenBonus() > 0){
            sb.append(getIncEnemyOpenBonus());
        }else{
        	sb.append("-");
        }
    	return sb.toString();
    }

	@JsonIgnore
    public String getIncomeClosedString(){
    	StringBuffer sb = new StringBuffer();
        if (getIncOwnClosedBonus() > 0){
            sb.append(getIncOwnClosedBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncFriendlyClosedBonus() > 0){
            sb.append(getIncFriendlyClosedBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncNeutralClosedBonus() > 0){
            sb.append(getIncNeutralClosedBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncEnemyClosedBonus() > 0){
            sb.append(getIncEnemyClosedBonus());
        }else{
        	sb.append("-");
        }
    	return sb.toString();
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
        setArmor(0,0,0,0);
    }
	
    public SpaceshipType(String name, String shortName, SpaceShipSize size, int shields, int hits, SpaceshipRange range, int upkeep, int buildCost, int weaponsStrengthSmall, int weaponsStrengthSquadron){
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
        setStandardArmorLevels();
        // Default (almost) infinite number of salvoes
        this.weaponsMaxSalvoesMedium = Integer.MAX_VALUE;
        this.weaponsMaxSalvoesLarge = Integer.MAX_VALUE;
        this.weaponsMaxSalvoesHuge = Integer.MAX_VALUE;
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

    public SpaceshipType(SpaceshipType oldsst){
        this.name = oldsst.getName();
        this.shortName = oldsst.getShortName();
        this.size = oldsst.getSize();
        this.range = oldsst.getRange();
        this.shields = oldsst.getShields();
        this.upkeep = oldsst.getUpkeep();
        this.buildCost = oldsst.getBuildCost(null);
        this.bombardment = oldsst.getBombardment();
        this.noRetreat = oldsst.getNoRetreat();
        this.hits = oldsst.getHits();
        this.setInitSupport(oldsst.getInitSupport());
        if (oldsst.getInitSupport()){
        	this.increaseInitiative = oldsst.getInitSupportBonus();
        }else{
        	this.increaseInitiative = oldsst.getInitiativeBonus();
        }
        this.initDefence = oldsst.getInitDefence();
        this.weaponsStrengthSmall = oldsst.getWeaponsStrengthSmall(); 
        this.weaponsStrengthMedium = oldsst.getWeaponsStrengthMedium();
        this.weaponsStrengthLarge = oldsst.getWeaponsStrengthLarge(); 
        this.weaponsStrengthHuge = oldsst.getWeaponsStrengthHuge();
        this.weaponsMaxSalvoesMedium = oldsst.getWeaponsMaxSalvoesMedium();
        this.weaponsMaxSalvoesLarge = oldsst.getWeaponsMaxSalvoesLarge();
        this.weaponsMaxSalvoesHuge = oldsst.getWeaponsMaxSalvoesHuge();
        this.supply = oldsst.getSupply();
        this.armorSmall = oldsst.getArmorSmall();
        this.armorMedium = oldsst.getArmorMedium();
        this.armorLarge = oldsst.getArmorLarge();
        this.armorHuge = oldsst.getArmorHuge();
        this.planetarySurvey = oldsst.isPlanetarySurvey();
//        this.siegeBonus = oldsst.getSiegeBonus();
//        this.troops = oldsst.getTroops();
        this.psychWarfare = oldsst.getPsychWarfare();
        this.targetingType = oldsst.getTargetingType();
        this.squadronCapacity = oldsst.getSquadronCapacity();
        this.weaponsStrengthSquadron = oldsst.getWeaponsStrengthSquadron();
        this.squadron = oldsst.isSquadron();
        this.description = oldsst.getDescription();
        this.history = oldsst.getHistory();
        this.incEnemyClosedBonus = oldsst.incEnemyClosedBonus;
        this.incEnemyOpenBonus = oldsst.incEnemyOpenBonus;
        this.incFriendlyClosedBonus = oldsst.incFriendlyClosedBonus;
        this.incFriendlyOpenBonus = oldsst.incFriendlyOpenBonus;
        this.incNeutralClosedBonus = oldsst.incNeutralClosedBonus;
        this.incNeutralOpenBonus = oldsst.incNeutralOpenBonus;
        this.incOwnClosedBonus = oldsst.incOwnClosedBonus;
        this.incOwnOpenBonus = oldsst.incOwnOpenBonus;
        this.canAttackScreenedShips = oldsst.canAttackScreenedShips;
        this.civilian = oldsst.civilian;
        this.lookAsCivilian = oldsst.lookAsCivilian;
        this.canBlockPlanet = oldsst.canBlockPlanet;
        this.visibleOnMap = oldsst.isVisibleOnMap();
        this.availableToBuild = oldsst.isAvailableToBuild();
        this.troopCapacity = oldsst.getTroopCapacity();
     //   this.weaponsAirToGround = oldsst.getWeaponsAirToGround();
     //   this.nrAirToGoundAttacks = oldsst.getNrAirToGoundAttacks();
        this.worldUnique = oldsst.isWorldUnique();
        this.factionUnique = oldsst.isFactionUnique();
        this.playerUnique = oldsst.isPlayerUnique();
        this.alwaysRetreat = oldsst.isAlwaysRetreat();
        this.screened = oldsst.isScreened();
        
        this.advantages = oldsst.getAdvantages();
        this.disadvantages = oldsst.getDisadvantages();
        this.canAppearOnBlackMarket = oldsst.isCanAppearOnBlackMarket();
        this.blackMarketFrequency = oldsst.getBlackMarketFrequency();
        this.blackmarketFirstTurn = oldsst.getBlackmarketFirstTurn();
        this.bluePrintFirstTurn = oldsst.getBluePrintFirstTurn();
        this.bluePrintFrequency = oldsst.getBluePrintFrequency();
    }

    /**
     * Used to get players SpaceshipType updated by PlayerSpaceshipType.
     */
    public SpaceshipType(SpaceshipType originSpaceshipType, PlayerSpaceshipImprovement playerSpaceshipImprovement){
        this.name = originSpaceshipType.getName();
        this.shortName = originSpaceshipType.getShortName();
        this.size = originSpaceshipType.getSize();
        this.range = playerSpaceshipImprovement.getRange() != null ? playerSpaceshipImprovement.getRange() : originSpaceshipType.getRange();
        this.shields = originSpaceshipType.getShields() + playerSpaceshipImprovement.getShields();
        this.upkeep = originSpaceshipType.getUpkeep() + playerSpaceshipImprovement.getUpkeep();
        this.buildCost = originSpaceshipType.getBuildCost(null) + playerSpaceshipImprovement.getBuildCost();
        this.bombardment = originSpaceshipType.getBombardment() + playerSpaceshipImprovement.getBombardment();
        this.noRetreat = playerSpaceshipImprovement.isNoRetreat();

        //Why can't we research hitpoints?
        this.hits = originSpaceshipType.getHits();

        this.setInitSupport(originSpaceshipType.getInitSupport());
        if (originSpaceshipType.getInitSupport()){
            this.increaseInitiative = originSpaceshipType.getInitSupportBonus() + playerSpaceshipImprovement.getIncreaseInitiative();
        }else{
            this.increaseInitiative = originSpaceshipType.getInitiativeBonus() + playerSpaceshipImprovement.getIncreaseInitiative();
        }
        this.initDefence = originSpaceshipType.getInitDefence() + playerSpaceshipImprovement.getInitDefence();
        this.weaponsStrengthSquadron = originSpaceshipType.getWeaponsStrengthSquadron() + playerSpaceshipImprovement.getWeaponsStrengthSquadron();
        this.weaponsStrengthSmall = originSpaceshipType.getWeaponsStrengthSmall() + playerSpaceshipImprovement.getWeaponsStrengthSmall();
        this.weaponsStrengthMedium = originSpaceshipType.getWeaponsStrengthMedium() + playerSpaceshipImprovement.getWeaponsStrengthMedium();
        this.weaponsStrengthLarge = originSpaceshipType.getWeaponsStrengthLarge() + playerSpaceshipImprovement.getWeaponsStrengthLarge();
        this.weaponsStrengthHuge = originSpaceshipType.getWeaponsStrengthHuge() + playerSpaceshipImprovement.getWeaponsStrengthHuge();
        this.weaponsMaxSalvoesMedium = originSpaceshipType.getWeaponsMaxSalvoesMedium() + playerSpaceshipImprovement.getWeaponsMaxSalvosMedium();
        this.weaponsMaxSalvoesLarge = originSpaceshipType.getWeaponsMaxSalvoesLarge() + playerSpaceshipImprovement.getWeaponsMaxSalvosLarge();
        this.weaponsMaxSalvoesHuge = originSpaceshipType.getWeaponsMaxSalvoesHuge() + playerSpaceshipImprovement.getWeaponsMaxSalvosHuge();
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
        this.squadron = originSpaceshipType.isSquadron();
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
    
    public boolean getInitSupport(){
    	return initSupport;
    }

    public void setBuildCost(int buildCost){
    	this.buildCost = buildCost;
    }
    
    @JsonIgnore
    public int getBuildCost(VIP vipWithBonus){
      int tempBuildCost = buildCost;
      if (vipWithBonus != null){
    	  int vipBuildbonus = 100 - vipWithBonus.getShipBuildBonus();
    	  double tempBuildBonus = vipBuildbonus / 100.0;
    	  tempBuildCost = (int) Math.round(tempBuildCost * tempBuildBonus);
    	  if (tempBuildCost < 1){
    		  tempBuildCost = 1;
    	  }
      }
      return tempBuildCost;
    }

    @JsonIgnore
    public Spaceship getShip(VIP vipWithBonus, int factionTechBonus, int buildingBonus, int uniqueId){
      return new Spaceship(this, null, 0, vipWithBonus, factionTechBonus,buildingBonus);
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
/*
 * public int getRealWeapons(){ return getVirtualShip().getWeapons(); }
 * 
 * public int getRealShields(){ return getVirtualShip().getShields(); }
 * 
 * public int getRealDamageCapacity(){ return
 * getVirtualShip().getDamageCapacity(); }
 */
    public String getName(){
      return name;
    }

    public String getShortName(){
      return shortName;
    }
/*
 * public int getDamageCapacity(){ return damagecapacity; }
 */
    public int getInitiativeBonus(){
    	int tmpInitBonus = 0;
    	if (!initSupport){
    		tmpInitBonus = increaseInitiative;
    	}
    	return tmpInitBonus;
    }

    public int getInitSupportBonus(){
    	int tmpInitSupportBonus = 0;
    	if (initSupport){
    		tmpInitSupportBonus = increaseInitiative;
    	}
    	return tmpInitSupportBonus;
    }

    public int getInitDefence(){
        return initDefence;
      }

    @JsonIgnore
    public String getRangeString(){
/*
 * String returnValue = "short"; if (range == LONG){ returnValue = "long"; }else
 * if (range == NONE){ returnValue = "none"; } return returnValue;
 */
    	return range.toLowercaseString();
    }

    public boolean getNoRetreat(){
      return noRetreat;
    }

    public void setNoRetreat(boolean newValue){
      noRetreat = newValue;
    }

    @JsonIgnore
    public String getNoRetreatString(){
      String returnString = "No";
      if (noRetreat){
        returnString = "Yes";
      }
      return returnString;
    }

	public int getWeaponsMaxSalvoesHuge() {
		return weaponsMaxSalvoesHuge;
	}
	
	public int getWeaponsMaxSalvoesLarge() {
		return weaponsMaxSalvoesLarge;
	}
	
	public int getWeaponsMaxSalvoesMedium() {
		return weaponsMaxSalvoesMedium;
	}

	@JsonIgnore
	public String getWeaponsMaxSalvoesMediumString() {
		String retStr = "0";
		if ((weaponsMaxSalvoesMedium > 0) & (weaponsMaxSalvoesMedium < Integer.MAX_VALUE)){
			retStr = String.valueOf(weaponsMaxSalvoesMedium);
		}
		return retStr;
	}

	@JsonIgnore
	public String getWeaponsMaxSalvoesLargeString() {
		String retStr = "0";
		if ((weaponsMaxSalvoesLarge > 0) & (weaponsMaxSalvoesLarge < Integer.MAX_VALUE)){
			retStr = String.valueOf(weaponsMaxSalvoesLarge);
		}
		return retStr;
	}

	@JsonIgnore
	public String getWeaponsMaxSalvoesHugeString() {
		String retStr = "0";
		if ((weaponsMaxSalvoesHuge > 0) & (weaponsMaxSalvoesHuge < Integer.MAX_VALUE)){
			retStr = String.valueOf(weaponsMaxSalvoesHuge);
		}
		return retStr;
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

	public boolean isSquadron() {
		return squadron;
	}
	
	public void setSquadron(boolean newValue){
		squadron = newValue;
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
	
	public void setStandardArmorLevels(){
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

	public void setWeaponsMaxSalvoesHuge(int weaponsMaxSalvoesHuge) {
		this.weaponsMaxSalvoesHuge = weaponsMaxSalvoesHuge;
	}

	public void setWeaponsMaxSalvoesLarge(int weaponsMaxSalvoesLarge) {
		this.weaponsMaxSalvoesLarge = weaponsMaxSalvoesLarge;
	}

	public void setWeaponsMaxSalvoesMedium(int weaponsMaxSalvoesMedium) {
		this.weaponsMaxSalvoesMedium = weaponsMaxSalvoesMedium;
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
	
	@JsonIgnore
	public SpaceshipTargetingType getTargetingType(){
		return targetingType;
	}
		
	public String getShipType(){
		String type = "Capital Ship";
		if (isSquadron()){
			if (targetingType == SpaceshipTargetingType.ANTIAIR){
				type = "Fighter Squadron";
			}else
			if (targetingType == SpaceshipTargetingType.ANTIMBU){
				type = "Bomber Squadron";
			}else{ // ALLROUND
				type = "Multirole Squadron";
			}
		}
		return type;
	}
	
	public SpaceshipType copy(){
		return new SpaceshipType(this);
	}

	public boolean isDefenceShip(){
		return !isCivilian() & !squadron & (range == SpaceshipRange.NONE); 
	}
	
	@JsonIgnore
	public String getTotalDescription(){
		String totalDescription = "";
    	
		if(advantages != null && !advantages.equals("")){
    		totalDescription += "Advantages: " + advantages + "\n\n";
        }
    	if(disadvantages != null && !disadvantages.equals("")){
    		totalDescription += "Disadvantages: " + disadvantages + "\n\n";
        }
    	
    	if(shortDescription != null && !shortDescription.equals("")){
    		totalDescription += "Short Description\n";
        	totalDescription += shortDescription + "\n\n";
    	}
    	
    	if(description != null && !description.equals("")){
    		totalDescription +="Description\n";
        	totalDescription += description + "\n\n";
    	}
    	if(history != null && !history.equals("")){
    		totalDescription +="History\n";
        	totalDescription += history;
    	}
    	
    	return totalDescription;
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
	
	public void setArmor(int... armorValues){
		armorSmall = armorValues[0];
		if (armorValues.length > 1){
			armorMedium = armorValues[1];
			if (armorValues.length > 2){
				armorLarge = armorValues[2];
				if (armorValues.length > 3){
					armorHuge = armorValues[3];
				}
			}
		}
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
	
	public String getUniqueString(){
		String uniqueString = "";
  
		if(playerUnique){
			uniqueString = "Player unique";
		}else
		if(factionUnique){
			uniqueString = "Faction unique";
		}else
		if(worldUnique){
			uniqueString = "World unique";
		}
  
		return uniqueString;
	}

	public boolean isPlayerUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().spaceshipTypeExist(this, null, aPlayer);
	}

	public boolean isWorldUniqueBuild(Galaxy aGalaxy) {
		return aGalaxy.spaceshipTypeExist(this, null, null);
	}

	public boolean isFactionUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().spaceshipTypeExist(this, aPlayer.getFaction(), null);
	}

	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}
	
	public boolean isConstructible(Player aPlayer){
	//	LoggingHandler.fine("isConstructible aPlayer: " + aPlayer.getName() + " SpaceType namn: " + name);
	//	LoggingHandler.fine("isWorldUnique isFactionUnique isPlayerUnique : " + isWorldUnique() + " " + isFactionUnique() + " " +isPlayerUnique());
		
		boolean constructible =  true;
		if(!isAvailableToBuild()){
			constructible = false;
		}else if((isWorldUnique() && isWorldUniqueBuild(aPlayer.getGalaxy())) || (isFactionUnique() && isFactionUniqueBuild(aPlayer)) || (isPlayerUnique() && isPlayerUniqueBuild(aPlayer))){
			constructible = false;
		}else if(isWorldUnique() || isFactionUnique() || isPlayerUnique()){
		//	LoggingHandler.fine("isWorldUnique/isFactionUnique/isPlayerUnique check orders ");
			// check if a build order already exist
			if(aPlayer.getOrders().haveSpaceshipTypeBuildOrder(this)){
				constructible = false;
			}
			for (BlackMarketOffer aBlackMarketOffer : aPlayer.getGalaxy().getCurrentOffers()) {
				if(aBlackMarketOffer.isShip() && aBlackMarketOffer.getSpaceshipType().getName().equals(name)){
					constructible = false;
				}
			}
		}
		return constructible;
	}
	
	@JsonIgnore
	public boolean isReadyToUseInBlackMarket(Galaxy aGalaxy){
		boolean constructible =  false;
		
		if (aGalaxy.getTurn() >= getBlackmarketFirstTurn()){
			if (getRange().canMove() | isSquadron()){
				if (isCanAppearOnBlackMarket()){
					if(!isPlayerUnique() && !isFactionUnique()){
						if(isWorldUnique() && !isWorldUniqueBuild(aGalaxy)){
							boolean isAlreadyAoffer = false;
							for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getCurrentOffers()) {
								if(aBlackMarketOffer.isShip() && aBlackMarketOffer.getSpaceshipType().getName().equals(name)){
									isAlreadyAoffer = true;
								}
							}
							if(!isAlreadyAoffer){
								boolean haveBuildingOrder = false;
								for (Player tempPlayer : aGalaxy.getPlayers()) {
									if(tempPlayer.getOrders().haveSpaceshipTypeBuildOrder(this)){
										haveBuildingOrder = true;
									}
								}
								if(!haveBuildingOrder){
									constructible =  true;
								}
							}
						}else{
							constructible = true;
						}
					}
				}
			}
		}
		return constructible;
	}
	
	@JsonIgnore
	public boolean isBluePrintReadyToUseInBlackMarket(int turn){
		boolean constructible =  false;
		
		if (turn >= getBluePrintFirstTurn()){
			if (bluePrintFrequency != BlackMarketFrequency.NEVER){
				if(!isPlayerUnique() && !isFactionUnique() && !isWorldUnique()){
					constructible = true;
				}
			}
		}
		
		return constructible;
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
	
	public boolean isCapitalShip(){
		return !isCivilian() & !isSquadron() & (getRange() != SpaceshipRange.NONE);
	}
	
	public boolean isCarrier() {
		return getSquadronCapacity() > 0;
	}

	/**
	 * Trader ships is defined by this method as a civilian ship who have bonus in at least one open planet income bonus
	 */
	public boolean isTrader() {
		return isCivilian() & (incEnemyOpenBonus > 0) | (incNeutralOpenBonus > 0) | (incFriendlyOpenBonus > 0) | (incOwnOpenBonus > 0);
	}

	/**
	 * Smuggler ships is defined by this method as a civilian ship who have bonus in at least one closed planet income bonus
	 */
	public boolean isSmuggler() {
		return isCivilian() & ((incEnemyClosedBonus > 0) | (incNeutralClosedBonus > 0) | (incFriendlyClosedBonus > 0) | (incOwnClosedBonus > 0));
	}
	
	/**
	 * Any ship that have psych warfare > 0
	 * @return true if psych warfare > 0
	 */
	/*public boolean isPsychWarfare(){
		return psychWarfare > 0;
	}*/

    /**
     * @return true if small capital psych warfare shiptype
     */
    public boolean isSmallPsychWarfareShiptype(){
    	return isCapitalShip() & psychWarfare > 0 & (getSize().getSlots() == 1) & !isCarrier(); // !isCarrier() is added to exclude Bug small meteorite
    }

}

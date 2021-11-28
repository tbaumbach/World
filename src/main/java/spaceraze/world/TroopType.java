package spaceraze.world;

import java.io.Serializable;
import java.util.UUID;

import lombok.*;
import spaceraze.world.enums.BattleGroupPosition;
import spaceraze.world.enums.BlackMarketFrequency;
import spaceraze.world.enums.TroopTargetingType;
import spaceraze.world.enums.TypeOfTroop;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "TROOP_TYPE")
public class TroopType implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_GAME_WORLD")
	private GameWorld gameWorld;

	private String key;
	private String name;
	private String shortName;

	@Column(length = 4000)
	private String description;

	@Column(length = 4000)
	private String history;

	@Column(length = 4000)
	private String advantages;

	@Column(length = 4000)
	private String disadvantages;

	@Column(length = 4000)
	private String shortDescription;
	// attack values
	private int attackInfantry;
	private int attackArmored;
	private int attackArtillery;
	// other abilities
	private int damageCapacity;
	private int dropPenalty = 1; // lowers the number of attacks on the first turn when dropped on a planet
	private int firingBackPenalty = 10;  // lowers the damage roll when firing back when under attack
	private int nrAttacks = 3; // number of attacks each turn
	@Enumerated(EnumType.STRING)
	private TypeOfTroop typeOfTroop = TypeOfTroop.INFANTRY; // infantry, armored or support
	private boolean spaceshipTravel = true; // if false a unit can't leave the planet where it is built
	@Enumerated(EnumType.STRING)
	private BlackMarketFrequency blackMarketFrequency = BlackMarketFrequency.COMMON;
	private int blackmarketFirstTurn = 0;
	
	
//	private boolean attackScreened;  // if true unit can be set to flanker
@Enumerated(EnumType.STRING)
	private BattleGroupPosition defaultPosition = BattleGroupPosition.FIRST_LINE;
	private boolean canBuild = true; // determines if a player with this trooptype can build this type
	private boolean visible = true; // if false other players cannot see the troop on the
	private boolean canAppearOnBlackMarket = true;
	@Enumerated(EnumType.STRING)
	private TroopTargetingType targetingType = TroopTargetingType.ALLROUND;
	private int landBattleGroupAttacksBonus;
	// costs
	private int costBuild;
	private int upkeep; // count against each planet's individual upkeep value (=production)
//	worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player.
	private boolean worldUnique = false;
	private boolean factionUnique = false;
	private boolean playerUnique =  false;
	
    public TroopType(String aUniqueName, String aUniqueShortName, int aDamageCapacity, int aUpkeep, int aCostBuild, int anAttackInfantry, int anAttackArmor){
		this.key = UUID.randomUUID().toString();
    	this.name = aUniqueName;
    	this.shortName = aUniqueShortName;
    	this.damageCapacity = aDamageCapacity;
    	this.upkeep = aUpkeep;
    	this.costBuild = aCostBuild;
    	this.attackInfantry = anAttackInfantry;
    	this.attackArmored = anAttackArmor;
    }

    public TroopType(TroopType originalType, PlayerTroopImprovement improvement){
    	this.key = originalType.getKey();
		this.name = originalType.getName();
		this.shortName = originalType.getShortName();
		this.description = originalType.getDescription();
		this.history = originalType.getHistory();
		this.advantages = originalType.getAdvantages();
		this.disadvantages = originalType.getDisadvantages();
		this.shortDescription = originalType.getShortDescription();
		this.typeOfTroop = originalType.getTypeOfTroop();
		this.damageCapacity = originalType.getDamageCapacity() + improvement.getDamageCapacity();
		this.upkeep = originalType.getUpkeep() + improvement.getCostSupport();
		this.costBuild = originalType.getCostBuild() + improvement.getCostBuild();
		this.attackInfantry = originalType.getAttackInfantry() + improvement.getAttackArtillery();
		this.attackArmored = originalType.getAttackArmored() + improvement.getAttackArmored();
		this.attackArtillery = originalType.getAttackArtillery() + improvement.getAttackArtillery();
		this.spaceshipTravel = improvement.isChangeSpaceshipTravel() ? improvement.isSpaceshipTravel() : originalType.isSpaceshipTravel();
		this.canAppearOnBlackMarket = originalType.isCanAppearOnBlackMarket();
		this.blackMarketFrequency = originalType.getBlackMarketFrequency();
		this.blackmarketFirstTurn = originalType.getBlackmarketFirstTurn();
		this.defaultPosition = originalType.getDefaultPosition();
		this.targetingType = originalType.getTargetingType();
		this.canBuild = improvement.isAvailableToBuild();
		this.visible = improvement.isChangeVisible() ? improvement.isVisible() :originalType.isVisible();
		this.landBattleGroupAttacksBonus = originalType.getLandBattleGroupAttacksBonus();
		this.worldUnique = originalType.isWorldUnique();
		this.factionUnique = originalType.isFactionUnique();
		this.playerUnique = originalType.isPlayerUnique();

    }
    
    @Override
    public TroopType clone(){
    	TroopType tmpTroopType = null;
    	try {
			tmpTroopType = (TroopType)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
    	return tmpTroopType;
    }
    
    @Override
    public String toString(){
    	return name + " (" + shortName + ")";
    }

	public String getAdvantages() {
		return advantages;
	}

	public void setAdvantages(String advantages) {
		this.advantages = advantages;
	}

	public TypeOfTroop getTypeOfTroop() {
		return typeOfTroop;
	}
	
	public boolean isArmor(){
		return typeOfTroop == TypeOfTroop.ARMORED;
	}

	public void setTypeOfTroop(TypeOfTroop newType) {
		this.typeOfTroop = newType;
	}

	public int getAttackArmored() {
		return attackArmored;
	}

	public void setAttackArmored(int attackArmored) {
		this.attackArmored = attackArmored;
	}

	public int getAttackArtillery() {
		return attackArtillery;
	}

	public void setAttackArtillery(int attackArtillery) {
		this.attackArtillery = attackArtillery;
	}

	public int getAttackInfantry() {
		return attackInfantry;
	}

	public void setAttackInfantry(int attackInfantry) {
		this.attackInfantry = attackInfantry;
	}
/*
	public boolean isAttackScreened() {
		return attackScreened;
	}

	public void setAttackScreened(boolean attackScreened) {
		this.attackScreened = attackScreened;
	}
*/
	public BlackMarketFrequency getBlackMarketFrequency() {
		return blackMarketFrequency;
	}

	public void setBlackMarketFrequency(BlackMarketFrequency blackMarketFrequency) {
		this.blackMarketFrequency = blackMarketFrequency;
	}

	public void setCostBuild(int costBuild) {
		this.costBuild = costBuild;
	}

	public int getUpkeep() {
		return upkeep;
	}

	public void setUpkeep(int upkeep) {
		this.upkeep = upkeep;
	}

	public int getDamageCapacity() {
		return damageCapacity;
	}

	public void setDamageCapacity(int damageCapacity) {
		this.damageCapacity = damageCapacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisadvantages() {
		return disadvantages;
	}

	public void setDisadvantages(String disadvantages) {
		this.disadvantages = disadvantages;
	}

	public int getDropPenalty() {
		return dropPenalty;
	}

	public void setDropPenalty(int dropPenalty) {
		this.dropPenalty = dropPenalty;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public int getNrAttacks() {
		return nrAttacks;
	}
	
	public int getBlackmarketFirstTurn() {
		return blackmarketFirstTurn;
	}

	public void setBlackmarketFirstTurn(int blackmarketFirstTurn) {
		this.blackmarketFirstTurn = blackmarketFirstTurn;
	}

/*
	public void setNrAttacks(int nrAttacks) {
		this.nrAttacks = nrAttacks;
	}
*/
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public boolean isSpaceshipTravel() {
		return spaceshipTravel;
	}

	public void setSpaceshipTravel(boolean spaceshipTravel) {
		this.spaceshipTravel = spaceshipTravel;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public boolean isCanBuild() {
		return canBuild;
	}

	public void setCanBuild(boolean canBuild) {
		this.canBuild = canBuild;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public BattleGroupPosition getDefaultPosition() {
		return defaultPosition;
	}

	public void setDefaultPosition(BattleGroupPosition defaultPosition) {
		this.defaultPosition = defaultPosition;
	}

	public TroopTargetingType getTargetingType() {
		return targetingType;
	}

	public void setTargetingType(TroopTargetingType targetingType) {
		this.targetingType = targetingType;
	}

	public boolean isCanAppearOnBlackMarket() {
		return canAppearOnBlackMarket;
	}

	public void setCanAppearOnBlackMarket(boolean canAppearOnBlackMarket) {
		this.canAppearOnBlackMarket = canAppearOnBlackMarket;
	}

	public int getFiringBackPenalty() {
		return firingBackPenalty;
	}

	public void setFiringBackPenalty(int firingBackPenalty) {
		this.firingBackPenalty = firingBackPenalty;
	}
	public int getLandBattleGroupAttacksBonus() {
		return landBattleGroupAttacksBonus;
	}

	public void setLandBattleGroupAttacksBonus(int landBattleGroupAttacksBonus) {
		this.landBattleGroupAttacksBonus = landBattleGroupAttacksBonus;
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
	

}

package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.enums.SpaceShipSize;
import spaceraze.world.enums.SpaceshipRange;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class SpaceshipImprovements implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeId;
    private int shields;
    private int upkeep;
    private int buildCost;
    private int bombardment;
    private int increaseInitiative;
    private int initDefence;
    private SpaceshipRange range;
    private boolean noRetreat = false;
    private boolean initSupport = false;
    private int weaponsStrengthSquadron;
    private int weaponsStrengthSmall;
    private int weaponsStrengthMedium;
    private int weaponsStrengthLarge;
    private int weaponsStrengthHuge;
    private int weaponsMaxSalvosMedium;
    private int weaponsMaxSalvosLarge;
    private int weaponsMaxSalvosHuge; // if Integer.MAX then treat as infinite
    private int armorSmall;
    private int armorMedium;
    private int armorLarge;
    private int armorHuge;
    private SpaceShipSize supply; // Max size of ship that can be resupplied
    private int squadronCapacity;
    private String description;
    private String history;
    private String shortDescription;
    private String advantages;
    private String disadvantages;
    private int incEnemyClosedBonus;
    private int incNeutralClosedBonus;
    private int incFriendlyClosedBonus;
    private int incOwnClosedBonus;
    private int incEnemyOpenBonus;
    private int incNeutralOpenBonus;
    private int incFriendlyOpenBonus;
    private int incOwnOpenBonus;
    private boolean planetarySurvey;
    private boolean canAttackScreenedShips;
    private boolean lookAsCivilian = false;
    private boolean canBlockPlanet = true;
    private boolean visibleOnMap = true;
    private int psychWarfare;
    private int troopCarrier;
    private boolean changeNoRetreat = false;
    private boolean changeInitSupport = false;
    private boolean changePlanetarySurvey = false;
    private boolean changeCanAttackScreenedShips = false;
    private boolean changeLookAsCivilian = false;
    private boolean changeCanBlockPlanet = false;
    private boolean changeVisibleOnMap = false;

    public SpaceshipImprovements(String typeId){
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public int getShields() {
        return shields;
    }

    public void setShields(int shields) {
        this.shields = shields;
    }

    public int getUpkeep() {
        return upkeep;
    }

    public void setUpkeep(int upkeep) {
        this.upkeep = upkeep;
    }

    public int getBuildCost() {
        return buildCost;
    }

    public void setBuildCost(int buildCost) {
        this.buildCost = buildCost;
    }

    public int getBombardment() {
        return bombardment;
    }

    public void setBombardment(int bombardment) {
        this.bombardment = bombardment;
    }

    public int getIncreaseInitiative() {
        return increaseInitiative;
    }

    public void setIncreaseInitiative(int increaseInitiative) {
        this.increaseInitiative = increaseInitiative;
    }

    public int getInitDefence() {
        return initDefence;
    }

    public void setInitDefence(int initDefence) {
        this.initDefence = initDefence;
    }

    public SpaceshipRange getRange() {
        return range;
    }

    public void setRange(SpaceshipRange range) {
        this.range = range;
    }

    public boolean isNoRetreat() {
        return noRetreat;
    }

    public void setNoRetreat(boolean noRetreat) {
        this.noRetreat = noRetreat;
    }

    public boolean isInitSupport() {
        return initSupport;
    }

    public void setInitSupport(boolean initSupport) {
        this.initSupport = initSupport;
    }

    public int getWeaponsStrengthSquadron() {
        return weaponsStrengthSquadron;
    }

    public void setWeaponsStrengthSquadron(int weaponsStrengthSquadron) {
        this.weaponsStrengthSquadron = weaponsStrengthSquadron;
    }

    public int getWeaponsStrengthSmall() {
        return weaponsStrengthSmall;
    }

    public void setWeaponsStrengthSmall(int weaponsStrengthSmall) {
        this.weaponsStrengthSmall = weaponsStrengthSmall;
    }

    public int getWeaponsStrengthMedium() {
        return weaponsStrengthMedium;
    }

    public void setWeaponsStrengthMedium(int weaponsStrengthMedium) {
        this.weaponsStrengthMedium = weaponsStrengthMedium;
    }

    public int getWeaponsStrengthLarge() {
        return weaponsStrengthLarge;
    }

    public void setWeaponsStrengthLarge(int weaponsStrengthLarge) {
        this.weaponsStrengthLarge = weaponsStrengthLarge;
    }

    public int getWeaponsStrengthHuge() {
        return weaponsStrengthHuge;
    }

    public void setWeaponsStrengthHuge(int weaponsStrengthHuge) {
        this.weaponsStrengthHuge = weaponsStrengthHuge;
    }

    public int getWeaponsMaxSalvosMedium() {
        return weaponsMaxSalvosMedium;
    }

    public void setWeaponsMaxSalvosMedium(int weaponsMaxSalvosMedium) {
        this.weaponsMaxSalvosMedium = weaponsMaxSalvosMedium;
    }

    public int getWeaponsMaxSalvosLarge() {
        return weaponsMaxSalvosLarge;
    }

    public void setWeaponsMaxSalvosLarge(int weaponsMaxSalvosLarge) {
        this.weaponsMaxSalvosLarge = weaponsMaxSalvosLarge;
    }

    public int getWeaponsMaxSalvosHuge() {
        return weaponsMaxSalvosHuge;
    }

    public void setWeaponsMaxSalvosHuge(int weaponsMaxSalvosHuge) {
        this.weaponsMaxSalvosHuge = weaponsMaxSalvosHuge;
    }

    public int getArmorSmall() {
        return armorSmall;
    }

    public void setArmorSmall(int armorSmall) {
        this.armorSmall = armorSmall;
    }

    public int getArmorMedium() {
        return armorMedium;
    }

    public void setArmorMedium(int armorMedium) {
        this.armorMedium = armorMedium;
    }

    public int getArmorLarge() {
        return armorLarge;
    }

    public void setArmorLarge(int armorLarge) {
        this.armorLarge = armorLarge;
    }

    public int getArmorHuge() {
        return armorHuge;
    }

    public void setArmorHuge(int armorHuge) {
        this.armorHuge = armorHuge;
    }

    public SpaceShipSize getSupply() {
        return supply;
    }

    public void setSupply(SpaceShipSize supply) {
        this.supply = supply;
    }

    public int getSquadronCapacity() {
        return squadronCapacity;
    }

    public void setSquadronCapacity(int squadronCapacity) {
        this.squadronCapacity = squadronCapacity;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getAdvantages() {
        return advantages;
    }

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getDisadvantages() {
        return disadvantages;
    }

    public void setDisadvantages(String disadvantages) {
        this.disadvantages = disadvantages;
    }

    public int getIncEnemyClosedBonus() {
        return incEnemyClosedBonus;
    }

    public void setIncEnemyClosedBonus(int incEnemyClosedBonus) {
        this.incEnemyClosedBonus = incEnemyClosedBonus;
    }

    public int getIncNeutralClosedBonus() {
        return incNeutralClosedBonus;
    }

    public void setIncNeutralClosedBonus(int incNeutralClosedBonus) {
        this.incNeutralClosedBonus = incNeutralClosedBonus;
    }

    public int getIncFriendlyClosedBonus() {
        return incFriendlyClosedBonus;
    }

    public void setIncFriendlyClosedBonus(int incFriendlyClosedBonus) {
        this.incFriendlyClosedBonus = incFriendlyClosedBonus;
    }

    public int getIncOwnClosedBonus() {
        return incOwnClosedBonus;
    }

    public void setIncOwnClosedBonus(int incOwnClosedBonus) {
        this.incOwnClosedBonus = incOwnClosedBonus;
    }

    public int getIncEnemyOpenBonus() {
        return incEnemyOpenBonus;
    }

    public void setIncEnemyOpenBonus(int incEnemyOpenBonus) {
        this.incEnemyOpenBonus = incEnemyOpenBonus;
    }

    public int getIncNeutralOpenBonus() {
        return incNeutralOpenBonus;
    }

    public void setIncNeutralOpenBonus(int incNeutralOpenBonus) {
        this.incNeutralOpenBonus = incNeutralOpenBonus;
    }

    public int getIncFriendlyOpenBonus() {
        return incFriendlyOpenBonus;
    }

    public void setIncFriendlyOpenBonus(int incFriendlyOpenBonus) {
        this.incFriendlyOpenBonus = incFriendlyOpenBonus;
    }

    public int getIncOwnOpenBonus() {
        return incOwnOpenBonus;
    }

    public void setIncOwnOpenBonus(int incOwnOpenBonus) {
        this.incOwnOpenBonus = incOwnOpenBonus;
    }

    public boolean isPlanetarySurvey() {
        return planetarySurvey;
    }

    public void setPlanetarySurvey(boolean planetarySurvey) {
        this.planetarySurvey = planetarySurvey;
    }

    public boolean isCanAttackScreenedShips() {
        return canAttackScreenedShips;
    }

    public void setCanAttackScreenedShips(boolean canAttackScreenedShips) {
        this.canAttackScreenedShips = canAttackScreenedShips;
    }

    public boolean isLookAsCivilian() {
        return lookAsCivilian;
    }

    public void setLookAsCivilian(boolean lookAsCivilian) {
        this.lookAsCivilian = lookAsCivilian;
    }

    public boolean isCanBlockPlanet() {
        return canBlockPlanet;
    }

    public void setCanBlockPlanet(boolean canBlockPlanet) {
        this.canBlockPlanet = canBlockPlanet;
    }

    public boolean isVisibleOnMap() {
        return visibleOnMap;
    }

    public void setVisibleOnMap(boolean visibleOnMap) {
        this.visibleOnMap = visibleOnMap;
    }

    public int getPsychWarfare() {
        return psychWarfare;
    }

    public void setPsychWarfare(int psychWarfare) {
        this.psychWarfare = psychWarfare;
    }

    public int getTroopCarrier() {
        return troopCarrier;
    }

    public void setTroopCarrier(int troopCarrier) {
        this.troopCarrier = troopCarrier;
    }

    public boolean isChangeNoRetreat() {
        return changeNoRetreat;
    }

    public void setChangeNoRetreat(boolean changeNoRetreat) {
        this.changeNoRetreat = changeNoRetreat;
    }

    public boolean isChangeInitSupport() {
        return changeInitSupport;
    }

    public void setChangeInitSupport(boolean changeInitSupport) {
        this.changeInitSupport = changeInitSupport;
    }

    public boolean isChangePlanetarySurvey() {
        return changePlanetarySurvey;
    }

    public void setChangePlanetarySurvey(boolean changePlanetarySurvey) {
        this.changePlanetarySurvey = changePlanetarySurvey;
    }

    public boolean isChangeCanAttackScreenedShips() {
        return changeCanAttackScreenedShips;
    }

    public void setChangeCanAttackScreenedShips(boolean changeCanAttackScreenedShips) {
        this.changeCanAttackScreenedShips = changeCanAttackScreenedShips;
    }

    public boolean isChangeLookAsCivilian() {
        return changeLookAsCivilian;
    }

    public void setChangeLookAsCivilian(boolean changelookAsCivilian) {
        this.changeLookAsCivilian = changelookAsCivilian;
    }

    public boolean isChangeCanBlockPlanet() {
        return changeCanBlockPlanet;
    }

    public void setChangeCanBlockPlanet(boolean changeCanBlockPlanet) {
        this.changeCanBlockPlanet = changeCanBlockPlanet;
    }

    public boolean isChangeVisibleOnMap() {
        return changeVisibleOnMap;
    }

    public void setChangeVisibleOnMap(boolean changeVisibleOnMap) {
        this.changeVisibleOnMap = changeVisibleOnMap;
    }
}

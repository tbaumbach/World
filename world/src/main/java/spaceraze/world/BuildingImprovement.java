package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class BuildingImprovement implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int openPlanetBonus = 0;
    private int closedPlanetBonus = 0;
    private int techBonus = 0;
    private int wharfSize = 0; // if = cannot build ships
    private int troopSize = 0; // if = cannot build troops
    private int buildCost = 0;
    private boolean changeSpaceport;
    private boolean spaceport;
    private boolean changeVisibleOnMap;
    private boolean visibleOnMap;
    private int resistanceBonus = 0;
    private int shieldCapacity = 0;
    private int cannonDamage;
    private int cannonRateOfFire;

    private boolean changeAlienKiller;
    private boolean alienKiller;
    private int counterEspionage = 0;
    private int exterminator = 0;

    public BuildingImprovement(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getOpenPlanetBonus() {
        return openPlanetBonus;
    }

    public void setOpenPlanetBonus(int openPlanetBonus) {
        this.openPlanetBonus = openPlanetBonus;
    }

    public int getClosedPlanetBonus() {
        return closedPlanetBonus;
    }

    public void setClosedPlanetBonus(int closedPlanetBonus) {
        this.closedPlanetBonus = closedPlanetBonus;
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

    public int getTroopSize() {
        return troopSize;
    }

    public void setTroopSize(int troopSize) {
        this.troopSize = troopSize;
    }

    public int getBuildCost() {
        return buildCost;
    }

    public void setBuildCost(int buildCost) {
        this.buildCost = buildCost;
    }

    public boolean isChangeSpaceport() {
        return changeSpaceport;
    }

    public void setChangeSpaceport(boolean changeSpaceport) {
        this.changeSpaceport = changeSpaceport;
    }

    public boolean isSpaceport() {
        return spaceport;
    }

    public void setSpaceport(boolean spaceport) {
        this.spaceport = spaceport;
    }

    public boolean isChangeVisibleOnMap() {
        return changeVisibleOnMap;
    }

    public void setChangeVisibleOnMap(boolean changeVisibleOnMap) {
        this.changeVisibleOnMap = changeVisibleOnMap;
    }

    public boolean isVisibleOnMap() {
        return visibleOnMap;
    }

    public void setVisibleOnMap(boolean visibleOnMap) {
        this.visibleOnMap = visibleOnMap;
    }

    public int getResistanceBonus() {
        return resistanceBonus;
    }

    public void setResistanceBonus(int resistanceBonus) {
        this.resistanceBonus = resistanceBonus;
    }

    public int getShieldCapacity() {
        return shieldCapacity;
    }

    public void setShieldCapacity(int shieldCapacity) {
        this.shieldCapacity = shieldCapacity;
    }

    public int getCannonDamage() {
        return cannonDamage;
    }

    public void setCannonDamage(int cannonDamage) {
        this.cannonDamage = cannonDamage;
    }

    public int getCannonRateOfFire() {
        return cannonRateOfFire;
    }

    public void setCannonRateOfFire(int cannonRateOfFire) {
        this.cannonRateOfFire = cannonRateOfFire;
    }

    public void setChangeAlienKiller(boolean changeAlienKiller) {
        this.changeAlienKiller = changeAlienKiller;
    }

    public boolean isChangeAlienKiller() {
        return changeAlienKiller;
    }

    public boolean isAlienKiller() {
        return alienKiller;
    }

    public void setAlienKiller(boolean alienKiller) {
        this.alienKiller = alienKiller;
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

}

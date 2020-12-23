package spaceraze.world;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.util.general.Logger;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "VIP")
public class VIP implements Serializable, ShortNameable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_GALAXY")
    private Galaxy galaxy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLANET", insertable = false, updatable = false)
    private Planet planetLocation = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_SPACESHIP", insertable = false, updatable = false)
    private Spaceship shipLocation = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_TROOP", insertable = false, updatable = false)
    private Troop troopLocation = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLAYER", insertable = false, updatable = false)
    private Player boss = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_VIP_TYPE", insertable = false, updatable = false)
    private VIPType vipType = null;
    private String uniqueId;
    private int govCounter = -1; // used by diplomats and infestators
    private int govLastTurn = -1;
    private int kills = 0;
    private boolean hasKilled = false;
    private int buildCost = 0;
    private int upkeep = 0;
//  private List<String> visitedPlanetNames; // used by AI to compute how some VIPs should move

    public VIP(VIPType aVIPType, boolean isFanatic) {

        vipType = aVIPType;
        this.uniqueId = UUID.randomUUID().toString();
        if (!isFanatic) {
            buildCost = vipType.getBuildCost();
            upkeep = vipType.getUpkeep();
        }
    }

    // används bl.a i Galaxy när nya spelare skapas. Denna ser inte ut att användas.
    public VIP(Player newBoss, Planet aPlanet, VIPType aVIPType) {
        boss = newBoss;
        planetLocation = aPlanet;
        vipType = aVIPType;
        this.uniqueId = UUID.randomUUID().toString();
    }

    public String getShortName() {
        return vipType.getShortName();
    }

    public String getLocationString() {
        String locationString = "";
        if (planetLocation != null) {
            locationString = planetLocation.getName();
        } else if (troopLocation != null) {
            locationString = troopLocation.getName();
        } else {
            locationString = shipLocation.getName();
        }
        return locationString;
    }

    public Planet getLocation() {
        Planet location = null;
        if (planetLocation != null) {
            location = planetLocation;
        } else if (troopLocation != null) {
            if (troopLocation.getPlanetLocation() != null) {
                location = troopLocation.getPlanetLocation();
            } else {
                location = troopLocation.getShipLocation().getLocation();
            }
        } else {
            location = shipLocation.getLocation();
        }
        return location;
    }

    public void moveVIP(Planet moveToPlanet, TurnInfo ti) {
        String oldLocationString = getLocationString();
        planetLocation = moveToPlanet;
        shipLocation = null;
        troopLocation = null;
        ti.addToLatestVIPReport(vipType.getTypeName() + " has moved from " + oldLocationString + " to " + planetLocation.getName() + ".");
    }

    public void moveVIP(Spaceship moveToShip, TurnInfo ti) {
        String oldLocationString = getLocationString();
        planetLocation = null;
        shipLocation = moveToShip;
        troopLocation = null;
        ti.addToLatestVIPReport(vipType.getTypeName() + " has moved from " + oldLocationString + " to " + shipLocation.getName() + ".");
    }

    public void moveVIP(Troop moveToTroop, TurnInfo ti) {
        String oldLocationString = getLocationString();
        planetLocation = null;
        shipLocation = null;
        troopLocation = moveToTroop;
        ti.addToLatestVIPReport(vipType.getTypeName() + " has moved from " + oldLocationString + " to " + troopLocation.getName() + ".");
    }

    public String getName() {
        return vipType.getTypeName();
    }

    public List<String> getAbilitiesStrings() {
        return vipType.getAbilitiesStrings();
    }

    public void setBoss(Player newBoss) {
        boss = newBoss;
    }

    public void setLocation(Spaceship aShip) {
        shipLocation = aShip;
        planetLocation = null;
        troopLocation = null;
    }

    public void setLocation(Planet aPlanet) {
        planetLocation = aPlanet;
        shipLocation = null;
        troopLocation = null;
    }

    public void setLocation(Troop aTroop) {
        planetLocation = null;
        shipLocation = null;
        troopLocation = aTroop;
    }

    public boolean isDuellist() {
        return vipType.isDuellist();
    }

    public boolean isHardToKill() {
        return vipType.isHardToKill();
    }

    public boolean isGovernor() {
        return vipType.isGovernor();
    }

    public boolean isFTLbonus() {
        return vipType.isFTLbonus();
    }

    public boolean isSpy() {
        return vipType.isSpying();
    }

    public boolean isDiplomat() {
        return vipType.isDiplomat();
    }

    public boolean isCounterSpy() {
        return vipType.isCounterSpy();
    }

    public boolean isAssassin() {
        return vipType.isAssassin();
    }

    public int getAssassinationSkill() {
        int skill = vipType.getAssassination();
        skill = skill + (kills * 5);
        return skill;
    }

    public Player getBoss() {
        return boss;
    }

    public boolean onPlanet() {
        return planetLocation != null;
    }

    public boolean onShip() {
        return shipLocation != null;
    }

    public Planet getPlanetLocation() {
        return planetLocation;
    }

    public Spaceship getShipLocation() {
        return shipLocation;
    }

    public Troop getTroopLocation() {
        return troopLocation;
    }

    public boolean canVisitNeutralPlanets() {
        return vipType.getCanVisitNeutralPlanets();
    }

    public boolean canVisitEnemyPlanets() {
        return vipType.getCanVisitEnemyPlanets();
    }

    public String getTypeName() {
        return vipType.getTypeName();
    }

    public int getGovCounter() {
        return govCounter;
    }

    public void clearGovCounter() {
        govCounter = 0;
    }

    public void incGovCounter() {
        govCounter = govCounter + 1;
    }

    public void incGovCounter(int i) {
        govCounter = govCounter + i;
    }

    public int getGovLastTurn() {
        return govLastTurn;
    }

    public void clearLastTurn() {
        govLastTurn = -1;
    }

    public void setLastTurn(int i) {
        govLastTurn = i;
    }

    public void incKills() {
        kills = kills + 1;
    }

    public int getKills() {
        return kills;
    }

    public void clearHasKilled() {
        hasKilled = false;
    }

    public boolean getHasKilled() {
        return hasKilled;
    }

    public void setHasKilled() {
        hasKilled = true;
    }

    public int getInitDefence() {
        return vipType.getInitDefence();
    }

    public int getInitBonus() {
        return vipType.getInitBonus();
    }

    public String getAlignmentString() {
        return vipType.getAlignmentString();
    }

    public int getInitSquadronBonus() {
        return vipType.getInitFighterSquadronBonus();
    }

    public boolean getShowOnOpenPlanet() {
        return vipType.getShowOnOpenPlanet();
    }

    public boolean hasResistanceBonus() {
        return vipType.getResistanceBonus() > 0;
    }

    public int getResistanceBonus() {
        return vipType.getResistanceBonus();
    }

    public boolean hasPsychWarfareBonus() {
        return vipType.getPsychWarfareBonus() > 0;
    }

    public int getPsychWarfareBonus() {
        return vipType.getPsychWarfareBonus();
    }

    public int getOpenIncBonus() {
        return vipType.getOpenIncBonus();
    }

    public int getClosedIncBonus() {
        return vipType.getClosedIncBonus();
    }

    public int getTechBonus() {
        return vipType.getTechBonus();
    }

    public int getShipBuildBonus() {

        return vipType.getShipBuildBonus();
    }

    public int getTroopBuildBonus() {

        return vipType.getTroopBuildBonus();
    }

    public int getBuildingBuildBonus() {
        return vipType.getBuildingBuildBonus();
    }

    /*
    public int getWharfUpgradeBonus(){
        return vipType.getWharfUpgradeBonus();
    }
  */
    public int getDuellistSkill() {
        int skill = vipType.getDuellistSkill();
        skill = skill + (kills * 5);
        return skill;
    }

    public boolean isWellGuarded() {
        return vipType.isWellGuarded();
    }

    public boolean isImmuneToCounterEspionage() {
        return vipType.isImmuneToCounterEspionage();
    }

    public int getCounterEspionage() {
        int skill = vipType.getCounterEspionage();
        skill = skill + (kills * 5);
        if (skill > 95) {
            skill = 95;
        }
        return skill;
    }

    public boolean getCanVisitEnemyPlanets() {
        return vipType.getCanVisitEnemyPlanets();
    }

    public boolean isInfestator() {
        return vipType.isInfestate();
    }

    public boolean isExterminator() {
        return vipType.getExterminator() > 0;
    }

    public int getExterminatorSkill() {
        int skill = vipType.getExterminator();
        skill = skill + (kills * 5);
        if (skill > 95) {
            skill = 95;
        }
        return skill;
    }

    public boolean hatesDuellist(VIP anotherVIP) {
        return vipType.getAlignment().hateDuellist(anotherVIP.getAlignment().getName());
    }

    public Alignment getAlignment() {
        return vipType.getAlignment();
    }

    public boolean isBattleVip() {
        return vipType.isBattleVip();
    }

    public int getAimBonus() {
        return vipType.getAimBonus();
    }

    public boolean hasAimBonus() {
        return getAimBonus() > 0;
    }

    public String toString() {
        return vipType.getName();
    }

    public boolean isLandBattleVIP() {
        return vipType.isLandBattleVip();
    }


//  public int getTroopAttacksBonus() {
//	  return vipType.getTroopAttacksBonus();
//  }

    public int getLandBattleGroupAttackBonus() {
        return vipType.getLandBattleGroupAttackBonus();
    }

    public int getBuildCost() {
        return buildCost;
    }


    public int getUpkeep() {
        return upkeep;
    }

    public boolean isTroopVIP() {
        return vipType.isTroopVIP();
    }

    public boolean isStealth() {
        return vipType.isStealth();
    }

    public int getBombardmentBonus() {
        return vipType.getBombardmentBonus();
    }

    public boolean isAttackScreenedSquadron() {
        return vipType.isAttackScreenedSquadron();
    }

    public boolean isAttackScreenedCapital() {
        return vipType.isAttackScreenedCapital();
    }

    public boolean isPlanetarySurvey() {
        return vipType.isPlanetarySurvey();
    }

//	public List<String> getVisitedPlanetNames() {
//		return visitedPlanetNames;
//	}
//
//	public void setVisitedPlanetNames(List<String> visitedPlanetNames) {
//		this.visitedPlanetNames = visitedPlanetNames;
//	}

    public static VIP getNewVIP(String vipTypeName, GameWorld gameWorld) {
        VIPType vipType = gameWorld.getVIPTypeByName(vipTypeName);
        VIP tempVIP = vipType.createNewVIP(true);
        return tempVIP;
    }

    public static VIP getNewVIPshortName(String vipTypeShortName, GameWorld gameWorld) {
        Logger.finer("getNewVIPshortName: " + vipTypeShortName);
        VIPType vipType = gameWorld.getVIPTypeByShortName(vipTypeShortName);
        VIP tempVIP = vipType.createNewVIP(true);
        return tempVIP;
    }

}

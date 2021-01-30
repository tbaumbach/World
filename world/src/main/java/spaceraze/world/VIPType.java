package spaceraze.world;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.enums.BlackMarketFrequency;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "VIP_TYPE")
public class VIPType implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String key;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_GAME_WORLD")
    private GameWorld gameWorld;

    // Required values
    private String name; // required, must be unique
    private String shortName; // required, must be unique

    private String advanteges;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_ALIGNMENT")
    private Alignment alignment = null;
    // determines who can have this VIP
    // if duellist is true - determines if this duellist will
    // attack another duellist in the same system
    private BlackMarketFrequency frequency = BlackMarketFrequency.COMMON; // how often is this VIPType found. Default => common => 100

    // Abilities
    private int assassination; // chance to assassinate
    private int counterEspionage; // chance to catch other spies and assassins
    private int exterminator; // chance to catch other spies and assassins
    private boolean spying; // can spy on planets
    private boolean immuneToCounterEspionage; // can not be caught by counter espionage
    private int initBonus; // % bonus
    private int initSupportBonus; // % bonus (not used - yet?), should not be used together with squadrons
    private int initDefence; // % bonus, should not be used together with squadrons
    private int initFighterSquadronBonus; // % bonus
    private int psychWarfareBonus; // bonus to resistance decrease of besieged planet
    //  private int siegeBonus; // bonus to resistance decrease of besieged planet
    private int resistanceBonus; // bonus to resistance on planet
    private int shipBuildBonus; // decreases build cost of ship
    private int troopBuildBonus; // decreases build cost of troops
    private int buildingBuildBonus; // decreases build cost of buildings
    private int techBonus; // increased shields & weapons for ships and troops built (%)
    private int openIncBonus; // increase income on open planet
    private int closedIncBonus; // increase income on closed planet
    private boolean canVisitEnemyPlanets = false;
    private boolean canVisitNeutralPlanets = false;
    private int duellistSkill; // may duel with other duellists, value = skill
    private boolean hardToKill; // cannot be killed by ship destroyed or planet conquered/razed
    private boolean wellGuarded; // cannot be assassinated
    private boolean governor; // player looses if a vip with this ability dies
    private boolean FTLbonus; // enables a short range ship to travel long range - not implemented (yet?)
    private boolean diplomat; // can persuade neutral planets
    private boolean infestate; // can infestate neutral planets
    private boolean showOnOpenPlanet; // makes this vip visible to other players if on an open planet
    private String description, howToPlay;
    private int aimBonus; // increases the chance to fire against the most damage ship.
    //  private int troopAttacksBonus;
    private int landBattleGroupAttackBonus; // bonus percentage to all own troops attack strength in a landbattle. Not cimulative.
    private boolean stealth; // makes a ship invisible on the map
    private int bombardmentBonus; // increase bombardment for a fleet (with a bombardment of at least 1)
    private boolean attackScreenedSquadron; // enables a squadron to attack screened enemy ships
    private boolean attackScreenedCapital; // enables a capital ship to attack screened enemy ships
    private boolean planetarySurvey; // on a ship, act as spy on closed planets

    public static final int DUELLIST_APPRENTICE = 30;
    public static final int DUELLIST_VETERAN = 50;
    public static final int DUELLIST_MASTER = 70;


    private int buildCost = 0;
    private int upkeep = 0;
    private boolean availableToBuild = true; //TODO 2020-05-04 Kolla upp det här
    // private boolean availableToBuild = false; skall vara false om forskning på VIPar skall tillåtas

    // worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player.
    private boolean worldUnique = false;
    private boolean factionUnique = false;
    private boolean playerUnique = false;

    public VIPType(String name, String shortName, Alignment alignment) {
        this.key = UUID.randomUUID().toString();
        this.name = name;
        this.shortName = shortName;
        this.alignment = alignment;
    }

    public String getFrequencyString() {
        return frequency.toString();
    }

    public String getAlignmentString() {
        return alignment.toString();
    }

    public String getTypeName() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public int getInitDefence() {
        return initDefence;
    }

    public boolean isCanVisitEnemyPlanets() {
        return canVisitEnemyPlanets;
    }

    public boolean isCanVisitNeutralPlanets() {
        return canVisitNeutralPlanets;
    }

    public boolean getShowOnOpenPlanet() {
        return showOnOpenPlanet;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public int getAssassination() {
        return assassination;
    }

    public void setAssassination(int assassination) {
        this.assassination = assassination;
    }

    public int getClosedIncBonus() {
        return closedIncBonus;
    }

    public void setClosedIncBonus(int closedIncBonus) {
        this.closedIncBonus = closedIncBonus;
    }

    public int getCounterEspionage() {
        return counterEspionage;
    }

    public void setCounterEspionage(int counterEspionage) {
        this.counterEspionage = counterEspionage;
    }

    public boolean isDiplomat() {
        return diplomat;
    }

    public void setDiplomat(boolean diplomat) {
        this.diplomat = diplomat;
    }

    public void setDuellistSkill(int duellist) {
        this.duellistSkill = duellist;
    }

    public boolean isFTLbonus() {
        return FTLbonus;
    }

    public void setFTLbonus(boolean lbonus) {
        FTLbonus = lbonus;
    }

    public boolean isGovernor() {
        return governor;
    }

    public void setGovernor(boolean governor) {
        this.governor = governor;
    }

    public boolean isHardToKill() {
        return hardToKill;
    }

    public void setHardToKill(boolean hardToKill) {
        this.hardToKill = hardToKill;
    }

    public int getInitBonus() {
        return initBonus;
    }

    public void setInitBonus(int initBonus) {
        this.initBonus = initBonus;
    }

    public int getInitSupportBonus() {
        return initSupportBonus;
    }

    public void setInitSupportBonus(int initSupportBonus) {
        this.initSupportBonus = initSupportBonus;
    }

    public int getOpenIncBonus() {
        return openIncBonus;
    }

    public void setOpenIncBonus(int openIncBonus) {
        this.openIncBonus = openIncBonus;
    }

    public int getPsychWarfareBonus() {
        return psychWarfareBonus;
    }

    public void setPsychWarfareBonus(int psychWarfareBonus) {
        this.psychWarfareBonus = psychWarfareBonus;
    }

    public boolean isSpying() {
        return spying;
    }

    public void setSpying(boolean spying) {
        this.spying = spying;
    }

    public int getTechBonus() {
        return techBonus;
    }

    public void setTechBonus(int techBonus) {
        this.techBonus = techBonus;
    }

    public boolean isWellGuarded() {
        return wellGuarded;
    }

    public void setWellGuarded(boolean wellGuarded) {
        this.wellGuarded = wellGuarded;
    }

    public void setCanVisitEnemyPlanets(boolean canVisitEnemyPlanets) {
        this.canVisitEnemyPlanets = canVisitEnemyPlanets;
    }

    public void setCanVisitNeutralPlanets(boolean canVisitNeutralPlanets) {
        this.canVisitNeutralPlanets = canVisitNeutralPlanets;
    }

    public void setInitDefence(int initDefence) {
        this.initDefence = initDefence;
    }

    public void setShowOnOpenPlanet(boolean showOnOpenPlanet) {
        this.showOnOpenPlanet = showOnOpenPlanet;
    }

    public BlackMarketFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(BlackMarketFrequency newFrequency) {
        this.frequency = newFrequency;
    }

    public boolean isAlignment(Alignment anAlignment) {
        return this.alignment == anAlignment;
    }

    public boolean isCounterSpy() {
        return counterEspionage > 0;
    }

    public int getInitFighterSquadronBonus() {
        return initFighterSquadronBonus;
    }

    public void setInitFighterSquadronBonus(int initFighterSquadronBonus) {
        this.initFighterSquadronBonus = initFighterSquadronBonus;
    }

    public int getResistanceBonus() {
        return resistanceBonus;
    }

    public void setResistanceBonus(int resistanceBonus) {
        this.resistanceBonus = resistanceBonus;
    }

    public boolean isImmuneToCounterEspionage() {
        return immuneToCounterEspionage;
    }

    public void setImmuneToCounterEspionage(boolean immuneToCounterEspionage) {
        this.immuneToCounterEspionage = immuneToCounterEspionage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDesc) {
        description = newDesc;
    }

    public boolean isInfestate() {
        return infestate;
    }

    public void setInfestate(boolean infestate) {
        this.infestate = infestate;
    }

    public int getExterminator() {
        return exterminator;
    }

    public void setExterminator(int exterminator) {
        this.exterminator = exterminator;
    }

    public int getAimBonus() {
        return aimBonus;
    }

    public void setAimBonus(int aimBonus) {
        this.aimBonus = aimBonus;
    }

    public int getLandBattleGroupAttackBonus() {
        return landBattleGroupAttackBonus;
    }

    public void setLandBattleGroupAttackBonus(int landBattleGroupAttackBonus) {
        this.landBattleGroupAttackBonus = landBattleGroupAttackBonus;
    }

    public int getBuildCost() {
        return buildCost;
    }

    public void setBuildCost(int buildcost) {
        this.buildCost = buildcost;
    }

    public int getUpkeep() {
        return upkeep;
    }

    public void setUpkeep(int upkeep) {
        this.upkeep = upkeep;
    }

    public boolean isAvailableToBuild() {
        return availableToBuild;
    }

    public void setAvailableToBuild(boolean availableToBuild) {
        this.availableToBuild = availableToBuild;
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

    public void setPlayerUnique(boolean playerUnique) {
        this.playerUnique = playerUnique;
    }

    public void setFactionUnique(boolean factionUnique) {
        this.factionUnique = factionUnique;
    }

    @JsonProperty("alignment")
    public String getAlignmentName(){
        return alignment.getName();
    }
}
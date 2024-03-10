package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "RESEARCH_ADVANTAGE")
public class ResearchAdvantage implements Serializable, Cloneable  {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false)
	private Long id;

	private String uuid;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_FACTION")
	private Faction faction;
	
	private String name;
	private String description;

	private int timeToResearch = 1;
	private int researchedTurns = 0;
	private int costToResearchOneTurn = 0;
	private int costToResearchOneTurnInPercent = 0;
	private int openPlanetBonus = 0;
	private int closedPlanetBonus = 0;
	private int resistanceBonus = 0;

	@ElementCollection
	@CollectionTable(name = "RESEARCH_ADVANTAGE_CHILDREN")
	private List<String> children = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "RESEARCH_ADVANTAGE_TO_SPACESHIP")
	private List<String> ships = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "RESEARCH_ADVANTAGE_TO_REPLACE_SPACESHIP")
	private List<String> replaceShips = new ArrayList<>();


	private int techBonus = 0; 
	private boolean canReconstruct;
	private int reconstructCostBase = 0;
	private int reconstructCostMultiplier = 0;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "researchAdvantage")
	private List<ResearchUpgradeShip> researchUpgradeShip = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "RESEARCH_ADVANTAGE_TO_TROOP")
	private List<String> troops = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "RESEARCH_ADVANTAGE_TO_REPLACE_TROOP")
	private List<String> replaceTroops = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "researchAdvantage")
	private List<ResearchUpgradeTroop> researchUpgradeTroop = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "RESEARCH_ADVANTAGE_BUILDINGS")
	private List<String> buildings = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "RESEARCH_ADVANTAGE_TO_REPLACE_BUILDING")
	private List<String> replaceBuildings = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "researchAdvantage")
	private List<ResearchUpgradeBuilding> researchUpgradeBuilding = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_CORRUPTION_POINT")
	CorruptionPoint corruptionPoint;
	
	public ResearchAdvantage(String name, String description) {
		this.uuid = UUID.randomUUID().toString();
	    this.name = name;
	    this.description = description;
	    children = new ArrayList<>();
		//parents = new ArrayList<>();
		ships = new ArrayList<>();
		replaceShips = new ArrayList<>();
		troops = new ArrayList<>();
		replaceTroops = new ArrayList<>();
		buildings = new ArrayList<>();
		replaceBuildings = new ArrayList<>();
		researchUpgradeShip =  new ArrayList<>();
		researchUpgradeTroop = new ArrayList<>();
		researchUpgradeBuilding = new ArrayList<>();
	}

	public ResearchAdvantage clone(){
		//TODO När används den här? ser inte rätt ut.
		try {
			ResearchAdvantage temp = (ResearchAdvantage)super.clone();
			temp.children = new ArrayList<>();
			//temp.parents = new ArrayList<>();
			temp.researchUpgradeShip = new ArrayList<>();
			for(int i=0;i<researchUpgradeShip.size();i++){
				temp.researchUpgradeShip.add(researchUpgradeShip.get(i));
			}

			temp.researchUpgradeTroop = new ArrayList<>();
			for(int i=0;i<researchUpgradeTroop.size();i++){
				temp.researchUpgradeTroop.add(researchUpgradeTroop.get(i));
			}

			temp.researchUpgradeBuilding = new ArrayList<>();
			for(int i=0;i<researchUpgradeBuilding.size();i++){
				temp.researchUpgradeBuilding.add(researchUpgradeBuilding.get(i));
			}

			return temp;
		}
		catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public int getTimeToResearch(){
		return timeToResearch;
	}
	
	public void setTimeToResearch(int timeToResearch){
		this.timeToResearch = timeToResearch;
	}
	
	public int getResearchedTurns(){
		return researchedTurns;
	}
	
	public void setResearchedTurns(int researchedTurns){
		this.researchedTurns = researchedTurns;
	}


	public void addChild(ResearchAdvantage child){
		this.children.add(child.getUuid());
	}

	public void addShip(SpaceshipType ship){
		this.ships.add(ship.getUuid());
	}

	public void setShips(List<String> ships){
		this.ships = ships;
	}

	public void addTroopType(TroopType aTroopType){
		troops.add(aTroopType.getUuid());
	}

	public void setTroopTypes(List<String> newTroops){
		troops = newTroops;
	}

	public void addBuildingType(BuildingType aBuildingType){
		buildings.add(aBuildingType.getUuid());
	}

	public void addReplaceShip(SpaceshipType ship){
		this.replaceShips.add(ship.getUuid());
	}

	public void addReplaceTroopTypes(TroopType aTroopType){
		this.replaceTroops.add(aTroopType.getUuid());
	}

	public void addReplaceType(String uuid){
		replaceBuildings.add(uuid);
	}

	public boolean isCanReconstruct() {
		return canReconstruct;
	}

	public void setCanReconstruct(boolean canReconstruct) {
		this.canReconstruct = canReconstruct;
	}

	public int getClosedPlanetBonus() {
		return closedPlanetBonus;
	}

	public void setClosedPlanetBonus(int closedPlanetBonus) {
		this.closedPlanetBonus = closedPlanetBonus;
	}

	public int getOpenPlanetBonus() {
		return openPlanetBonus;
	}

	public void setOpenPlanetBonus(int openPlanetBonus) {
		this.openPlanetBonus = openPlanetBonus;
	}

	public int getReconstructCostBase() {
		return reconstructCostBase;
	}

	public void setReconstructCostBase(int reconstructCostBase) {
		this.reconstructCostBase = reconstructCostBase;
	}

	public int getResistanceBonus() {
		return resistanceBonus;
	}

	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}
	
	public int getTechBonus() {
		return techBonus;
	}

	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}
	
	public int getCostToResearchOneTurn() {
		return costToResearchOneTurn;
	}

	public void setCostToResearchOneTurn(int costToResearchOneTurn) {
		this.costToResearchOneTurn = costToResearchOneTurn;
	}

	public int getCostToResearchOneTurnInPercent() {
		return costToResearchOneTurnInPercent;
	}

	public void setCostToResearchOneTurnInPercent(int costToResearchOneTurnInProcent) {
		this.costToResearchOneTurnInPercent = costToResearchOneTurnInProcent;
	}

	public int getReconstructCostMultiplier() {
		return reconstructCostMultiplier;
	}
	public void setReconstructCostMultiplier(int reconstructCostMultiplier) {
		this.reconstructCostMultiplier = reconstructCostMultiplier;
	}
	public List<ResearchUpgradeShip> getResearchUpgradeShip() {
		return researchUpgradeShip;
	}
	public List<ResearchUpgradeTroop> getResearchUpgradeTroop() {
		return researchUpgradeTroop;
	}
	public List<ResearchUpgradeBuilding> getResearchUpgradeBuilding() {
		return researchUpgradeBuilding;
	}
	public void addResearchUpgradeShip(ResearchUpgradeShip researchUpgradeShip) {
		this.researchUpgradeShip.add(researchUpgradeShip);
	}
	public void addResearchUpgradeTroop(ResearchUpgradeTroop researchUpgradeTroop) {
		this.researchUpgradeTroop.add(researchUpgradeTroop);
	}
	public void addResearchUpgradeBuilding(ResearchUpgradeBuilding researchUpgradeBuilding) {
		this.researchUpgradeBuilding.add(researchUpgradeBuilding);
	}

	public boolean isDeveloped(Player player){
		return player.getResearchProgress(this.getName()) != null && player.getResearchProgress(this.getName()).isDeveloped();
	}
	
}

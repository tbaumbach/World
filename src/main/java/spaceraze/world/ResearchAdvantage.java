package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "RESEARCH_ADVANTAGE")
public class ResearchAdvantage implements Serializable, Cloneable  {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false)
	private Long id;

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

	@JsonIgnore
	@ManyToMany(mappedBy = "parents")
	private List<ResearchAdvantage> children;

	@JoinTable(name = "RESEARCH_ADVANTAGE_CHILDREN", joinColumns = {
			@JoinColumn(name = "PARENT", referencedColumnName = "ID", nullable = false)}, inverseJoinColumns = {
			@JoinColumn(name = "CHILD", referencedColumnName = "ID", nullable = false)})
	@ManyToMany
	@JsonIgnore
	private List<ResearchAdvantage> parents = new ArrayList<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "RESEARCH_ADVANTAGE_TO_SPACESHIP",
			joinColumns = @JoinColumn(name = "RESEARCH_ADVANTAGE"),
			inverseJoinColumns = @JoinColumn(name = "SPACESHIP"))
	@Column(insertable = false, updatable = false)
	@Builder.Default
	private List<SpaceshipType> ships = new ArrayList<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "RESEARCH_ADVANTAGE_TO_REPLACE_SPACESHIP",
			joinColumns = @JoinColumn(name = "RESEARCH_ADVANTAGE"),
			inverseJoinColumns = @JoinColumn(name = "SPACESHIP"))
	@Column(insertable = false, updatable = false)
	@Builder.Default
	private List<SpaceshipType> replaceShips = new ArrayList<>();



	private int techBonus = 0; 
	private boolean canReconstruct = false;
	private int reconstructCostBase = 0;
	private int reconstructCostMultiplier = 0;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "researchAdvantage")
	@Builder.Default
	private List<ResearchUpgradeShip> researchUpgradeShip = new ArrayList<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "RESEARCH_ADVANTAGE_TO_TROOP",
			joinColumns = @JoinColumn(name = "RESEARCH_ADVANTAGE"),
			inverseJoinColumns = @JoinColumn(name = "TROOP"))
	@Column(insertable = false, updatable = false)
	@Builder.Default
	private List<TroopType> troops = new ArrayList<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "RESEARCH_ADVANTAGE_TO_REPLACE_TROOP",
			joinColumns = @JoinColumn(name = "RESEARCH_ADVANTAGE"),
			inverseJoinColumns = @JoinColumn(name = "TROOP"))
	@Column(insertable = false, updatable = false)
	@Builder.Default
	private List<TroopType> replaceTroops = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "researchAdvantage")
	@Builder.Default
	private List<ResearchUpgradeTroop> researchUpgradeTroop = new ArrayList<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "RESEARCH_ADVANTAGE_TO_BUILDING",
			joinColumns = @JoinColumn(name = "RESEARCH_ADVANTAGE"),
			inverseJoinColumns = @JoinColumn(name = "BUILDING"))
	@Column(insertable = false, updatable = false)
	@Builder.Default
	private List<BuildingType> buildings = new ArrayList<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "RESEARCH_ADVANTAGE_TO_REPLACE_BUILDING",
			joinColumns = @JoinColumn(name = "RESEARCH_ADVANTAGE"),
			inverseJoinColumns = @JoinColumn(name = "BUILDING"))
	@Column(insertable = false, updatable = false)
	@Builder.Default
	private List<BuildingType> replaceBuildings = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "researchAdvantage")
	@Builder.Default
	private List<ResearchUpgradeBuilding> researchUpgradeBuilding = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_CORRUPTION_POINT")
	CorruptionPoint corruptionPoint;
	
	public ResearchAdvantage(String name, String description) {
	    this.name = name;
	    this.description = description;
	    children = new ArrayList<>();
		parents = new ArrayList<>();
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
		
		try {
			ResearchAdvantage temp = (ResearchAdvantage)super.clone();
			temp.children = new ArrayList<>();
			temp.parents = new ArrayList<>();
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

	@JsonIgnore
	public String getResearchText(){
    	String text;
    	
    	text= "";
    	
    	if(openPlanetBonus > 0){
			text+="Open planet bonus: " + addplus(openPlanetBonus) + "\n";
		}
		if(closedPlanetBonus > 0){
			text+="Closed planet bonus: " + addplus(closedPlanetBonus) + "\n";
		}
		if(resistanceBonus > 0){
			text+="Resistance bonus: " + addplus(resistanceBonus) + "\n";
		}
		if(techBonus > 0){
			text+="Tech bonus: " + addplus(techBonus) + "%\n";
		}
		if(canReconstruct){
			text+="Can reconstruct planets\n";
		}
		if(reconstructCostBase > 0){
			text+="Reconstruct cost base: " + addplus(reconstructCostBase) + "\n";
		}
		if(reconstructCostMultiplier > 0){
			text+="Reconstruct multiplier cost: " + addplus(reconstructCostMultiplier) + "\n";
		}
		if(corruptionPoint != null){
			text+="Corruption: " + corruptionPoint.getDescription() + "\n";
		}
		
		for(int i=0;i<researchUpgradeShip.size();i++){			
			text+= "\n" + researchUpgradeShip.get(i).getResearchText();
		}
		
		for (ResearchUpgradeTroop aResearchTroopType: researchUpgradeTroop) {
			text += "\n" + aResearchTroopType.getResearchText();
		}

		for (ResearchUpgradeBuilding aResearchBuildingType: researchUpgradeBuilding) {
			text += "\n" + aResearchBuildingType.getResearchText();
		}
		
    	return text;
	}
	
	private String addplus(int number){
		if(number > 0){
			return "+" +number;
	    }
	    return Integer.valueOf(number).toString();
	}
	
	@SuppressWarnings("unused")
	private String addYesOrNo(boolean test){
	   	if(test){
	   		return "Yes";
	   	}
	   	return "No";
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

	@JsonProperty("children")
	public List<String> getChildrenName(){
		
		return this.children.stream().map(ResearchAdvantage::getName).collect(Collectors.toList());
	}

	@JsonProperty("parents")
	public List<String> getParentsName(){

		return this.parents.stream().map(ResearchAdvantage::getName).collect(Collectors.toList());
	}
	
	public ResearchAdvantage getChild(String key){
		Iterator<ResearchAdvantage> it = children.iterator();
		while(it.hasNext()){
			ResearchAdvantage temp = it.next();
			if(temp.getName().equals(key)){
				return temp;
			}
		}
		return null;
	}
	
	public void addChild(ResearchAdvantage child){		
		addChild(child,true);
	}

	public void addParent(ResearchAdvantage parent){
		addParent(parent,true);
	}

	private void addChild(ResearchAdvantage child, boolean addParentToChild){		
		this.children.add(child);
		if (addParentToChild){
			child.addParent(this,false);
		}
	}

	private void addParent(ResearchAdvantage parent, boolean addChildToParent){
		this.parents.add(parent);
		if (addChildToParent){
			parent.addChild(this,false);
		}
	}

	
	public void setParents(List<ResearchAdvantage> parants){
		this.parents = parants;
	}
		
	public SpaceshipType getShip(String name){
		Iterator<SpaceshipType> it = ships.iterator();
		while(it.hasNext()){
			SpaceshipType tempShip = it.next();
			if(tempShip.getName().equals(name)){
				return tempShip;
			}
		}
		return null;
	}

	public void addShip(SpaceshipType ship){
		this.ships.add(ship);
	}
	
	@JsonProperty("ships")
	public List<String> getShipNames(){
		return ships.stream().map(SpaceshipType::getName).collect(Collectors.toList());
	}

	@JsonProperty("replaceShips")
	public List<String> getReplaceShipNames(){
		return replaceShips.stream().map(SpaceshipType::getName).collect(Collectors.toList());
	}

	
	public void setShips(List<SpaceshipType> ships){
		this.ships = ships;
	}
	
	public TroopType getTroopType(String name){
		TroopType found = null;
		int counter = 0;
		while((found == null) & (counter < troops.size())){
			TroopType tempTroopType = troops.get(counter);
			if(tempTroopType.getName().equals(name)){
				found = tempTroopType;
			}
		}
		return found;
	}

	public void addTroopType(TroopType aTroopType){
		troops.add(aTroopType);
	}


	@JsonProperty("troops")
	public List<String> geTroopNames(){
		return troops.stream().map(TroopType::getName).collect(Collectors.toList());
	}

	@JsonProperty("replaceTroops")
	public List<String> getReplaceTroopNames(){
		return replaceTroops.stream().map(TroopType::getName).collect(Collectors.toList());
	}
	
	public void setTroopTypes(List<TroopType> newTroops){
		troops = newTroops;
	}

	public BuildingType getBuildingType(String name){
		BuildingType found = null;
		int counter = 0;
		while((found == null) & (counter < buildings.size())){
			BuildingType tempBuildingType = buildings.get(counter);
			if(tempBuildingType.getName().equals(name)){
				found = tempBuildingType;
			}
		}
		return found;
	}

	public void addBuildingType(BuildingType aBuildingType){
		buildings.add(aBuildingType);
	}
	
	@JsonProperty("buildings")
	public List<String> getBuildingTypesNames(){
		return buildings.stream().map(BuildingType::getName).collect(Collectors.toList());
	}

	@JsonProperty("replaceBuildings")
	public List<String> getReplaceBuildingTypesNames(){
		return replaceBuildings.stream().map(BuildingType::getName).collect(Collectors.toList());
	}
	
	public List<String> getBuildingTypesName(){
		List<String> buildingNames = new ArrayList<String>();
		for (BuildingType building : buildings) {
			buildingNames.add(building.getName());
		}
		return buildingNames;
	}
	
	public void setBuildingTypes(List<BuildingType> newBuildings){
		buildings = newBuildings;
	}
	
	public SpaceshipType getReplaceShip(String name){
		Iterator<SpaceshipType> it = replaceShips.iterator();
		while(it.hasNext()){
			SpaceshipType tempShip = it.next();
			if(tempShip.getName().equals(name)){
				return tempShip;
			}
		}
		return null;
	}

	public void addReplaceShip(SpaceshipType ship){
		this.replaceShips.add(ship);
	}

	public void setReplaceShips(List<SpaceshipType> replaceShips) {
		this.replaceShips = replaceShips;
	}
	
	public TroopType getReplaceTroopType(String name){
		TroopType found = null;
		int counter = 0;
		while((found == null) & (counter < replaceTroops.size())){
			TroopType tempTroopType = replaceTroops.get(counter);
			if(tempTroopType.getName().equals(name)){
				found = tempTroopType;
			}
		}
		return found;
	}

	public void addReplaceTroopTypes(TroopType aTroopType){
		this.replaceTroops.add(aTroopType);
	}

	public BuildingType getReplaceBuildingType(String name){
		BuildingType found = null;
		int counter = 0;
		while((found == null) & (counter < replaceBuildings.size())){
			BuildingType tempBuildingType = replaceBuildings.get(counter);
			if(tempBuildingType.getName().equals(name)){
				found = tempBuildingType;
			}
		}
		return found;
	}

	public void addReplaceType(BuildingType aBuildingType){
		replaceBuildings.add(aBuildingType);
	}

	public void setReplaceBuildingTypes(List<BuildingType> newBuildings){
		replaceBuildings = newBuildings;
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
	
	public boolean isReadyToBeResearchedOn(Player player){
		for(int i=0;i < parents.size();i++){
			if(!parents.get(i).isDeveloped(player)){
				return false;
			}
		}
		return true;	
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

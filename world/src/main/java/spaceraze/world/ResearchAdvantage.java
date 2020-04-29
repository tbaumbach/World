package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import spaceraze.world.enums.HighlightType;
import spaceraze.util.general.Logger;

public class ResearchAdvantage implements Serializable, Cloneable  {
	static final long serialVersionUID = 1L;
	
	private String name, description;
	private boolean developed;
	private int timeToResearch = 1, researchedTurns = 0;
	private int costToResearchOneTurn = 0;
	private int costToResearchOneTurnInProcent = 0;
	private List<ResearchAdvantage> children;
	@JsonIgnore private List<ResearchAdvantage> parents;
	private List<SpaceshipType> ships,replaceShips;
	private int openPlanetBonus = 0,closedPlanetBonus = 0,resistanceBonus = 0;

	private int techBonus = 0; 
	private boolean canReconstruct = false;
	private int reconstructCostBase = 0;
	private int reconstructCostMultiplier = 0;
	private List<ResearchUpgradeShip> researchUpgradeShip;
	// TroopTypes
	private List<TroopType> troops,replaceTroops;
	private List<ResearchUpgradeTroop> researchUpgradeTroop;
	
	private List<VIPType> vips,replaceVIPs;
	private List<BuildingType> buildings,replaceBuildings;
	private List<ResearchUpgradeVIP> researchUpgradeVIP;
	private List<ResearchUpgradeBuilding> researchUpgradeBuilding;
	
	Corruption corruption;
	
	public ResearchAdvantage(String name, String description) {
	    this.name = name;
	    this.description = description;
	    children = new ArrayList<>();
		parents = new ArrayList<>();
		ships = new ArrayList<>();
		replaceShips = new ArrayList<>();
		troops = new ArrayList<>();
		replaceTroops = new ArrayList<>();
		vips = new ArrayList<>();
		replaceVIPs = new ArrayList<>();
		buildings = new ArrayList<>();
		replaceBuildings = new ArrayList<>();
		//corruption = new Corruption();
		researchUpgradeShip =  new ArrayList<>();
		researchUpgradeTroop = new ArrayList<>();
		researchUpgradeVIP = new ArrayList<>();
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
			
			temp.researchUpgradeVIP = new ArrayList<>();
			for(int i=0;i<researchUpgradeVIP.size();i++){
				temp.researchUpgradeVIP.add(researchUpgradeVIP.get(i));
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
		if(corruption != null){
			text+="Corruption: " + corruption.getCorruptionDescription() + "\n";
		}
		
		for(int i=0;i<researchUpgradeShip.size();i++){			
			text+= "\n" + researchUpgradeShip.get(i).getResearchText();
		}
		
		for (ResearchUpgradeTroop aResearchTroopType: researchUpgradeTroop) {
			text += "\n" + aResearchTroopType.getResearchText();
		}
		
		for (ResearchUpgradeVIP aResearchVIPType: researchUpgradeVIP) {
			text += "\n" + aResearchVIPType.getResearchText();
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
	    return new Integer(number).toString();
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
	
	public boolean isDeveloped(){
		return developed;
	}
	
	public void setDeveloped(boolean developed){
		this.developed = developed;
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
	
	public List<ResearchAdvantage> getChildren(){
		
		return this.children;
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

	@JsonIgnore
	public List<ResearchAdvantage> getParents(){
		return parents;
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
	
	@JsonIgnore
	public List<SpaceshipType> getShips(){
		return ships;
	}
	
	public List<String> getShipsName(){
		List<String> shipNames = new ArrayList<String>();
		for (SpaceshipType Spaceship : ships) {
			shipNames.add(Spaceship.getName());
		}
		return shipNames;
	}
	
	public void setShips(List<SpaceshipType> ships){
		this.ships = ships;
	}
	
	public TroopType getTroopType(String name){
		TroopType found = null;
		int counter = 0;
		while((found == null) & (counter < troops.size())){
			TroopType tempTroopType = troops.get(counter);
			if(tempTroopType.getUniqueName().equals(name)){
				found = tempTroopType;
			}
		}
		return found;
	}

	public void addTroopType(TroopType aTroopType){
		troops.add(aTroopType);
	}
	
	@JsonIgnore
	public List<TroopType> getTroopTypes(){
		return troops;
	}
	
	public List<String> getTroopTypesName(){
		List<String> troopNames = new ArrayList<String>();
		for (TroopType troop : troops) {
			troopNames.add(troop.getUniqueName());
		}
		return troopNames;
	}
	
	public void setTroopTypes(List<TroopType> newTroops){
		troops = newTroops;
	}
	
	public VIPType getVIPType(String name){
		VIPType found = null;
		int counter = 0;
		while((found == null) & (counter < vips.size())){
			VIPType tempVIPType = vips.get(counter);
			if(tempVIPType.getName().equals(name)){
				found = tempVIPType;
			}
		}
		return found;
	}

	public void addVIPType(VIPType aVIPType){
		vips.add(aVIPType);
	}
	
	@JsonIgnore
	public List<VIPType> getVIPTypes(){
		return vips;
	}
	
	public List<String> getVIPTypesName(){
		List<String> vipNames = new ArrayList<String>();
		for (VIPType vip : vips) {
			vipNames.add(vip.getName());
		}
		return vipNames;
	}
	
	public void setVIPTypes(List<VIPType> newVIPs){
		vips = newVIPs;
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
	
	@JsonIgnore
	public List<BuildingType> getBuildingTypes(){
		return buildings;
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

	@JsonIgnore
	public List<SpaceshipType> getReplaceShips() {
		return replaceShips;
	}
	
	public List<String> getReplaceShipsName(){
		List<String> shipNames = new ArrayList<String>();
		for (SpaceshipType Spaceship : replaceShips) {
			shipNames.add(Spaceship.getName());
		}
		return shipNames;
	}
	
	
	public void setReplaceShips(List<SpaceshipType> replaceShips) {
		this.replaceShips = replaceShips;
	}
	
	public TroopType getReplaceTroopType(String name){
		TroopType found = null;
		int counter = 0;
		while((found == null) & (counter < replaceTroops.size())){
			TroopType tempTroopType = replaceTroops.get(counter);
			if(tempTroopType.getUniqueName().equals(name)){
				found = tempTroopType;
			}
		}
		return found;
	}

	public void addReplaceTroopTypes(TroopType aTroopType){
		this.replaceTroops.add(aTroopType);
	}

	@JsonIgnore
	public List<TroopType> getReplaceTroopTypes() {
		return replaceTroops;
	}
	
	public List<String> getReplaceTroopTypesName(){
		List<String> troopNames = new ArrayList<String>();
		for (TroopType troop : replaceTroops) {
			troopNames.add(troop.getUniqueName());
		}
		return troopNames;
	}
	
	public void setReplaceTroopTypes(List<TroopType> replaceTroopTypes) {
		this.replaceTroops = replaceTroopTypes;
	}
	
	public VIPType getReplaceVIPType(String name){
		VIPType found = null;
		int counter = 0;
		while((found == null) & (counter < replaceVIPs.size())){
			VIPType tempVIPType = replaceVIPs.get(counter);
			if(tempVIPType.getName().equals(name)){
				found = tempVIPType;
			}
		}
		return found;
	}

	public void addReplaceVIPType(VIPType aVIPType){
		replaceVIPs.add(aVIPType);
	}
	
	@JsonIgnore
	public List<VIPType> getReplaceVIPTypes(){
		return replaceVIPs;
	}
	
	public List<String> getReplaceVIPTypesName(){
		List<String> vipNames = new ArrayList<String>();
		for (VIPType vip : replaceVIPs) {
			vipNames.add(vip.getName());
		}
		return vipNames;
	}
	
	
	
	public void setReplaceVIPTypes(List<VIPType> replaceVIPTypes){
		replaceVIPs = replaceVIPTypes;
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
	
	@JsonIgnore
	public List<BuildingType> getReplaceBuildingTypes(){
		return replaceBuildings;
	}
	
	public List<String> getReplaceBuildingTypesName(){
		List<String> buildingNames = new ArrayList<String>();
		for (BuildingType building : replaceBuildings) {
			buildingNames.add(building.getName());
		}
		return buildingNames;
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

	public int getCostToResearchOneTurnInProcent() {
		return costToResearchOneTurnInProcent;
	}

	public void setCostToResearchOneTurnInProcent(int costToResearchOneTurnInProcent) {
		this.costToResearchOneTurnInProcent = costToResearchOneTurnInProcent;
	}
	
	public boolean isReadyToBeResearchedOn(){
		for(int i=0;i < parents.size();i++){
			if(!parents.get(i).isDeveloped()){
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
	public List<ResearchUpgradeVIP> getResearchUpgradeVIP() {
		return researchUpgradeVIP;
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

	public Corruption getCorruption() {
		return corruption;
	}

	public void setCorruption(Corruption corruption) {
		this.corruption = corruption;
	}
	
}

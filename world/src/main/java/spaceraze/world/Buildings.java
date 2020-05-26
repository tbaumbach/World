package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import spaceraze.util.general.Logger;

public class Buildings implements Serializable{
	static final long serialVersionUID = 1L;
	
	private List<BuildingType> buildings;
	
	
	public Buildings(){
		buildings = new ArrayList<>();
	}
	
	// kanske inte behövs användas då denna var tänkt att användas vid kloning.  kör functions.deepClone i stället.
	public Buildings(Buildings inObj){
		buildings = new ArrayList<>();
		
		for(int i=0;i < inObj.buildings.size();i++){
			buildings.add(inObj.buildings.get(i));
		}
		
		// klona alla Buildings och glöm inte att lista igenom alla relationer mellan bildings.
		// hoppa över relatioener då det bara är en string (men det kanske görs om så att det blir objektet.
	}
	
	public List<BuildingType> getBuildings() {
		return buildings;
	}
	
	public void addBuilding(BuildingType buildingType){
		buildings.add(buildingType);
	}
	
	public BuildingType getBuildingType(String name){
		BuildingType found = null;
		for(int i= 0; i < buildings.size();i++){
			if(buildings.get(i).getName().equalsIgnoreCase(name)){
				found = buildings.get(i);
			}
		}
		return found;
	}

	//TODO 2020-05-10 This is not a real clone, guess that Players in the same factions have the same Building(s).
	@Override
	public Buildings clone(){
		Logger.finer("Buildings Clone(): ");
		return new Buildings(this);
	}
	
	
}



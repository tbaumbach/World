package spaceraze.world;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import spaceraze.util.general.Logger;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.enums.SpaceShipSize;
import spaceraze.world.incomeExpensesReports.IncomeType;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "PLANET")
public class Planet extends BasePlanet{
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniqueId;
    private int population;
    private int resistance;
    private int basePopulation;
    private boolean startPlanet;
    private boolean open;
    private boolean besieged = false;


    @ManyToOne
    @JoinColumn(name = "FK_GALAXY")
    private Galaxy galaxy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLAYER_IN_CONTROL")
    private Player playerInControl = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_LAST KNOWN_PLAYER_IN_CONTROL")
    private Player lastKnownPlayerInControl = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "REACH_FROM", insertable = false, updatable = false)
    private Planet reachFrom;
    private boolean hasNeverSurrendered = true;
    private int rangeToClosestFriendly; // used in Galaxy when computing startplanet location

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    @Builder.Default
    private List<Building> buildings = new ArrayList<>();


    /**
     * Used when reading map data from file
     * @param dataString containing a tab separated list of data: name\tx\ty\tz
     */
    public Planet(String dataString){
        super(dataString);
		this.buildings = new ArrayList<>();
        population = 0;
        basePopulation = 0;
        resistance = 0;
        startPlanet = false;
        open = false;
    }

    public Planet(BasePlanet basePlanet){
        super(basePlanet);
		this.buildings = new ArrayList<>();
        population = 0;
        basePopulation = 0;
        resistance = 0;
        startPlanet = false;
        open = false;
    }
    
    public Planet(double x, double y, double z, String name, int population, int resistance, boolean startPlanet, boolean isPossibleStartPlanet){
        super(x, y, z, name, isPossibleStartPlanet);
		this.buildings = new ArrayList<>();
        this.population = population;
        basePopulation = population;
        this.resistance = resistance;
        this.startPlanet = startPlanet;
        open = true;
        if (startPlanet){ // A players start planet is always closed in the start of the game.
          open = false;
        }
    }
    
    public Planet clonePlanet(){
    	return new Planet(getX(),getY(),getZ(),getName(), population, resistance,startPlanet, isPossibleStartPlanet());
    }

    public void setProd(int newProd){
        population = newProd;
    }

    public void setProd(int newProd,int newBaseProd){
        population = newProd;
        basePopulation = newBaseProd;
    }

    public void setHomePlanet(Faction playersFaction){
		open = false;
		hasNeverSurrendered = false;
    	if (playersFaction.isAlien()){
    		setProd(0,7);
    		setResistance(7 + playersFaction.getResistanceBonus());
    	}else {
    		setProd(7,7);
    		setResistance(3 + playersFaction.getResistanceBonus());
    	}
    }

    public void setResistance(int newRes){
      resistance = newRes;
    }

    public int getMaxPopulation(){
      return basePopulation *2;
    }

    public Player getPlayerInControl(){
      return playerInControl;
    }

    public void setPlayerInControl(Player p){
      playerInControl = p;
    }

    public boolean isStartPlanet(){
      return startPlanet;
    }

    public void increasePopulation(){
      population = population + 1;
    }

    public void increaseResistance(){
      resistance = resistance + 1;
    }

    public int getPopulation(){
        return population;
    }

    public int getIncome(int openbonus, int closedbonus, TurnInfo playerTurnInfo){
//      LoggingHandler.finer("getIncome " + name + ": " + openbonus + " " + closedbonus);
      int tempIncome = 0;
      if (!besieged){
        if (population > 0){
          tempIncome = population;
          if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.PLANET, "Planet production", getName(), population);
          if (open){
            tempIncome = tempIncome + 2 + openbonus;
            if ((openbonus + 2) > 0){
            	if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.OPEN_BONUS, "Planet open bonus", getName(), 2 + openbonus);
            }
          }else{
            tempIncome = tempIncome + closedbonus;
            if (closedbonus > 0){
            	if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.CLOSED_BONUS, "Planet closed bonus", getName(), closedbonus);
            }
          }
        }
      }
      return tempIncome;
    }

    public int getIncomeAlien(int openbonus, int closedbonus, TurnInfo playerTurnInfo){ 
//    	LoggingHandler.finer("getIncomeAlien " + name + ": " + closedbonus);
    	int tempIncome = 0;
    	if (!besieged){
    		if (resistance > 0){
    			tempIncome = resistance;
    			if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.PLANET, "Planet resistance", getName(), population);
    			if (open){
    				tempIncome = tempIncome + 2 + openbonus;
    				if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.OPEN_BONUS, "Planet open bonus", getName(), 2 + openbonus);
    			}else{
    				tempIncome = tempIncome + closedbonus;
    				if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.CLOSED_BONUS, "Planet closed bonus", getName(), closedbonus);
    			}
    		}
    	}
    	return tempIncome;
    }

    public int getUpkeep(){
      int tempUpkeep = 0;
      if (playerInControl != null && playerInControl.isAlien()){
    	  tempUpkeep = resistance;
      }else{
        if (population > 0){
          tempUpkeep = population;
        }
      }
      return tempUpkeep;
    }

    public boolean isPlayerPlanet(){
      return playerInControl != null;
    }

    public boolean isFactionPlanet(Faction aFaction){
    	boolean sameFaction = false;
    	if (playerInControl != null){
    		if (playerInControl.getFaction() == aFaction){
    			sameFaction = true;
    		}
    	}
    	return sameFaction;
    }
    
    public void setBesieged(boolean value){
    	Logger.finer("setBesieged: " + value);
        besieged = value;
    }

    public boolean isBesieged(){
        return besieged;
    }

    public int getShield(){
    	int biggestShield=0;
    	for(int i=0; i < buildings.size(); i++){
    		if(buildings.get(i).getBuildingType().getShieldCapacity() > biggestShield){
    			biggestShield = buildings.get(i).getBuildingType().getShieldCapacity();
    		}
    	}
    	return biggestShield;
    }

    public boolean getInfectedByAlien(){
        boolean infectedByAlien = false;
        if (playerInControl != null){
        	if (playerInControl.isAlien()){
        		infectedByAlien = true;
        	}
        }
        return infectedByAlien;
    }

    public void besiegedAfterInconclusiveLandbattle(){
    	Logger.fine("besiegedAfterInconclusiveLandbattle");
        setBesieged(true);
    }

    public void peace(){
    	setBesieged(false);
    }

    public int getResistance(){
      return resistance;
    }

    public boolean isRazed(){
      return population == 0;
    }

    public boolean isRazedAndUninfected(){
    	boolean empty = false;
    	if (population == 0){
    		if (playerInControl == null){
    			empty = true;
    		}
    	}
        return empty;
      }

    public void reverseVisibility(){
      open = !open;
      if (open){
        lastKnownPlayerInControl = playerInControl;
      }
    }

    public Player getLastKnownPlayerInControl(){
      return lastKnownPlayerInControl;
    }

    public boolean isOpen(){
      return open;
    }

    public void setReachFrom(Planet newReachFrom){
      reachFrom = newReachFrom;
    }

    public Planet getReachFrom(){
      return reachFrom;
    }

	public int getBasePopulation(){
		return basePopulation;
	}
	
	public String toString(){
		return "Planet " + getName() + " (" + population + "/" + resistance + ")";
	}

	public int getRangeToClosestFriendly() {
		return rangeToClosestFriendly;
	}

	public void setRangeToClosestFriendly(int rangeToClosestFriendly) {
		this.rangeToClosestFriendly = rangeToClosestFriendly;
	}
	
	public void setRazed(){
		setProd(0);
		setResistance(0);
		if (isOpen()){
			reverseVisibility();
		}
		setBuildings(new ArrayList<Building>());
		setPlayerInControl(null);
	}
	
	public boolean isHasNeverSurrendered(){
		return hasNeverSurrendered;
	}
	
	public void setHasNeverSurrendered(boolean newHasNeverSurrendered){
		hasNeverSurrendered = newHasNeverSurrendered;
	}

	public List<Building> getBuildings() {
		return buildings;
	}
	
	public List<Building> getBuildingsInOrbit() {
		return getBuildings(true);
	}

	public List<Building> getBuildingsOnSurface() {
		return getBuildings(false);
	}

	private List<Building> getBuildings(boolean orbitOnly) {
		List<Building> buildingsInOrbit = new ArrayList<Building>();
		for (Building aBuilding : buildings) {
			if(orbitOnly == aBuilding.getBuildingType().isInOrbit()){
				buildingsInOrbit.add(aBuilding);
			}
		}
		return buildingsInOrbit;
	}

	public List<Building> getBuildingsByVisibility(boolean visible) {
		Logger.finer("visible, " + getName() + ": " + visible);
		List<Building> buildingsList = new ArrayList<Building>();
		for (Building aBuilding : buildings) {
			Logger.finer("visible, aBuilding, " + getName() + ": " + aBuilding.getName());
			Logger.finer("visible, aBuilding.getBuildingType().isVisibleOnMap(), " + getName() + ": " + aBuilding.getBuildingType().isVisibleOnMap());
			if(visible == aBuilding.getBuildingType().isVisibleOnMap()){
				Logger.finer("visible, aBuilding, " + getName() + ", ADDING: " + aBuilding.getName());
				buildingsList.add(aBuilding);
			}
		}
		return buildingsList;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}
	
	public void addBuilding(Building building) {
		this.buildings.add(building);
	}
	
	public void removeBuilding(int buildingId) {
		
		Logger.fine("(Planet.java) aBuilding.getUniqueId() " +  buildingId);
		
		int removeIndex = -1;
		for(int i=0; i < buildings.size();i++){
			Logger.fine("(Planet.java) buildings.get(i).getUniqueId() " + buildings.get(i).getUniqueId());
			if(buildings.get(i).getUniqueId() == buildingId){
				removeIndex = i;
			}
		}
		if(removeIndex > -1){
			Logger.fine("(Planet.java) this.buildings.remove(removeIndex) removeIndex  " + removeIndex);
			this.buildings.remove(removeIndex);
		}
	}
	//  method for buildings 
	
	public int getBuildingTechBonus(){
		int bonus=0;
		for(int i=0; i< buildings.size();i++){
			if(buildings.get(i).getBuildingType().getTechBonus() > bonus){
				if(!buildings.get(i).getBuildingType().isInOrbit() || !isBesieged()){
					bonus = buildings.get(i).getBuildingType().getTechBonus();
				}
				
			}
		}
		return bonus;
	}
	
	public boolean hasSpacePort(){
		for(int i=0; i< buildings.size();i++){
			if(buildings.get(i).getBuildingType().isSpaceport()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param conqueringPlayer if no conquering player (null) do not create any messages
	 */
	public void destroyBuildingsThatCanNotBeOverTaked(Player conqueringPlayer){
		int i=0;
		while(i< buildings.size()){
			if(buildings.get(i).getBuildingType().isAutoDestructWhenConquered()){
				if (conqueringPlayer != null){
					conqueringPlayer.addToGeneral("The " + buildings.get(i).getBuildingType().getName() + " on the planet " + getName() + " has been destroyed.");
					playerInControl.addToGeneral("Your " + buildings.get(i).getBuildingType().getName() + " on the planet " + getName() + " has been destroyed.");
				}
				buildings.remove(i);
			}else{
				i++;
			}
		}
	}
	
	public int getMaxWharfsSize(){
		int maxSize = 0;
		for(Building building : buildings){
			if(building.getBuildingType().getWharfSize() > maxSize){
				maxSize = building.getBuildingType().getWharfSize();
			}
		}
		return maxSize;
		
	}
	
	public boolean isPlanetOwner(Player aPlayer){
		if(playerInControl != null && playerInControl == aPlayer){
			return true;
		}
		return false;
	}
	
	public boolean isPlayerBuildingAtPlanet(Player aPlayer){
		boolean BuildingAtPlanet = false;
	    if(isPlanetOwner(aPlayer) && buildings.size() > 0){
	    	BuildingAtPlanet = true;
	    }
	      
	    return BuildingAtPlanet;
	}
	
	public Building getBuilding(int unigueId){
		Building tempBuilding = null;
		for(int i=0; i < buildings.size();i++){
			if(buildings.get(i).getUniqueId() == unigueId){
				tempBuilding = buildings.get(i);
			}
		}
		return tempBuilding;
	}
	
	
	public boolean hasBuilding(String buildingTypeName){
		for(int i = 0; i < buildings.size();i++){
			if(buildings.get(i).getBuildingType().getName().equalsIgnoreCase(buildingTypeName)){
				return true;
			}
		}
		return false;
	}


}
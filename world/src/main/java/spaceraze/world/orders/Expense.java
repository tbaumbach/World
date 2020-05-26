package spaceraze.world.orders;

import java.io.Serializable;

import spaceraze.util.general.Logger;
import spaceraze.world.*;

// representerar ett utlägg gjort av spelaren under sitt drag
public class Expense implements Serializable {
	static final long serialVersionUID = 1L;
  	private String type;
  	//Planet planet;
  	private String planetName;
 	// OrbitalWharf ow;
  	//SpaceshipType sst;
 	private String spaceshipTypeName;
  	//BuildingType buildingType;
  	private String buildingTypeName;
	private int currentBuildingId = 0;//byggnaden som bygger enheten.
  	//TroopType troopType;
  	private String troopTypeName;
  	//VIPType vipType;
  	private String typeVIPName;
  	//Galaxy g;
  	//Player player;
  	private String playerName=""; // Du eller player som mot tar en gåva.
	private BlackMarketBid blackMarketBid;
	private ResearchOrder researchOrder;
	private int sum;
  
  	public Expense(){}

  // Research
  public Expense(String temptype, ResearchOrder ro, Player aPlayer){
	  this.type = temptype;
	  researchOrder = ro;
	  this.playerName = aPlayer.getName();
	}
 

  // bjuda på ett erbjudande på svarta marknaden, type = "blackmarketbid"
  // Fungear inte med Rest API(JSON). Då det siter ihop för mycket med serven.
  public Expense(String temptype, BlackMarketBid blackMarketBid, Player aPlayer){
  		this.playerName = aPlayer.getName();
    	this.blackMarketBid = blackMarketBid;
	  	blackMarketBid.addPlayer(aPlayer);
    	this.type = temptype;
  }

  // bygga nytt skepp, type = "buildship"
  public Expense(String temptype, Building tempBuilding, SpaceshipType tempsst, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    //sst = tempsst;
    spaceshipTypeName = tempsst.getName();
    this.type = temptype;
    currentBuildingId = tempBuilding.getUniqueId();
    playerName =aPlayername;
  }
  
//bygga nytt troop, type = "buildtroop"
  public Expense(String temptype, Building tempBuilding, TroopType tempTroopType, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    //this.troopType = tempTroopType;
    this.troopTypeName = tempTroopType.getUniqueName();
    this.type = temptype;
    currentBuildingId = tempBuilding.getUniqueId();
    playerName =aPlayername;
  }
  
//bygga ny VIP, type = "buildVIP"
  public Expense(String temptype, Building tempBuilding, VIPType tempVIPTYPE, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    this.typeVIPName = tempVIPTYPE.getName();
    this.type = temptype;
    currentBuildingId = tempBuilding.getUniqueId();
    playerName =aPlayername;
  }
  
//bygga ny byggnad, type = "building"
  public Expense(String temptype, BuildingType tempbuildingType, String aPlayername,Planet planet, Building tempBuilding){
	  //this.buildingType = tempbuildingType;
	  this.buildingTypeName = tempbuildingType.getName();
	  this.type = temptype;
	  this.planetName = planet.getName();
	  if(tempBuilding!= null){
		  currentBuildingId = tempBuilding.getUniqueId();
	  }
	  playerName =aPlayername;
  }

  // ge pengar till en annan spelare, type = "transaction"
  public Expense(String temptype, Player aPlayer, int aSum){
    this.type = temptype;
    this.playerName = aPlayer.getName();
    this.sum = aSum;
  }

  // reconstruct a razed planet, type = "reconstruct"
  //type = "pop" eller "res"
  public Expense(String temptype, Planet aPlanet, Player aPlayer){
    this.type = temptype;
    this.playerName = aPlayer.getName();
    this.planetName = aPlanet.getName();
  }

  public String getType(){
	  return type;
  }

  public String getText(Galaxy aGalaxy, int cost){
    String returnString = "";
    if (type.equalsIgnoreCase("pop")){
      returnString = "Increase production on " + planetName + " with +1.";
    }else
    if (type.equalsIgnoreCase("res")){
      returnString = "Increase resistance on " + planetName + " with +1.";
    }else
    if (type.equalsIgnoreCase("building")){
    	BuildingType buildingType = aGalaxy.getGameWorld().getBuildingTypeByName(buildingTypeName);
    	if(buildingType.getParentBuildingName() == null){
    		returnString = "Build new " + buildingType.getName() + " at " + planetName + ".";
    	}else{
    		returnString = "Upgrade " + buildingType.getParentBuildingName() + " to " + buildingType.getName() + " at " + planetName + ".";
    	}
    }else
    if (type.equalsIgnoreCase("buildship")){
    	SpaceshipType sst = aGalaxy.findSpaceshipType(spaceshipTypeName);
    	returnString = "Build new " + sst.getName() + " at " + planetName + ".";
    }else
    if (type.equalsIgnoreCase("buildtroop")){
    	TroopType troopType = aGalaxy.findTroopType(troopTypeName);
        returnString = "Build new " + troopType.getUniqueName() + " at " + planetName + ".";
    }else
    if (type.equalsIgnoreCase("buildVIP")){
        returnString = "Build new " + typeVIPName + " at " + planetName + ".";
    }else
    if (type.equalsIgnoreCase("transaction")){
      returnString = "Transfer " + sum + " money to Govenor " + aGalaxy.getPlayer(playerName).getGovernorName();
    }else
    if(type.equalsIgnoreCase("blackmarketbid")){
      returnString = blackMarketBid.getText();
    }else
    if(type.equalsIgnoreCase("reconstruct")){
      returnString = "Reconstruct the planet " + planetName;
    }else
    if(type.equalsIgnoreCase("research")){
    	returnString = "Research on " + researchOrder.getAdvantageName();
    }
    returnString += " (cost: " + cost + ")";
    return returnString;
  }

  public String getPlanetName(){
    return planetName;
  }
/*
  public int getOrbitalWharfId(){
    return ow.getId();
  }
*/
  public String getSpaceshipTypeName(){
    return spaceshipTypeName;
  }
  
  
  public String getVIPType(){
	  return this.typeVIPName;
  }

  public BlackMarketBid getBlackMarketBid(){
    return blackMarketBid;
  }
  
  public boolean isBuildBuildingAt(Planet aPlanet, BuildingType aBuildingType){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) & (aPlanet.getName().equalsIgnoreCase(planetName)) & (buildingTypeName.equalsIgnoreCase(aBuildingType.getName()))){
	      returnValue = true;
	    }
	    return returnValue;
	}
  
  public boolean isBuildBuildingAt(Planet aPlanet){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) & (aPlanet.getName().equalsIgnoreCase(planetName)) & (currentBuildingId == 0)){
	      returnValue = true;
	    }
	    return returnValue;
	}
  
  public boolean isUpgradeBuilding(Building aBuilding){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) && (currentBuildingId > 0) && (currentBuildingId == aBuilding.getUniqueId()) && (buildingTypeName != null)){
	      returnValue = true;
	    }
	    return returnValue;
	}
/*
  public boolean isBuildWharfAt(Planet aPlanet){
    boolean returnValue = false;
    if ((type.equalsIgnoreCase("newwharf")) & (aPlanet == planet)){
      returnValue = true;
    }
    return returnValue;
  }

  public boolean isBuildSpaceStationAt(Planet aPlanet){
	  boolean returnValue = false;
	  if ((type.equalsIgnoreCase("newspacestation")) & (aPlanet == planet)){
		  returnValue = true;
	  }
	  return returnValue;
  }
*/
  public boolean isIncPopAt(Planet aPlanet){
    boolean returnValue = false;
    if ((type.equalsIgnoreCase("pop")) & (aPlanet.getName().equalsIgnoreCase(planetName))){
      returnValue = true;
    }
    return returnValue;
  }

  public boolean isIncResAt(Planet aPlanet){
    boolean returnValue = false;
    if ((type.equalsIgnoreCase("res")) & (aPlanet.getName().equalsIgnoreCase(planetName))){
      returnValue = true;
    }
    return returnValue;
  }

  public boolean isBuildingBuildingShip(Building aBuilding){
	  Logger.finer("aBuilding.getUniqueName(): " + aBuilding.getUniqueName());
	  Logger.finer("type currentBuildingId aBuilding.getUniqueId()" + type + " " + currentBuildingId + " " + aBuilding.getUniqueId());
	    boolean isBilding = false;
	    if (type.equalsIgnoreCase("buildship")){
	      if (currentBuildingId == aBuilding.getUniqueId()){
	    	  isBilding = true;
	      }
	    }
	    return isBilding;
	  }
  
  public boolean isBuildingBuildingTroop(Building aBuilding){
	    boolean isBilding = false;
	    if (type.equalsIgnoreCase("buildtroop")){
	      if (currentBuildingId == aBuilding.getUniqueId()){
	    	  isBilding = true;
	      }
	    }
	    return isBilding;
	  }
  
  public boolean isBuildingBuildingVIP(Building aBuilding){
	    boolean isBilding = false;
	    if (type.equalsIgnoreCase("buildVIP")){
	      if (currentBuildingId == aBuilding.getUniqueId()){
	    	  isBilding = true;
	      }
	    }
	    return isBilding;
	  }
  
  /*public boolean isWharfBuilding(OrbitalWharf aWharf){
    boolean isWharf = false;
    if (type.equalsIgnoreCase("buildship")){
      if (ow == aWharf){
        isWharf = true;
      }
    }
    return isWharf;
  }*/

  public boolean isReconstructAt(Planet aPlanet){
	  boolean returnValue = false;
	  if ((type.equalsIgnoreCase("reconstruct")) & (aPlanet.getName().equalsIgnoreCase(planetName))){
		  returnValue = true;
	  }
	  return returnValue;
  }

  public boolean isBlackMarketBid(){
    return type.equalsIgnoreCase("blackmarketbid");
  }

  public boolean isTransaction(){
    return type.equalsIgnoreCase("transaction");
  }

  public boolean isBuilding(Building aBuilding){
	  Logger.finer("############");
	  Logger.finer("aBuilding.getUniqueName(): " + aBuilding.getUniqueName());
	  Logger.finer("type.equalsIgnoreCase(building): " + type.equalsIgnoreCase("building"));
	  Logger.finer("currentBuildingId > 0: " + (currentBuildingId > 0));
	  Logger.finer("aBuilding.getUniqueId() == currentBuildingId: " + (aBuilding.getUniqueId() == currentBuildingId));
	  Logger.finer("type currentBuildingId aBuilding.getUniqueId()" + type + " " + currentBuildingId + " " + aBuilding.getUniqueId());
	  return ((type.equalsIgnoreCase("building")) && currentBuildingId > 0 && (aBuilding.getUniqueId() == currentBuildingId));
  }
  
  public boolean isBuilding(Planet aPlanet){
	  return ((type.equalsIgnoreCase("building")) && aPlanet.getName().equalsIgnoreCase(planetName) && currentBuildingId == 0);
  }
  
  public boolean isResearchOrder(String researchName){
	  if(type.equalsIgnoreCase("research")){
	    return (researchOrder.getAdvantageName().equalsIgnoreCase(researchName));
	  }
  return false;
  }

  public String getPlayerName(){
    return playerName;
  }

  public int getSum(){
    return sum;
  }

  public String getBuildingTypeName() {
	  return buildingTypeName;
  }

  public void setBuildingType(BuildingType buildingType) {
	  this.buildingTypeName = buildingType.getName();
  }

public String getTroopTypeName() {
	return troopTypeName;
}

public String getVipTypeName() {
	return typeVIPName;
}

public int getCurrentBuildingId(){
	return currentBuildingId;
}

public ResearchOrder getResearchOrder(){
  	return researchOrder;
}

}

package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.*;

import javax.persistence.*;

// representerar ett utlägg gjort av spelaren under sitt drag
@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "EXPENCE")
public class Expense implements Serializable {
	static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

  	private String type;
  	private String planetName;
 	private String spaceshipTypeUuid;
  	private String buildingTypeUuid;
	private String buildingUuid;
  	private String troopTypeUuid;
    private String typeVIPUuid;
  	private String playerName=""; // Du eller player som mot tar en gåva.

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_BLACK_MARKET_BID")
	private BlackMarketBid blackMarketBid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_RESEARCH_ORDERS")
	private ResearchOrder researchOrder;
	private int sum;

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
    spaceshipTypeUuid = tempsst.getUuid();
    this.type = temptype;
    this.buildingUuid = tempBuilding.getUuid();
    playerName =aPlayername;
  }
  
//bygga nytt troop, type = "buildtroop"
  public Expense(String temptype, Building tempBuilding, TroopType tempTroopType, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    //this.troopType = tempTroopType;
    this.troopTypeUuid = tempTroopType.getUuid();
    this.type = temptype;
      this.buildingUuid = tempBuilding.getUuid();
    playerName =aPlayername;
  }
  
//bygga ny VIP, type = "buildVIP"
  public Expense(String temptype, Building tempBuilding, VIPType tempVIPTYPE, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    this.typeVIPUuid = tempVIPTYPE.getUuid();
    this.type = temptype;
    this.buildingUuid = tempBuilding.getUuid();
    playerName =aPlayername;
  }
  
//bygga ny byggnad, type = "building"
  public Expense(String temptype, BuildingType tempbuildingType, String aPlayername,Planet planet, Building tempBuilding){
	  //this.buildingType = tempbuildingType;
	  this.buildingTypeUuid = tempbuildingType.getUuid();
	  this.type = temptype;
	  this.planetName = planet.getName();
	  if(tempBuilding!= null){
          this.buildingUuid = tempBuilding.getUuid();
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

  public String getPlanetName(){
    return planetName;
  }
/*
  public int getOrbitalWharfId(){
    return ow.getId();
  }
*/
  public String getSpaceshipTypeUuid(){
    return spaceshipTypeUuid;
  }

  public BlackMarketBid getBlackMarketBid(){
    return blackMarketBid;
  }
  
  public boolean isBuildBuildingAt(Planet aPlanet, BuildingType aBuildingType){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) & (aPlanet.getName().equalsIgnoreCase(planetName)) & (buildingTypeUuid.equalsIgnoreCase(aBuildingType.getName()))){
	      returnValue = true;
	    }
	    return returnValue;
	}
  
  public boolean isBuildBuildingAt(Planet aPlanet){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) & (aPlanet.getName().equalsIgnoreCase(planetName)) & (buildingUuid == null)){
	      returnValue = true;
	    }
	    return returnValue;
	}

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
	  return ((type.equalsIgnoreCase("building")) && buildingUuid != null && (aBuilding.getUuid().equalsIgnoreCase(buildingUuid)));
  }
  
  public boolean isBuilding(Planet aPlanet){
	  return ((type.equalsIgnoreCase("building")) && aPlanet.getName().equalsIgnoreCase(planetName) && buildingUuid == null);
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

  public String getBuildingTypeUuid() {
	  return buildingTypeUuid;
  }

  public void setBuildingType(BuildingType buildingType) {
	  this.buildingTypeUuid = buildingType.getName();
  }

public String getTroopTypeUuid() {
	return troopTypeUuid;
}

public ResearchOrder getResearchOrder(){
  	return researchOrder;
}

}

package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;
import spaceraze.util.general.Logger;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.mapinfo.MapInfos;
import spaceraze.world.orders.Expense;
import spaceraze.world.orders.Orders;
import spaceraze.world.report.PlayerReport;
import spaceraze.world.spacebattle.ReportLevel;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "PLAYER")
public class Player implements Serializable{
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_GALAXY")
    private Galaxy galaxy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<PlayerSpaceshipImprovement> spaceshipImprovements = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<PlayerTroopImprovement> troopImprovements = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<PlayerBuildingImprovement> buildingImprovements = new ArrayList<>();
    private String name;
    private String password;
    private String governorName;
    private String uuid;//TODO use this instead of "name"
    //TODO Koppla ihop den här när en användare(User) joinar ett spel, använd inte namn som nyckel (använderen ska ju vara inloggad).
    private String userUuid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;
    //TODO 2019-12-20 TurnInfo ersätts av PlayerReport
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_TURN_INFO")
    private TurnInfo turnInfo;
    private String errormessage = null, notes = "";
    private boolean defeated = false;
    private boolean win = false;
	private boolean updatedThisTurn = false; // saved this turn
    private boolean finishedThisTurn = false; // ok for server to update
    private int treasury;
    private int totalPop;

    private String factionUuid;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<PlanetInformation> planetInformations = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLANET", insertable = false, updatable = false)
    private Planet homePlanet;
//    public boolean writeUpkeep = false;
    private boolean retreatingGovernor = false;
    private int turnDefeated;
    private int nrTurnsBroke;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<ResearchProgress> researchProgresses = new ArrayList<>();

    private int openPlanetBonus = 0;
    private int closedPlanetBonus = 0;
    private int resistanceBonus = 0;
    private int techBonus = 0; // %
    private boolean canReconstruct = false;
    private int reconstructCostBase = 8;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CORRUPTION_POINT")
    CorruptionPoint corruptionPoint;

    private int latestMessageIdFromServer = 0;
    private int messageId;
    private ReportLevel reportLevel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<PlanetOrderStatus> planetOrderStatuses = new ArrayList<>();
    private MapInfos mapPlanetInfos;

    //TODO 2020-11-19 Can we romve this? we can use the methodes instead.
    private int income = -1;
    private int upkeepTroops = -1;
    private int upkeepVIPs = -1;
    private int upkeepShips = -1;
    
    //TODO 2019-12-20 lade till nya sättet att rapportera ett drag, med utbruten kopplingar till spel objekten som Spaceship, troop, VIP mm.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<PlayerReport>  playerReports = new ArrayList<>();
    
    public Player(String errormessage){
        this.errormessage = errormessage;
        spaceshipImprovements = new ArrayList<>();
        troopImprovements = new ArrayList<>();
        buildingImprovements = new ArrayList<>();
        planetInformations = new ArrayList<>();
        researchProgresses = new ArrayList<>();
        planetOrderStatuses = new ArrayList<>();
        playerReports = new ArrayList<>();
    }

    public Player(String errormessage, Galaxy ag){
    	this(errormessage);
    	galaxy = ag;
    }

    public Player(String name, String password, Galaxy g, String governorName, Faction faction, Planet homePlanet, List<PlanetOrderStatus> planetOrderStatuses){
        this(null);
        this.uuid = UUID.randomUUID().toString();
        Logger.fine("Name: " + name + " govenorName: " + governorName +  " factionName: " + faction.getName());
    	this.name = name;
        this.password = password;
        this.governorName = governorName.replace('#',' ');
        orders = new Orders();
        this.galaxy = g;
        turnInfo = new TurnInfo();
        turnInfo.newTurn();
        treasury = 0;
        this.factionUuid = faction.getUuid();
        this.homePlanet = homePlanet;

        //      Copy from Faction
        openPlanetBonus = faction.getOpenPlanetBonus();
        closedPlanetBonus = faction.getClosedPlanetBonus();
        resistanceBonus = faction.getResistanceBonus();
        techBonus = faction.getTechBonus(); // %
        canReconstruct = faction.isCanReconstruct();
        reconstructCostBase = faction.getReconstructCostBase();
        // set values from faction
        corruptionPoint = faction.getCorruptionPoint();
        reportLevel = ReportLevel.LONG;
        this.planetOrderStatuses = planetOrderStatuses;
        this.mapPlanetInfos = new MapInfos();
    }
    
    public ReportLevel getReportLevel(){
    	return reportLevel;
    }

    public void setReportLevel(ReportLevel newReportLevel){
    	reportLevel = newReportLevel;
    }

    public TurnInfo getTurnInfo(){
      return turnInfo;
    }

    public void setTotalPop(int newPop){
      totalPop = newPop;
    }

    public int getTotalPop(){
      return totalPop;
    }

    public void setDefeated(boolean newValue){
      defeated = newValue;
    }

    public void defeated(boolean govenorDead,int turnDefeated){
      defeated = true;
      this.turnDefeated = turnDefeated;
      if (govenorDead){
        turnInfo.addToLatestGeneralReport("Your Govenor has been killed.\n");
//        addToHighlights("",Highlight.TYPE_GOVENOR_KILLED); // Not needed, highlight already created in other places (vip killed in ship or planet)
        galaxy.govenorKilled(this);
      }else{
        turnInfo.addToLatestGeneralReport("You have no planets or spaceships left.\n");
        addToHighlights("",HighlightType.TYPE_NO_SHIPS_NO_PLANETS);
        galaxy.noPlanetsOrShips(this);
      }
      turnInfo.addToLatestGeneralReport("You have been defeated.\n");
      addToHighlights("",HighlightType.TYPE_DEFEATED);
      galaxy.playerDefeated(this);
    }

    public void abandonGame(int turnDefeated){
        defeated = true;
        this.turnDefeated = turnDefeated;
        turnInfo.addToLatestGeneralReport("You have abandoned this game and your Govenor have left this quadrant.\n");
        turnInfo.addToLatestGeneralReport("Game is over.\n");
        addToHighlights("",HighlightType.TYPE_GAME_ABANDONED);
        galaxy.playerAbandonsGame(this);
    }

    public void brokeRemovedFromGame(int turnRemoved){
        defeated = true;
        this.turnDefeated = turnRemoved;
        turnInfo.addToLatestGeneralReport("You have been broke for 5 turns in a row and government have broken down.\n");
        turnInfo.addToLatestGeneralReport("Game is over.\n");
        addToHighlights("",HighlightType.TYPE_GAME_BROKE_REMOVED);
        galaxy.playerRemovedBrokeGame(this);
    }

    public void brokeRemovedWarning(){
        turnInfo.addToLatestGeneralReport("Warning: you have been broke for 4 turns in a row and will lose the game if you are broke one more turn.\n");
        addToHighlights("",HighlightType.TYPE_GAME_BROKE_WARNING);
    }

    public boolean isDefeated(){
      return defeated;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public List<PlayerSpaceshipImprovement> getSpaceshipImprovements(){
        return spaceshipImprovements;
    }

    public boolean isPlayer(String name, String password){
      return name.equalsIgnoreCase(this.name) & password.equalsIgnoreCase(this.password);
    }

    // enklare variant av isPlayer som ej anv�nds vid inloggning
    public boolean isPlayer(String name){
      return name.equalsIgnoreCase(this.name);
    }
    
    public boolean isPlayer(Player anotherPlayer){
    	return name.equalsIgnoreCase(anotherPlayer.getName());
    }

    public boolean isPlayerByGovernorName(String aGovernorName){
      return this.governorName.equalsIgnoreCase(aGovernorName);
    }

    public String getErrorMessage(){
        return errormessage;
    }

    public void addSpaceshipImprovement(PlayerSpaceshipImprovement ship){
        spaceshipImprovements.add(ship);
    }

    public void setOrders(Orders newOrders){
      orders = newOrders;
    }

    public Orders getOrders(){
      return orders;
    }

    public Galaxy getGalaxy(){
      return galaxy;
    }

    public void addToGeneral(String str){
      turnInfo.addToLatestGeneralReport(str);
    }

    public void addToGeneralAt(String str, int index){
    	turnInfo.addToLatestGeneralReportAt(str,index);
    }

    public void addToHighlights(String str, HighlightType type){
        turnInfo.addToLatestHighlights(str,type);
      }

    public void addToLatestBlackMarketMessages(String aMessage){
      turnInfo.addToLatestBlackMarketReport(aMessage);
    }

    public void addToVIPReport(String str){
      turnInfo.addToLatestVIPReport(str);
    }

    public void updateTurnInfo(){
      turnInfo.newTurn();
    }

    public void addToTreasury(int money){
      treasury = treasury + money;
    }

    public void setTreasury(int money){
      treasury = money;
    }

    public void removeFromTreasury(int money){
      treasury = treasury - money;
      if (treasury < 0){
        treasury = 0;
      }
    }

    public int getTreasury(){
      return treasury;
    }

    public void addOrRemovePlanetVisib(Planet p){
      orders.addOrRemovePlanetVisib(p);
    }

    public void addOrRemoveAbandonPlanet(Planet p){
      orders.addOrRemoveAbandonPlanet(p);
    }

    public void addNewBuilding(Planet aPlanet, BuildingType bt){
      orders.addNewBuilding(aPlanet, getName(), bt, galaxy);
    }
    public void removeNewBuilding(Planet aPlanet){
        orders.removeNewBuilding(aPlanet, this.getGalaxy());
    }
    
    public void addReconstruct(Planet aPlanet){
    	orders.addReconstruct(aPlanet,this);
    }

    public void removeReconstruct(Planet aPlanet){
    	orders.removeReconstruct(aPlanet, this.getGalaxy());
    }

    public void addIncPop(Planet aPlanet){
      orders.addIncPop(aPlanet,this);
    }

    public void removeIncPop(Planet aPlanet){
      orders.removeIncPop(aPlanet, this.getGalaxy());
    }

    public void addIncRes(Planet aPlanet){
      orders.addIncRes(aPlanet,this);
    }

    public void removeIncRes(Planet aPlanet){
      orders.removeIncRes(aPlanet, this.getGalaxy());
    }

    public void addBuildShip(Building building, SpaceshipType sst){
      orders.addBuildShip(building, sst, this);
    }

    public void setAbandonGame(boolean abandonGame){
        orders.setAbandonGame(abandonGame);
    }

    public String getGovernorName(){
      return governorName;
    }

    public void addShipSelfDestruct(Spaceship currentss){
      orders.addShipSelfDestruct(currentss);
    }

    public void removeShipSelfDestruct(Spaceship currentss){
      orders.removeShipSelfDestruct(currentss);
    }

    public void addTroopSelfDestruct(Troop aTroop){
    	orders.addTroopSelfDestruct(aTroop);
    }

    public void removeTroopSelfDestruct(Troop aTroop){
    	orders.removeTroopSelfDestruct(aTroop);
    }

    public void addBuildingSelfDestruct(Building building){
      orders.addBuildingSelfDestruct(building);
    }

    public void removeBuildingSelfDestruct(Building building){
      orders.removeBuildingSelfDestruct(building);
    }

    public boolean getShipSelfDestruct(Spaceship ss){
      return orders.getShipSelfDestruct(ss);
    }

    public boolean getBuildingSelfDestruct(Building building){
      return orders.getBuildingSelfDestruct(building);
    }

    public Planet getHomePlanet(){
      return homePlanet;
    }


  public BlackMarketBid getBidToOffer(BlackMarketOffer tempOffer){
    BlackMarketBid tempBid = null;
    Expense anExpense = orders.getBidToOffer(tempOffer);
    if (anExpense != null){
      tempBid = anExpense.getBlackMarketBid();
    }
    return tempBid;
  }

  public void setUpdatedThisTurn(boolean newValue){
    updatedThisTurn = newValue;
  }

  public boolean getUpdatedThisTurn(){
    return updatedThisTurn;
  }

  public String getNotes(){
    return notes;
  }

  public void setNotes(String newNotes){
    notes = newNotes;
  }
  
  public void setPassword(String newPassword){
  	password = newPassword;
  }
  
  public String getTurnInfoText(int turn, ReportLevel reportLevel){
  	  StringBuffer text = new StringBuffer();
	  Report generalReport = getTurnInfo().getGeneralReport(turn);
 	  for (int i = 0; i < generalReport.size(); i++){
        text.append(generalReport.getInfoAt(i) + "\n");
      }
      List<String> infoStrings = generalReport.getAllReports();
      if ((infoStrings.get(infoStrings.size()-1)).equalsIgnoreCase("---------------")){
//      int generalReportIndex = generalReport.findInfoIndex("General Report");
  //    if ((generalReportIndex + 2) == generalReport.size()){
      	text.append("You had no general reports this turn.\n");
      	text.append("\n");
      }
      // Expences reports
//      if ((turn > 1) & (!getGalaxy().gameEnded)){
      if (turn > 1){
      	text.append("\n\nEconomy Report\n");
      	text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    EconomyReport er = getTurnInfo().getEconomyReport(turn);
      	text.append(" Saved from turn " + (turn-1) + ": " + er.getSavedLastTurn() + "\n");
      	text.append(" Income this turn: " + er.getIncomeLastTurn() + "\n");
      	text.append(" Income corruption this turn: " + er.getCorruptionIncomeLastTurn() + "\n");
      	text.append(" Upkeep for spaceships this turn: " + er.getSupportShipsLastTurn() + "\n");
      	text.append(" Upkeep corruption for spaceships this turn: " + er.getCorruptionUpkeepShipsLastTurn() + "\n");
      	text.append(" Upkeep for troops this turn: " + er.getSupportTroopsLastTurn() + "\n");
      	// TODO (Tobbe) Fixa så att kostnader för VIPar lägg till i turn Economy report.
      	text.append("Expences this turn: " + er.getExpensesLastTurn() + "\n");
        text.append("  (see expenses details below under \"Expenses Reports\")\n");
        text.append("\n");
      	text.append(" Saved to next turn: " + er.getSavedNextTurn() + "\n");
      	text.append(" Income next turn: " + er.getIncomeNextTurn() + "\n");
      	text.append(" Income corruption next turn: " + er.getCorruptionIncomeNextTurn() + "\n");
      	text.append(" Upkeep for spaceships next turn: " + er.getSupportShipsNextTurn() + "\n");
      	text.append(" Upkeep corruption for spaceships next turn: " + er.getCorruptionUpkeepShipsNextTurn() + "\n");
      	text.append(" Upkeep for troops next turn: " + er.getSupportTroopsNextTurn() + "\n");
//      TODO (Tobbe) Fixa så att kostnader för VIPar lägg till i turn Economy report.
      	if (er.getBrokeNextTurn()){
      		text.append("WARNING: Upkeep exceeds income. You are broke!\n");
      		text.append("Until upkeep gets below income you cannot move any ship or VIP, or have any expenses.\n");      		
      	}
        text.append("\n");

      	text.append("\nExpences Reports\n");
      	text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report expenceReport = getTurnInfo().getExpenseReport(turn);
        if (expenceReport.size() == 0){
        	text.append("You had no expences this turn.\n");
        }else{
  	      for (int j = 0; j < expenceReport.size(); j++){
  	      	text.append(expenceReport.getInfoAt(j) + "\n");
	      }
        }
        text.append("\n");

      	text.append("\nCivilian ships Reports\n");
      	text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report civilianReport = getTurnInfo().getCivilianReport(turn);
        if (civilianReport.size() == 0){
        	text.append("You had no civilian reports this turn.\n");
        }else{
  	      for (int j = 0; j < civilianReport.size(); j++){
  	      	text.append(civilianReport.getInfoAt(j) + "\n");
	      }
        }
        text.append("\n");

        // VIP reports
        text.append("\nVIP Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report VIPReport = getTurnInfo().getVIPReport(turn);
        if (VIPReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int j = 0; j < VIPReport.size(); j++){
  	      	text.append(VIPReport.getInfoAt(j) + "\n");
	      }
        }
        text.append("\n");

        // Diplomacy reports
        text.append("\nDiplomacy Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report diplomacyReport = getTurnInfo().getDiplomacyReport(turn);
	    if (diplomacyReport.size() == 0){
	    	text.append("Nothing to report this turn.\n");
	    }else{
	    	for (int k = 0; k < diplomacyReport.size(); k++){
	    		text.append(diplomacyReport.getInfoAt(k) + "\n");
	    	}
	    }
        text.append("\n");

	    // Black Market reports
        text.append("\nBlack Market Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report blackMarketReport = getTurnInfo().getBlackMarketReport(turn);
        if (blackMarketReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int k = 0; k < blackMarketReport.size(); k++){
  	      	text.append(blackMarketReport.getInfoAt(k) + "\n");
	      }
        }
        text.append("\n");
        
        //      Research reports
        text.append("\nResearch Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report researchReport = getTurnInfo().getResearchReport(turn);
        if (researchReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int k = 0; k < researchReport.size(); k++){
  	      	text.append(researchReport.getInfoAt(k) + "\n");
	      }
        }
        text.append("\n");
        
        // buildings reports
        text.append("\nBuilding Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report buildingsReport = getTurnInfo().getBuildingsReport(turn);
        if (buildingsReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int k = 0; k < buildingsReport.size(); k++){
  	      	text.append(buildingsReport.getInfoAt(k) + "\n");
	      }
        }
        text.append("\n");

        //TODO 2019-12-31 This is the new report logic, should replace all logic from TurnInfo (the old report logic).
        text.append(getPlayerReports().get(turn -2 < 0 ? 0 : turn -2).getFullReport());

        // Land battle reports
	    /*
        LandBattleReports landBattleReports = getTurnInfo().getLandBattleReports(turn);
	    if (landBattleReports.battleExist()){
	        text.append("\nLand battle Reports\n");
	        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    	text.append(landBattleReports.getAsString(reportLevel) + "\n");
	    }*/

      }
      return text.toString();
  }

	public boolean isRetreatingGovernor() {
		return retreatingGovernor;
	}
	
	public void setRetreatingGovernor(boolean retreatingGovernor) {
		this.retreatingGovernor = retreatingGovernor;
	}
	
	@Override
	public String toString(){
		String tmpStr = "";
		tmpStr += "Player: " + name + " (";
		tmpStr += governorName + ") - ";
		return tmpStr;
	}

	public int getTurnDefeated(){
		return turnDefeated;
	}

	public boolean isFinishedThisTurn() {
		return finishedThisTurn;
	}

	public void setFinishedThisTurn(boolean finishedThisTurn) {
		this.finishedThisTurn = finishedThisTurn;
	}

	public int getNrTurnsBroke() {
		return nrTurnsBroke;
	}

	public void setNrTurnsBroke(int nrTurnsBroke) {
		this.nrTurnsBroke = nrTurnsBroke;
	}

	public void incNrTurnsBroke() {
		nrTurnsBroke++;
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

	public int getReconstructCost(Planet aPlanet){
		return reconstructCostBase + aPlanet.getBasePopulation();
	}

	public void addBuildTroop(Building building, TroopType tt){
	      orders.addBuildTroop(building, tt, this);
	    }
	
	public void addBuildVIP(Building building, VIPType vipTypes){
	      orders.addBuildVIP(building, vipTypes, this);
	    }


	public int getLatestMessageIdFromServer() {
		return latestMessageIdFromServer;
	}

	public void setLatestMessageIdFromServer(int latestMessageIdFromServer) {
		this.latestMessageIdFromServer = latestMessageIdFromServer;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

    public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public List<PlanetOrderStatus> getPlanetOrderStatuses() {
		return planetOrderStatuses;
	}

	public MapInfos getMapInfos() {
		return mapPlanetInfos;		
	}

	public List<PlayerReport> getPlayerReports() {
		return playerReports;
	}

    public List<PlayerTroopImprovement> getTroopImprovements() {
        return troopImprovements;
    }

    public void addTroopImprovement(PlayerTroopImprovement troopImprovement){
	    troopImprovements.add(troopImprovement);
    }

    public List<PlayerBuildingImprovement> getBuildingImprovements() {
        return buildingImprovements;
    }

    public void addBuildingImprovement(PlayerBuildingImprovement buildingImprovement) {
        this.buildingImprovements.add(buildingImprovement);
    }

    public ResearchProgress getResearchProgress(String name){
	    return getResearchProgresses().stream().filter(researchProgress -> researchProgress.getResearchAdvantageName().equals(name)).findFirst().orElse(creatAndAddResearchProgress(name));
    }

    private ResearchProgress creatAndAddResearchProgress(String name) {
        ResearchProgress researchProgress = new ResearchProgress();
        researchProgress.setResearchAdvantageName(name);
        getResearchProgresses().add(researchProgress);
        return researchProgress;
    }
}

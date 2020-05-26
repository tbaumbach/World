//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world.orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import spaceraze.util.general.Logger;
import spaceraze.world.BlackMarketBid;
import spaceraze.world.BlackMarketOffer;
import spaceraze.world.Building;
import spaceraze.world.BuildingType;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import spaceraze.world.Spaceship;
import spaceraze.world.SpaceshipType;
import spaceraze.world.Troop;
import spaceraze.world.TroopType;
import spaceraze.world.VIP;
import spaceraze.world.VIPType;
import spaceraze.world.diplomacy.Diplomacy;
import spaceraze.world.diplomacy.DiplomacyChange;
import spaceraze.world.diplomacy.DiplomacyLevel;
import spaceraze.world.diplomacy.DiplomacyOffer;

public class Orders implements Serializable {
  static final long serialVersionUID = 1L;
  private List<Expense> expenses;
  private List<ShipMovement> shipMoves;
  private List<String> planetVisibilities;
  private List<String> abandonPlanets;
  private List<Integer> shipSelfDestructs;
  private List<Integer> screenedShips;
  private List<ResearchOrder> researchOrder;
  private List<VIPMovement> VIPMoves;
  private List<ShipToCarrierMovement> shipToCarrierMoves;
  private List<TroopToCarrierMovement> troopToCarrierMoves;
  private List<TroopToPlanetMovement> troopToPlanetMoves;
  private List<Integer> troopSelfDestructs;
  private boolean abandonGame;
  private List<Integer> buildingSelfDestructs;
  private List<DiplomacyOffer> diplomacyOffers;
  private List<DiplomacyChange> diplomacyChanges;
  private List<Integer> VIPSelfDestructs;
  private List<TaxChange> taxChanges;
  private List<PlanetNotesChange> planetNotesChanges;

  public Orders(){
    expenses = new ArrayList<Expense>();
    shipMoves = new ArrayList<ShipMovement>();
    shipToCarrierMoves = new ArrayList<ShipToCarrierMovement>();
    // innehåller de planeters namn vars synlighet skall togglas, i.e. closed->open och vice versa
    planetVisibilities = new ArrayList<String>();
    // innehåller de planeters namn som skall överges
    abandonPlanets = new ArrayList<String>();
    // innehåller de skepps namn som skall förstöras
    shipSelfDestructs = new ArrayList<Integer>();
    // innehåller de buildings som skall förstöras
    buildingSelfDestructs = new ArrayList<Integer>();
//  innehåller de VIPar som skall förstöras
    VIPSelfDestructs = new ArrayList<Integer>();
    // vektor över alla skepp som skall ändra/togglas screened-status
    screenedShips = new ArrayList<Integer>();
    // vektor över alla meddelanden som spelaren vill skicka
   // messages = new Vector<Message>();
    VIPMoves = new ArrayList<VIPMovement>();
    researchOrder = new ArrayList<ResearchOrder>();
    troopToCarrierMoves = new ArrayList<TroopToCarrierMovement>();
    troopToPlanetMoves = new ArrayList<TroopToPlanetMovement>();
    troopSelfDestructs = new ArrayList<Integer>();
    diplomacyChanges = new LinkedList<DiplomacyChange>();
    diplomacyOffers = new LinkedList<DiplomacyOffer>();
    taxChanges = new LinkedList<TaxChange>();
    planetNotesChanges = new LinkedList<PlanetNotesChange>();
  }

  public Orders(Orders oldOrders, Player oldPlayer, Galaxy newGalaxy){
    // fixa alla utgifter
	Logger.finest("New Orders");
    expenses = new Vector<Expense>();
    List<Expense> oldExpenses = oldOrders.getExpenses();
    for (int i = 0; i < oldExpenses.size(); i++){
      Expense oldExpense = (Expense)oldExpenses.get(i);
	  Logger.finest( "  oldExpense: " + oldExpense.getType() + (newGalaxy == null));
      expenses.add(oldExpense);
    }
    // fixa ship movements
    shipMoves = new ArrayList<ShipMovement>();
    shipMoves.addAll(oldOrders.getShipMoves());
    
    // fixa ship to carrier movements
    shipToCarrierMoves = new ArrayList<ShipToCarrierMovement>();
    shipToCarrierMoves.addAll(oldOrders.getShipToCarrierMoves());
   
    // fixa planet visibilities
    planetVisibilities = new ArrayList<String>();
    planetVisibilities.addAll(oldOrders.getPlanetVisibilities());
    
    // fixa abandon planets
    abandonPlanets = new ArrayList<String>();
    abandonPlanets.addAll(oldOrders.getAbandonPlanets());
    
    // fixa selfdestruct skepp
    shipSelfDestructs = new ArrayList<Integer>();
    shipSelfDestructs.addAll(oldOrders.getShipSelfDestructs());
    
    // fixa selfdestruct VIP
    VIPSelfDestructs = new ArrayList<Integer>();
    VIPSelfDestructs.addAll(oldOrders.getVIPSelfDestructs());
    
    
    // TODO (Tobbe) här måste vi kanske titta på building id  men vi testar med Name först och får se om vi måste markera just den building som skall tas bort
    // provade o kollade om objekten var samma.  troligtvis går det nog inte då det är cloner av varandra o inte samma obj.
    // fixa selfdestruct Building
    buildingSelfDestructs = new ArrayList<Integer>();
    buildingSelfDestructs.addAll(oldOrders.getBuildingSelfDestructs());
   
    // fixa screenade skepp
    screenedShips = new ArrayList<Integer>();
    screenedShips.addAll(oldOrders.getScreenedShips());
    
    // fixa meddelanden
    /*
    messages = new Vector<Message>();
    messages.addAll(oldOrders.getMessages());
    */
    // fixa VIP movements
    VIPMoves = new ArrayList<VIPMovement>();
    VIPMoves.addAll(oldOrders.getVIPMoves());
   
    //  fixa Research orders
    researchOrder = new ArrayList<ResearchOrder>();
    List<ResearchOrder> tempOldResearchOrder = oldOrders.getResearchOrders();
    
    for (int r = 0; r < tempOldResearchOrder.size(); r++){
    	ResearchOrder oldResearchOrder = (ResearchOrder)tempOldResearchOrder.get(r);
    	researchOrder.add(new ResearchOrder(oldResearchOrder.getAdvantageName(), oldResearchOrder.getCost()));
    	Logger.fine("(orders.java) tempOldResearchOrder.size() " + tempOldResearchOrder.size() + " oldResearchOrder.getAdvantageName() " + oldResearchOrder.getAdvantageName());
    }
    // troop to planet movements
    troopToPlanetMoves = new ArrayList<TroopToPlanetMovement>();
    troopToPlanetMoves.addAll(oldOrders.getTroopToPlanetMoves());
   
    // troop to carrier movements
    troopToCarrierMoves = new ArrayList<TroopToCarrierMovement>();
    troopToCarrierMoves.addAll(oldOrders.getTroopToCarrierMoves());
    
    // selfdestruct troops
    troopSelfDestructs = new ArrayList<Integer>();
    troopSelfDestructs.addAll(oldOrders.getTroopSelfDestructs());
    
    // diplomacy changes
    diplomacyChanges = new LinkedList<DiplomacyChange>();
    diplomacyChanges.addAll(oldOrders.getDiplomacyChanges());
    
    // diplomacy offers
    diplomacyOffers = new LinkedList<DiplomacyOffer>();
    diplomacyOffers.addAll(oldOrders.getDiplomacyOffers());
    
    // abandon game?
    abandonGame = oldOrders.isAbandonGame();
    
    taxChanges = new LinkedList<TaxChange>();
    taxChanges.addAll(oldOrders.getTaxChanges());

    planetNotesChanges = new LinkedList<PlanetNotesChange>();
    planetNotesChanges.addAll(oldOrders.getPlanetNotesChanges());

  }
  
  public Orders(Player p){
	  this();  
	  // adding research Orders that should continue.
	  for(int i=0; i < p.getOrders().researchOrder.size();i++){
		  if(!p.getResearch().getAdvantage(p.getOrders().researchOrder.get(i).getAdvantageName()).isDeveloped()){
			 // p.getOrders().getExpense(p.getOrders().researchOrder.get(i).getAdvantageName());
			  this.addResearchOrder(p.getOrders().researchOrder.get(i),p);
		  }
	  }
  }

  public boolean haveConfOrder(Player anotherPlayer){
	  return checkDiplomacyChange(anotherPlayer, DiplomacyLevel.CONFEDERACY);
  }

  public boolean haveConfOffer(Player anotherPlayer){
	  return checkDiplomacyOffer(anotherPlayer, DiplomacyLevel.CONFEDERACY);
  }

  private void addExpenses(Expense aExpense){
	  expenses.add(aExpense);
  }
  
  private void removeExpense(Expense aExpense){
	  expenses.remove(aExpense);
  }
  /*
  private void removeExpense(int index, Galaxy aGalaxy){
	  cost = cost - aExpense.getCost(this, aGalaxy);
	  expenses.remove(index);
  }*/
  

  
  public DiplomacyOffer findDiplomacyOffer(Player otherPlayer){
	  DiplomacyOffer foundOffer = null;
	  int counter = 0;
	  while ((foundOffer == null) & (counter < diplomacyOffers.size())){
		  DiplomacyOffer anOffer = diplomacyOffers.get(counter);
		  if (anOffer.getOtherPlayerName().equals(otherPlayer.getName())){
			  foundOffer = anOffer;
		  }else{
			  counter++;
		  }
	  }
	  return foundOffer;
  }

  public DiplomacyChange findDiplomacyChange(Player otherPlayer){
	  Logger.finest("otherPlayer.getName(): " + otherPlayer.getName());
	  DiplomacyChange foundChange = null;
	  int counter = 0;
	  Logger.finest("diplomacyChanges.size(): " + diplomacyChanges.size());
	  while ((foundChange == null) & (counter < diplomacyChanges.size())){
		  DiplomacyChange aChange = diplomacyChanges.get(counter);
		  Logger.finest("aChange.getOtherPlayerName(): " + aChange.getOtherPlayerName());
		  if (aChange.getOtherPlayerName().equalsIgnoreCase(otherPlayer.getName())){
			  foundChange = aChange;
		  }else{
			  counter++;
		  }
	  }
	  return foundChange;
  }
  
  public void removeDiplomacyOrder(Player otherPlayer){
	  DiplomacyOffer tmpOffer = findDiplomacyOffer(otherPlayer);
	  Diplomacy d = otherPlayer.getGalaxy().getDiplomacy();
	  if (tmpOffer != null){
		  if (tmpOffer.getSuggestedLevel() == DiplomacyLevel.CONFEDERACY){
			  // check if otherPlayer is in a confederacy
			  List<Player> confPlayers = d.getConfederacyPlayers(otherPlayer);
			  if (confPlayers.size() > 0){
				  for (Player confPlayer : confPlayers) {
					  DiplomacyOffer confOffer = findDiplomacyOffer(confPlayer);
					  Logger.fine("confOffer: " + confOffer.toString());
					  getDiplomacyOffers().remove(confOffer);
				  }
			  }
		  }
		  Logger.finest("tmpOffer: " + tmpOffer.toString());
		  getDiplomacyOffers().remove(tmpOffer);
	  }else{
		  DiplomacyChange tmpChange = findDiplomacyChange(otherPlayer);
		  if (tmpChange != null){
			  if (tmpChange.getNewLevel() == DiplomacyLevel.CONFEDERACY){
				  // check if otherPlayer is in a confederacy
				  List<Player> confPlayers = d.getConfederacyPlayers(otherPlayer);
				  if (confPlayers.size() > 0){
					  for (Player confPlayer : confPlayers) {
						  DiplomacyChange confChange = findDiplomacyChange(confPlayer);
						  Logger.finest("confChange: " + confChange.toString());
						  getDiplomacyChanges().remove(confChange);
					  }
				  }
			  }
			  Logger.finest("tmpChange: " + tmpChange.toString());
			  getDiplomacyChanges().remove(tmpChange);
		  }
	  }
  }
  
  public boolean diplomacyOrderExist(Player otherPlayer){
	  boolean found = false;
	  DiplomacyOffer tmpOffer = findDiplomacyOffer(otherPlayer);
	  if (tmpOffer != null){
		  Logger.fine("found = true: " + tmpOffer);
		  found = true;
	  }else{
		  Logger.fine("Change?");
		  DiplomacyChange tmpChange = findDiplomacyChange(otherPlayer);
		  Logger.fine("tmpChange: " + tmpChange);
		  if (tmpChange != null){
			  Logger.fine("found = true: " + tmpChange);
			  found = true;
		  }
	  }
	  return found;
  }

  public List<Expense> getExpenses(){
    return expenses;
  }

  public List<ShipMovement> getShipMoves(){
    return shipMoves;
  }

  public List<ShipToCarrierMovement> getShipToCarrierMoves(){
	  return shipToCarrierMoves;
  }

  public List<TroopToCarrierMovement> getTroopToCarrierMoves(){
	  return troopToCarrierMoves;
  }

  public List<TroopToPlanetMovement> getTroopToPlanetMoves(){
	  return troopToPlanetMoves;
  }
  
  public List<Integer> getTroopSelfDestructs(){
	  return troopSelfDestructs;
  }
  
  public List<Integer> getVIPSelfDestructs(){
	  return VIPSelfDestructs;
  }
  
  public List<VIPMovement> getVIPMoves(){
    return VIPMoves;
  }
  
  public List<VIPMovement> getVIPMoves(Planet aPlanet){
	  List<VIPMovement> vipMoves = new LinkedList<VIPMovement>();
	  for (VIPMovement aVIPMovement : VIPMoves) {
		  if (aPlanet.getName().equals(aVIPMovement.getPlanetDestinationName())){
			  vipMoves.add(aVIPMovement);
		  }
	  }
	  return vipMoves;
  }

  public List<String> getPlanetVisibilities(){
    return planetVisibilities;
  }

  public List<String> getAbandonPlanets(){
    return abandonPlanets;
  }

  public List<Integer> getShipSelfDestructs(){
    return shipSelfDestructs;
  }

  public List<Integer> getScreenedShips(){
    return screenedShips;
  }

  public List<Integer> getBuildingSelfDestructs(){
    return buildingSelfDestructs;
  }
/*
  public Vector<Message> getMessages(){
    return messages;
  }*/

  public List<TaxChange> getTaxChanges(){
	  return taxChanges;
  }

  public List<PlanetNotesChange> getPlanetNotesChanges(){
	  return planetNotesChanges;
  }

  public List<DiplomacyOffer> getDiplomacyOffers(){
	  return diplomacyOffers;
  }
  
  public List<DiplomacyChange> getDiplomacyChanges(){
	  return diplomacyChanges;
  }
  
  public PlanetNotesChange getPlanetNotesChange(Planet aPlanet){
	  PlanetNotesChange planetNotesChange = null;
	  for (PlanetNotesChange aPlanetNotesChange : planetNotesChanges) {
		  if (aPlanet.getName().equals(aPlanetNotesChange.getPlanetName())){
			  planetNotesChange = aPlanetNotesChange;
		  }
	  }
	  return planetNotesChange;
  }
	  
  public List<BlackMarketBid> getBlackMarketBids(){
	  List<BlackMarketBid> allBids = new LinkedList<BlackMarketBid>();
	  for (Expense anExpense : expenses) {
		  if (anExpense.isBlackMarketBid()){
			  allBids.add(anExpense.getBlackMarketBid());
		  }
	  }
	  return allBids;
  }

  public boolean getPlanetVisibility(Planet aPlanet){
    boolean found = false;
    for (int i = 0; i < planetVisibilities.size(); i++){
      String tempPlanet = planetVisibilities.get(i);
      if (aPlanet.getName().equalsIgnoreCase(tempPlanet)){
        found = true;
      }
    }
    return found;
  }

  public boolean getAbandonPlanet(Planet aPlanet){
    boolean found = false;
    for (int i = 0; i < abandonPlanets.size(); i++){
      if (aPlanet.getName().equalsIgnoreCase(abandonPlanets.get(i))){
        found = true;
      }
    }
    return found;
  }
  
  public boolean buildBuildingExpenseExist(Planet p, BuildingType bt){
	    boolean returnValue = false;
	    for (int i = 0; i < expenses.size(); i++){
	      Expense tempExpense = (Expense)expenses.get(i);
	      if (tempExpense.isBuildBuildingAt(p, bt)){
	        returnValue = true;
	      }
	    }
	    return returnValue;
  }
  
  public boolean incPopExpenseExist(Planet p){
    boolean returnValue = false;
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isIncPopAt(p)){
        returnValue = true;
      }
    }
    return returnValue;
  }

  public boolean incResExpenseExist(Planet p){
    boolean returnValue = false;
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isIncResAt(p)){
        returnValue = true;
      }
    }
    return returnValue;
  }

  public void addOrRemovePlanetVisib(Planet aPlanet){
    // finns det en planet visibility redan?
    int found = -1;
    for (int i = 0; i < planetVisibilities.size(); i++){
      if (aPlanet.getName().equalsIgnoreCase(planetVisibilities.get(i))){
        found = i;
      }
    }
    if (found > -1){ // ta bort den
      planetVisibilities.remove(found);
    }else{ // annars l�gg till den
      planetVisibilities.add(aPlanet.getName());
    }
  }

  
  public int checkScreenedShip(Spaceship aShip){
	    // finns det en screened ship order redan?
	  
	  
	    int found = -1;
	    for (int i = 0; i < screenedShips.size(); i++){
	      if (aShip.getId() == screenedShips.get(i)){
	        found = i;
	      }
	    }
	    
	    return found;
	  }

  
  public void addOrRemoveScreenedShip(Spaceship aShip){
    // finns det en screened ship order redan?
    int found = -1;
    for (int i = 0; i < screenedShips.size(); i++){
      if (aShip.getId() == screenedShips.get(i)){
        found = i;
      }
    }
    if (found > -1){ // ta bort den
      screenedShips.remove(found);
    }else{ // annars l�gg till den
      screenedShips.add(aShip.getId());
    }
  }
  
  public void addPlanetNotesChange(Planet aPlanet,String notesText){
	  Logger.fine("Notesorder: " + notesText);
	  PlanetNotesChange aPlanetNotesChange = getPlanetNotesChange(aPlanet);
	  if (aPlanetNotesChange != null){
		  aPlanetNotesChange.setNotesText(notesText);
	  }else{
		  planetNotesChanges.add(new PlanetNotesChange(aPlanet.getName(), notesText));
	  }
  }

  public void removePlanetNotesChange(Planet aPlanet){
	  PlanetNotesChange aPlanetNotesChange = getPlanetNotesChange(aPlanet);
	  if (aPlanetNotesChange != null){
		  planetNotesChanges.remove(aPlanetNotesChange);
	  }
  }

  public void removeAllGroundAttacksAgainstPlanet(Planet inPlanet, Player inPlayer){
	  Logger.fine("inPlanet: " + inPlanet.getName() + " player: " + inPlayer.getName());
	  
	  // remove troop moves to planet
	  List<TroopToPlanetMovement> removeList2 = new ArrayList<TroopToPlanetMovement>();
	  for(TroopToPlanetMovement aTroopToPlanetMovement: troopToPlanetMoves){
		  if (aTroopToPlanetMovement.isThisDestination(inPlanet)){
			  removeList2.add(aTroopToPlanetMovement);
		  }
	  }
	  // remove found orders
	  for(TroopToPlanetMovement aTroopToPlanetMovement: removeList2){
		  troopToPlanetMoves.remove(aTroopToPlanetMovement);
	  }
  }
 
  public void addOrRemoveAbandonPlanet(Planet aPlanet){
    // finns det en planet visibility redan?
    int found = -1;
    for (int i = 0; i < abandonPlanets.size(); i++){
      if (aPlanet.getName().equalsIgnoreCase(abandonPlanets.get(i))){
        found = i;
      }
    }
    if (found > -1){ // ta bort den
      abandonPlanets.remove(found);
    }else{ // annars l�gg till den
      abandonPlanets.add(aPlanet.getName());
    }
  }
  
  public void addNewBuilding(Planet aPlanet, String playerName, BuildingType tempbuilding, Galaxy aGalaxy){
	  addExpenses(new Expense("building", tempbuilding, playerName,aPlanet, null));
  }
  
  //public void removeNewBuilding(Planet aPlanet, BuildingType tempbuilding){
  public void removeNewBuilding(Planet aPlanet, Galaxy aGalaxy){
    int findIndex = -1;
	  for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
     // if (tempExpense.isBuildBuildingAt(aPlanet, tempbuilding)){
      if (tempExpense.isBuildBuildingAt(aPlanet)){
    	  findIndex = i;
      }
    }
    if (findIndex > -1){
    	removeExpense(expenses.get(findIndex));
    }
    
  }
  
  public void addReconstruct(Planet aPlanet, Player aPlayer){
	  addExpenses(new Expense("reconstruct",aPlanet,aPlayer));
  }

  public void removeReconstruct(Planet aPlanet, Galaxy aGalaxy){
	  int findIndex = -1;
	  for (int i = 0; i < expenses.size(); i++){
		  Expense tempExpense = (Expense)expenses.get(i);
		  if (tempExpense.isReconstructAt(aPlanet)){
			  findIndex = i;
			}
	  }
	  if (findIndex > -1){
	    	removeExpense(expenses.get(findIndex));
	    }
}

  public void addIncPop(Planet aPlanet,Player aPlayer){
	  addExpenses(new Expense("pop",aPlanet,aPlayer));
  }
	  
  public void removeIncPop(Planet aPlanet, Galaxy aGalaxy){
	  int findIndex = -1;
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isIncPopAt(aPlanet)){
    	  findIndex = i;
      }
    }
    if (findIndex > -1){
    	removeExpense(expenses.get(findIndex));
    }
  }

  public void addIncRes(Planet aPlanet, Player aPlayer){
	  addExpenses(new Expense("res",aPlanet,aPlayer));
  }

  public void removeIncRes(Planet aPlanet, Galaxy aGalaxy){
	  int findIndex = -1;
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isIncResAt(aPlanet)){
    	  findIndex = i;
      }
    }
    if (findIndex > -1){
    	removeExpense(expenses.get(findIndex));
    }
   }

  public void addNewShipMove(Spaceship ss, Planet destination){
    // f�rst kolla om det finns en gammal order f�r detta skepp som skall tas bort
    int found = -1;
    for (int i = 0; i < shipMoves.size(); i++){
      ShipMovement tempShipMove = (ShipMovement)shipMoves.get(i);
      if (tempShipMove.isThisShip(ss)){
        found = i;
      }
    }
    if (found > -1){
      shipMoves.remove(found);
    }
    if (destination != null){
      shipMoves.add(new ShipMovement(ss,destination));
    }
  }

  public void addNewTroopToPlanetMove(Troop aTroop, Planet destination, int turn){
	  // f�rst kolla om det finns en gammal order f�r detta skepp som skall tas bort
	  int found = -1;
	  for (int i = 0; i < troopToPlanetMoves.size(); i++){
		  TroopToPlanetMovement tempTroopToPlanetMove = troopToPlanetMoves.get(i);
		  if (tempTroopToPlanetMove.isThisTroop(aTroop)){
			  found = i;
		  }
	  }
	  if (found > -1){
		  troopToPlanetMoves.remove(found);
	  }
	  if (destination != null){
		  troopToPlanetMoves.add(new TroopToPlanetMovement(aTroop,destination,turn));
	  }
  }

  public void addNewTroopToCarrierMove(Troop aTroop, Spaceship destinationCarrier){
	  // f�rst kolla om det finns en gammal order f�r detta skepp som skall tas bort
	  int found = -1;
	  for (int i = 0; i < troopToCarrierMoves.size(); i++){
		  TroopToCarrierMovement tempTroopToCarrierMove = troopToCarrierMoves.get(i);
		  if (tempTroopToCarrierMove.isThisTroop(aTroop)){
			  found = i;
		  }
	  }
	  if (found > -1){
		  troopToCarrierMoves.remove(found);
	  }
	  if (destinationCarrier != null){
		  troopToCarrierMoves.add(new TroopToCarrierMovement(aTroop,destinationCarrier));
	  }
  }

  public void addNewTaxChange(String playerName, int amount){
	  TaxChange found = checkTaxChange(playerName);
	  // först kolla om det finns en gammal order för detta skepp som skall tas bort
//	  int found = -1;
//	  int i = 0;
//	  while ((found == -1) && (i < taxChanges.size())){
//		  TaxChange tempTaxChange = taxChanges.get(i);
//		  if (tempTaxChange.isThisPlayer(playerName)){
//			  found = i;
//		  }else{
//			  i++;
//		  }
//	  }
//	  if (found > -1){
	  if (found != null){
		  taxChanges.remove(found);
	  }
	  taxChanges.add(new TaxChange(amount,playerName));
  }
  
  public TaxChange checkTaxChange(String playerName){
	  TaxChange found = null;
	  int i = 0;
	  while ((found == null) && (i < taxChanges.size())){
		  TaxChange tempTaxChange = taxChanges.get(i);
		  if (tempTaxChange.isThisPlayer(playerName)){
			  found = tempTaxChange;
		  }else{
			  i++;
		  }
	  }
	  return found;
  }

  public void addNewShipToCarrierMove(Spaceship ss, Spaceship destinationCarrier){
	    // först kolla om det finns en gammal order f�r detta skepp som skall tas bort
	    int found = -1;
	    for (int i = 0; i < shipToCarrierMoves.size(); i++){
	      ShipToCarrierMovement tempShipToCarrierMove = (ShipToCarrierMovement)shipToCarrierMoves.get(i);
	      if (tempShipToCarrierMove.isThisShip(ss)){
	        found = i;
	      }
	    }
	    if (found > -1){
	      shipToCarrierMoves.remove(found);
	    }
	    if (destinationCarrier != null){
	      shipToCarrierMoves.add(new ShipToCarrierMovement(ss,destinationCarrier));
	    }
	  }

  public void addNewVIPMove(VIP aVIP,Object destination){
    // först kolla om det finns en gammal order f�r denna vip som skall tas bort
    int found = -1;
    for (int i = 0; i < VIPMoves.size(); i++){
      VIPMovement tempVIPMove = VIPMoves.get(i);
      if (tempVIPMove.isThisVIP(aVIP)){
        found = i;
      }
    }
    if (found > -1){
      VIPMoves.remove(found);
    }
    if (destination != null){
      if (destination instanceof Planet){
        VIPMoves.add(new VIPMovement(aVIP,(Planet)destination));
      }else
      if (destination instanceof Spaceship){
        VIPMoves.add(new VIPMovement(aVIP,(Spaceship)destination));
      }else{ // troop move
    	  VIPMoves.add(new VIPMovement(aVIP,(Troop)destination));
      }
    }
  }

  // kolla om det finns en gammal order f�r detta skepp
  public boolean checkShipMove(Spaceship ss){
    boolean found = false;
    int i = 0;
    while ((found == false) & (i < shipMoves.size())){
      ShipMovement tempShipMove = (ShipMovement)shipMoves.get(i);
      if (tempShipMove.isThisShip(ss)){
        found = true;
      }else{
        i++;
      }
    }
    return found;
  }
  
  // kolla om det finns en gammal order för detta VIP
  public boolean checkVIPMove(VIP vip){
	  boolean found = false;
	  int i = 0;
	  while ((found == false) & (i < VIPMoves.size())){
		  VIPMovement tempVIPMove = (VIPMovement)VIPMoves.get(i);
		  if (tempVIPMove.isThisVIP(vip)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  return found;
  }

  // aPlayer only needed to access galaxy???
  public boolean checkDiplomacyConfLordVassal(Player aPlayer){
	  boolean found = false;
	  List<Player> players = aPlayer.getGalaxy().getPlayers();
	  DiplomacyLevel[] levels = {DiplomacyLevel.CONFEDERACY,DiplomacyLevel.VASSAL,DiplomacyLevel.LORD};
	  for (DiplomacyLevel level : levels) {
		  for (Player player : players) {
			if (checkDiplomacy(player, level)){
				found = true;
			}
		  }
	  }
	  return found;
  }

  // aPlayer only needed to access galaxy???
  public boolean checkDiplomacyConfVassal(Player aPlayer){
	  boolean found = false;
	  List<Player> players = aPlayer.getGalaxy().getPlayers();
	  DiplomacyLevel[] levels = {DiplomacyLevel.CONFEDERACY,DiplomacyLevel.VASSAL};
	  for (DiplomacyLevel level : levels) {
		  for (Player player : players) {
			if (checkDiplomacyReversedLordVassalOrders(player, level)){
				found = true;
			}
		  }
	  }
	  return found;
  }

  /**
   * Check for diplomacy order
   * @param otherPlayer
   * @param newLevel
   * @return
   */
  public boolean checkDiplomacy(Player otherPlayer, DiplomacyLevel newLevel){
    boolean found = false;
	Logger.fine("checkDiplomacy: " + otherPlayer.getGovernorName() + " " + newLevel.name());
    for (DiplomacyChange aChange : diplomacyChanges) {
		if (aChange.isPlayerAndLevel(otherPlayer,newLevel)){
			Logger.fine("aChange found = true: " + aChange);
			found = true;
		}
	}
    if (!found){
        for (DiplomacyOffer anOffer : diplomacyOffers) {
    		if (anOffer.isPlayerAndLevel(otherPlayer,newLevel)){
    			Logger.fine("anOffer found = true: " + anOffer);
    			found = true;
    		}
    	}
    }
    return found;
  }

  public boolean checkDiplomacyReversedLordVassalOrders(Player otherPlayer, DiplomacyLevel newLevel){
	  boolean found = false;
	  for (DiplomacyChange aChange : diplomacyChanges) {
		  DiplomacyLevel tmpLevel = newLevel;
		  if (newLevel == DiplomacyLevel.LORD){
			  tmpLevel = DiplomacyLevel.VASSAL;
		  }else
		  if (newLevel == DiplomacyLevel.VASSAL){
			  tmpLevel = DiplomacyLevel.LORD;
		  }
		  if (aChange.isPlayerAndLevel(otherPlayer,tmpLevel)){
			  found = true;
		  }
	  }
	  if (!found){
		  for (DiplomacyOffer anOffer : diplomacyOffers) {
			  DiplomacyLevel tmpLevel = newLevel;
			  if (newLevel == DiplomacyLevel.LORD){
				  tmpLevel = DiplomacyLevel.VASSAL;
			  }else
			  if (newLevel == DiplomacyLevel.VASSAL){
				  tmpLevel = DiplomacyLevel.LORD;
			  }
			  if (anOffer.isPlayerAndLevel(otherPlayer,tmpLevel)){
				  found = true;
			  }
		  }
	  }
	  return found;
  }

  public boolean checkDiplomacyChange(Player otherPlayer, DiplomacyLevel newLevel){
	  boolean found = false;
	  for (DiplomacyChange aChange : diplomacyChanges) {
		  if (aChange.isPlayerAndLevel(otherPlayer,newLevel)){
			  found = true;
		  }
	  }
	  return found;
  }

  public boolean checkDiplomacyOffer(Player otherPlayer, DiplomacyLevel newLevel){
	  boolean found = false;
	  for (DiplomacyOffer anOffer : diplomacyOffers) {
		  if (anOffer.isPlayerAndLevel(otherPlayer,newLevel)){
			  found = true;
		  }
	  }
	  return found;
  }

  public void addDiplomacyOffer(DiplomacyOffer newDiplomacyOffer){
	  diplomacyOffers.add(newDiplomacyOffer);
  }

  public void addDiplomacyChange(DiplomacyChange newDiplomacyChange){
	  diplomacyChanges.add(newDiplomacyChange);
  }

  // check if there exist a planet move order for this troop
  public boolean checkTroopToPlanetMove(Troop aTroop){
	  boolean found = false;
	  int i = 0;
	  while ((found == false) & (i < troopToPlanetMoves.size())){
		  TroopToPlanetMovement tempMove = troopToPlanetMoves.get(i);
		  if (tempMove.isThisTroop(aTroop)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  return found;
  }

  // check if there exist a carrier move order for this troop
  public boolean checkTroopToCarrierMove(Troop aTroop){
	  boolean found = false;
	  int i = 0;
	  while ((found == false) & (i < troopToCarrierMoves.size())){
		  TroopToCarrierMovement tempMove = troopToCarrierMoves.get(i);
		  if (tempMove.isThisTroop(aTroop)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  return found;
  }

  // check if there exist a carrier move order for this troop to aCarrier
  public boolean checkTroopToCarrierMove(Troop aTroop, Spaceship aCarrier){
	  boolean found = false;
	  boolean moveToCarrier = false;
	  int i = 0;
	  while ((found == false) & (i < troopToCarrierMoves.size())){
		  TroopToCarrierMovement tempMove = troopToCarrierMoves.get(i);
		  if (tempMove.isThisTroop(aTroop)){
			  found = true;
			  if (tempMove.getDestinationCarrierId() == aCarrier.getId()){
				  moveToCarrier = true;
			  }
		  }else{
			  i++;
		  }
	  }
	  return moveToCarrier;
  }

  // kolla om det finns en gammal order f�r detta skepp
  public boolean checkShipToCarrierMove(Spaceship ss){
    boolean found = false;
    int i = 0;
    while ((found == false) & (i < shipToCarrierMoves.size())){
      ShipToCarrierMovement tempShipToCarrierMove = (ShipToCarrierMovement)shipToCarrierMoves.get(i);
      if (tempShipToCarrierMove.isThisShip(ss)){
        found = true;
      }else{
        i++;
      }
    }
    return found;
  }

  // kolla om det finns en gammal order f�r detta skepp
  public boolean checkShipToCarrierMove(Spaceship aSqd,Spaceship aCarrier){
    boolean found = false;
    int i = 0;
    while ((found == false) & (i < shipToCarrierMoves.size())){
      ShipToCarrierMovement tempShipToCarrierMove = (ShipToCarrierMovement)shipToCarrierMoves.get(i);
      if (tempShipToCarrierMove.isThisShip(aSqd)){
    	  if (tempShipToCarrierMove.isThisDestination(aCarrier)){
    		  found = true;
    	  }else{
    		  i++;
    	  }
      }else{
        i++;
      }
    }
    return found;
  }

  // kolla hur m�nga moveToCarrier orders det finns till den anvivna carriern
  public int countShipToCarrierMoves(Spaceship aCarrier){
    int count = 0;
    for (ShipToCarrierMovement aShipToCarrierMove : shipToCarrierMoves) {
//		LoggingHandler.finest( "STCM dest" + aShipToCarrierMove.getDestinationCarrier().getName());
		if (aShipToCarrierMove.isThisDestination(aCarrier)){
			count++;
			Logger.finest( "adding carrier count = " + count);
		}
	}
    return count;
  }

  // count how many troops are ordered to move to acarrier
  public int countTroopToCarrierMoves(Spaceship aCarrier){
	  int count = 0;
	  for (TroopToCarrierMovement aTroopToCarrierMove : troopToCarrierMoves) {
		  if (aTroopToCarrierMove.isThisDestination(aCarrier)){
			  count++;
		  }
	  }
	  return count;
  }

  // count how many troops are ordered to move from a carrier to a planet
  public int countTroopToPlanetMoves(Spaceship aCarrier, Planet aPlanet, Galaxy aGalaxy){
	  int count = 0;
	  for (TroopToPlanetMovement aTroopToPlanetMove : troopToPlanetMoves) {
		  if (aTroopToPlanetMove.getTroop(aGalaxy).getShipLocation() == aCarrier){
			  if (aTroopToPlanetMove.isThisDestination(aPlanet)){
				  count++;
			  }
		  }
	  }
	  return count;
  }

  // count how many troops are ordered to move to a planet
  public List<TroopToPlanetMovement> getTroopToPlanetMoves(Planet aPlanet){
	  List<TroopToPlanetMovement> troopMoves = new LinkedList<TroopToPlanetMovement>();
	  for (TroopToPlanetMovement aTroopToPlanetMove : troopToPlanetMoves) {
		  if (aTroopToPlanetMove.isThisDestination(aPlanet)){
			  troopMoves.add(aTroopToPlanetMove);
		  }
	  }
	  return troopMoves;
  }

  public void addNewTransaction(int aSum, Player recipient){
    // f�rst kolla om det finns en gammal transaktion som skall tas bort
	  int findIndex = -1;
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.getType().equalsIgnoreCase("transaction")){
        if (tempExpense.getPlayerName() == recipient.getName()){
        	findIndex = i;
        }
      }
    }
    if (findIndex > -1){
    	removeExpense(expenses.get(findIndex));
    }
    
    if (aSum > 0){
    	addExpenses(new Expense("transaction",recipient,aSum));
    }
  }

  public Planet getDestination(Spaceship tempss, Galaxy aGalaxy){
	  Planet dest = null;
	  boolean found = false;
	  int i = 0;
	  ShipMovement tempShipMove = null;
	  while ((i < shipMoves.size()) & !found){
		  tempShipMove = (ShipMovement)shipMoves.get(i);
		  if (tempShipMove.isThisShip(tempss)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  if (found){
		  
		  dest = aGalaxy.getPlanet(tempShipMove.getDestinationName());
	  }
	  return dest;
  }

  public Planet getDestinationPlanet(Troop aTroop, Galaxy aGalaxy){
	  Planet dest = null;
	  boolean found = false;
	  int i = 0;
	  TroopToPlanetMovement tempMove = null;
	  while ((i < troopToPlanetMoves.size()) & !found){
		  tempMove = troopToPlanetMoves.get(i);
		  if (tempMove.isThisTroop(aTroop)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  if (found){
		  dest = tempMove.getDestination(aGalaxy);
	  }
	  return dest;
  }

  public Spaceship getDestinationCarrier(Spaceship tempss, Galaxy aGalaxy){
	  Spaceship dest = null;
	  boolean found = false;
	  int i = 0;
	  ShipToCarrierMovement tempShipToCarrierMove = null;
	  while ((i < shipToCarrierMoves.size()) & !found){
		  tempShipToCarrierMove = (ShipToCarrierMovement)shipToCarrierMoves.get(i);
		  if (tempShipToCarrierMove.isThisShip(tempss)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  if (found){
		  dest = tempShipToCarrierMove.getDestinationCarrier(aGalaxy);
	  }
	  return dest;
  }

  public Spaceship getTroopDestinationCarrier(Troop aTroop, Galaxy aGalaxy){
	  Spaceship dest = null;
	  boolean found = false;
	  int i = 0;
	  TroopToCarrierMovement tempTroopToCarrierMove = null;
	  while ((i < troopToCarrierMoves.size()) & !found){
		  tempTroopToCarrierMove = (TroopToCarrierMovement)troopToCarrierMoves.get(i);
		  if (tempTroopToCarrierMove.isThisTroop(aTroop)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  if (found){
		  dest = tempTroopToCarrierMove.getDestinationCarrier(aGalaxy);
	  }
	  return dest;
  }

  public String getDestinationName(Spaceship tempss, Galaxy aGalaxy){
    String destName = "";
    Planet destination = getDestination(tempss, aGalaxy);
    if (destination != null){
    	destName = destination.getName();
    }
    return destName;
  }

  public String getDestinationPlanetName(Troop aTroop, Galaxy aGalaxy){
	  String destName = "";
	  Planet destination = getDestinationPlanet(aTroop, aGalaxy);
	  if (destination != null){
		  destName = destination.getName();
	  }
	  return destName;
  }

  public String getDestinationCarrierName(Spaceship tempss, Galaxy aGalaxy){
	  String destName = "";
	  Spaceship destination = getDestinationCarrier(tempss, aGalaxy);
	  if (destination != null){
		  destName = destination.getName();
	  }
	  return destName;
  }

  public String getDestinationCarrierShortName(Spaceship tempss, Galaxy aGalaxy){
	  String destName = "";
	  Spaceship destination = getDestinationCarrier(tempss, aGalaxy);
	  if (destination != null){
		  destName = destination.getShortName();
	  }
	  return destName;
  }

  public String getTroopDestinationCarrierName(Troop aTroop, Galaxy aGalaxy){
	  String destName = "";
	  Spaceship destination = getTroopDestinationCarrier(aTroop, aGalaxy);
	  if (destination != null){
		  destName = destination.getName();
	  }
	  return destName;
  }

  public String getTroopDestinationCarrierShortName(Troop aTroop, Galaxy aGalaxy){
	  String destName = "";
	  Spaceship destination = getTroopDestinationCarrier(aTroop, aGalaxy);
	  if (destination != null){
		  destName = destination.getShortName();
	  }
	  return destName;
  }

  public String getDestinationName(VIP tempVIP, Galaxy aGalaxy, boolean longName){
    String destName = "";
    boolean found = false;
    int i = 0;
    VIPMovement tempVIPMove = null;
    while ((i < VIPMoves.size()) & !found){
      tempVIPMove = VIPMoves.get(i);
      if (tempVIPMove.isThisVIP(tempVIP)){
        found = true;
      }else{
        i++;
      }
    }
    if (found){
      destName = tempVIPMove.getDestinationName(longName, aGalaxy);
    }
    return destName;
  }

  public void addBuildShip(Building aBuilding, SpaceshipType sst, Player  aPlayer){
    // skapa ny order
	  addExpenses(new Expense("buildship",aBuilding,sst, aPlayer.getName()));
  }
  
  public void addBuildTroop(Building aBuilding, TroopType tt, Player  aPlayer){
    // skapa ny order
	  addExpenses(new Expense("buildtroop",aBuilding,tt,aPlayer.getName()));
  }
  
  public void addBuildVIP(Building aBuilding, VIPType vt, Player  aPlayer){
    // skapa ny order
	  addExpenses(new Expense("buildVIP",aBuilding,vt,aPlayer.getName()));
  }

  public void removeAllBuildShip(Building aBuilding, Galaxy aGalaxy){
    int nrFoundIndexes = 0;
    int[] removeIndexes = new int[expenses.size()];
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isBuildingBuildingShip(aBuilding)){
  		Logger.finest( "Shall remove: " + nrFoundIndexes);
        removeIndexes[nrFoundIndexes] = i;
        nrFoundIndexes++;
      }
    }
    for (int j = nrFoundIndexes-1; j >= 0; j--){
	  Logger.finest( "Removing: " + j);
	  Logger.finest( "Removing: " + expenses.get(j).getSpaceshipTypeName());
	  removeExpense(expenses.get(removeIndexes[j]));
    }
  }
  
  public void removeAllBuildTroop(Building aBuilding, Galaxy aGalaxy){
    int nrFoundIndexes = 0;
    int[] removeIndexes = new int[expenses.size()];
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isBuildingBuildingTroop(aBuilding)){
  		Logger.finest("Shall remove: " + nrFoundIndexes);
        removeIndexes[nrFoundIndexes] = i;
        nrFoundIndexes++;
      }
    }
    for (int j = nrFoundIndexes-1; j >= 0; j--){
	  Logger.finest("Removing: " + j);
	  removeExpense(expenses.get(removeIndexes[j]));
	}
  }
  
  public void removeBuildVIP(Building aBuilding, Galaxy aGalaxy){
    int foundIndexes = -1;
    for (int i = 0; i < expenses.size(); i++){
    	Expense tempExpense = (Expense)expenses.get(i);
    	if (tempExpense.isBuildingBuildingVIP(aBuilding)){
    		foundIndexes = i;
    	}
    }
    if(foundIndexes >= 0){
    	removeExpense(expenses.get(foundIndexes));
    }
  }

  /*
	public void removeAllBuildShip(OrbitalWharf aWharf){
    int nrFoundIndexes = 0;
    int[] removeIndexes = new int[expenses.size()];
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.elementAt(i);
      if (tempExpense.isWharfBuilding(aWharf)){
  		LoggingHandler.finest( "Shall remove: " + nrFoundIndexes);
        removeIndexes[nrFoundIndexes] = i;
        nrFoundIndexes++;
      }
    }
    for (int j = nrFoundIndexes-1; j >= 0; j--){
	  LoggingHandler.finest( "Removing: " + j);
      expenses.removeElementAt(removeIndexes[j]);
    }
  }
	*/

  public void removeUpgradeBuilding(Building aBuilding, Galaxy aGalaxy){
    int findIndex = -1;
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isUpgradeBuilding(aBuilding)){
        findIndex = i;
      }
    }
    if (findIndex > -1){
    	removeExpense(expenses.get(findIndex));
    }
  }

  public void addUpgradeBuilding(Building currentBuilding, BuildingType newBuilding, Player aPlayer){
	  // skapa ny order om inte varvet redan �r satt att uppgradera
    if (!alreadyUpgrading(currentBuilding)){
    	addExpenses(new Expense("building", newBuilding, aPlayer.getName(),currentBuilding.getLocation(), currentBuilding));
    }
  }

  
  public boolean alreadyUpgrading(Building currentBuilding){
    boolean found = false;
    int i = 0;
    while ((i < expenses.size()) && (!found)){
      Logger.finer("alreadyUpgrading lop index: " + i);
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.isBuilding(currentBuilding)){
        found = true;
      }else{
        i++;
      }
    }
    return found;
  }
  
  /**
   * Check if there already exist a reconstruct order for this planet
   * 
   * @return
   */
  public boolean alreadyReconstructing(Planet aPlanet){
	  boolean found = false;
	  int i = 0;
	  while ((i < expenses.size()) & (!found)){
		  Expense tempExpense = (Expense)expenses.get(i);
		  if (tempExpense.isReconstructAt(aPlanet)){
			  found = true;
		  }else{
			  i++;
		  }
	  }
	  return found;
  }
  
  public boolean haveSpaceshipTypeBuildOrder(SpaceshipType aSpaceshipType){
	  for (int i = 0; i < expenses.size(); i++){
	      Expense tempExpense = (Expense)expenses.get(i);
	      if (aSpaceshipType.getName().equals(tempExpense.getSpaceshipTypeName())){
	      return true;
	      }
	    }
	  return false;
  }
  
  
  public boolean haveBuildingTypeBuildOrder(BuildingType aBuildingType, int buildingId){
	  for (int i = 0; i < expenses.size(); i++){
	      Expense tempExpense = (Expense)expenses.get(i);
	      if (aBuildingType.getName().equals(tempExpense.getBuildingTypeName())){
	    	  if(buildingId < 0 || buildingId != tempExpense.getCurrentBuildingId()){
	    		  return true;
	    	  }
	      }
	    }
	  return false;
  }
  
  public boolean haveVIPTypeBuildOrder(VIPType aVIPType){
	  for (int i = 0; i < expenses.size(); i++){
	      Expense tempExpense = (Expense)expenses.get(i);
	      if (aVIPType.getName().equals(tempExpense.getVipTypeName())){
	      return true;
	      }
	    }
	  return false;
  }
  
  public boolean haveTroopTypeBuildOrder(TroopType aTroopType){
	  for (int i = 0; i < expenses.size(); i++){
	      Expense tempExpense = (Expense)expenses.get(i);
	      if (aTroopType.getUniqueName().equals(tempExpense.getTroopTypeName())){
	      return true;
	      }
	    }
	  return false;
  }

  
  public String getVIPBuild(Building currentBuilding){
	  String tempVIPName = null;
    for (int i = 0; i < expenses.size(); i++){
      Expense tempExpense = (Expense)expenses.get(i);
      if(tempExpense.isBuildingBuildingVIP(currentBuilding)){
    	  tempVIPName = tempExpense.getVIPType();
      }
    }
    return tempVIPName;
  }

  public void addShipSelfDestruct(Spaceship currentss){
    shipSelfDestructs.add(currentss.getId());
  }

  public void addTroopSelfDestruct(Troop aTroop){
	  troopSelfDestructs.add(aTroop.getId());
  }
  
  public void addVIPSelfDestruct(VIP aVIP){
	  
	  int found = -1;
	    for (int i = 0; i < VIPMoves.size(); i++){
	      VIPMovement tempVIPMove = VIPMoves.get(i);
	      if (tempVIPMove.isThisVIP(aVIP)){
	        found = i;
	      }
	    }
	    if (found > -1){
	      VIPMoves.remove(found);
	    }
	  
	  VIPSelfDestructs.add(aVIP.getId());
  }

  public void removeShipSelfDestruct(Spaceship currentss){
	  for (int i=0; i < shipSelfDestructs.size(); i++) {
		if(shipSelfDestructs.get(i) == currentss.getId()){
			shipSelfDestructs.remove(i);
		}
	  }
  }

  public void removeTroopSelfDestruct(Troop aTroop){
	  for (int i=0; i < troopSelfDestructs.size(); i++) {
		if(troopSelfDestructs.get(i) == aTroop.getId()){
			troopSelfDestructs.remove(i);
		}
	  }
  }
  
  public void removeVIPSelfDestruct(VIP aVIP){
	  for (int i=0; i < VIPSelfDestructs.size(); i++) {
			if(VIPSelfDestructs.get(i) == aVIP.getId()){
				VIPSelfDestructs.remove(i);
			}
		  }
	}
  

  public void addBuildingSelfDestruct(Building currentBuilding){
	  buildingSelfDestructs.add(currentBuilding.getUniqueId());
  }

  public void removeBuildingSelfDestruct(Building currentBuilding){
	  for (int i=0; i < buildingSelfDestructs.size(); i++) {
		if(buildingSelfDestructs.get(i) == currentBuilding.getUniqueId()){
			buildingSelfDestructs.remove(i);
		}
	  }
  }

  public void addNewBlackMarketBid(int aSum,BlackMarketOffer aOffer,Planet destination, Player aPlayer){
    // remove old any bid to this offer
    Expense oldExpenseBid = getBidToOffer(aOffer);
    if (oldExpenseBid != null){
    	removeExpense(oldExpenseBid);
    }
    // add new bid if sum > 0
    if (aSum > 0){
    	if( destination == null){
    		addExpenses(new Expense("blackmarketbid",new BlackMarketBid(aSum,aOffer,null),aPlayer));
    	}
    	else
    	{
    		addExpenses(new Expense("blackmarketbid",new BlackMarketBid(aSum,aOffer,destination.getName()),aPlayer));
    	}	
    }
  }

  /**
   * Returns the amount to give to aPlayer. If none is to be given (no gift exists),
   * 0 is returned.
   * @param aPlayer The player to find gift to
   * @return gift sum to aPlayer
   */
  public int findGift(Player aPlayer){
  	int giftSum = 0;
  	for (Expense anExpense : expenses) {
		if (anExpense.isTransaction()){
			if (aPlayer.isPlayer(anExpense.getPlayerName())){
				giftSum = anExpense.getSum();
			}
		}
	}
  	return giftSum;
  }

  public boolean getShipSelfDestruct(Spaceship ss){
    boolean found = false;
    for (int i = 0; i < shipSelfDestructs.size(); i++){
      if (ss.getId() == shipSelfDestructs.get(i)){
        found = true;
      }
    }
    return found;
  }

  public boolean getTroopSelfDestruct(Troop aTroop){
	  boolean found = false;
	  for (int i = 0; i < troopSelfDestructs.size(); i++){
		  int tempTroop = troopSelfDestructs.get(i);
		  if (tempTroop == aTroop.getId()){
			  found = true;
		  }
	  }
	  return found;
  }
  
  public boolean getVIPSelfDestruct(VIP aVIP){
	  boolean found = false;
	  for (int i = 0; i < VIPSelfDestructs.size(); i++){
		  int tempVIP = VIPSelfDestructs.get(i);
		  if (tempVIP == aVIP.getId()){
			  found = true;
		  }
	  }
	  return found;
  }

  public boolean getScreenedShip(Spaceship ss){
    boolean found = false;
    for (int i = 0; i < screenedShips.size(); i++){
      if (ss.getId() == screenedShips.get(i)){
        found = true;
      }
    }
    return found;
  }
  
//TODO (Tobbe) gör om
  public boolean getBuildingSelfDestruct(Building aBuilding){
    boolean found = false;
    for (int i = 0; i < buildingSelfDestructs.size(); i++){
      if (buildingSelfDestructs.get(i) == aBuilding.getUniqueId()){
        found = true;
      }
    }
    return found;
  }

  // leta igenom alla VIPMoves och kolla om någon av dem förflyttar iväg denna vip
  public boolean VIPWillStay(VIP tempEngineer){
    boolean vipStays = true;
    int i = 0;
    while((i < VIPMoves.size()) & (vipStays)){
      VIPMovement tempVIPMove = VIPMoves.get(i);
      if (tempVIPMove.isThisVIP(tempEngineer)){
        vipStays = false;
      }else{
        i++;
      }
    }
    return vipStays;
  }
  
  public Expense getBidToOffer(BlackMarketOffer tempOffer){
    Expense found = null;
    int i = 0;
    while ((i < expenses.size()) & (found == null)){
      Expense tempExpense = (Expense)expenses.get(i);
      if (tempExpense.getBlackMarketBid() != null){
        if (tempExpense.getBlackMarketBid().getOffer().getUniqueId() == tempOffer.getUniqueId()){
          found = tempExpense;
        }else{
          i++;
        }
      }else{
        i++;
      }
    }
    return found;
  }

  public boolean isAbandonGame() {
	  return abandonGame;
  }
  
  public void setAbandonGame(boolean abandonGame) {
	  this.abandonGame = abandonGame;
  }
  
  	public List<ResearchOrder> getResearchOrders() {
		return researchOrder;
	}

  	public ResearchOrder getResearchOrder(String name) {
  		for(int i=0; i < researchOrder.size();i++){
			if(researchOrder.get(i).getAdvantageName().equals(name)){
				return researchOrder.get(i);
			}
		}
		return null;
	}

	public void setResearchOrders(List<ResearchOrder> researchOrder) {
		this.researchOrder = researchOrder;
	}
	public void addResearchOrder(ResearchOrder researchOrder, Player p) {
		this.researchOrder.add(researchOrder);
		if(researchOrder.getCost()> 0){
			addExpenses(new Expense("research", researchOrder, p));
		}
		
		
		//Expense(String temptype, ResearchOrder ro, Player aPlayer, int aSum){
	}
	public void removeResearchOrder(ResearchOrder researchOrder, Galaxy aGalaxy) {
		int findIndex = -1;
	    for (int i = 0; i < expenses.size(); i++){
	      Expense tempExpense = (Expense)expenses.get(i);
	      if (tempExpense.isResearchOrder(researchOrder.getAdvantageName())){
	        findIndex = i;
	      }
	    }
	    if (findIndex > -1){
	    	removeExpense(expenses.get(findIndex));
	    }
		this.researchOrder.remove(researchOrder);
	}
	
	public void removeResearchOrder(String rOrder, Galaxy aGalaxy) {
		for(int i=0; i < researchOrder.size();i++){
			if(researchOrder.get(i).getAdvantageName().equals(rOrder)){
				researchOrder.remove(i);
			}
		}
		int findIndex = -1;
	    for (int i = 0; i < expenses.size(); i++){
	      Expense tempExpense = (Expense)expenses.get(i);
	      if (tempExpense.isResearchOrder(rOrder)){
	        findIndex = i;
	      }
	    }
	    if (findIndex > -1){
	    	removeExpense(expenses.get(findIndex));
	    }
	}
	
	  // kolla om det finns en gammal order för denna forskning
	public boolean checkResearchOrder(String rOrder){
	    boolean found = false;
	    int i = 0;
	    while ((found == false) & (i < researchOrder.size())){
	      ResearchOrder tempResearchOrder = researchOrder.get(i);
	      if (tempResearchOrder.getAdvantageName().equals(rOrder)){
	        found = true;
	      }else{
	        i++;
	      }
	    }
	    return found;
	}
	
	/**
	 * Returns all ships on planet with no move orders and all ships with orders against planet.
	 * @param aPlayer
	 * @param aPlanet
	 * @return
	 */
	public List<Spaceship> getShipAtPlanetNextTurn(Player aPlayer, Planet aPlanet){
		List<Spaceship> tempShipList = aPlayer.getGalaxy().getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		List<ShipMovement> shipMovemants = getShipMoves();
		for (ShipMovement shipMovement : shipMovemants) {
			if(shipMovement.getDestinationName().equalsIgnoreCase(aPlanet.getName())){// adding ships with travel ordes against the planet.
				Spaceship tempShip = aPlayer.getGalaxy().findSpaceship(shipMovement.getSpaceShipID());
				if(!tempShipList.contains(tempShip)){
					tempShipList.add(tempShip);
				}
				
				if(tempShip.isCarrier()){
					List<ShipToCarrierMovement> SqdToCarrierMovementList = getShipToCarrierMoves();
					for (ShipToCarrierMovement shipToCarrierMovement : SqdToCarrierMovementList) {
						if(shipToCarrierMovement.getDestinationCarrierId() == tempShip.getId()){
							tempShipList.add(aPlayer.getGalaxy().findSpaceship(shipToCarrierMovement.getSpaceshipId()));
						}
					}
					List<Spaceship> shipListAtCarrierPlanet =  aPlayer.getGalaxy().getPlayersSpaceshipsOnPlanet(aPlayer, tempShip.getLocation());
					for (Spaceship spaceship : shipListAtCarrierPlanet) {
						if(spaceship.getCarrierLocation() == tempShip){
							boolean add = true;
							for (ShipMovement shipMove : shipMovemants) {
								if(shipMove.getSpaceShipID() == spaceship.getId()){
									add = false;
								}
							}
							if(add){
								tempShipList.add(spaceship);
							}
						}
					}
				}
				
			}else{
				Spaceship tempSpaceship = aPlayer.getGalaxy().findSpaceship(shipMovement.getSpaceShipID());
				if(tempSpaceship.getLocation() != null && tempSpaceship.getLocation() == aPlanet){// removing ships on the planet with move orders
					tempShipList.remove(tempSpaceship);
					if(tempSpaceship.isCarrier()){
						List<Spaceship> removeShips = new ArrayList<Spaceship>();
						for (Spaceship tempShip : tempShipList) {
							if(tempShip.isSquadron()){
								List<ShipToCarrierMovement> SqdToCarrierMovementList = getShipToCarrierMoves();
								for (ShipToCarrierMovement shipToCarrierMovement : SqdToCarrierMovementList) {
									if(shipToCarrierMovement.getDestinationCarrierId() == tempSpaceship.getId()){
										removeShips.add(tempShip);
									}
								}
								if(tempShip.getCarrierLocation() == tempSpaceship){
									boolean add = true;
									for (ShipMovement shipMove : shipMovemants) {
										if(shipMove.getSpaceShipID() == tempShip.getId()){
											add = false;
										}
									}
									if(add){
										removeShips.add(tempShip);
									}
									
									
								}
							}
						}
						tempShipList.removeAll(removeShips);
					}
				}
			}
		}
		return tempShipList;
	}


	/**
	 * Return text lines for all orders (used in the app client)
	 */
	/*
	public List<String> dumpOrders(Galaxy g, Player player){
		List<String> ordersTextList = new ArrayList<String>();
		// add expenses
		for (Expense anExpence : expenses){
			ordersTextList.add("Expense: " + anExpence.getText(g,this));
		}

		// add ship movements
		for (ShipMovement aShipMovement : shipMoves){
			ordersTextList.add(aShipMovement.getText(g));
		}
		
		// add squadron moves to carriers
		for (ShipToCarrierMovement aShipToCarrierMovement : shipToCarrierMoves) {
			ordersTextList.add(aShipToCarrierMovement.getText(g));
		}

		// add changes in planet visibility
		for (String aPlanetName : planetVisibilities){
			StringBuffer sb = new StringBuffer();
			Planet tempPlanet = g.findPlanet(aPlanetName);
			sb.append("Change planet " + tempPlanet.getName() + " to ");
			if (tempPlanet.isOpen()){  // change to closed
				sb.append("closed");
			}else{ // change to open
				sb.append("open");
			}
			sb.append(" status.");
			ordersTextList.add(sb.toString());
		}

		// add changes in abandoning planets
		for (String aPlanetName : abandonPlanets){
			Planet tempPlanet = g.findPlanet(aPlanetName);
			ordersTextList.add("Planet " + tempPlanet.getName() + " is to be abandoned.");
		}

		// add ships do be selfdestroyed
		for (Integer shipId : shipSelfDestructs){
			Spaceship tempss = g.findSpaceship(shipId);
			ordersTextList.add("Spaceship " + tempss.getName() + " is to be destroyed.");
		}

		//  add VIPs do be selfdestroyed
		for (Integer vipId : VIPSelfDestructs){
			VIP tempVIP = g.findVIP(vipId);
			ordersTextList.add("VIP " + tempVIP.getName() + " is to be retired.");
		}

		// add ships do be selfdestroyed
		for (Integer buildingId : buildingSelfDestructs){
			Building tempBuilding = g.findBuilding(buildingId,player);
			ordersTextList.add("Building " + tempBuilding.getBuildingType().getName() + " at " + tempBuilding.getLocation().getName() + " is to be destroyed.");
		}

		// add VIP movements
		for (VIPMovement aVIPMovement : VIPMoves){
			ordersTextList.add(aVIPMovement.getText(g));
		}

		// Change planet notes
		for (PlanetNotesChange change : planetNotesChanges) {
			ordersTextList.add(change.getText());
		}

		// other orders (i.e. abandon game)
		if (abandonGame){
			ordersTextList.add("Abandon game");
		}

		if (ordersTextList.size() == 0){
			ordersTextList.add("No orders exist");
		}
		
		return ordersTextList;
	}
	*/
}
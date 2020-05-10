//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class BlackMarketOffer implements Serializable {
  static final long serialVersionUID = 1L;
  VIPType offeredVIPType;
  SpaceshipType offeredShiptype;
  SpaceshipType offeredShiptypeBlueprint;
  TroopType offeredTroopType;
  boolean hotStuff;
  final int uniqueId;
  int hotStuffAmount;
  List<BlackMarketBid> bids;
  //Galaxy g;
  private int lastTurnAction = 0;

    public BlackMarketOffer(int uniqueId) {
        this.uniqueId = uniqueId;
        this.bids = new LinkedList<>();
    }

  public String getString(){
    String returnString = "Hot stuff";
    if (offeredVIPType != null){
      returnString = offeredVIPType.getTypeName();
    }else
    if (offeredShiptype != null){
      returnString = offeredShiptype.getName();
    }else
    if (offeredTroopType != null){
        returnString = offeredTroopType.getUniqueName();
    }else
    if (offeredShiptypeBlueprint != null){
        returnString = "Blueprint: " + offeredShiptypeBlueprint.getName();
    }
    return returnString;
  }

  public String getTypeString(){
	  String returnString = "Illegal goods for sale";
	  if (offeredVIPType != null){
		  returnString = "VIP for hire";
	  }else
	  if (offeredShiptype != null){
		  returnString = "Spaceship for sale";
	  }else
	  if (offeredTroopType != null){
		  returnString = "Troop unit for hire";
	  }else
	  if (offeredShiptypeBlueprint != null){
		  returnString = "Ship blueprint for sale";
	  }
	  return returnString;
  }

  public int getUniqueId(){
    return uniqueId;
  }

  public void addBid(BlackMarketBid aBid){
    bids.add(aBid);
  }

  public void resetBids(){
      bids = new LinkedList<BlackMarketBid>();
  }
  
  private boolean tooOld(Galaxy galaxy){
	  boolean old = false;
	  if ((lastTurnAction + 2) < galaxy.getTurn()){
		  old = true;
	  }
	  return old;
  }

  public List<BlackMarketBid> getBids(){
      return bids;
    }

    public boolean removeTooOldAndSendMessage(Galaxy galaxy){
      boolean tooOld = tooOld(galaxy);
      if(tooOld){
          createRemovedOldMessage(galaxy);
      }
      return tooOld;
    }
  
  public void createRemovedOldMessage(Galaxy galaxy){
	  // create turn info row for all undefeated players 
      galaxy.addBlackMarketMessages(null,getString() + " removed from Black market due to lack of bids for 3 turns.");
  }

  public void sendRefundingMessages(BlackMarketBid winningBid, Galaxy galaxy){
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      if (aBid != winningBid){
    	  galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("Your bid of " + aBid.getCost() + " have been refunded.");
      }
    }
  }

  public void sendDrawMessages(Galaxy galaxy){
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("The bidding for a " + getString() + " ended in a draw at the cost of " + getHighestBid() + ".");
      galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("The " + getString() + " will remain for sale.");
    }
  }

  public BlackMarketBid getHighestBidder(Galaxy galaxy){
    BlackMarketBid maxBid = null;
    boolean draw = false;
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      if (maxBid == null){
        maxBid = aBid;
        lastTurnAction = galaxy.getTurn();
      }else
      if (aBid.getCost() > maxBid.getCost()){
        maxBid = aBid;
        draw = false;
      }else
      if (aBid.getCost() == maxBid.getCost()){
        draw = true;
      }
    }
    if (draw){ // if the bidding was a draw, no-one wins...
      maxBid = null;
    }
    return maxBid;
  }

  private int getHighestBid(){
    int maxBid = 0;
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      if (maxBid == 0){
        maxBid = aBid.getCost();
      }else
      if (aBid.getCost() > maxBid){
        maxBid = aBid.getCost();
      }
    }
    return maxBid;
  }

  public boolean isHotStuff(){
    return hotStuff;
  }
  
  public boolean isShip(){
	  return offeredShiptype != null;
  }

  public boolean isShipBlueprint(){
	  return offeredShiptypeBlueprint != null;
  }

  public boolean isVIP(){
	return offeredVIPType != null;
  }

  public boolean isTroop(){
	  return offeredTroopType != null;
  }

  public VIPType getVIPType(){
	  return offeredVIPType;
  }

  public VIPType getOfferedVIPType() {
	  return offeredVIPType;
  }

  public SpaceshipType getOfferedShiptype() {
	  return offeredShiptype;
  }

  public SpaceshipType getOfferedShiptypeBlueprint() {
	  return offeredShiptypeBlueprint;
  }
  
  public SpaceshipType getShipType(){
	  SpaceshipType aSST = offeredShiptype;
	  if (aSST == null){
		  aSST = offeredShiptypeBlueprint;
	  }
	  return aSST;
  }

  public TroopType getOfferedTroopType() {
	  return offeredTroopType;
  }
  
  public boolean destinationRequired(){
	  return isVIP() | isShip() | isTroop();
  }

    public int getHotStuffAmount() {
        return hotStuffAmount;
    }

    public int getLastTurnAction() {
        return lastTurnAction;
    }

    public void setLastTurnAction(int lastTurnAction) {
        this.lastTurnAction = lastTurnAction;
    }

    public void setOfferedVIPType(VIPType offeredVIPType) {
        this.offeredVIPType = offeredVIPType;
    }

    public void setOfferedShiptype(SpaceshipType offeredShiptype) {
        this.offeredShiptype = offeredShiptype;
    }

    public void setOfferedShiptypeBlueprint(SpaceshipType offeredShiptypeBlueprint) {
        this.offeredShiptypeBlueprint = offeredShiptypeBlueprint;
    }

    public void setOfferedTroopType(TroopType offeredTroopType) {
        this.offeredTroopType = offeredTroopType;
    }

    public void setHotStuffAmount(int hotStuffAmount) {
        this.hotStuff = true;
        this.hotStuffAmount = hotStuffAmount;
    }

    public void setBids(List<BlackMarketBid> bids) {
        this.bids = bids;
    }
}
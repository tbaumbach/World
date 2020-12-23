package spaceraze.world;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity()
@Table(name = "BLACK_MARKET_OFFER")
public class BlackMarketOffer implements Serializable {
  static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "FK_GALAXY")
  private Galaxy galaxy;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_VIP_TYPE", insertable = false, updatable = false)
  VIPType vipType;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_SPACESHIP_TYPE", insertable = false, updatable = false)
  SpaceshipType spaceshipType;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_SPACESHIP_TYP_BLUE_PRINT", insertable = false, updatable = false)
  SpaceshipType blueprint;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "TROOP_TYPE", insertable = false, updatable = false)
  TroopType troopType;

  boolean hotStuff;
  int uniqueId;
  int hotStuffAmount;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "blackMarketOffer")
  @Builder.Default
  List<BlackMarketBid> blackMarketBids = new ArrayList<>();

  private int lastTurnAction = 0;

    public BlackMarketOffer(int uniqueId) {
        this.uniqueId = uniqueId;
        this.blackMarketBids = new LinkedList<>();
    }

  public String getString(){
    String returnString = "Hot stuff";
    if (vipType != null){
      returnString = vipType.getTypeName();
    }else
    if (spaceshipType != null){
      returnString = spaceshipType.getName();
    }else
    if (troopType != null){
        returnString = troopType.getName();
    }else
    if (blueprint != null){
        returnString = "Blueprint: " + blueprint.getName();
    }
    return returnString;
  }

  public String getTypeString(){
	  String returnString = "Illegal goods for sale";
	  if (vipType != null){
		  returnString = "VIP for hire";
	  }else
	  if (spaceshipType != null){
		  returnString = "Spaceship for sale";
	  }else
	  if (troopType != null){
		  returnString = "Troop unit for hire";
	  }else
	  if (blueprint != null){
		  returnString = "Ship blueprint for sale";
	  }
	  return returnString;
  }

  public int getUniqueId(){
    return uniqueId;
  }

  public void addBid(BlackMarketBid aBid){
    blackMarketBids.add(aBid);
  }

  public void resetBids(){
      blackMarketBids = new LinkedList<BlackMarketBid>();
  }
  
  private boolean tooOld(Galaxy galaxy){
	  boolean old = false;
	  if ((lastTurnAction + 2) < galaxy.getTurn()){
		  old = true;
	  }
	  return old;
  }

  public List<BlackMarketBid> getBlackMarketBids(){
      return blackMarketBids;
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
    for (int i = 0; i < blackMarketBids.size(); i++){
      BlackMarketBid aBid = blackMarketBids.get(i);
      if (aBid != winningBid){
    	  galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("Your bid of " + aBid.getCost() + " have been refunded.");
      }
    }
  }

  public void sendDrawMessages(Galaxy galaxy){
    for (int i = 0; i < blackMarketBids.size(); i++){
      BlackMarketBid aBid = blackMarketBids.get(i);
      galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("The bidding for a " + getString() + " ended in a draw at the cost of " + getHighestBid() + ".");
      galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("The " + getString() + " will remain for sale.");
    }
  }

  public BlackMarketBid getHighestBidder(Galaxy galaxy){
    BlackMarketBid maxBid = null;
    boolean draw = false;
    for (int i = 0; i < blackMarketBids.size(); i++){
      BlackMarketBid aBid = blackMarketBids.get(i);
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
    for (int i = 0; i < blackMarketBids.size(); i++){
      BlackMarketBid aBid = blackMarketBids.get(i);
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
	  return spaceshipType != null;
  }

  public boolean isShipBlueprint(){
	  return blueprint != null;
  }

  public boolean isVIP(){
	return vipType != null;
  }

  public boolean isTroop(){
	  return troopType != null;
  }

  public VIPType getVIPType(){
	  return vipType;
  }

  public VIPType getVipType() {
	  return vipType;
  }

  public SpaceshipType getSpaceshipType() {
	  return spaceshipType;
  }

  public SpaceshipType getBlueprint() {
	  return blueprint;
  }
  
  public SpaceshipType getShipType(){
	  SpaceshipType aSST = spaceshipType;
	  if (aSST == null){
		  aSST = blueprint;
	  }
	  return aSST;
  }

  public TroopType getTroopType() {
	  return troopType;
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

    public void setVipType(VIPType vipType) {
        this.vipType = vipType;
    }

    public void setSpaceshipType(SpaceshipType spaceshipType) {
        this.spaceshipType = spaceshipType;
    }

    public void setBlueprint(SpaceshipType blueprint) {
        this.blueprint = blueprint;
    }

    public void setTroopType(TroopType troopType) {
        this.troopType = troopType;
    }

    public void setHotStuffAmount(int hotStuffAmount) {
        this.hotStuff = true;
        this.hotStuffAmount = hotStuffAmount;
    }

    public void setBlackMarketBids(List<BlackMarketBid> bids) {
        this.blackMarketBids = bids;
    }
}
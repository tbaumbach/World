//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;

import spaceraze.util.general.Logger;
import spaceraze.world.BlackMarketBid;
import spaceraze.world.BlackMarketOffer;
import spaceraze.world.Galaxy;
import spaceraze.world.Player;

public class BlackMarketBid implements Serializable {
  static final long serialVersionUID = 1L;
  private int cost;
  private BlackMarketOffer aOffer;
  private String destination;
  private String playerName;

  public BlackMarketBid(int cost,BlackMarketOffer aOffer,String destination) {
    this.cost = cost;
    this.aOffer = aOffer;
    this.destination = destination;
  }

  public BlackMarketBid(BlackMarketBid oldBlackMarketBid, Galaxy newGalaxy){
    this.cost = oldBlackMarketBid.getCost();
    this.aOffer = newGalaxy.findBlackMarketOffer(oldBlackMarketBid.getOfferUniqueId());
    this.destination = oldBlackMarketBid.getDestination();
	Logger.finest( "BlackMarketBid 1: " + oldBlackMarketBid.getPlayerName());
    this.playerName = oldBlackMarketBid.getPlayerName();
	Logger.finest( "BlackMarketBid 2: " + playerName);
  }

  public int getCost(){
    return cost;
  }

  public String getText(){
    String returnString = "You have made a bid for a " + aOffer.getString() + " at the cost " + cost + ".";
    return returnString;
  }

  public String getDestinationString(){
    String returnString = null;
    if (destination != null){
      returnString = destination;
    }
    return returnString;
  }

  public int getOfferUniqueId(){
    return aOffer.getUniqueId();
  }

  public BlackMarketOffer getOffer(){
    return aOffer;
  }

  public void addPlayer(Player aPlayer){
	  playerName = aPlayer.getName();
  }

  public String getPlayerName(){
    return playerName;
  }

  public String getDestination(){
    return destination;
  }

}
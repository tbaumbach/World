package spaceraze.world;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.util.general.Logger;
import spaceraze.world.orders.Expense;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "BLACK_MARKET_BID")
public class BlackMarketBid implements Serializable {
  static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "blackMarketBid")
  private Expense expense;

  @ManyToOne
  @JoinColumn(name = "FK_BLACK_MARKET_OFFER")
  private BlackMarketOffer blackMarketOffer;

  private int cost;
  private int blackMarketOfferUniqueId;
  private String destination;
  private String playerName;

  public BlackMarketBid(int cost,int blackMarketOfferUniqueId,String destination) {
    this.cost = cost;
    this.blackMarketOfferUniqueId = blackMarketOfferUniqueId;
    this.destination = destination;
  }

  public BlackMarketBid(BlackMarketBid oldBlackMarketBid, Galaxy newGalaxy){
    this.cost = oldBlackMarketBid.getCost();
    this.blackMarketOfferUniqueId = oldBlackMarketBid.getOfferUniqueId();
    this.destination = oldBlackMarketBid.getDestination();
	Logger.finest( "BlackMarketBid 1: " + oldBlackMarketBid.getPlayerName());
    this.playerName = oldBlackMarketBid.getPlayerName();
	Logger.finest( "BlackMarketBid 2: " + playerName);
  }

  public int getCost(){
    return cost;
  }

  public static String getBiddingText(BlackMarketOffer aOffer, BlackMarketBid winningBid){
    String returnString = "You have made a bid for a " + aOffer.getString() + " at the cost " + winningBid.getCost() + ".";
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
    return blackMarketOfferUniqueId;
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
package spaceraze.world.diplomacy;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Player;
import spaceraze.world.orders.Orders;

import javax.persistence.*;

/**
 * This class is used when a player wish to send an offer to another player
 * to enter a higher level. the other player must respond with a DiplomacyChange
 * the next turn or send an offer the same turn for the change to be performed.
 * 
 * The suggested level is the level of the recipient = otherPlayer. For instance if a offer to become 
 * a vassal is sent, the suggested level i LORD, because the otherPlayer would become Lord.
 * 
 * @author WMPABOD
 */

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "DIPLOMACY_OFFER")
public class DiplomacyOffer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_PLAYER")
	private Player player;

	@ManyToOne
	@JoinColumn(name = "FK_ORDERS")
	private Orders orders;

	private String thePlayerName;
	private String otherPlayerName;
	private DiplomacyLevel suggestedLevel;
	private boolean offerPerformed;
	
	public DiplomacyOffer(Player thePlayer, Player otherPlayer, DiplomacyLevel suggestedLevel){
		this.thePlayerName = thePlayer.getName();
		this.otherPlayerName = otherPlayer.getName();
		this.suggestedLevel = suggestedLevel;
	}

	public boolean isPlayerAndLevel(Player anOtherPlayer, DiplomacyLevel aNewLevel){
		Logger.fine("otherPlayerName: " + otherPlayerName + " anOtherPlayer.getName(): " + anOtherPlayer.getName());
		boolean retVal = false;
		if (otherPlayerName.equalsIgnoreCase(anOtherPlayer.getName()) & (suggestedLevel == aNewLevel)){
			retVal = true;
		}
		return retVal;
	}
	
	public boolean isLevel(DiplomacyLevel aLevel){
		return aLevel == suggestedLevel;
	}
	
	/**
	 * Find if one of the players is aPlayer
	 * @param aPlayer
	 * @return
	 */
	public boolean isOtherPlayer(Player aPlayer){
		boolean isOtherPlayer = false;
		if (aPlayer.getName().equalsIgnoreCase(otherPlayerName)){
			isOtherPlayer = true;
		}else{
			if (aPlayer.getName().equalsIgnoreCase(thePlayerName)){
				isOtherPlayer = true;
			}
		}
		return isOtherPlayer;
	}
	
	public Player getThePlayer(Galaxy aGalaxy){
		return aGalaxy.getPlayer(thePlayerName);
	}

	public Player getOtherPlayer(Galaxy aGalaxy){
		return aGalaxy.getPlayer(otherPlayerName);
	}

	public DiplomacyLevel getSuggestedLevel(){
		return suggestedLevel;
	}

	public boolean isOfferPerformed() {
		return offerPerformed;
	}

	public void setOfferPerformed(boolean offerPerformed) {
		this.offerPerformed = offerPerformed;
	}

	public String getOtherPlayerName() {
		return otherPlayerName;
	}

	public String getThePlayerName() {
		return thePlayerName;
	}
	
}

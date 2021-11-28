//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;

import spaceraze.util.general.Logger;

//TODO 2020-11-25 Change this to an entity and save in the database, guess messages should be a stand alone application.
public class Message implements Serializable,Comparable<Message> {
  static final long serialVersionUID = 1L;
  private String content,type;
  private String recipientFaction;
  private String recipientPlayer,sender;
  private int turn;
  private String owner; // owner of this message (used on server)
  private boolean read = false;
  private int uniqueId;
  
  public Message(String content, Object recipient, Player sender) {
    Logger.finest("new Message, recipient=" + recipient);
    this.sender = sender.getName();
    this.turn = sender.getGalaxy().getTurn();
    this.content = content;
    if (recipient == null){ // meddelandet ska till alla
      type = "all";
    }else
    if (recipient instanceof Player){ // meddelandet ska till en separat spelare
      recipientPlayer = ((Player)recipient).getName();
      type = "private";
    }else{ // meddelandet skall till alla i en Faction
      recipientFaction = ((Faction)recipient).getName();
      type = "faction";
    }
  }
  

  /**
   * Cloning
   * @param aMessage
   */
  public Message(Message aMessage, int uniqueIdCounter) {
	  this.sender = aMessage.sender;
	  this.content = aMessage.content;
	  this.type = aMessage.type;
	  this.recipientPlayer = aMessage.recipientPlayer;
	  this.recipientFaction = aMessage.recipientFaction;
	  this.turn = aMessage.turn;
	  this.uniqueId = uniqueIdCounter;
  }

  public String getContent(){
    return content;
  }

  public String getType(){
    return type;
  }

  public String getRecipientFaction(){
    return recipientFaction;
  }

  public Player getRecipientPlayer(Galaxy aGalaxy){
    return aGalaxy.getPlayer(recipientPlayer);
  }

  public String getRecipientPlayer(){
	  return recipientPlayer;
  }

  public Player getSender(Galaxy aGalaxy){
    return aGalaxy.getPlayer(sender);
  }
  
  public int getTurn() {
	  return turn;
  }
  
  public void setTurn(int turn) {
	  this.turn = turn;
  }

  public int compareTo(Message tmpMessage) {
	  return tmpMessage.getTurn() - turn;
  }



	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean isOwner(String playerName){
		return (playerName.equals(owner));
	}

	public boolean isRead() {
		return read;
	}



	public void setRead(boolean read) {
		this.read = read;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public boolean isSender(String playerName){
		return (playerName.equals(sender));
	}

	public String getSender() {
		return sender;
	}
	
	
}
//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;

import spaceraze.util.general.Logger;

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
  
  public String getRecipientString(Galaxy aGalaxy) {
    String recieverString = "";
    if (type.equalsIgnoreCase("faction")){ // meddelandet skall till alla i en Faction
      recieverString = "Faction: " + recipientFaction;
    }else
    if (type.equalsIgnoreCase("private")){ // meddelandet ska till en separat spelare
      recieverString = "Govenor: " + aGalaxy.getPlayer(recipientPlayer).getGovernorName() + " (" +aGalaxy.getPlayer(recipientPlayer).getFaction().getName() + ")";
    }else
    if (type.equalsIgnoreCase("all")){ // meddelandet ska till en separat spelare
      recieverString = "Public message";
    }
    return recieverString;
  }

  public String getRecipientString2(Galaxy aGalaxy) {
	  String recieverString = "";
	  if (type.equalsIgnoreCase("faction")){ // meddelandet skall till alla i en Faction
		  recieverString = recipientFaction + " faction";
	  }else
		  if (type.equalsIgnoreCase("private")){ // meddelandet ska till en separat spelare
			  recieverString = "Govenor " + aGalaxy.getPlayer(recipientPlayer).getGovernorName() + " (" +aGalaxy.getPlayer(recipientPlayer).getFaction().getName() + ")";
		  }else
			  if (type.equalsIgnoreCase("all")){ // meddelandet ska till en separat spelare
				  recieverString = "all governors";
			  }
	  return recieverString;
  }

  public boolean haveSameReciever(Message aMessage, Galaxy aGalaxy){
    boolean same = false;
    if ((recipientFaction != null) & aMessage.getRecipientFaction().equals(recipientFaction)){
      same = true;
    }else
    if ((recipientPlayer != null) & aMessage.getRecipientPlayer(aGalaxy).getName().equals(recipientPlayer)){
      same = true;
    }else
    if ((aMessage.getRecipientFaction() == null) & (aMessage.getRecipientPlayer(aGalaxy) == null) & (recipientFaction == null) & (recipientPlayer == null)){
      same = true;
    }
    return same;
  }

  public boolean haveReciever(Object recipient, Galaxy aGalaxy){
	Logger.finest( "haveReciever: ");
    boolean same = false;
    if ((recipient == null) & (recipientFaction == null) & (recipientPlayer == null)){
      same = true;
    }else
    if (recipient instanceof Faction){
      if ((recipientFaction != null) & (((Faction)recipient).getName().equalsIgnoreCase(recipientFaction))){
        same = true;
      }
    }else
    if (recipient instanceof Player){
      if ((recipientPlayer != null) & (((Player)recipient) == aGalaxy.getPlayer(recipientPlayer))){
        same = true;
      }
    }
    return same;
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
  
  public String getRecievedMessageListString(Galaxy aGalaxy){
	  StringBuffer strBuff = new StringBuffer();
	  if(!read){
		  strBuff.append("NEW "); 
	  }
	  strBuff.append("Turn " + turn);
	  if (aGalaxy.getPlayer(sender) != null){
		  strBuff.append(" from Governor " + aGalaxy.getPlayer(sender).getGovernorName());
	  }else{
		  strBuff.append(" from " + sender);
	  }
	  strBuff.append(" to " + getRecipientString2(aGalaxy));
	  strBuff.append(": " + content);
	  return strBuff.toString();
  }

  public String getSentMessageListString(Galaxy aGalaxy){
	  StringBuffer strBuff = new StringBuffer();
	  strBuff.append("Turn " + turn);
	  strBuff.append(" to " + getRecipientString2(aGalaxy));
	  strBuff.append(": " + content);
	  return strBuff.toString();
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
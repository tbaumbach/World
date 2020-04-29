//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.BlackMarketBid;
import spaceraze.world.BlackMarketOffer;
import spaceraze.world.Galaxy;
import spaceraze.world.UniqueIdCounter;
 
public class BlackMarket implements Serializable {
  static final long serialVersionUID = 1L;
  List<BlackMarketOffer> currentOffers;
  UniqueIdCounter uic;

  public BlackMarket(UniqueIdCounter uic) {
    currentOffers = new ArrayList<>();
    this.uic = uic;
  }

  // skapa nya offers
  public void newTurn(Galaxy g){
    int nrOffers = Functions.getRandomInt(1,2);
    int nrPlayers = g.getNrActivePlayers();
    if (nrPlayers == 2){ 
    	int tmpRdm = Functions.getRandomInt(1,4);
    	if (tmpRdm == 4){ // 25% of 2 offers
    		nrOffers = 2;
    	}else{ // otherwise 1 offer
    		nrOffers = 1;
    	}
    }else if (nrPlayers > 5){ // in big games, can be up to 3 offers
    	nrOffers = Functions.getRandomInt(1,3);
    }else{ // otherwise 1-2 offers
    	nrOffers = Functions.getRandomInt(1,2);
    }
    for (int i = 0; i < nrOffers; i++){
      BlackMarketOffer tempOffer = new BlackMarketOffer(g,uic.getUniqueId());
      currentOffers.add(tempOffer);
      g.addBlackMarketMessages(null,"New item for sale: a " + tempOffer.getString() + " is for sale at the Black Market.");
    }
  }

  public void addBlackMarketBid(BlackMarketBid aBid){
	  Logger.finer("BlackMarketBid: " + aBid.getOfferUniqueId());
	  BlackMarketOffer aOffer = findBlackMarketOffer(aBid.getOfferUniqueId());
	  if(aOffer != null){
		  aOffer.addBid(aBid);
	  }
	  
  }

  public BlackMarketOffer findBlackMarketOffer(int aUniqueId){
    BlackMarketOffer found = null;
    int i = 0;
    while ((i < currentOffers.size()) & (found == null)){
      BlackMarketOffer tempOffer = (BlackMarketOffer)currentOffers.get(i);
      if (tempOffer.getUniqueId() == aUniqueId){
        found = tempOffer;
      }else{
        i++;
      }
    }
    return found;
  }

  public List<BlackMarketOffer> getCurrentOffers(){
    return currentOffers;
  }

}

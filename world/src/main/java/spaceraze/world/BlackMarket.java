//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import spaceraze.util.general.Logger;
 
public class BlackMarket implements Serializable {
  static final long serialVersionUID = 1L;
  List<BlackMarketOffer> currentOffers;
  UniqueIdCounter uic;

  public BlackMarket(UniqueIdCounter uic) {
    currentOffers = new ArrayList<>();
    this.uic = uic;
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

    public UniqueIdCounter getUic() {
        return uic;
    }
}

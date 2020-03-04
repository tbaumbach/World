//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import spaceraze.world.comparator.TroopTypeAndBuildCostComparator;
import spaceraze.world.enums.HighlightType;

/**
 * 
 * @author WMPABOD
 *
 * All information about a turn is stored in the infoStrings.
 * The information that should be highlighted for the player is also added to 
 * the highlights list.
 * Lost and destroyed ships are also added in a separate list
 */
public class Report implements Serializable{
  static final long serialVersionUID = 1L;
  // All information about last turn is stored in this vector
  List<String> infoStrings;
  // important information from last turn will also be stored in this vector
  List<Highlight> highlights;
  List<Spaceship> shipsLostInSpace;
  List<Troop> troopsLostInSpace;
  
  public Report(){
    infoStrings = new LinkedList<String>();
    highlights = new LinkedList<Highlight>();
    shipsLostInSpace = new LinkedList<Spaceship>();
    troopsLostInSpace = new LinkedList<Troop>();
  }

  public void addReport(String str){
	  infoStrings.add(str);
  }

  public void addReportAt(String str,int index){
	  infoStrings.add(index,str);
  }

  public void addHighlight(String str, HighlightType type){
	  highlights.add(new Highlight(str,type));
  }

  public void addShipLostInSpace(Spaceship ss){
	  shipsLostInSpace.add(ss);
  }

  public void addTroopLostInSpace(Troop aTroop){
	  troopsLostInSpace.add(aTroop);
  }

  /**
   * Returns a sorted list of highlight objects
   * @return
   */
  public List<Highlight> getHighlights(){
  	// sort the list first
  	Collections.sort(highlights);
  	return highlights;
  }
  
  public List<CanBeLostInSpace> getLostShips() {
  	Collections.sort(shipsLostInSpace);
  	List<CanBeLostInSpace> allLostInSpace = new LinkedList<CanBeLostInSpace>();
  	allLostInSpace.addAll(shipsLostInSpace);
  	return allLostInSpace;
  }
  
  public List<CanBeLostInSpace> getLostTroops(){
  	Collections.sort(troopsLostInSpace, new TroopTypeAndBuildCostComparator());
  	List<CanBeLostInSpace> allLostInSpace = new LinkedList<CanBeLostInSpace>();
  	allLostInSpace.addAll(troopsLostInSpace);
  	return allLostInSpace;
  }

  public List<CanBeLostInSpace> getLostInSpace(){
  	// sort the list first
  	Collections.sort(shipsLostInSpace);
  	Collections.sort(troopsLostInSpace, new TroopTypeAndBuildCostComparator());
  	// add the lists together
  	List<CanBeLostInSpace> allLostInSpace = new LinkedList<CanBeLostInSpace>();
  	for (Spaceship aShip : shipsLostInSpace) {
		allLostInSpace.add(aShip);
	}
  	for (Troop aTroop : troopsLostInSpace) {
		allLostInSpace.add(aTroop);
	}
  	return allLostInSpace;
  }

  public List<String> getAllReports(){
    return infoStrings;
  }

  public int size(){
    return infoStrings.size();
  }

  public String getInfoAt(int nr){
    return infoStrings.get(nr);
  }

  public int findInfoIndex(String findString){
    int found = -1;
    int index = 0;
    while ((found == -1) & (index < infoStrings.size())){
      String tempString = infoStrings.get(index);
      if (tempString.equalsIgnoreCase(findString)){
        found = index;
      }else{
        index++;
      }
    }
    return found;
  }
}
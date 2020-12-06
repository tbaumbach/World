package spaceraze.world.diplomacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.GameWorld;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import spaceraze.world.enums.DiplomacyGameType;

/**
 * Handles all diplomacy in a game
 * @author bodinp
 *
 */
public class Diplomacy implements Serializable{
	private static final long serialVersionUID = 1L;
    private List<DiplomacyState> diplomacyStates; // current states between all players
    private DiplomacyGameType diplomacyGameType;
    private Galaxy g;

    public Diplomacy(Galaxy g){
    	this.g = g;
        diplomacyStates = new ArrayList<DiplomacyState>();
    }
}

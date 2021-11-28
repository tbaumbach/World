package spaceraze.world.diplomacy;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.world.Faction;

import javax.persistence.*;

/**
 * Handles diplomacy in gameWorld and games
 */

@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public  abstract class DiplomacyRelation implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_FACTION_ONE", insertable = false, updatable = false)
	private Faction faction1;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_FACTION_TWO", insertable = false, updatable = false)
	private Faction faction2;

	private DiplomacyLevel highestRelation;
	private DiplomacyLevel startRelation;
	private DiplomacyLevel lowestRelation;
	
	/**
	 * Create a default relation between two factions
	 * @param aFaction1 a faction
	 * @param aFaction2 another faction (can be same as faction1)
	 */
	public DiplomacyRelation(Faction aFaction1, Faction aFaction2){
		faction1 = aFaction1;
		faction2 = aFaction2;
		highestRelation = DiplomacyLevel.CONFEDERACY;
		lowestRelation = DiplomacyLevel.ETERNAL_WAR;
		if (faction1 == faction2){
			startRelation = DiplomacyLevel.PEACE;
		}else{
			startRelation = DiplomacyLevel.WAR;
		}
	}
	
	@Override
	public DiplomacyRelation clone(){
		DiplomacyRelation dr = null;
		try {
			dr = (DiplomacyRelation)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return dr;
	}
	
	@Override
	public String toString(){
		return "DiplomacyRelation (" + faction1.getName() + "-" + faction2.getName() + "), highest: " + highestRelation + ", lowest: " + lowestRelation + ", start: " + startRelation;
	}
	
	public boolean isRelation(Faction aFaction1,Faction aFaction2){
		boolean found = false;
		if ((faction1 == aFaction1) & (faction2 == aFaction2)){
			found = true;
		}else
		if ((faction1 == aFaction2) & (faction2 == aFaction1)){
			found = true;
		}
		return found;
	}

	public DiplomacyLevel getHighestRelation() {
		return highestRelation;
	}

	public void setHighestRelation(DiplomacyLevel highestRelation) {
		this.highestRelation = highestRelation;
	}

	public DiplomacyLevel getLowestRelation() {
		return lowestRelation;
	}

	public void setLowestRelation(DiplomacyLevel lowestRelation) {
		this.lowestRelation = lowestRelation;
	}

	public DiplomacyLevel getStartRelation() {
		return startRelation;
	}

	public void setStartRelation(DiplomacyLevel startRelation) {
		this.startRelation = startRelation;
	}


	@JsonProperty("faction1")
	public String getFaction1Name(){
		return faction1.getName();
	}

	@JsonProperty("faction2")
	public String getFaction2Name(){
		return faction2.getName();
	}
		
}

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

	private String factionOne;
	private String factionTwo;

	private DiplomacyLevel highestRelation;
	private DiplomacyLevel startRelation;
	private DiplomacyLevel lowestRelation;
	
	/**
	 * Create a default relation between two factions
	 * @param factionOne a faction
	 * @param factionTwo another faction (can be same as faction1)
	 */
	public DiplomacyRelation(String factionOne, String factionTwo){
		this.factionOne = factionOne;
		this.factionTwo = factionTwo;
		highestRelation = DiplomacyLevel.CONFEDERACY;
		lowestRelation = DiplomacyLevel.ETERNAL_WAR;
		if (this.factionOne.equals(this.factionTwo)){
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
		return "DiplomacyRelation (" + factionOne + "-" + factionTwo + "), highest: " + highestRelation + ", lowest: " + lowestRelation + ", start: " + startRelation;
	}
	
	public boolean isRelation(Faction aFaction1,Faction aFaction2){
		return (factionOne.equals(aFaction1.getUuid()) && factionTwo.equals(aFaction2.getUuid())) ||
				(factionOne.equals(aFaction2.getUuid()) && factionTwo.equals(aFaction1.getUuid()));
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

		
}

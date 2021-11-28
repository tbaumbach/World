package spaceraze.world.diplomacy;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Galaxy;
import spaceraze.world.Player;
import spaceraze.world.enums.DiplomacyGameType;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "DIPLOMACY_STATE")
public class DiplomacyState implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_GALAXY")
	private Galaxy galaxy;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLAYER_ONE", insertable = false, updatable = false)
	private Player player1;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLAYER_TWO", insertable = false, updatable = false)
	private Player player2;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PLAYER_LORD", insertable = false, updatable = false)
	private Player lord;

	private DiplomacyLevel currentLevel;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_GAME_DIPLOMACY_RELATION")
	private GameDiplomacyRelation relation;

	private boolean changedThisTurn;
	
	public DiplomacyState(GameDiplomacyRelation aRelation, Player aPlayer1, Player aPlayer2){
		relation = aRelation;
		player1 = aPlayer1;
		player2 = aPlayer2;
		currentLevel = relation.getStartRelation();
	}
	
	public DiplomacyLevel getStartLevel(){
		return currentLevel;
	}
	
	public DiplomacyLevel getNextLowerLevel(){
		return currentLevel.getNextLowerLevel();
	}
	
	public void setCurrentLevel(DiplomacyLevel newLevel){
		currentLevel = newLevel;
	}
	
	public Player getOtherPlayer(Player aPlayer){
		Player otherPlayer = player1;
		if (aPlayer == player1){
			otherPlayer = player2;
		}
		return otherPlayer;
	}
	
	public boolean isPlayers(Player aPlayer1,Player aPlayer2){
		boolean found = false;
		if ((player1 == aPlayer1) & (player2 == aPlayer2)){
			found = true;
		}else
		if ((player1 == aPlayer2) & (player2 == aPlayer1)){
			found = true;
		}
		return found;
	}

	public boolean isPlayer(Player aPlayer){
		boolean found = false;
		if ((player1 == aPlayer) | (player2 == aPlayer)){
			found = true;
		}
		return found;
	}

	@Override
	public String toString(){
		String retVal = "Diplomacy state: " + player1.getName() + " vs " + player2.getName() + ", current: " + currentLevel.toString() + ", relation: " + relation;
		if (lord != null){
			retVal += ", Lord: " + lord.getName();
		}
		return retVal;
	}
	
	public void modifyDueToGameType(DiplomacyGameType diplomacyGameType){
		if (diplomacyGameType == DiplomacyGameType.GAMEWORLD){
			// do nothing, kepp all settings
		}else
		if (diplomacyGameType == DiplomacyGameType.OPEN){
			relation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
			relation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);
			if (relation.getFaction1() == relation.getFaction2()){
				relation.setStartRelation(DiplomacyLevel.PEACE);
				currentLevel = DiplomacyLevel.PEACE;
			}else{
				relation.setStartRelation(DiplomacyLevel.CEASE_FIRE);
				currentLevel = DiplomacyLevel.CEASE_FIRE;
			}
		}else
		if (diplomacyGameType == DiplomacyGameType.FACTION){
			if (relation.getFaction1() == relation.getFaction2()){
				relation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
				relation.setLowestRelation(DiplomacyLevel.CONFEDERACY);
				relation.setStartRelation(DiplomacyLevel.CONFEDERACY);
				currentLevel = DiplomacyLevel.CONFEDERACY;
			}else{
				relation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
				relation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);
				relation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);
				currentLevel = DiplomacyLevel.ETERNAL_WAR;
			}
		}else
		if (diplomacyGameType == DiplomacyGameType.DEATHMATCH){
			relation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
			relation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);
			relation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);
			currentLevel = DiplomacyLevel.ETERNAL_WAR;
		}
	}

	public boolean isChangedThisTurn() {
		return changedThisTurn;
	}

	public void setChangedThisTurn(boolean changedThisTurn) {
		this.changedThisTurn = changedThisTurn;
	}

	public Player getLord() {
		return lord;
	}

	public void setLord(Player lord) {
		this.lord = lord;
	}

	public DiplomacyLevel getCurrentLevel() {
		return currentLevel;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
}

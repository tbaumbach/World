package spaceraze.world.report.spacebattle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.util.general.Logger;
import spaceraze.world.report.EventReport;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "SPACE_BATTLE_ATTACK")
public class SpaceBattleAttack extends EventReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "FK_SPACE_BATTLE_REPORT")
	private SpaceBattleReport spaceBattleReport;

	@OneToOne
	@JoinColumn(name = "FK_SPACESHIP_ATTACK")
	private SpaceshipAttack spaceshipAttack;

	@OneToOne
	@JoinColumn(name = "FK_SPACESHIP_TARGET")
    private SpaceshipTarget spaceshipTarget;

	private int damageNoArmor;
	private int actualDamage;
	private int damageLeftAfterShields;
	private boolean wantsToRetreat;
	
	public static String YOU_COULD_NOT_RETRET = "Your %s could not retreat due to an enemy ship that stops retreats.";
	public static String ENEMY_COULD_NOT_RETRET = "An enemy %s was stopped by one of your ships that stops retreats, when trying to flee, and forced to fight instead.";
	public static String YOUR_SHIP_SCUTTLED = "Your %s has been scuttled by its own crew, because there was nowhere they could run to.";
	public static String ENEMY_SHIP_SCUTTLED = "An enemy %s has been scuttled by its own crew instead of fighting us.";
	public static String YOUR_SHIP_RETRETS =  "Your %s has run away, and is heading for %s.";
	public static String ENEMY_SHIP_RETRETS =  "An enemy %s has run away from the battle.";
	public static String OWN_SHIP_DESTROYED =  "Your %s was destroyed when hit (%d) by an enemy %s.";
	public static String OWN_SHIP_WAS_HIT =  "Your %s was hit by an enemy %s and the damage (%d) %s.";
	public static String ENEMY_SHIP_DESTROYED =  "Your %s hit (%d) and destroyed an enemy %s.";
	public static String ENEMY_SHIP_WAS_HIT =  "Your %s hit an enemy %s and the damage (%d) %s";
	public static String HIT_ABSORBED_BY_SHIELD = "was absorbed by the shields (shield strength: %d%%).";
	public static String HIT_HULL = "damaged the ship (hull strength: %d%%).";
	public static String HIT_KILLED = "cdc: %d, adam: %d, damcap: %d";

	@Override
	public List<EventReport> getChildReports() {
		return Collections.emptyList();
	}

	@Override
	public String getReport() {
		StringBuilder builder = new StringBuilder();
		if (wantsToRetreat){
			retreats(builder);
		}
		if (spaceshipTarget != null){ // if ship have retreated or been scuttled, there are no target ship
			fire(builder);
		}
		return builder.toString();
	}

	@Override
	public EventReport getParent() {
		return spaceBattleReport;
	}

	private void retreats(StringBuilder builder) {
		if (spaceshipTarget != null){ // ship could not retreat and fired instead
			if (spaceshipAttack.isOwn()){
				builder.append(String.format(YOU_COULD_NOT_RETRET, spaceshipAttack.getSpaceshipName()));
			}else{
				builder.append(String.format(ENEMY_COULD_NOT_RETRET, spaceshipAttack.getSpaceshipName()));
			}
		}else{
			if (spaceshipAttack.isScuttled()){ // ship selfdestructed
				if (spaceshipAttack.isOwn()){
					builder.append(String.format(YOUR_SHIP_SCUTTLED, spaceshipAttack.getSpaceshipName()));
				}else{
					builder.append(String.format(ENEMY_SHIP_SCUTTLED, spaceshipAttack.getSpaceshipName()));
				}
			}else{ // ship retreated
				if (spaceshipAttack.isOwn()){
					Logger.fine("attackingShip: " + spaceshipAttack);
					Logger.fine("attackingShip.getRetreatingTo(): " + spaceshipAttack.getRetreatsToPlanetName());
					builder.append(String.format(YOUR_SHIP_RETRETS, spaceshipAttack.getSpaceshipName(), spaceshipAttack.getRetreatsToPlanetName()));
				}else{
					builder.append(String.format(ENEMY_SHIP_RETRETS, spaceshipAttack.getSpaceshipName()));
				}
			}
		}
	}
	
	private void fire(StringBuilder builder){
		if (spaceshipTarget.isOwn()){ // own ship hit
			if (spaceshipTarget.getCurrentDamageCapacity() <= 0){ // own ship destroyed
				builder.append(String.format(OWN_SHIP_DESTROYED, spaceshipTarget.getSpaceshipName(), damageNoArmor, spaceshipAttack.getSpaceshipName()));
			}else{ // own ship damaged
				builder.append(String.format(OWN_SHIP_WAS_HIT, spaceshipTarget.getSpaceshipName(), spaceshipAttack.getSpaceshipName(), damageNoArmor, getDamageStatus()));
			}
		}else{ // enemy ship hit
			if (spaceshipTarget.getCurrentDamageCapacity() <= 0){ // enemy ship destroyed
				builder.append(String.format(ENEMY_SHIP_DESTROYED, spaceshipAttack.getSpaceshipName(), damageNoArmor, spaceshipTarget.getSpaceshipName()));
			}else{
				builder.append(String.format(ENEMY_SHIP_WAS_HIT, spaceshipAttack.getSpaceshipName(), spaceshipTarget.getSpaceshipName(), damageNoArmor, getDamageStatus()));
			}
		}
	}

	private String getDamageStatus(){
		int currentShields = spaceshipTarget.getCurrentShield();
		//TODO 2019-12-08 Debugga detta i strid, tror shieldsIncKillsFactor är fel då spaceshipTarget.getShields() ger shields med killFactor, d.v.s. detta ger dubbelt upp 20% i ställer för 10%
		int currentdc = spaceshipTarget.getCurrentDamageCapacity();
		int damagecapacity = spaceshipTarget.getDamageCapacity();
		if (damageLeftAfterShields == 0) { // all damage was absorbed by the shields
			int shieldStrength = (int) Math.round((100.0 * currentShields) / spaceshipTarget.getShield());
			return String.format(HIT_ABSORBED_BY_SHIELD, shieldStrength);
		} else {
			if (currentdc > 0) {
				int hullStrength = (int) Math.round((100.0 * currentdc)	/ damagecapacity);
				return String.format(HIT_HULL, hullStrength);
				
			}else{
				//TODO 2019-12-17 Vad är det här? Är det en typ av debugg? Skeppet har blivit förstört?
				return String.format(HIT_KILLED, currentdc, actualDamage, damagecapacity); 
			}
		}
	}

	public SpaceshipAttack getSpaceshipAttack() {
		return spaceshipAttack;
	}

	public void setSpaceshipAttack(SpaceshipAttack spaceshipAttack) {
		this.spaceshipAttack = spaceshipAttack;
	}

	public SpaceshipTarget getSpaceshipTarget() {
		return spaceshipTarget;
	}

	public void setSpaceshipTarget(SpaceshipTarget spaceshipTarget) {
		this.spaceshipTarget = spaceshipTarget;
	}

	public int getDamageNoArmor() {
		return damageNoArmor;
	}

	public void setDamageNoArmor(int damageNoArmor) {
		this.damageNoArmor = damageNoArmor;
	}

	public int getActualDamage() {
		return actualDamage;
	}

	public void setActualDamage(int actualDamage) {
		this.actualDamage = actualDamage;
	}

	public int getDamageLeftAfterShields() {
		return damageLeftAfterShields;
	}

	public void setDamageLeftAfterShields(int damageLeftAfterShields) {
		this.damageLeftAfterShields = damageLeftAfterShields;
	}

	public boolean isWantsToRetreat() {
		return wantsToRetreat;
	}

	public void setWantsToRetreat(boolean wantsToRetreat) {
		this.wantsToRetreat = wantsToRetreat;
	}
}

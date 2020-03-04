package spaceraze.world.report.landbattle;

import java.io.Serializable;

import spaceraze.util.general.Functions;
import spaceraze.world.report.EventReport;

public class LandBattleAttack extends EventReport implements Serializable {
	static final long serialVersionUID = 1L;
	
	private TroopAttack troopAttack;
	private TroopTarget troopTarget;
	private int damage;
	private int counterDamage;
	private int attMultiplier;
	private int counterMultiplier = 0;
	
	
	public static String VERY_WEEK = "very weak";
	public static String WEEK = "weak";
	public static String GOOD = "good";
	public static String EXCELLENT = "excellent";
	
	public static String TROOP = "troop";
	public static String ARTILLERY = "artillery";
	//Owner is the attacker
	public static String OWN_ATTACK = "Your %s %s have made %s %s attack (%d damage) against an enemy %s (%s)"; //First %s is TROOP or ARTILLERY. included destroyed or " (" + targetTroop.getTroopStrength() + "%).\n"
	public static String ENEMY_COUNTERFIRE = "   The enemy perform %s counterfire (%s damage) against your troop  (%s)"; // included destroyed or (" + attackingTroop.getTroopStrength() + "%)
	//public static String ENEMY_NO_COUNTERFIRE = "   The enemy troop does not procuce any significant counterfire."; // Should we write this out? talky talky...
	//Enemy is the attacker
	public static String ENEMY_ATTACK = "Enemy %s %s have made %s %s attack (%d damage) against your %s %s"; // include (destroyed) or " (" + targetTroop.getTroopStrength() + "%).\n"
	//public static String ENEMY_ARTILLERY_ATTACK = "An enemy artillery troop %s have made %s %s attack (%d damage) against your %s %s "; // include " (destroyed).\n" or " (" + targetTroop.getTroopStrength() + "%).\n"
	public static String OWN_COUNTERFIRE = "   You perform %s counterfire (%d damage) against the enemy troop (%s)"; // include " (destroyed).\n" or " (" + attackingTroop.getTroopStrength() + "%).\n"
	//public static String OWN_NO_COUNTERFIRE = "   You does not procuce any significant counterfire.";
	public static String TARGET_DESTROYED = "destroyed";
	
	public LandBattleAttack() {
		
	}
	
	public LandBattleAttack(TroopAttack troopAttack, TroopTarget troopTarget, int damage, int counterDamage, int attMultiplier, int counterMultiplier) {
		this.troopAttack = troopAttack;
		this.troopTarget = troopTarget;
		this.damage = damage;
		this.counterDamage = counterDamage;
		this.attMultiplier = attMultiplier;
		this.counterMultiplier = counterMultiplier;
	}
	
	/*
	public String getAsString(){
		StringBuffer sb = new StringBuffer();
		String attDesc = getAttackLevelDescription(attMultiplier);
		if (attacker){
			sb.append("Your artillery troop " + attackingTroop.getUniqueShortName() + " have made " + Functions.getDeterminedForm(attDesc) + " " + attDesc + " attack (" + attackerActualDamage + " damage) against an enemy " + targetTroop.getTroopType().getUniqueShortName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
		}else{
			sb.append("An enemy artillery troop " + attackingTroop.getTroopType().getUniqueShortName() + " have made " + Functions.getDeterminedForm(attDesc) + " " + attDesc + " attack (" + attackerActualDamage + " damage) against your " + targetTroop.getUniqueShortName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
		}
		return sb.toString();
	}
	
	public String getAsString(){
		StringBuffer sb = new StringBuffer();
		String attDesc = getAttackLevelDescription(attMultiplier);
		String defDesc = getAttackLevelDescription(defMultiplier);
		if (attacker){
			sb.append("Your troop " + attackingTroop.getUniqueName() + " have made " + Functions.getDeterminedForm(attDesc)+ " " + attDesc + " attack (" + attackerActualDamage + " damage) against an enemy " + targetTroop.getTroopType().getUniqueName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
			if (defMultiplier > 0){
				sb.append("   The enemy troop perform " + defDesc + " counterfire (" + defenderActualDamage + " damage) against your troop");
				if (attackingTroop.isDestroyed()){
					sb.append(" (destroyed).\n");
				}else{
					sb.append(" (" + attackingTroop.getTroopStrength() + "%).\n");
				}
			}else{
				sb.append("   The enemy troop does not procuce any significant counterfire.\n");
			}
		}else{
			sb.append("Enemy troop " + attackingTroop.getTroopType().getUniqueName() + " have made " + Functions.getDeterminedForm(attDesc)+ " " + attDesc + " attack (" + attackerActualDamage + " damage) against your " + targetTroop.getUniqueName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
			if (defMultiplier > 0){
				sb.append("   Your troop perform " + defDesc + " counterfire (" + defenderActualDamage + " damage) against the enemy troop");
				if (attackingTroop.isDestroyed()){
					sb.append(" (destroyed).\n");
				}else{
					sb.append(" (" + attackingTroop.getTroopStrength() + "%).\n");
				}
			}else{
				sb.append("   Your troop does not procuce any significant counterfire.\n");
			}
		}
		return sb.toString();
	}*/
	
	@Override
	public String getReport() {
		StringBuilder builder = new StringBuilder();
		attack(builder);
		counterAttack(builder);
		
		/*
		if (wantsToRetreat){
			retrets(buffer);
		}
		if (spaceshipTarget != null){ // if ship have retreated or been scuttled, there are no target ship
			fire(buffer);
		}*/
		return builder.toString();
	}
	
	private void attack(StringBuilder builder) {
		String attDesc = getAttackLevelDescription(attMultiplier);
		if(troopAttack instanceof OwnTroopAttack) {
			builder.append(String.format(OWN_ATTACK, troopAttack.isArtillery() ? ARTILLERY : TROOP, troopAttack.getName(), Functions.getDeterminedForm(attDesc), attDesc, damage, troopTarget.getName(), troopTarget.getCurrentDamageCapacity() < 1 ? TARGET_DESTROYED : getTargetTroopDamageCapacityAfterFight() + "%"));
		} else {
			builder.append(String.format(ENEMY_ATTACK, troopAttack.isArtillery() ? ARTILLERY : TROOP, troopAttack.getName(), Functions.getDeterminedForm(attDesc), attDesc, damage, troopTarget.getName(), troopTarget.getCurrentDamageCapacity() < 1 ? TARGET_DESTROYED : getTargetTroopDamageCapacityAfterFight() + "%"));
			
			
		}
	}
	
	private void counterAttack(StringBuilder builder) {
		if(counterMultiplier > 0) {
			String attDesc = getAttackLevelDescription(counterMultiplier);
			if(troopTarget instanceof OwnTroopTarget) {
				builder.append(String.format(OWN_COUNTERFIRE, attDesc, counterDamage, troopTarget.getName(), troopTarget.getCurrentDamageCapacity() < 1 ? TARGET_DESTROYED : getAttackerTroopDamageCapacityAfterFight() + "%"));
			}else {
				builder.append(String.format(ENEMY_COUNTERFIRE, attDesc, counterDamage, troopAttack.getName(), troopTarget.getCurrentDamageCapacity() < 1 ? TARGET_DESTROYED : getAttackerTroopDamageCapacityAfterFight() + "%"));
			}
		}
	}
	
	private String getTargetTroopDamageCapacityAfterFight() {
		return Integer.toString((int) Math.round((100.0 * troopTarget.getCurrentDamageCapacity() - damage)	/ troopTarget.getDamageCapacity()));
	}
	
	private String getAttackerTroopDamageCapacityAfterFight() {
		return Integer.toString((int) Math.round((100.0 * troopAttack.getCurrentDamageCapacity() - counterDamage)	/ troopAttack.getDamageCapacity()));
	}
	
	
	protected String getAttackLevelDescription(int aMultiplier){
		String desc = "";
		if (aMultiplier <= 5){
			desc = VERY_WEEK;
		}else
		if ((aMultiplier >= 6) & (aMultiplier <= 10)){
			desc = WEEK;
		}else
		if ((aMultiplier >= 11) & (aMultiplier <= 15)){
			desc = GOOD;
		}else
		if (aMultiplier >= 16){
			desc = EXCELLENT;
		}
		return desc;
	}

}

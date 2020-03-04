package spaceraze.world.report.landbattle;

import java.util.List;
import java.util.stream.Stream;
import spaceraze.world.enums.BattleGroupPosition;
import spaceraze.world.report.EventReport;


public class LandBattleReport extends EventReport {
	private static final long serialVersionUID = 1L;
	
	private final List<OwnTroop> ownTroops;
	private final List<EnemyTroop> enemyTroops;
	private final String enemyName;
	private final String enemyFaction;
	private final boolean defending;
	
	//public final static String START = "Land battle report for the planet %s";
	public final static String DEFENDING = "Your forces are defending";
	public final static String ATTACKING = "Your forces are attacking";
	public final static String BATTLE_AGAINST_NEUTRAL ="neutrals";
	public final static String FIRST_LINE = "First-line: %s";
	public final static String RESERVE = "Reserver: %s";
	public final static String FLANKERS = "Flankers: %s";
	public final static String SUPPORT = "Support: %s";
	public final static String OWN_FORCES = "Your forces are:";
	public final static String ENEMY_NEUTRAL = "Enemy forces owned by %s are:";
	public final static String ENEMY_FORCES = "Enemy forces owned by %s ( %s ) are:";
	public final static String BATTLE_LENGTH = "The battle lasted %d rounds.";
	public final static String OWN_SURVIVING_FORCE = "Own surviving forces are:";
	public final static String ENEMY_SURVIVING_FORCE = "Enemy surviving forces are:";
	public final static String TROOPS = "Troops: %s";
	public final static String OWN_LOST = "Own forces lost:";
	public final static String ENEMY_LOST = "Enemy forces destroyed:";
	public final static String NONE = "None";
	public final static String RESULET_BOTH_DESTROYED = "Both forces have been destroyed";
	public final static String BATTLE_RESULET_WON = "Battle won!";
	public final static String BATTLE_RESULET_BATTLE_LOST = "Battle lost!";
	public final static String BATTLE_RESULET_INCONCLUSIVE = "Battle was inconclusive, both sides still have forces on planet";
	
	
	public LandBattleReport(List<OwnTroop> ownTroops, List<EnemyTroop> enemyTroops, String enemyName,
			String enemyFaction, boolean defending) {
		this.ownTroops = ownTroops;
		this.enemyTroops = enemyTroops;
		this.enemyName = enemyName;
		this.enemyFaction = enemyFaction;
		this.defending = defending;
	}

	@Override
	public String getReport() {
		return createReport(false);
	}

	@Override
	public String getReports() {
		return createReport(true);
	}

	private String createReport(boolean includeChildes) {
		
		StringBuilder stringBuilderReport = new StringBuilder();
		
		getStart(stringBuilderReport);
		getOwnArmy(stringBuilderReport);
		getEnemyArmy(stringBuilderReport);
		if(includeChildes) {
			stringBuilderReport.append(getChildereports());
		}
		getNumberOfRounds(stringBuilderReport);
		getOwnPostBattleTroops(stringBuilderReport, OWN_SURVIVING_FORCE, false);
		getEnemyPostBattleTroops(stringBuilderReport, ENEMY_SURVIVING_FORCE, false);
		getOwnPostBattleTroops(stringBuilderReport, OWN_LOST, true);
		getEnemyPostBattleTroops(stringBuilderReport, ENEMY_LOST, true);
		getBattleResult(stringBuilderReport);
		return stringBuilderReport.toString();
	}
	
	private void getStart(StringBuilder report) {
		if(isDefending()) {
			report.append(DEFENDING);
		}else {
			report.append(ATTACKING);
		}
	}
	
	private void getOwnArmy(StringBuilder report) {
		report.append(OWN_FORCES);
		getOwnArmy(report, BattleGroupPosition.FIRST_LINE, FIRST_LINE);
		getOwnArmy(report, BattleGroupPosition.RESERVE, RESERVE);
		getOwnArmy(report, BattleGroupPosition.FLANKER, FLANKERS);
		getOwnArmy(report, BattleGroupPosition.SUPPORT, SUPPORT);
	}
	
	private void getEnemyArmy(StringBuilder report) {
		if (enemyName == null) {
			report.append(String.format(ENEMY_NEUTRAL, BATTLE_AGAINST_NEUTRAL));
		} else {
			report.append(String.format(ENEMY_FORCES, enemyName, enemyFaction));
		}
		getEnemyArmy(report, BattleGroupPosition.FIRST_LINE, FIRST_LINE);
		getEnemyArmy(report, BattleGroupPosition.RESERVE, RESERVE);
		getEnemyArmy(report, BattleGroupPosition.FLANKER, FLANKERS);
		getEnemyArmy(report, BattleGroupPosition.SUPPORT, SUPPORT);
	}
	
	private void getOwnArmy(StringBuilder report, BattleGroupPosition position, String line) {
		if(ownTroops.stream().anyMatch(troop -> position == troop.getPosition())) {
			report.append(String.format(line, getOwnTroopList(ownTroops.stream().filter(troop -> position == troop.getPosition()), false)));
		}
	}
	private void getEnemyArmy(StringBuilder report, BattleGroupPosition position, String line) {
		if(enemyTroops.stream().anyMatch(troop -> position == troop.getPosition())) {
			report.append(String.format(line, getEnemyTroopList(enemyTroops.stream().filter(troop -> position == troop.getPosition()), false)));
		}
	}
	
	private String getEnemyTroopList(Stream<EnemyTroop> troopList, boolean postBattle) {
		StringBuilder troops = new StringBuilder();
		troopList.forEach(troop -> {
			if (troops.length() == 0) {
				troops.append(troop.getType());
				troops.append(postBattle ? troop.getPostBattleHitpoints() != 0 ? "(" + troop.getPostBattleHitpoints() + ")": "" 
					: troop.getStartHitpoints() != 100 ? "(" + troop.getStartHitpoints() + ")" : "");
			} else {
				troops.append(", ").append(troop.getType());
				troops.append(postBattle ? troop.getPostBattleHitpoints() != 0 ? "(" + troop.getPostBattleHitpoints() + ")": "" 
					: troop.getStartHitpoints() != 100 ? "(" + troop.getStartHitpoints() + ")" : "");
			}
		});
		return troops.toString();
	}

	private String getOwnTroopList(Stream<OwnTroop> troopList, boolean postBattle) {
		StringBuilder troops = new StringBuilder();
		troopList.forEach(troop -> {
			if (troops.length() == 0) {
				troops.append(troop.getName());
				troops.append(postBattle ? troop.getPostBattleHitpoints() != 0 ? "(" + troop.getPostBattleHitpoints() + ")": "" 
					: troop.getStartHitpoints() != 100 ? "(" + troop.getStartHitpoints() + ")" : "");
			} else {
				troops.append(", ").append(troop.getName());
				troops.append(postBattle ? troop.getPostBattleHitpoints() != 0 ? "(" + troop.getPostBattleHitpoints() + ")": "" 
					: troop.getStartHitpoints() != 100 ? "(" + troop.getStartHitpoints() + ")" : "");
			}
		});
		return troops.toString();
	}
	
	private void getNumberOfRounds(StringBuilder report) {
		report.append(String.format(BATTLE_LENGTH, getChildeReports().size()));
	}
	
	private void getOwnPostBattleTroops(StringBuilder report, String troopStatus, boolean isDestroyed) {
		if(ownTroops.stream().anyMatch(troop -> troop.isDestroyed() == isDestroyed)){
			report.append(troopStatus);
			getOwnPostBattleTroops(report, BattleGroupPosition.FIRST_LINE, FIRST_LINE, isDestroyed);
			getOwnPostBattleTroops(report, BattleGroupPosition.RESERVE, RESERVE, isDestroyed);
			getOwnPostBattleTroops(report, BattleGroupPosition.FLANKER, FLANKERS, isDestroyed);
			getOwnPostBattleTroops(report, BattleGroupPosition.SUPPORT, SUPPORT, isDestroyed);
		}
	}
	
	private void getEnemyPostBattleTroops(StringBuilder report, String troopStatus, boolean isDestroyed) {
		if(enemyTroops.stream().anyMatch(troop -> troop.isDestroyed() == isDestroyed)) {
			report.append(troopStatus);
			getEnemyPostBattleTroops(report, BattleGroupPosition.FIRST_LINE, FIRST_LINE, isDestroyed);
			getEnemyPostBattleTroops(report, BattleGroupPosition.RESERVE, RESERVE, isDestroyed);
			getEnemyPostBattleTroops(report, BattleGroupPosition.FLANKER, FLANKERS, isDestroyed);
			getEnemyPostBattleTroops(report, BattleGroupPosition.SUPPORT, SUPPORT, isDestroyed);
		}
	}
	
	private void getOwnPostBattleTroops(StringBuilder report, BattleGroupPosition position, String line, boolean isDestroyed) {
		if(ownTroops.stream().filter(troop -> troop.isDestroyed() == isDestroyed).anyMatch(troop -> position == troop.getPosition())) {
			report.append(String.format(line, getOwnTroopList(ownTroops.stream().filter(troop -> troop.isDestroyed() == isDestroyed && position == troop.getPosition()), true)));
		}
	}
	
	private void getEnemyPostBattleTroops(StringBuilder report, BattleGroupPosition position, String line , boolean isDestroyed) {
		if(enemyTroops.stream().filter(troop -> troop.isDestroyed() == isDestroyed).anyMatch(troop -> position == troop.getPosition())) {
			report.append(String.format(line, getEnemyTroopList(enemyTroops.stream().filter(troop -> troop.isDestroyed() == isDestroyed && position == troop.getPosition()), true)));
		}
	}
	
	private void getBattleResult(StringBuilder report) {
		if(enemyTroops.stream().allMatch(troop -> troop.isDestroyed())) {
			if(ownTroops.stream().allMatch(troop -> troop.isDestroyed())) {
				report.append(RESULET_BOTH_DESTROYED);
			}else{
				report.append(BATTLE_RESULET_WON);
			}
		}else{
			if(ownTroops.stream().allMatch(troop -> troop.isDestroyed())) {
				report.append(BATTLE_RESULET_BATTLE_LOST);
			}else{
				report.append(BATTLE_RESULET_INCONCLUSIVE);
			}
		}
	}

	public List<OwnTroop> getOwnTroops() {
		return ownTroops;
	}

	public List<EnemyTroop> getEnemyTroops() {
		return enemyTroops;
	}

	public String getEnemyName() {
		return enemyName;
	}

	public String getEnemyFaction() {
		return enemyFaction;
	}
	
	public boolean isDefending() {
		return defending;
	}

}

package spaceraze.world.report.spacebattle;

import java.util.List;
import java.util.stream.Stream;

import spaceraze.world.report.EventReport;

public class SpaceBattleReport extends EventReport {

	private static final long serialVersionUID = 1L;
	private final List<OwnSpaceship> ownSpaceships;
	private final List<EnemySpaceship> enemySpaceships;
	private final String enemyName;
	private final String enemyFaction;

	public final static String BATTLE_AGAINST_ENEMY = "Your forces have engaged hostile forces from governor %s.";
	public final static String BATTLE_AGAINST_NEUTRAL = "Your forces have engaged neutral forces.";
	// 2019-12-22 public final static String START = "Your forces at %s have engaged
	// hostile forces from governor %s."; Possible need for general reports.
	public final static String OWN_FORCE = "Your initial forces consisted of: %s";
	public final static String OWN_FIRST_LINE = "Your initial first line forces consisted of: %s";
	public final static String OWN_SCREEND_LINE = "Your initial screened forces consisted of: %s";
	public final static String ENEMY_NEUTRAL_FORCE = "%s (neutral) initial forces consisted of: %s";
	public final static String ENEMY_FORCE = "Enemy initial forces consisted of: %s";
	public final static String ENEMY_FIRST_LINE = "Enemy initial first line forces consisted of: %s";
	public final static String ENEMY_SCREEND_LINE = "Initial screened forces consisted of: %s";
	public final static String ENEMY_DESTROYED = "The last of your opponents ships has been destroyed.";
	public final static String ENEMY_RETRETS = "The last of your opponents ships has run away like cowards.";
	public final static String OWN_DESTROYED = "The last of your ships has been destroyed.";
	public final static String OWN_RETRETS = "The last of your ships has made a tactical retreat.";
	public final static String BATTLE_DURATION = "The battle lasted %d rounds.";
	public final static String WIN = "The last of your ships has made a tactical retreat.";
	public final static String LOST = "The last of your ships has made a tactical retreat.";
	public final static String OWN_LOST_SHIPS = "You lost the following ships in the battle: ";
	public final static String OWN_NO_LOST = "You lost no ships in the battle";
	public final static String ENEMY_LOST_SHIPS = "Your opponent lost the following ships in the battle: ";
	public final static String ENEMY_NO_LOST = "Your opponent lost no ships in the battle.";
	public final static String ENEMY_RETRETING_SHIP = "The following of your opponents ships fled the battle: ";
	public final static String OWN_RETRETING_SHIP = "The following of your ships retreated from the battle: ";
	public final static String OWN_POST_BATTLE_SHIP_STATUS = "Damage status of your surviving ships: ";
	public final static String SHIP_POST_BATTLE_STATUS = "%s hull: %d%%";

	public SpaceBattleReport(List<OwnSpaceship> ownSpaceships, List<EnemySpaceship> enemySpaceships, String enemyName,
			String enemyFaction) {
		this.ownSpaceships = ownSpaceships;
		this.enemySpaceships = enemySpaceships;
		this.enemyName = enemyName;
		this.enemyFaction = enemyFaction;
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
		getOwnFleet(stringBuilderReport);
		getEnemyFleet(stringBuilderReport);
		if(includeChildes) {
			stringBuilderReport.append(getChildereports());
		}
		getMediumLevelResultReport(stringBuilderReport);
		getPostBattleReport(stringBuilderReport);
		getPostBattleDamgeReport(stringBuilderReport);
		return stringBuilderReport.toString();
	}

	// TODO 2019-12-15 ReportLevel.MEDIUM
	private void getMediumLevelResultReport(StringBuilder report) {
		if (isWin()) {
			if (enemySpaceships.stream().allMatch(EnemySpaceship::isDestroyed)) {
				report.append(ENEMY_DESTROYED);
			} else {
				report.append(ENEMY_RETRETS);
			}
		} else { // lost battle
			if (ownSpaceships.stream().allMatch(OwnSpaceship::isDestroyed)) {
				report.append(OWN_DESTROYED);
			} else {
				report.append(OWN_RETRETS);
			}
		}
	}

	// TODO 2019-12-15 ReportLevel.SHORT
	private void getLowLevelResultReport(StringBuilder report) {
		report.append(String.format(BATTLE_DURATION, getChildeReports().size()));
		if (isWin()) {
			report.append(WIN);
		} else { // lost battle
			report.append(LOST);
		}
	}

	private void getPostBattleReport(StringBuilder report) {
		// own ships
		if (ownSpaceships.stream().anyMatch(OwnSpaceship::isDestroyed)) {
			report.append(OWN_LOST_SHIPS)
					.append(getOwnShipList(ownSpaceships.stream().filter(OwnSpaceship::isDestroyed))).append("\n");
		} else {
			report.append(OWN_NO_LOST).append("\n");
		}

		// enemy ships
		if (enemySpaceships.stream().anyMatch(EnemySpaceship::isDestroyed)) {
			report.append(ENEMY_LOST_SHIPS)
					.append(getEnemyShipList(enemySpaceships.stream().filter(EnemySpaceship::isDestroyed)))
					.append("\n");
		} else {
			report.append(ENEMY_NO_LOST).append("\n");
		}

		report.append(String.format(BATTLE_DURATION, getChildeReports().size()));
		if (isWin()) {
			report.append(ENEMY_RETRETING_SHIP);
			report.append(getEnemyShipList(enemySpaceships.stream().filter(EnemySpaceship::isRetret)));
		} else { // lost battle
			report.append(OWN_RETRETING_SHIP);
			report.append(getOwnShipList(ownSpaceships.stream().filter(OwnSpaceship::isRetret)));
		}
	}

	// TODO 2019-12-15 ReportLevel.LONG
	private void getPostBattleDamgeReport(StringBuilder report) {
		if (!ownSpaceships.stream().allMatch(OwnSpaceship::isDestroyed)) {
			report.append(OWN_POST_BATTLE_SHIP_STATUS);
			StringBuilder shipList = new StringBuilder();
			ownSpaceships.stream().filter(ship -> !ship.isDestroyed())
					.forEach(ship -> shipDamagereport(ship, shipList));
			report.append(shipList);
		}
	}

	private void shipDamagereport(OwnSpaceship ship, StringBuilder report) {
		if (report.length() != 0) {
			report.append(", ");
		}
		report.append(String.format(SHIP_POST_BATTLE_STATUS, ship.getName(), ship.getPostBattleHullState()));
	}

	private boolean isWin() {
		return ownSpaceships.stream().anyMatch(OwnSpaceship::isStillBattleReady);
	}

	private void getEnemyFleet(StringBuilder report) {
		if (enemySpaceships.stream().anyMatch(EnemySpaceship::isScreend)
				&& ownSpaceships.stream().anyMatch(ship -> !ship.isScreend())) {
			report.append(String.format(ENEMY_FIRST_LINE, getEnemyShipList(enemySpaceships.stream().filter(ship -> !ship.isScreend()))));
			report.append("\n");
			report.append(String.format(ENEMY_SCREEND_LINE, getEnemyShipList(enemySpaceships.stream().filter(EnemySpaceship::isScreend))));
			report.append("\n");
		} else {
			if (enemyFaction != null) {
				report.append(String.format(ENEMY_FORCE, getEnemyShipList(enemySpaceships.stream())));
				report.append("\n");
			} else { // neutral opponent
				report.append(String.format(ENEMY_NEUTRAL_FORCE, getEnemyName(), getEnemyShipList(enemySpaceships.stream())));
				report.append("\n");
			}
		}
	}

	private void getStart(StringBuilder report) {
		if (enemyName == null) {
			report.append(BATTLE_AGAINST_NEUTRAL);
		} else {
			report.append(String.format(BATTLE_AGAINST_ENEMY, enemyName));
		}
	}

	private void getOwnFleet(StringBuilder report) {
		if (ownSpaceships.stream().anyMatch(OwnSpaceship::isScreend)) {
			report = report.append(String.format(OWN_FIRST_LINE,
					getOwnShipList(ownSpaceships.stream().filter(ship -> !ship.isScreend()))));
			report.append("\n");
			report = report.append(String.format(OWN_SCREEND_LINE,
					getOwnShipList(ownSpaceships.stream().filter(OwnSpaceship::isScreend))));
			report.append("\n");
		} else {
			report = report.append(String.format(OWN_FORCE, getOwnShipList(ownSpaceships.stream())));
			report.append("\n");
		}
	}

	private String getEnemyShipList(Stream<EnemySpaceship> shipList) {
		StringBuilder ships = new StringBuilder();
		shipList.forEach(ship -> {
			if (ships.length() == 0) {
				ships.append(ship.getType());
			} else {
				ships.append(", ").append(ship.getType());
			}
		});
		return ships.toString();
	}

	private String getOwnShipList(Stream<OwnSpaceship> shipList) {
		StringBuilder ships = new StringBuilder();
		shipList.forEach(ship -> {
			if (ships.length() == 0) {
				ships.append(ship.getName());
			} else {
				ships.append(", ").append(ship.getName());
			}
		});
		return ships.toString();
	}

	public List<OwnSpaceship> getOwnSpaceships() {
		return ownSpaceships;
	}

	public List<EnemySpaceship> getEnemySpaceships() {
		return enemySpaceships;
	}

	public String getEnemyName() {
		return enemyName;
	}

	public String getEnemyFaction() {
		return enemyFaction;
	}

}

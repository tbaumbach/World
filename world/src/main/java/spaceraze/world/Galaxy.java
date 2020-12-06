package spaceraze.world;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lombok.*;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.diplomacy.DiplomacyState;
import spaceraze.world.enums.DiplomacyGameType;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.enums.SpaceShipSize;
import spaceraze.world.enums.SpaceshipRange;
import spaceraze.world.orders.Orders;

import javax.persistence.*;

/**
The container for a game, the galaxy the game is played in.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity()
@Table(name = "GALAXY")
public class Galaxy implements Serializable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //TODO check if we should use SEQUENCE or TABLE
	private Long id;

	public int turn;

	@Column(name="GAME_ENDED")
	public boolean gameEnded = false;
	@Column(name="GAME_NAME")
	private String gameName;
	@Column(name="PASSWORD")
	private String password;

	//TODO should just have a mapId, is this just for info or do we use the map?
	private String mapFileName;
	private String mapFullName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	private List<PlanetConnection> planetConnections = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	public List<VIP> allVIPs = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	public List<Player> players = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	public List<Planet> planets = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	private List<Spaceship> spaceships = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	private List<Troop> troops = new ArrayList<>();

	//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld.
	//private List<SpaceshipType> spaceshipTypes;
	//TODO get it from the GameWorld
	//private List<TroopType> troopTypes;

	//TODO move this out to a own table, create a enum for the names. Do we need this if we havde database id?

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	private List<UniqueIdCounter> uniqueIdCounters = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	private List<BlackMarketOffer> currentOffers = new ArrayList<>();

	@Column(name="last_updated")
	private Date lastUpdated;
	@Column(name="auto_balance")
	private boolean autoBalance;
	@Column(name="has_auto_updated")
	private boolean hasAutoUpdated;
	@Column(name="last_update_complete")
	private boolean lastUpdateComplete = true;
	private long time; // used to determine if and how often a game should update itself

	private StringBuffer lastLog;
	@Column(name="started_by_player") //TODO change to id.
	private String startedByPlayer; // contains the login of the player who started this game

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_GAME_WORLD")
	private GameWorld gameWorld;

	//Start game parameters. Move to own class?

	@Column(name="Number_of_start_planets")
	public int maxNrStartPlanets;
	@Column(name="number_of_start_planet")
	private int numberOfStartPlanet = 1;
	private int steps; // used to determine how close homeplanets for new players can be
	@Column(name="group_same_faction")
	private boolean groupSameFaction = false;

	@ElementCollection
	@CollectionTable(name="selectable_faction_names") //Change to id.
	private List<String> selectableFactionNames;


	@Column(name="random_faction")
	private boolean randomFaction = false;
	private boolean ranked = false;
	@Column(name="single_victory")
	private int singleVictory = 60;
	@Column(name="faction_victory")
	private int factionVictory = 65;
	private int endTurn = 0;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	private List<Statistics> allStatistics = new ArrayList<>();

	private StatisticGameType statisticGameType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy_states")
	@Builder.Default
	private List<DiplomacyState> diplomacyStates = new ArrayList<>(); // current states between all players

	private DiplomacyGameType diplomacyGameType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy_post_conflicts")
	@Builder.Default
	private List<DiplomacyState> postConflicts = new ArrayList<>();

	public Galaxy(Map aMap, String gameName, int steps, GameWorld aGameWorld) {
		this.mapFileName = aMap.getFileName();
		this.mapFullName = aMap.getNameFull();
		this.gameName = gameName;
		this.steps = steps;
		this.gameWorld = aGameWorld;
		planets = new ArrayList<>();
		players = new ArrayList<>();
		spaceships = new ArrayList<>();
		planetConnections = new ArrayList<>();
		allVIPs = new ArrayList<>();
		troops = new ArrayList<>();
		uniqueIdCounters = new ArrayList<>();
		postConflicts = new ArrayList<>();
		diplomacyStates = new ArrayList<>();
		allStatistics = new ArrayList<>();
		currentOffers = new ArrayList<>();
		//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld.
		//spaceshipTypes = gw.getShipTypes();
		//troopTypes = gw.getTroopTypes();
		//vipTypes = gw.getVipTypes();
		turn = 0;
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.SHIP));
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.VIP));
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.BLACK_MARKET));
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.BUILDING));

		// create map data
		for (BasePlanet aPlanet : aMap.getPlanets()) {
			planets.add(new Planet(aPlanet));
		}
		for (PlanetConnection aConnection : aMap.getConnections()) {
			Planet p1 = findPlanet(aConnection.getPlanetOne().getName());
			Planet p2 = findPlanet(aConnection.getPlanetTwo().getName());
			planetConnections.add(new PlanetConnection(p1, p2, aConnection.isLongRange()));
		}
		maxNrStartPlanets = aMap.getMaxNrStartPlanets();
	}

	public void setranked(boolean branked) {
		this.ranked = branked;
	}

	public boolean getranked() {
		Logger.finer("RANKED GAME: " + ranked);
		return this.ranked;
	}

	public String getMapFileName() {
		return mapFileName;
	}

	public void setLastUpdated(Date newDate) {
		lastUpdated = newDate;
	}

	public void setTime(long newTime) {
		this.time = newTime;
	}

	public long getTime() {
		return time;
	}

	public String getLastUpdatedString() {
		String retStr = "Game has not been updated yet";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HH:mm:ss");
		Date tmpDate = getLastUpdated();
		if (tmpDate != null) {
			retStr = sdf.format(tmpDate);
		}
		return retStr;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setMaxNrStartPlanets(int newValue) {
		maxNrStartPlanets = newValue;
	}

	public int getVIPResBonus(Planet aPlanet, Player aPlayer) {
		int resBonus = findHighestVIPResistanceBonus(aPlanet, aPlayer);
		return resBonus;
	}

	public VIP findVIPGovenor(Planet aPlanet, Player aPlayer) {
		VIP foundVIP = null;
		int i = 0;
		while ((foundVIP == null) & (i < allVIPs.size())) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.isGovernor()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				foundVIP = tempVIP;
			} else {
				i++;
			}
		}
		return foundVIP;
	}

	public VIP findVIPGovenor(Player aPlayer) {
		VIP foundVIP = null;
		int i = 0;
		while ((foundVIP == null) & (i < allVIPs.size())) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.isGovernor()) & (tempVIP.getBoss() == aPlayer)) {
				foundVIP = tempVIP;
			} else {
				i++;
			}
		}
		return foundVIP;
	}

	public int findHighestVIPResistanceBonus(Planet aPlanet, Player aPlayer) {
		int highestResistanceBonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.hasResistanceBonus()) & (tempVIP.getBoss() == aPlayer)
					& (tempVIP.getPlanetLocation() == aPlanet)) {
				if (tempVIP.getResistanceBonus() > highestResistanceBonus) {
					highestResistanceBonus = tempVIP.getResistanceBonus();
				}
			}
		}
		return highestResistanceBonus;
	}

	public VIP findHighestVIPPsychWarfareBonus(Spaceship aShip, Player aPlayer) {
		VIP foundVIP = null;
		int highestPsychWarfareBonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.hasPsychWarfareBonus()) & (tempVIP.getBoss() == aPlayer)
					& (tempVIP.getShipLocation() == aShip)) {
				if (tempVIP.getPsychWarfareBonus() > highestPsychWarfareBonus) {
					highestPsychWarfareBonus = tempVIP.getPsychWarfareBonus();
					foundVIP = tempVIP;
				}
			}
		}
		return foundVIP;
	}

	public VIP findHighestVIPAimBonus(Spaceship aShip, Player aPlayer) {
		VIP foundVIP = null;
		int highestAimBonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.hasAimBonus()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)) {
				if (tempVIP.getAimBonus() > highestAimBonus) {
					highestAimBonus = tempVIP.getAimBonus();
					foundVIP = tempVIP;
				}
			}
		}
		return foundVIP;
	}

	public VIP findVIPResistanceBonus(Planet aPlanet, Player aPlayer) {
		VIP foundVIP = null;
		int bonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				if (tempVIP.getResistanceBonus() > bonus) {
					bonus = tempVIP.getResistanceBonus();
					foundVIP = tempVIP;
				}
			}
		}
		return foundVIP;
	}

	public VIP findVIPTechBonus(Planet aPlanet, Player aPlayer, Orders orders) {
		VIP foundVIP = null;
		int bonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				if (tempVIP.getTechBonus() > bonus) {
					if (orders.VIPWillStay(tempVIP)) {
						foundVIP = tempVIP;
						bonus = tempVIP.getTechBonus();
					}
				}
			}
		}
		return foundVIP;
	}

	public VIP findVIPShipBuildBonus(Planet aPlanet, Player aPlayer, Orders orders) {
		VIP foundVIP = null;
		int bonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				if (tempVIP.getShipBuildBonus() > bonus) {
					if (orders.VIPWillStay(tempVIP)) {
						foundVIP = tempVIP;
						bonus = tempVIP.getShipBuildBonus();
					}
				}
			}
		}
		return foundVIP;
	}

	public VIP findVIPTroopBuildBonus(Planet aPlanet, Player aPlayer, Orders orders) {
		VIP foundVIP = null;
		int bonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				if (tempVIP.getTroopBuildBonus() > bonus) {
					if (orders.VIPWillStay(tempVIP)) {
						foundVIP = tempVIP;
						bonus = tempVIP.getTroopBuildBonus();
					}
				}
			}
		}
		return foundVIP;
	}

	public VIP findVIPBuildingBuildBonus(Planet aPlanet, Player aPlayer, Orders orders) {
		VIP foundVIP = null;
		int bonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				if (tempVIP.getBuildingBuildBonus() > bonus) {
					if (orders.VIPWillStay(tempVIP)) {
						foundVIP = tempVIP;
						bonus = tempVIP.getBuildingBuildBonus();
					}
				}
			}
		}
		return foundVIP;
	}

	/**
	 * 
	 * @param aShip
	 *            a capital ship
	 * @param aPlayer
	 * @return
	 */
	public int findVIPhighestInitBonusCapitalShip(Spaceship aShip, Player aPlayer) {
		int initBonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)) {
				if (tempVIP.getInitBonus() > initBonus) {
					initBonus = tempVIP.getInitBonus();
				}
			}
		}
		return initBonus;
	}

	public int findVIPhighestInitBonusSquadron(Spaceship aShip, Player aPlayer) {
		int initSquadronBonus = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)) {
				if (tempVIP.getInitSquadronBonus() > initSquadronBonus) {
					initSquadronBonus = tempVIP.getInitSquadronBonus();
				}
			}
		}
		Logger.finer("initSquadronBonus: " + initSquadronBonus);
		return initSquadronBonus;
	}

	public boolean findVIPcanAttackScreened(Spaceship firingShip) {
		boolean found = false;
		int index = 0;
		while ((!found) & index < allVIPs.size()) {
			VIP aVIP = allVIPs.get(index);
			if (aVIP.getShipLocation() == firingShip) {
				if (firingShip.isSquadron()) {
					if (aVIP.isAttackScreenedSquadron()) {
						found = true;
					}
				} else { // capital ship
					if (aVIP.isAttackScreenedCapital()) {
						found = true;
					}
				}
			}
			if (!found) {
				index++;
			}
		}
		return found;
	}

	public int findVIPhighestInitDefence(Spaceship aShip, Player aPlayer) {
		int initDefence = 0;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.getInitDefence() > 0) & (tempVIP.getBoss() == aPlayer)
					& (tempVIP.getShipLocation() == aShip)) {
				if (tempVIP.getInitDefence() > initDefence) {
					initDefence = tempVIP.getInitDefence();
				}
			}
		}
		return initDefence;
	}

	public Spaceship findSurveyShip(Planet aPlanet, Player aPlayer) {
		Spaceship foundShip = null;
		int i = 0;
		while ((foundShip == null) & (i < spaceships.size())) {
			Spaceship tempShip = spaceships.get(i);
			if ((tempShip.isPlanetarySurvey()) & (tempShip.getOwner() == aPlayer)
					& (tempShip.getLocation() == aPlanet)) {
				foundShip = tempShip;
			} else {
				i++;
			}
		}
		return foundShip;
	}

	public VIP findSurveyVIPonShip(Planet aPlanet, Player aPlayer) {
		VIP foundVIP = null;
		int i = 0;
		while ((foundVIP == null) & (i < allVIPs.size())) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.isPlanetarySurvey() & (tempVIP.getBoss() == aPlayer)) {
				if (tempVIP.getShipLocation() != null) { // VIP is on a ship
					if (tempVIP.getShipLocation().getLocation() == aPlanet) { // ship is in orbit around aPlanet
						foundVIP = tempVIP;
					}
				}
			}
			if (foundVIP == null) {
				i++;
			}
		}
		return foundVIP;
	}

	/**
	 * Find all other players that have troops on planet aPlanet
	 * 
	 * @param aPlayer
	 *            Find any other player than aPlayer
	 * @param aPlanet
	 *            the planet
	 * @return a set containing all other players
	 */
	public Set<Player> findOtherTroopsPlayersOnRazedPlanet(Player aPlayer, Planet aPlanet) {
		Set<Player> otherPlayers = new HashSet<Player>();
		// find all troops on aPlanet
		List<Troop> troopsOnPlanet = findAllTroopsOnPlanet(aPlanet);
		for (Troop aTroop : troopsOnPlanet) {
			// if not aPlayer or null
			Player owner = aTroop.getOwner();
			if ((owner != aPlayer) & (owner != null)) {
				// add the troops owner to set
				otherPlayers.add(owner);
			}
		}
		return otherPlayers;
	}

	public boolean findDropShip(Planet aPlanet, Player aPlayer) {
		boolean found = false;
		int i = 0;
		while (!found & (i < spaceships.size())) {
			Spaceship tempShip = spaceships.get(i);
			if ((tempShip.getTroopCapacity() > 0) & (tempShip.getOwner() == aPlayer)
					& (tempShip.getLocation() == aPlanet)) {
				found = true;
			} else {
				i++;
			}
		}
		return found;
	}

	public VIP findVIPSpy(Planet aPlanet, Player aPlayer) {
		VIP foundVIP = null;
		int i = 0;
		while ((foundVIP == null) & (i < allVIPs.size())) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.isSpy()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				foundVIP = tempVIP;
			} else {
				i++;
			}
		}
		return foundVIP;
	}

	public VIP findVIPAssassin(Planet aPlanet, Player aPlayer) {
		VIP foundVIP = null;
		int i = 0;
		while ((foundVIP == null) & (i < allVIPs.size())) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if ((tempVIP.isAssassin()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)) {
				foundVIP = tempVIP;
			} else {
				i++;
			}
		}
		return foundVIP;
	}

	public int getNrTroops(Planet aPlanet) {
		int nrTroopsOnPlanet = 0;
		for (Troop aTroop : troops) {
			if (aTroop.getPlanetLocation() != null) {
				if (aTroop.getPlanetLocation() == aPlanet) {
					if (aPlanet.getPlayerInControl() == aTroop.getOwner()) {
						if (aTroop.getTroopType().isVisible()) {
							nrTroopsOnPlanet++;
						}
					}
				}
			}
		}
		return nrTroopsOnPlanet;
	}

	public List<Troop> findAllTroopsOnShip(Spaceship aShip) {
		List<Troop> troopsOnShip = new LinkedList<Troop>();
		for (Troop troop : troops) {
			if (troop.getShipLocation() == aShip) {
				troopsOnShip.add(troop);
			}
		}
		return troopsOnShip;
	}

	public List<Troop> findAllTroopsOnPlanet(Planet aPlanet) {
		List<Troop> troopsOnPlanet = new LinkedList<Troop>();
		for (Troop troop : troops) {
			if (troop.getPlanetLocation() == aPlanet) {
				troopsOnPlanet.add(troop);
			}
		}
		return troopsOnPlanet;
	}

	public List<Troop> findAllDefendingTroopsOnPlanet(Planet aPlanet) {
		Player planetOwner = aPlanet.getPlayerInControl();
		return findTroopsOnPlanet(aPlanet, planetOwner);
	}

	public List<Troop> findTroopsOnPlanet(Planet aPlanet, Player aPlayer) {
		List<Troop> troopsOnPlanet = new LinkedList<Troop>();
		for (Troop troop : troops) {
			if (troop.getPlanetLocation() == aPlanet) {
				if (troop.getOwner() == aPlayer) {
					troopsOnPlanet.add(troop);
				}
			}
		}
		return troopsOnPlanet;
	}

	/**
	 * Find if there are at least one ship at the planet aPlanet that belongs to
	 * aPlayer.
	 *
	 * @return if there are at least one dropship at aPlanet belonging to aPlayer
	 */
	/*
	 * public boolean dropShipInOrbit(Planet aPlanet, Player aPlayer){ boolean
	 * dropShipExist = false; int i = 0; while ((i < spaceships.size()) &
	 * !dropShipExist){ Spaceship aShip = (Spaceship)spaceships.get(i); if
	 * (aShip.getOwner() == aPlayer){ if (aShip.getTroopLaunchCapacity() > 0){
	 * dropShipExist = true; }else{ i++; } }else{ i++; } } return dropShipExist; }
	 */

	public List<VIP> findAllVIPsOnShip(Spaceship aShip) {
		List<VIP> tempAllVIPs = new LinkedList<VIP>();
		for (VIP tempVIP : allVIPs) {
			if (tempVIP.getShipLocation() != null) {
				Spaceship tempss = tempVIP.getShipLocation();
				if (tempss == aShip) {
					tempAllVIPs.add(tempVIP);
				}
			}
		}
		return tempAllVIPs;
	}

	public List<VIP> findAllVIPsOnTroop(Troop aTroop) {
		List<VIP> tempAllVIPs = new LinkedList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.getTroopLocation() != null) {
				Troop tempTroop = tempVIP.getTroopLocation();
				if (tempTroop == aTroop) {
					tempAllVIPs.add(tempVIP);
				}
			}
		}
		return tempAllVIPs;
	}

	public String getAllBattleSimVipsOnShip(Spaceship aShip) {
		StringBuffer sb = new StringBuffer();
		List<VIP> vipsOnShip = findAllVIPsOnShip(aShip);
		List<VIP> battleVips = new LinkedList<VIP>();
		for (VIP aVIP : vipsOnShip) {
			if (aVIP.isBattleVip()) {
				battleVips.add(aVIP);
			}
		}
		for (VIP aVIP : battleVips) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(aVIP.getShortName());
		}
		return sb.toString();
	}

	public String getAllBattleSimVipsOnTroop(Troop aTroop) {
		StringBuffer sb = new StringBuffer();
		List<VIP> vipsOnTroop = findAllVIPsOnTroop(aTroop);
		List<VIP> battleVips = new LinkedList<VIP>();
		for (VIP aVIP : vipsOnTroop) {
			if (aVIP.isLandBattleVIP()) {
				battleVips.add(aVIP);
			}
		}
		for (VIP aVIP : battleVips) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(aVIP.getShortName());
		}
		return sb.toString();
	}

	// remove all VIPs belonging to a player. Used primarily (only?) to remove
	// defeated players VIPs
	public void removeVIPs(Player aPlayer) {
		List<VIP> removeVIPs = new LinkedList<VIP>();
		for (int i = allVIPs.size() - 1; i >= 0; i--) {
			VIP tempVIP = allVIPs.get(i);
			if (tempVIP.getBoss() == aPlayer) {
				removeVIPs.add(tempVIP);
			}
		}
		for (VIP aVIP : removeVIPs) {
			removeVIP(aVIP);
		}
	}

	public void removeVIP(VIP aVIP) {
		allVIPs.remove(aVIP);
	}

	public void checkVIPsInScuttledShips(Spaceship aShip, Player aPlayer) {
		List<VIP> allVIPsOnShip = findAllVIPsOnShip(aShip);
		for (VIP tempVIP : allVIPsOnShip) {
			allVIPs.remove(tempVIP);
			aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName()
					+ " have abandoned your cause when the ship was scuttled.");
			aPlayer.addToHighlights(tempVIP.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
		}
	}

	public void checkVIPsInSelfDestroyedShips(Spaceship aShip, Player aPlayer) {
		List<VIP> allVIPsOnShip = findAllVIPsOnShip(aShip);
		for (VIP tempVIP : allVIPsOnShip) {
			if (aShip.getLocation() == null) {
				// ship is retreating, vip is killed
				allVIPs.remove(tempVIP);
				aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has been killed when your retreating ship "
						+ aShip.getName() + " was selfdestructed.");
				aPlayer.addToHighlights(tempVIP.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
			} else if (aShip.getLocation().getPlayerInControl() == aPlayer) {
				// om skeppet är vid en egen planet så flyttar VIPen dit
				tempVIP.setLocation(aShip.getLocation());
				aPlayer.addToVIPReport(
						"Your " + tempVIP.getName() + " travelling in " + aShip.getName() + " have moved to the planet "
								+ aShip.getLocation().getName() + " when the ship was selfdestructed.");
			} else {
				// annars om VIPen kan vara på fientliga planeter så flyttar den dit
				if (tempVIP.canVisitEnemyPlanets()) {
					tempVIP.setLocation(aShip.getLocation());
					aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName()
							+ " have moved to the planet " + aShip.getLocation().getName()
							+ " when the ship was selfdestructed.");
				} else // annars om det är en neutral planet och VIP en är en guvenör flyttar han dit
				if ((tempVIP.canVisitNeutralPlanets()) & (aShip.getLocation().getPlayerInControl() == null)) {
					tempVIP.setLocation(aShip.getLocation());
					aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName()
							+ " have moved to the planet " + aShip.getLocation().getName()
							+ " when the ship was selfdestructed.");
				} else { // annars dör VIPen
					allVIPs.remove(tempVIP);
					aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has been killed when your ship "
							+ aShip.getName() + " was selfdestructed at " + aShip.getLocation().getName() + ".");
					aPlayer.addToHighlights(tempVIP.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
				}
			}
		}
	}

	public void checkVIPsInSelfDestroyedTroops(Troop aTroop, Player aPlayer) {
		List<VIP> allVIPsOnTroop = findAllVIPsOnTroop(aTroop);
		for (VIP aVip : allVIPsOnTroop) {
			TurnInfo ti = aVip.getBoss().getTurnInfo();
			// troop is aboard ship -> move VIP to ship
			if (aTroop.getShipLocation() != null) {
				ti.addToLatestGeneralReport(aVip.getName() + " has been forced to move when " + aTroop.getUniqueName()
						+ " was selfdestructed.");
				aVip.moveVIP(aTroop.getShipLocation(), ti);
			} else { // troop is on planet
				Planet thePlanet = aTroop.getPlanetLocation();
				// own planet -> move VIP to planet
				if (thePlanet.getPlayerInControl() == aVip.getBoss()) {
					ti.addToLatestGeneralReport(aVip.getName() + " has been forced to move when "
							+ aTroop.getUniqueName() + " was selfdestructed.");
					aVip.moveVIP(thePlanet, ti);
				} else if (thePlanet.getPlayerInControl() == null) {
					// neutral planet
					if (aVip.canVisitNeutralPlanets()) {
						// VIP can visit neutral planets -> move VIP to planet
						ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName()
								+ " to " + thePlanet.getName());
						ti.addToLatestVIPReport(
								aVip.getName() + " has been forced to move to the planet " + thePlanet.getName()
										+ " when your troop " + aTroop.getUniqueName() + " was selfdestructed.");
						aVip.setLocation(thePlanet);
					} else {
						// otherwise VIP is killed
						allVIPs.remove(aVip);
						aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop "
								+ aTroop.getUniqueName() + " was selfdestructed at " + thePlanet.getName() + ".");
						aPlayer.addToHighlights(aVip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
					}
				} else {
					// enemy planet
					if (aVip.canVisitEnemyPlanets()) {
						// VIP can visit enemy planets -> move VIP to planet
						ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName()
								+ " to " + thePlanet.getName());
						ti.addToLatestVIPReport(
								aVip.getName() + " has been forced to move to the planet " + thePlanet.getName()
										+ " when your troop " + aTroop.getUniqueName() + " was selfdestructed.");
						aVip.setLocation(thePlanet);
					} else {
						// otherwise VIP is killed
						allVIPs.remove(aVip);
						aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop "
								+ aTroop.getUniqueName() + " was selfdestructed at " + thePlanet.getName() + ".");
						aPlayer.addToHighlights(aVip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
					}
				}
			}
		}
	}

	public void checkVIPsInTroopsInSelfDestroyedShips(Troop aTroop, Player aPlayer) {
		List<VIP> allVIPsOnTroop = findAllVIPsOnTroop(aTroop);
		for (VIP aVip : allVIPsOnTroop) {
			TurnInfo ti = aVip.getBoss().getTurnInfo();
			Spaceship carrier = aTroop.getShipLocation();
			// troop is aboard retreating ship -> destroy VIP
			if (carrier.isRetreating()) {
				ti.addToLatestGeneralReport(aVip.getName() + " has been lost when " + aTroop.getUniqueName()
						+ " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
				allVIPs.remove(aVip);
				aPlayer.addToHighlights(aVip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
			} else { // troops carrier is at a planet
				Planet thePlanet = carrier.getLocation();
				// own planet -> move VIP to planet
				if (thePlanet.getPlayerInControl() == aVip.getBoss()) {
					ti.addToLatestGeneralReport(
							aVip.getName() + " has been forced to move when " + aTroop.getUniqueName()
									+ " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
					aVip.moveVIP(thePlanet, ti);
				} else if (thePlanet.getPlayerInControl() == null) {
					// neutral planet
					if (aVip.canVisitNeutralPlanets()) {
						// VIP can visit neutral planets -> move VIP to planet
						ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName()
								+ " to " + thePlanet.getName());
						ti.addToLatestVIPReport(aVip.getName() + " has been forced to move to the planet "
								+ thePlanet.getName() + " when your troop " + aTroop.getUniqueName()
								+ " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
						aVip.setLocation(thePlanet);
					} else {
						// otherwise VIP is killed
						allVIPs.remove(aVip);
						aPlayer.addToVIPReport(
								"Your " + aVip.getName() + " has been killed when your troop " + aTroop.getUniqueName()
										+ " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
						aPlayer.addToHighlights(aVip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
					}
				} else {
					// enemy planet
					if (aVip.canVisitEnemyPlanets()) {
						// VIP can visit enemy planets -> move VIP to planet
						ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName()
								+ " to " + thePlanet.getName());
						ti.addToLatestVIPReport(
								aVip.getName() + " has been forced to move to the planet " + thePlanet.getName()
										+ " when your troop " + aTroop.getUniqueName() + " was selfdestructed.");
						aVip.setLocation(thePlanet);
					} else {
						// otherwise VIP is killed
						allVIPs.remove(aVip);
						aPlayer.addToVIPReport(
								"Your " + aVip.getName() + " has been killed when your troop " + aTroop.getUniqueName()
										+ " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
						aPlayer.addToHighlights(aVip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
					}
				}
			}
		}
	}

	public void checkVIPsInDestroyedShips(Spaceship aShip, Player aPlayer) {
		List<VIP> allVIPsOnShip = findAllVIPsOnShip(aShip);
		for (int i = 0; i < allVIPsOnShip.size(); i++) {
			VIP tempVIP = allVIPsOnShip.get(i);
			// if VIP is hard to kill he moves to the nearby planet
			if (tempVIP.isHardToKill()) {
				tempVIP.setLocation(aShip.getLocation());
				aPlayer.addToVIPReport(
						"Your " + tempVIP.getName() + " travelling in " + aShip.getName() + " have moved to the planet "
								+ aShip.getLocation().getName() + " when the ship was destroyed.");
			} else { // annars dör VIPen
				allVIPs.remove(tempVIP);
				aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has been killed when your ship "
						+ aShip.getName() + " was destroyed at " + aShip.getLocation().getName() + ".");
				aPlayer.addToHighlights(tempVIP.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
			}
		}
	}

	public void checkTroopsInDestroyedShips(Spaceship aShip, Player aPlayer) {
		List<Troop> troopList = getTroopsOnShip(aShip);
		for (Troop aTroop : troopList) {
			aPlayer.addToTroopsLostInSpace(aTroop);
			aTroop.getOwner().addToGeneral("Your troop " + aTroop.getUniqueName() + " has been killed when your ship "
					+ aShip.getName() + " was destroyed at " + aShip.getLocation().getName() + ".");
			aTroop.getOwner().addToHighlights(aTroop.getUniqueName(), HighlightType.TYPE_OWN_TROOP_DESTROYED);
			removeTroop(aTroop);
		}
	}

	public void checkVIPsInDestroyedTroop(Troop aTroop) {
		List<VIP> allVIPsOnTroop = findAllVIPsOnTroop(aTroop);
		Player aPlayer = aTroop.getOwner();
		for (VIP vip : allVIPsOnTroop) {
			// if VIP is hard to kill he moves to the nearby planet
			if (vip.isHardToKill()) {
				if (aTroop.getPlanetLocation() != null) {
					vip.setLocation(aTroop.getPlanetLocation());
					aPlayer.addToVIPReport("Your " + vip.getName() + " travelling in " + aTroop.getUniqueName()
							+ " have moved to the planet " + aTroop.getPlanetLocation().getName()
							+ " when the ship was destroyed.");
				} else { // VIP is on troop on a ship
					vip.setLocation(aTroop.getShipLocation().getLocation());
					aPlayer.addToVIPReport("Your " + vip.getName() + " travelling in " + aTroop.getUniqueName()
							+ " have moved to the planet " + aTroop.getShipLocation().getLocation().getName()
							+ " when the ship carrying the troop was destroyed.");
				}
			} else { // annars d�r VIPen
				allVIPs.remove(vip);
				if (aTroop.getPlanetLocation() != null) {
					aPlayer.addToVIPReport(
							"Your " + vip.getName() + " has been killed when your troop " + aTroop.getUniqueName()
									+ " was destroyed at " + aTroop.getPlanetLocation().getName() + ".");
				} else {
					aPlayer.addToVIPReport(
							"Your " + vip.getName() + " has been killed when your troop " + aTroop.getUniqueName()
									+ " was destroyed at " + aTroop.getShipLocation().getLocation().getName()
									+ " when the ship carrying the troop was destroyed.");
				}
				aPlayer.addToHighlights(vip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
			}
		}
	}

	public void checkTroopsOnInfestedPlanet(Planet aPlanet, Player aPlayer) {
		List<Troop> allTroopsOnPlanet = findAllTroopsOnPlanet(aPlanet);
		for (Troop aTroop : allTroopsOnPlanet) {
			if (aTroop.getOwner() == aPlanet.getPlayerInControl()) { // if troop belongs to the same player that
																		// controls the planet (or is neutral)
				troops.remove(aTroop);
				if (aTroop.getOwner() != null) {
					aTroop.getOwner().addToGeneral("Your " + aTroop.getUniqueName()
							+ " have been destroyed when the planet " + aPlanet.getName() + " was infested.");
				}
				// aTroop.getOwner().addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
				aPlayer.addToGeneral("An enemy " + aTroop.getTroopType().getUniqueName()
						+ " have been killed when you infested the planet " + aPlanet.getName() + ".");
				// aPlayer.addToHighlights(tempVIP.getName(),Highlight.TYPE_ENEMY_VIP_KILLED);
			}
		}
	}

	public VIP createRandomVIP() {
		VIPType tempviptype = getRandomVIPType();
		return tempviptype.createNewVIP(true);
	}

	public VIP maybeAddVIP(Player aPlayer) {
		VIP aVIP = null;
		int aRandom = Functions.getRandomInt(1, 2);
		if (aRandom == 1) {
			aVIP = createPlayerVIP(aPlayer);
		}
		return aVIP;
	}

	/**
	 * Create a VIP for a certain player
	 * 
	 * @param aPlayer
	 *            the player for whom this VIP will belong
	 * @return a VIP compatible with the players faction
	 */
	public VIP createPlayerVIP(Player aPlayer) {
		Logger.finer("createPlayerVIP: " + aPlayer.getName() + ", alignment=" + aPlayer.getFaction().getAlignment());
		VIP aVIP = null;
		boolean ok = false;
		while (!ok) { // loopa tills det blir en vip som spelaren kan ha
			ok = true;
			aVIP = createRandomVIP();
			Logger.finer(aVIP.getName() + ", alignment=" + aVIP.getAlignment() + ", canHaveVip="
					+ aPlayer.getFaction().getAlignment().canHaveVip(aVIP.getAlignment().getName()));
			if (!aPlayer.getFaction().getAlignment().canHaveVip(aVIP.getAlignment().getName())) {
				ok = false;
			}
		}
		aVIP.setBoss(aPlayer);
		allVIPs.add(aVIP);
		return aVIP;
	}
	
	//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld. Move this method to GameWorld and remove the duplicating

	@SuppressWarnings("unused")
	private SpaceshipType getSpaceshipTypeByName(String sstname) {
		return gameWorld.getSpaceshipTypeByName(sstname);
		//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld.
		/*
		SpaceshipType foundsst = null;
		int i = 0;
		while ((i < spaceshipTypes.size()) & (foundsst == null)) {
			SpaceshipType tempsst = (SpaceshipType) spaceshipTypes.get(i);
			if (tempsst.getName().equalsIgnoreCase(sstname)) {
				foundsst = tempsst;
			} else {
				i++;
			}
		}
		return foundsst;*/
	}
	
	public SpaceshipType getShipType(String typename) {
		return gameWorld.getSpaceshipTypeByName(typename);
		//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld.
		/*
		SpaceshipType st = null;
		List<SpaceshipType> allshiptypes = spaceshipTypes;
		int i = 0;
		while (st == null) {
			SpaceshipType temp = (SpaceshipType) allshiptypes.get(i);
			if (temp.getName().equalsIgnoreCase(typename)) {
				st = temp;
			} else {
				i++;
			}
		}
		return st;*/
	}
	
	public SpaceshipType findSpaceshipType(String findname) {
		return gameWorld.getSpaceshipTypeByName(findname);
		//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld.
		/*
		SpaceshipType sst = null;
		int i = 0;
		while ((sst == null) & (i < spaceshipTypes.size())) {
			SpaceshipType temp = (SpaceshipType) spaceshipTypes.get(i);
			if (temp.getName().equalsIgnoreCase(findname)) {
				sst = temp;
			}
			i++;
		}
		return sst;
		*/
	}
	
	public SpaceshipType findSpaceshipTypeShortName(String shortfindname) {
		SpaceshipType sst = null;
		int i = 0;
		//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld.
		while ((sst == null) & (i < gameWorld.getShipTypes().size())) {
			SpaceshipType temp = (SpaceshipType) gameWorld.getShipTypes().get(i);
			if (temp.getShortName().equalsIgnoreCase(shortfindname)) {
				sst = temp;
			}
			i++;
		}
		return sst;
	}
	
	//TODO 2020-01-01 removed from Galaxy, should be the same list as in GameWorld.
	/*
	public List<SpaceshipType> getSpaceshipTypes() {
		return spaceshipTypes;
	}*/

	public Spaceship findSpaceship(String ssname, Player owner) {
		Spaceship foundss = null;
		int i = 0;
		while ((i < spaceships.size()) & (foundss == null)) {
			Spaceship tempss = (Spaceship) spaceships.get(i);
			if (tempss.getName().equalsIgnoreCase(ssname) && tempss.getOwner() == owner) {
				foundss = tempss;
			} else {
				i++;
			}
		}
		return foundss;
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	// enklare variant av getPlayer som ej används vid inloggning
	public Faction getFaction(String name) {
		int i = 0;
		Faction found = null;
		Faction temp = null;
		while ((i < gameWorld.getFactions().size()) & (found == null)) {
			temp = gameWorld.getFactions().get(i);
			if (temp.isFaction(name)) {
				found = temp;
			} else {
				i++;
			}
		}
		return found;
	}

	public Player getPlayer(String name, String password) {
		int i = 0;
		Player p = null;
		Logger.finer("getPlayer: " + players.size());
		Player temp = null;
		while ((i < players.size()) & (p == null)) {
			temp = (Player) players.get(i);
			if (temp.isPlayer(name, password)) {
				p = temp;
			}
			i++;
		}
		if (p == null) {
			p = new Player("Couldn't find player");
		}
		return p;
	}

	// enklare variant av getPlayer som ej används vid inloggning
	public Player getPlayer(String name) {
		Logger.finer("name: " + name);
		int i = 0;
		Player found = null;
		Player temp = null;
		while ((i < players.size()) & (found == null)) {
			temp = (Player) players.get(i);
			Logger.finer("temp: " + temp.getName());
			if (temp.isPlayer(name)) {
				found = temp;
			} else {
				i++;
			}
		}
		Logger.finer("found: " + found);
		return found;
	}

	// enklare variant av getPlayer som ej används vid inloggning
	public Player getPlayerByGovenorName(String govenorName) {
		int i = 0;
		Player found = null;
		Player temp = null;
		while ((i < players.size()) & (found == null)) {
			temp = players.get(i);
			if (temp.isPlayerByGovernorName(govenorName)) {
				found = temp;
			} else {
				i++;
			}
		}
		return found;
	}

	public void removeNeutralShips(Planet homeplanet) {
		for (int i = spaceships.size() - 1; i >= 0; i--) {
			Spaceship tempShip = spaceships.get(i);
			if ((tempShip.getLocation() == homeplanet) & (tempShip.getOwner() == null)) {
				spaceships.remove(tempShip);
			}
		}
	}

	public void removeNeutralTroops(Planet homeplanet) {
		List<Troop> troopsToRemove = new LinkedList<Troop>();
		for (Troop aTroop : troops) {
			if ((aTroop.getPlanetLocation() == homeplanet) & (aTroop.getOwner() == null)) {
				troopsToRemove.add(aTroop);
			}
		}
		for (Troop troop : troopsToRemove) {
			troops.remove(troop);
		}
	}

	public int getNrStartPlanets() {
		return maxNrStartPlanets;
	}

	public void addPlayer(Player p) {
		players.add(p);
	}

	public int getTurn() {
		return turn;
	}

	// extract Order from Player and insert it into the original Player instance
	// and do the same for PlanetInfos
	public String replacePlayer(Player p) {
		Logger.finer("replaceplayer(galaxy): " + p.getName()); // sp�rutskrift till consolen
		if (p.getErrorMessage() != null) {
			return "Insert failed: " + p.getErrorMessage();
		} else {
			Player temp = getPlayer(p.getName(), p.getPassword());
			temp.setOrders(p.getOrders());
			temp.setPlanetInformations(p.getPlanetInformations());
			temp.setNotes(p.getNotes());
			if (turn > 0) {
				temp.setUpdatedThisTurn(true);
				temp.setFinishedThisTurn(p.isFinishedThisTurn());
				Logger.finer("p.isFinishedThisTurn(): " + p.isFinishedThisTurn());
			}
			return "Player data inserted successfully.";
		}
	}

	public Planet findPlanet(String findname) {
		// Logger.fine("findname: " + findname);
		Planet p = null;
		int i = 0;
		while ((p == null) & (i < planets.size())) {
			Planet temp = (Planet) planets.get(i);
			// Logger.fine("temp.getName(): " + temp.getName());
			if (temp.getName().equalsIgnoreCase(findname)) {
				p = temp;
			} else {
				i++;
			}
		}
		// Logger.fine("return: " + p);
		return p;
	}

	public Spaceship findSpaceship(String spaceshipName) {
		Spaceship ss = null;
		int i = 0;
		while ((ss == null) & (i < spaceships.size())) {
			Spaceship temp = spaceships.get(i);
			if (temp.getName().equalsIgnoreCase(spaceshipName)) {
				ss = temp;
			} else {
				i++;
			}
		}
		return ss;
	}

	public Spaceship findSpaceshipByUniqueId(String uniqueId) {
		Spaceship ss = null;
		int i = 0;
		while ((ss == null) & (i < spaceships.size())) {
			Spaceship temp = spaceships.get(i);
			if (temp.getUniqueId().equals(uniqueId)) {
				ss = temp;
			} else {
				i++;
			}
		}
		return ss;
	}

	public Troop findTroop(int findid) {
		Troop foundTroop = null;
		int i = 0;
		while ((foundTroop == null) & (i < troops.size())) {
			Troop temp = troops.get(i);
			if (temp.getUniqueId() == findid) {
				foundTroop = temp;
			} else {
				i++;
			}
		}
		return foundTroop;
	}
	
	public TroopType findTroopType(String ttname) {
		TroopType tt = null;
		int i = 0;
		while ((tt == null) & (i < gameWorld.getTroopTypes().size())) {
			TroopType aTT = gameWorld.getTroopTypes().get(i);
			if (aTT.getUniqueName().equals(ttname)) {
				tt = aTT;
			} else {
				i++;
			}
		}
		return tt;
	}

	public List<Player> getSoloConfederacyWinner() {
		List<Player> confPlayers = new LinkedList<Player>();
		for (Player aPlayer : players) {
			if (!aPlayer.isDefeated()) {
				confPlayers.add(aPlayer);
			}
		}
		return confPlayers;
	}

	public boolean playersLeft() {
		boolean playersExist = false;
		for (Player aPlayer : players) {
			if (!aPlayer.isDefeated()) {
				playersExist = true;
			}
		}
		return playersExist;
	}

	// check if there is only 1 player left in the game and that he has at least 1
	// planet
	public Player checkSoloPlayerWinner() {
		Player winner = null;
		Player tempPlayer2 = null;
		int tempnr = 0;
		for (int i = 0; i < players.size(); i++) {
			Player tempPlayer = (Player) players.get(i);
			if (!tempPlayer.isDefeated()) {
				tempnr = tempnr + 1;
				tempPlayer2 = tempPlayer;
			}
		}
		if (tempnr == 1) {
			// om det bara finns en spelare kvar m�ste han ha minst 1 planet f�r att vinna
			boolean atLeastOnePlanet = false;
			for (int j = 0; j < planets.size(); j++) {
				Planet tempPlanet = (Planet) planets.get(j);
				if (tempPlanet.getPlayerInControl() == tempPlayer2) {
					atLeastOnePlanet = true;
				}
			}
			if (atLeastOnePlanet) {
				winner = tempPlayer2;
			} else {
				winner = null;
			}
		}
		return winner;
	}

	public int findPlayerConfederacy(Player aPlayer, List<List<Player>> allConfederacies) {
		int foundIndex = -1;
		int tempIndex = 0;
		while ((foundIndex == -1) & (tempIndex < allConfederacies.size())) {
			List<Player> aConf = allConfederacies.get(tempIndex);
			if (aConf.contains(aPlayer)) {
				foundIndex = tempIndex;
			} else {
				tempIndex++;
			}
		}
		return foundIndex;
	}

	public Faction checkWinningFaction() {
		return checkWinningFaction(factionVictory);
	}

	// check if 1 faction has at least factionVictory(65) % of the total pop of all
	// planets in the game
	public Faction checkWinningFaction(int factionVictoryLimit) {
		Faction winner = null;
		// nolls�tt totalpop f�r alla factioner
		for (int i = 0; i < gameWorld.getFactions().size(); i++) {
			((Faction) gameWorld.getFactions().get(i)).setTotalPop(0);
		}
		int neutralPop = 0; // räkna popen på alla neutrala planeter
		// räkna popen på alla factioner
		for (int j = 0; j < planets.size(); j++) {
			Planet tempPlanet = planets.get(j);
			if (tempPlanet.getPlayerInControl() != null) {
				if (tempPlanet.getPlayerInControl().isAlien()) {
					tempPlanet.getPlayerInControl().getFaction().setTotalPop(
							tempPlanet.getPlayerInControl().getFaction().getTotalPop() + tempPlanet.getResistance());
				} else {
					tempPlanet.getPlayerInControl().getFaction().setTotalPop(
							tempPlanet.getPlayerInControl().getFaction().getTotalPop() + tempPlanet.getPopulation());
				}
			} else {
				neutralPop = neutralPop + tempPlanet.getPopulation();
			}
		}
		// summera all pop i sectorn
		int totalPop = neutralPop;
		for (int k = 0; k < gameWorld.getFactions().size(); k++) {
			totalPop = totalPop + gameWorld.getFactions().get(k).getTotalPop();
		}
		for (int l = 0; l < gameWorld.getFactions().size(); l++) {
			if (winner == null) {
				Faction tempFaction = gameWorld.getFactions().get(l);
				if ((((tempFaction).getTotalPop() * 1.0) / totalPop * 1.0) > (factionVictoryLimit / 100.0)) {
					winner = tempFaction;
				}
			}
		}
		return winner;
	}

	// check if 1 faction has at least total production >= factionVictoryLimit
	public Faction checkWinningFactionLimit(int factionVictoryLimit) {
		Faction winner = null;
		// nollsätt totalpop för alla factioner
		for (Faction aFaction : gameWorld.getFactions()) {
			aFaction.setTotalPop(0);
		}
		int neutralPop = 0; // räkna popen på alla neutrala planeter
		// räkna popen på alla factioner
		for (Planet aPlanet : planets) {
			if (aPlanet.getPlayerInControl() != null) {
				Faction tempFaction = aPlanet.getPlayerInControl().getFaction();
				int oldFactionProd = tempFaction.getTotalPop();
				if (aPlanet.getPlayerInControl().isAlien()) {
					tempFaction.setTotalPop(oldFactionProd + aPlanet.getResistance());
				} else {
					tempFaction.setTotalPop(oldFactionProd + aPlanet.getPopulation());
				}
			} else {
				neutralPop = neutralPop + aPlanet.getPopulation();
			}
		}
		// summera all pop i sectorn
		int totalPop = neutralPop;
		for (Faction aFaction : gameWorld.getFactions()) {
			totalPop = totalPop + aFaction.getTotalPop();
		}
		// kolla om det finns en vinnare
		for (Faction aFaction : gameWorld.getFactions()) {
			if (winner == null) {
				if (aFaction.getTotalPop() > factionVictoryLimit) {
					winner = aFaction;
				}
			}
		}
		return winner;
	}

	public Player checkWinningPlayer() {
		return checkWinningPlayer(singleVictory);
	}

	// check if 1 player has at least singleVictory (60) % of all pop in the game
	public Player checkWinningPlayer(int singleVictoryLimit) {
		Player winner = null;
		// nollsätt totalpop för alla factioner
		for (int i = 0; i < players.size(); i++) {
			((Player) players.get(i)).setTotalPop(0);
		}
		int neutralPop = 0; // räkna popen på alla neutrala planeter
		// räkna popen för alla spelare
		for (int j = 0; j < planets.size(); j++) {
			Planet tempPlanet = (Planet) planets.get(j);
			if (tempPlanet.getPlayerInControl() != null) {
				if (tempPlanet.getPlayerInControl().isAlien()) {
					tempPlanet.getPlayerInControl()
							.setTotalPop(tempPlanet.getPlayerInControl().getTotalPop() + tempPlanet.getResistance());
				} else {
					tempPlanet.getPlayerInControl()
							.setTotalPop(tempPlanet.getPlayerInControl().getTotalPop() + tempPlanet.getPopulation());
				}
			} else {
				neutralPop = neutralPop + tempPlanet.getPopulation();
			}
		}
		// summera all pop i sectorn
		int totalPop = neutralPop;
		for (int k = 0; k < players.size(); k++) {
			totalPop = totalPop + ((Player) players.get(k)).getTotalPop();
		}
		for (int l = 0; l < players.size(); l++) {
			if (winner == null) {
				Player tempPlayer = (Player) players.get(l);
				if ((((tempPlayer).getTotalPop() * 1.0) / totalPop * 1.0) > (singleVictoryLimit / 100.0)) {
					winner = tempPlayer;
				}
			}
		}
		return winner;
	}

	public void removeShip(Spaceship ss) {
		boolean ok;
		ss.setDestroyed();
		if (ss.isCarrier()) {
			removeSquadronsFromCarrier(ss);
		}
		ok = spaceships.remove(ss);
		if (!ok) {
			Logger.severe("Couldn't find spaceship to delete!!!");
		} // spårutskrift
	}

	public void removeTroop(Troop aTroop) {
		boolean ok;
		aTroop.setDestroyed();
		ok = troops.remove(aTroop);
		if (aTroop.getOwner() != null) { // only players can have vips on troops
			checkVIPsInDestroyedTroop(aTroop);
		}
		if (!ok) {
			Logger.finer("Couldn't find troop to delete!!!");
		} // sp�rutskrift
	}

	private void removeSquadronsFromCarrier(Spaceship aCarrier) {
		for (Iterator<Spaceship> iter = spaceships.iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if (aShip.isSquadron()) {
				if (aShip.getCarrierLocation() == aCarrier) {
					aShip.setCarrierLocation(null);
				}
			}
		}
	}

	public List<PlanetConnection> getPlanetConnections() {
		return planetConnections;
	}

	public void addPlanetConnections(PlanetConnection pc) {
		planetConnections.add(pc);
	}

	public int getNrPlayers() {
		return players.size();
	}

	public int getLongestGovenorName() {
		int longest = 0;
		for (int i = 0; i < players.size(); i++) {
			Player temp = (Player) players.get(i);
			if (temp.getGovernorName().length() > longest) {
				longest = temp.getGovernorName().length();
			}
		}
		return longest;
	}

	public int getNrActivePlayers() {
		return getActivePlayers().size();
	}

	public List<Player> getActivePlayers() {
		List<Player> tmpPlayers = new LinkedList<Player>();
		for (Player aPlayer : players) {
			if (!aPlayer.isDefeated()) {
				tmpPlayers.add(aPlayer);
			}
		}
		return tmpPlayers;
	}

	public int getNrFinishedPlayers() {
		int nrTemp = 0;
		for (int i = 0; i < players.size(); i++) {
			Player temp = (Player) players.get(i);
			if (!temp.isDefeated() & temp.isFinishedThisTurn()) {
				nrTemp = nrTemp + 1;
			}
		}
		return nrTemp;
	}

	public boolean isPlayerShipAtPlanet(Player aPlayer, Planet aPlanet) {
		boolean shipAtPlanet = false;
		for (int j = 0; j < spaceships.size(); j++) {
			Spaceship tempss = spaceships.get(j);
			if ((tempss.getOwner() == aPlayer) & (tempss.getLocation() == aPlanet)) {
				shipAtPlanet = true;
			}
		}
		return shipAtPlanet;
	}

	public boolean isPlayerTroopsAtPlanet(Player aPlayer, Planet aPlanet) {
		boolean troopAtPlanet = false;
		for (int j = 0; j < troops.size(); j++) {
			Troop tempt = (Troop) troops.get(j);
			if (tempt.getOwner() == aPlayer) {
				if (tempt.getPlanetLocation() == aPlanet) {
					troopAtPlanet = true;
				} else {
					if (tempt.getShipLocation() != null) {
						if (tempt.getShipLocation().getLocation() == aPlanet) {
							troopAtPlanet = true;
						}
					}
				}
			}
		}
		return troopAtPlanet;
	}

	public boolean isPlayerVIPAtPlanet(Player aPlayer, Planet aPlanet) {
		boolean VIPAtPlanet = false;
		for (int j = 0; j < allVIPs.size(); j++) {
			VIP tempVIP = (VIP) allVIPs.get(j);
			if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getLocation() == aPlanet)) {
				VIPAtPlanet = true;
			}
		}
		return VIPAtPlanet;
	}

	private VIP findStealthVIPonShip(Planet aPlanet, Spaceship aShip) {
		VIP foundVIP = null;
		int index = 0;
		while ((foundVIP == null) & (index < allVIPs.size())) {
			VIP aVIP = allVIPs.get(index);
			if (aVIP.getShipLocation() == aShip) {
				if (aVIP.isStealth()) {
					foundVIP = aVIP;
				}
			}
			if (foundVIP == null) {
				index++;
			}
		}
		return foundVIP;
	}

	public String getLargestShipSizeOnPlanet(Planet aPlanet, Player aPlayer, boolean civilian) {
		SpaceShipSize maxSize = null;
		for (Spaceship aShip : spaceships) {
			if ((aShip.getOwner() == aPlayer) & (aShip.getLocation() == aPlanet)) {
				if (aShip.isLookAsCivilian() == civilian) {
					VIP stealthVIP = findStealthVIPonShip(aPlanet, aShip);
					if (aShip.isVisibleOnMap() & (stealthVIP == null)) {
						if (maxSize == null || aShip.getType().getSize().getCompareSize() > maxSize.getCompareSize()) {
							maxSize = aShip.getType().getSize();
						}
					}
				}
			}
		}
		return maxSize != null ? maxSize.getName() : "";
	}

	public int getLargestLookAsMilitaryShipSizeOnPlanet(Planet aPlanet, Player aPlayer) {
		SpaceShipSize maxSize = null;
		for (Spaceship aShip : spaceships) {
			if ((aShip.getOwner() == aPlayer) & (aShip.getLocation() == aPlanet)) {
				if (!aShip.isLookAsCivilian()) {
					VIP stealthVIP = findStealthVIPonShip(aPlanet, aShip);
					if (aShip.isVisibleOnMap() & (stealthVIP == null)) {
						if (maxSize == null || aShip.getType().getSize().getCompareSize() > maxSize.getCompareSize()) {
							// Logger.info("aShip name" + aShip.getName());
							// Logger.info("aShip location" + aShip.getLocation());
							maxSize = aShip.getType().getSize();
						}
					}
				}
			}
		}
		return maxSize != null ? maxSize.getSlots() : -1;
	}

	/**
	 * Find the max ship size string to be shown on the map, ex: "small+civ"
	 * 
	 * @param aPlanet
	 * @param aPlayer
	 *            find largest size of ships belonging to other players/neutral
	 * @return
	 */
	public String getLargestShipSizeOnPlanet(Planet aPlanet, Player aPlayer) {
		String shipSize = "";
		int maxTonnage = 0;
		SpaceShipSize maxSize = null;
		boolean civ = false;
		for (Spaceship aShip : spaceships) {
			if ((aShip.getOwner() != aPlayer) & (aShip.getLocation() == aPlanet)) {
				if (aShip.isLookAsCivilian()) {
					civ = true;
				} else if (aShip.isVisibleOnMap()) {
					if (maxSize == null || aShip.getType().getSize().getCompareSize() > maxSize.getCompareSize()) {
						maxSize = aShip.getType().getSize();
					}
				}
			}
		}
		shipSize = maxSize != null ? maxSize.getName() : "";
		if (civ) {
			shipSize += "+civ";
		}
		return shipSize;
	}

	public SpaceShipSize getMaxResupplyFromShip(Planet aPlanet, Player aPlayer) {
		SpaceShipSize maxSize = SpaceShipSize.NONE;
		for (Iterator<Spaceship> ss = spaceships.iterator(); ss.hasNext();) {
			Spaceship aShip = ss.next();
			if (aShip.getLocation() == aPlanet) {
				if (aShip.getOwner() == aPlayer) {
					if (aShip.getType().getSupply().getCompareSize() > maxSize.getCompareSize()) {
						maxSize = aShip.getType().getSupply();
					}
				}
			}
		}
		return maxSize;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Planet> getAllDestinations(Planet location, boolean longRange) {
		List<Planet> alldest = new ArrayList<Planet>();
		BasePlanet tempPlanet = null;
		for (int i = 0; i < planetConnections.size(); i++) {
			PlanetConnection temppc = planetConnections.get(i);
			tempPlanet = temppc.getOtherEnd(location, longRange);
			if (tempPlanet != null) { // there is a connection within range
				alldest.add(getPlanet(tempPlanet.getName()));
			}
		}
		return alldest;
	}

	/**
	 * Returns 0 if there is no connection between the two planets
	 * 
	 * @param planet1
	 * @param planet2
	 * @return
	 */
	public SpaceshipRange getDistance(Planet planet1, Planet planet2) {
		SpaceshipRange dist = SpaceshipRange.NONE;
		for (int i = 0; i < planetConnections.size(); i++) {
			PlanetConnection temppc = (PlanetConnection) planetConnections.get(i);
			if (temppc.isConnection(planet1, planet2)) { // there is a connection within range
				if (temppc.isLongRange()) {
					dist = SpaceshipRange.LONG;
				} else {
					dist = SpaceshipRange.SHORT;
				}
			}
		}
		return dist;
	}

	public boolean playerHasShipsInSystem(Player aPlayer, Planet aPlanet) {
		boolean hasShipsInSystem = false;
		int i = 0;
		List<Spaceship> playerShips = getPlayersSpaceships(aPlayer);
		while ((i < playerShips.size()) & !hasShipsInSystem) {
			Spaceship tempss = playerShips.get(i);
			if (tempss.getLocation() == aPlanet) {
				hasShipsInSystem = true;
			} else {
				i++;
			}
		}
		return hasShipsInSystem;
	}

	public List<Spaceship> getPlayersSpaceshipsOnPlanet(Player aPlayer, Planet aPlanet) {
		List<Spaceship> playersss = new ArrayList<Spaceship>();
		for (int i = 0; i < spaceships.size(); i++) {
			Spaceship tempss = spaceships.get(i);
			if ((tempss.getOwner() == aPlayer) & (tempss.getLocation() == aPlanet)) {
				playersss.add(tempss);
			}
		}
		return playersss;
	}

	public List<Spaceship> findPlayersSpaceshipsOnPlanet(Player aPlayer, Planet aPlanet, SpaceshipRange range) {
		List<Spaceship> retShips = new LinkedList<Spaceship>();
		List<Spaceship> playersss = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		for (Spaceship aSpaceship : playersss) {
			if (aSpaceship.getRange() == range) {
				retShips.add(aSpaceship);
			}
		}
		return retShips;
	}

	public List<Troop> getPlayersTroopsOnPlanet(Player aPlayer, Planet aPlanet) {
		List<Troop> playerst = new LinkedList<Troop>();
		Logger.finest("galaxy getPlayersTroopsOnPlanet: aPlanet: " + aPlanet.getName());
		for (Troop aTroop : troops) {
			if (aTroop.getOwner() != null) {
				// Logger.finest("aTroop.getOwner() + aPlayer: " + aTroop.getOwner().getName() +
				// " " + aPlayer.getName());

				if (aTroop.getOwner() == aPlayer) {
					// Logger.finest("aTroop.getOwner() == aPlayer: True" );

					if (aTroop.getPlanetLocation() != null && aTroop.getPlanetLocation() == aPlanet) {
						// Logger.finest("aTroop.getPlanetLocation() + aPlanet: true");
						playerst.add(aTroop);
					} else {
						if (aTroop.getShipLocation() != null) {
							if (aTroop.getShipLocation().getLocation() != null) {
								Logger.finest("aTroop.getShipLocation().getLocation() + aPlanet: "
										+ aTroop.getShipLocation().getLocation().getName() + " " + aPlanet.getName());
								if (aTroop.getShipLocation().getLocation() == aPlanet) {
									Logger.finest("aTroop.getShipLocation().getLocation() + aPlanet: true");
									playerst.add(aTroop); // this should also cover troops in retreating ships
								}
								// }else{
								// Logger.finest("aTroop.getShipLocation().getLocation() = null. + aPlanet: " +
								// aPlanet.getName());
							}
						}
					}
				}
			}
		}
		return playerst;
	}

	public int getMaxBombardment(Planet aPlanet, Player aPlayer) {
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		int maxBombardment = 0;
		for (Spaceship ss : shipsAtPlanet) {
			if (ss.getBombardment() > maxBombardment) {
				maxBombardment = ss.getBombardment();
			}
		}
		return maxBombardment;
	}

	public boolean isPsychWarfare(Planet aPlanet, Player aPlayer) {
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		boolean tempPW = false;
		for (Spaceship ss : shipsAtPlanet) {
			if (ss.getPsychWarfare() > 0) {
				tempPW = true;
			}
		}
		return tempPW;
	}

	public int getMaxPsychWarfare(Planet aPlanet, Player aPlayer) {
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		int maxPW = 0;
		for (Spaceship ss : shipsAtPlanet) {
			if (ss.getPsychWarfare() > maxPW) {
				maxPW = ss.getPsychWarfare();
			}
		}
		return maxPW;
	}

	/*
	 * public boolean getTroops(Planet aPlanet, Player aPlayer){ List<Spaceship>
	 * shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet); boolean
	 * tempTroops = false; for (Spaceship ss : shipsAtPlanet) { if (ss.getTroops()){
	 * tempTroops = true; } } return tempTroops; }
	 */

	/**
	 * Get the number of troops on a planet, from other players / neutral
	 */
	public int getTroopsNrOnPlanet(Planet aPlanet, Player aPlayer) {
		int nrTroops = 0;
		for (Troop aTroop : troops) {
			if (aTroop.getPlanetLocation() == aPlanet) {
				if (aTroop.getOwner() != aPlayer) {
					nrTroops++;
				}
			}
		}
		return nrTroops;
	}

	public List<Troop> getTroopsOnPlanet(Planet aPlanet, Player aPlayer) {
		return getTroopsOnPlanet(aPlanet, aPlayer, true);
	}

	public List<Troop> getTroopsOnPlanet(Planet aPlanet, Player aPlayer, boolean showUnVisible) {
		List<Troop> troopsAtPlanet = new LinkedList<Troop>();
		for (Troop aTroop : troops) {
			if (aTroop.getPlanetLocation() == aPlanet) {
				if (aTroop.getOwner() == aPlayer) {
					if (showUnVisible || aTroop.getTroopType().isVisible()) {
						troopsAtPlanet.add(aTroop);
					}
				}
			}
		}
		return troopsAtPlanet;
	}

	public List<Troop> getTroopsOnShip(Spaceship sShip) {
		List<Troop> troopsAtPlanet = new LinkedList<Troop>();
		for (Troop aTroop : troops) {
			if (aTroop.getShipLocation() == sShip) {
				troopsAtPlanet.add(aTroop);
			}
		}
		return troopsAtPlanet;
	}

	public List<Troop> getTroops() {
		return troops;
	}

	public VIP getPsychWarfareBonusVIPs(Planet aPlanet, Player aPlayer) {
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		VIP highestPsychWarfareVIP = null;
		for (Spaceship ss : shipsAtPlanet) {
			VIP aVIP = findHighestVIPPsychWarfareBonus(ss, aPlayer);
			if (aVIP != null) {
				if (highestPsychWarfareVIP == null) {
					highestPsychWarfareVIP = aVIP;
				} else if (aVIP.getPsychWarfareBonus() > highestPsychWarfareVIP.getPsychWarfareBonus()) {
					highestPsychWarfareVIP = aVIP;
				}
			}
		}
		return highestPsychWarfareVIP;
	}

	public List<Spaceship> getPlayersSpaceships(Player aPlayer) {
		List<Spaceship> playersss = new ArrayList<Spaceship>();
		for (int i = 0; i < spaceships.size(); i++) {
			Spaceship tempss = spaceships.get(i);
			if (tempss.getOwner() == aPlayer) {
				playersss.add(tempss);
			}
		}
		return playersss;
	}

	public List<Spaceship> getRetreatingShips(Player aPlayer) {
		List<Spaceship> allShips = getPlayersSpaceships(aPlayer);
		List<Spaceship> rShips = new ArrayList<Spaceship>();
		for (Iterator<Spaceship> iter = allShips.iterator(); iter.hasNext();) {
			Spaceship tmpss = iter.next();
			// if (tmpss.getRetreatingTo() != null){
			if (tmpss.isRetreating()) {
				rShips.add(tmpss);
			}
		}
		return rShips;
	}

	public List<VIP> getPlayersVips(Player aPlayer) {
		List<VIP> playersv = new LinkedList<VIP>();
		for (VIP tempv : allVIPs) {
			if (tempv.getBoss() == aPlayer) {
				playersv.add(tempv);
			}
		}
		return playersv;
	}

	public List<Troop> getPlayersTroops(Player aPlayer) {
		List<Troop> playersTroops = new LinkedList<Troop>();
		for (int i = 0; i < troops.size(); i++) {
			Troop tempTroop = (Troop) troops.get(i);
			if (tempTroop.getOwner() == aPlayer) {
				playersTroops.add(tempTroop);
			}
		}
		return playersTroops;
	}

	public List<Planet> getPlayersPlanets(Player aPlayer) {
		List<Planet> playersPlanets = new ArrayList<Planet>();
		for (int i = 0; i < planets.size(); i++) {
			Planet tempPlanet = (Planet) planets.get(i);
			if (tempPlanet.getPlayerInControl() == aPlayer) {
				playersPlanets.add(tempPlanet);
			}
		}
		return playersPlanets;
	}

	public int getNumberUnbesiegedPlayerPlanets(Player player) {
		List<Planet> playerPlanets = getPlayersPlanets(player);
		int nr = 0;
		for (Planet aPlanet : playerPlanets) {
			if (!aPlanet.isBesieged()) {
				nr++;
			}
		}
		return nr;
	}

	public List<Spaceship> getSpaceships() {
		return spaceships;
	}

	/**
	 * Return a list with all spaceships at aPlanet. If civilian is false, only
	 * military ships are returned.
	 * 
	 * @param aPlanet
	 *            a planet
	 * @param civilian
	 *            if only civilian ships should be returned
	 * @return a list of civilian or military ships at the aPlanet
	 */
	public List<Spaceship> getShips(Planet aPlanet, boolean civilian) {
		List<Spaceship> ships = new LinkedList<Spaceship>();
		for (Spaceship aShip : spaceships) {
			if ((aShip.getLocation() != null) && (aShip.getLocation() == aPlanet)) {
				if (aShip.isCivilian() == civilian) {
					ships.add(aShip);
				}
			}
		}
		return ships;
	}

	/**
	 * Return a list with all spaceships belonging to the planets player (can be
	 * neutral) at aPlanet.
	 * 
	 * @param aPlanet
	 *            a planet
	 * @return a list of ships at the aPlanet
	 */
	public List<Spaceship> getShips(Planet aPlanet) {
		List<Spaceship> ships = new LinkedList<Spaceship>();
		for (Iterator<Spaceship> iter = spaceships.iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if ((aShip.getLocation() != null) && (aShip.getLocation() == aPlanet)) {
				if (aShip.getOwner() == aPlanet.getPlayerInControl()) {
					ships.add(aShip);
				}
			}
		}
		return ships;
	}

	public void addSpaceship(Spaceship tempss) {
		spaceships.add(tempss);
	}

	public Faction findFaction(String uniqueFactionName) {
		int index = 0;
		Faction found = null;
		while ((index < gameWorld.getFactions().size()) & (found == null)) {
			Faction tempFaction = gameWorld.getFactions().get(index);
			if (tempFaction.getName().equalsIgnoreCase(uniqueFactionName)) {
				found = tempFaction;
			} else {
				index++;
			}
		}
		return found;
	}

	public Spaceship findEmptyShipWithoutOrders(Player aPlayer, Planet aPlanet, String shipTypeName) {
		Spaceship foundShip = null;
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		for (Spaceship aShip : shipsAtPlanet) {
			if (aShip.getName().equals(shipTypeName)) {
				if (!aPlayer.getOrders().checkShipMove(aShip)) { // if there are no order already for this ship
					if (findAllVIPsOnShip(aShip).size() == 0) { // check that ship is empty (no carried VIPs)
						foundShip = aShip;
					}
				}
			}
		}
		return foundShip;
	}

	/**
	 * Used for VIP who should move toward a planet
	 */
	public Planet findFirstJumpTowardsPlanet(Planet aLocation, Planet aDestination, Spaceship aSpaceship) {
		return findFirstJumpTowardsPlanet(aLocation, aDestination, aSpaceship.getRange());
	}

	public Planet findFirstJumpTowardsPlanet(Planet aLocation, Planet aDestination, SpaceshipRange aSpaceshipRange) {
		Logger.finer("findFirstJumpTowardsPlanet aDestination: " + aDestination.getName());
		Planet firstStopPlanet = null;
		boolean found = false;
		// s�tt reachFrom p� startplaneten s� den blir rotnod
		aLocation.setReachFrom(null);
		List<Planet> edgePlanets = new LinkedList<Planet>(); // de planeter som är på gränsen till det genomsökta
																// området
		edgePlanets.add(aLocation);
		List<Planet> newEdgePlanets = new LinkedList<Planet>(); // de planeter som är på gränsen till det genomsökta
																// området
		List<Planet> searchedPlanets = new LinkedList<Planet>(); // lägg in alla som genomsökts + startplaneten
		searchedPlanets.add(aLocation);
		List<Planet> allNeighbours;
		// loopa tills alla planeter har letats igenom eller minst 1 lämplig planet har
		// hittats
		while (!found) {
			Logger.finest("while (!found) found: " + found);
			// Gå igenom alla edgePlanets
			for (int i = 0; i < edgePlanets.size(); i++) {
				Planet tempPlanet = (Planet) edgePlanets.get(i);
				Logger.finest("tempPlanet: " + tempPlanet.getName());
				// Hämta alla grannar till tempPlanet
				allNeighbours = getAllDestinations(tempPlanet, aSpaceshipRange == SpaceshipRange.LONG);
				// Gå igenom alla allNeighbours (lägg i newEdgePlanets)
				for (int j = 0; j < allNeighbours.size(); j++) {
					Planet tempNeighbourPlanet = allNeighbours.get(j);
					Logger.finest("tempNeighbourPlanet: " + tempNeighbourPlanet.getName());
					// kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
					if ((!Galaxy.containsPlanet(searchedPlanets, tempNeighbourPlanet))
							& (!Galaxy.containsPlanet(newEdgePlanets, tempNeighbourPlanet))) {
						Logger.finest(
								"containsPlanet: " + !Galaxy.containsPlanet(searchedPlanets, tempNeighbourPlanet));
						Logger.finest("containsPlanet: " + !Galaxy.containsPlanet(newEdgePlanets, tempNeighbourPlanet));
						Logger.finest("inside if: ");
						// sätt reachFrom så det går att hitta pathen senare
						tempNeighbourPlanet.setReachFrom(tempPlanet);
						// lägg i newEdgePlanets
						newEdgePlanets.add(tempNeighbourPlanet);
						// kolla om det är den eftersökta planeten
						if (tempNeighbourPlanet == aDestination) {
							Logger.finest("found = true ");
							found = true;
						}
					}
				}
			}
			// töm edgePlanets
			edgePlanets.clear();
			for (int l = 0; l < newEdgePlanets.size(); l++) {
				// kopiera över newEdgePlanets till edgePlanets
				edgePlanets.add((Planet) newEdgePlanets.get(l));
				// kopiera över newEdgePlanets till searchedPlanets
				searchedPlanets.add((Planet) newEdgePlanets.get(l));
			}
			// töm newEdgePlanets
			newEdgePlanets.clear();
		}
		Planet lastStop = aDestination;
		// loopa tills reachFrom är null
		Logger.finest("before while, lastStop: " + lastStop.getName());
		while (lastStop.getReachFrom().getReachFrom() != null) {
			Logger.finest("");
			Logger.finest("inside if: " + lastStop.getReachFrom().getName());
			Logger.finest("inside if: " + lastStop.getReachFrom().getReachFrom().getName());
			lastStop = lastStop.getReachFrom();
		}
		firstStopPlanet = lastStop;
		return firstStopPlanet;
	}

	public static boolean containsPlanet(List<Planet> v, Planet p) {
		boolean found = false;
		int i = 0;
		while ((i < v.size()) & (!found)) {
			Planet tempp = (Planet) v.get(i);
			if (tempp == p) {
				found = true;
			} else {
				i++;
			}
		}
		return found;
	}

	/*
	 * public void sendMessageToAllPlayers(Message aMessage){ for (int i = 0; i <
	 * players.size(); i++){ Player aPlayer = (Player)players.get(i); if ((aPlayer
	 * != aMessage.getSender(this)) & (!aPlayer.isDefeated())){
	 * aPlayer.addToLatestRecievedMessages(aMessage);
	 * aPlayer.addToHighlights(aMessage.getSender(this).getGovenorName(),Highlight.
	 * TYPE_MESSAGE_PUBLIC); } } }
	 * 
	 * public void sendMessageToFactionPlayers(Message aMessage){ for (int i = 0; i
	 * < players.size(); i++){ Player aPlayer = (Player)players.get(i); if ((aPlayer
	 * != aMessage.getSender(this)) & (!aPlayer.isDefeated())){ if
	 * (aPlayer.getFaction().getName().equalsIgnoreCase(aMessage.getRecipientFaction
	 * ())){ aPlayer.addToLatestRecievedMessages(aMessage);
	 * aPlayer.addToHighlights(aMessage.getSender(this).getGovenorName() + ";" +
	 * aMessage.getRecipientFaction(),Highlight.TYPE_MESSAGE_FACTION); } } } }
	 */

	/**
	 * Get numbers of player in a faction
	 */
	public int getFactionMemberNr(Faction aFaction) {
		int nr = 0;
		for (int i = 0; i < players.size(); i++) {
			Player aPlayer = (Player) players.get(i);
			if (aPlayer.getFaction() == aFaction) {
				nr++;
			}
		}
		return nr;
	}

	/**
	 * Get numbers of player in a faction still alive.
	 * 
	 * @param aFaction
	 * @return
	 */
	public int getFactionLivingMemberNr(Faction aFaction) {
		int nr = 0;
		for (int i = 0; i < players.size(); i++) {
			Player aPlayer = (Player) players.get(i);
			if (aPlayer.getFaction() == aFaction && !aPlayer.isDefeated()) {
				nr++;
			}
		}
		return nr;
	}

	public List<Player> getFactionMember(Faction aFaction) {
		List<Player> factionPlayers = new ArrayList<Player>();
		for (int i = 0; i < players.size(); i++) {
			Player aPlayer = (Player) players.get(i);
			if (aPlayer.getFaction() == aFaction) {
				factionPlayers.add(aPlayer);
			}
		}
		return factionPlayers;
	}

	public int getUndefeatedFactionMemberNr(Faction aFaction) {
		int nr = 0;
		for (int i = 0; i < players.size(); i++) {
			Player aPlayer = (Player) players.get(i);
			if (aPlayer.getFaction() == aFaction) {
				if (!aPlayer.isDefeated()) {
					nr++;
				}
			}
		}
		return nr;
	}

	public List<Faction> getFactions() {
		return gameWorld.getFactions();
	}

	public List<VIP> getAllVIPs() {
		return allVIPs;
	}

	public VIP findVIP(String aUniqueId) {
		int index = 0;
		VIP found = null;
		while ((index < allVIPs.size()) & (found == null)) {
			VIP tempVIP = (VIP) allVIPs.get(index);
			if (tempVIP.getUniqueId().equals(aUniqueId)) {
				found = tempVIP;
			} else {
				index++;
			}
		}
		return found;
	}

	public VIPType findVIPType(String vipTypeName) {
		int index = 0;
		VIPType found = null;
		while ((index < gameWorld.getVipTypes().size()) & (found == null)) {
			VIPType tempVIPType = gameWorld.getVipTypes().get(index);
			// System.out.println(tempVIPType.getName() + " = " + vipTypeName);
			if (tempVIPType.getName().equalsIgnoreCase(vipTypeName)) {
				found = tempVIPType;
			} else {
				index++;
			}
		}
		return found;
	}

	public VIPType findVIPTypeShortName(String vipTypeShortName) {
		int index = 0;
		VIPType found = null;
		while ((index < gameWorld.getVipTypes().size()) & (found == null)) {
			VIPType tempVIPType = gameWorld.getVipTypes().get(index);
			if (tempVIPType.getShortName().equalsIgnoreCase(vipTypeShortName)) {
				found = tempVIPType;
			} else {
				index++;
			}
		}
		return found;
	}

	public SpaceshipType getRandomCommonShiptype() {
		SpaceshipType returnType = null;
		SpaceshipType tempShipType = null;
		List<SpaceshipType> allAvailableTypes = getShiptypesToBlackMarket();
		int totalFrequencypoint = 0;
		for (SpaceshipType spaceshipType : allAvailableTypes) {
			totalFrequencypoint += spaceshipType.getBlackMarketFrequency().getFrequency();
		}

		int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
		int counter = 0;
		int tmpFreqSum = 0;
		while (returnType == null) {
			tempShipType = allAvailableTypes.get(counter);
			tmpFreqSum = tmpFreqSum + tempShipType.getBlackMarketFrequency().getFrequency();
			if (tmpFreqSum > freqValue) {
				returnType = tempShipType;
			}
			counter++;
		}
		return returnType;
	}

	public TroopType getRandomCommonTroopType() {
		TroopType aTroopType = null;
		TroopType tempTroopType = null;
		List<TroopType> allAvailableTroopTypes = getTroopTypesToBlackMarket();
		int totalFrequencypoint = 0;
		for (TroopType troopType : allAvailableTroopTypes) {
			totalFrequencypoint += troopType.getBlackMarketFrequency().getFrequency();
		}

		int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
		int counter = 0;
		int tmpFreqSum = 0;
		while (aTroopType == null) {
			tempTroopType = allAvailableTroopTypes.get(counter);
			tmpFreqSum = tmpFreqSum + tempTroopType.getBlackMarketFrequency().getFrequency();
			if (tmpFreqSum > freqValue) {
				aTroopType = tempTroopType;
			}
			counter++;
		}

		return aTroopType;
	}

	/**
	 * Searches through all sst lists for all factions, limited by the turn number
	 * (no medium+ on turn 1 etc)
	 * 
	 * @return
	 */
	private List<SpaceshipType> getShiptypesToBlackMarket() {
		Logger.fine("getShiptypesToBlackMarket() called");
		List<SpaceshipType> ssTypes = new LinkedList<SpaceshipType>();
		// LoggingHandler.fine("Faction: " + aFaction.getName(),this);
		// LoggingHandler.fine("Ships nr: " + tmpSsTypes.size(),this);
		for (SpaceshipType aSST : gameWorld.getShipTypes()) {
			if (aSST.isReadyToUseInBlackMarket(this)) {
				ssTypes.add(aSST);
			}
		}
		return ssTypes;
	}

	/**
	 * Searches through all trooptype lists for all factions
	 * 
	 * @return list containing trooptypes. If a trooptype can be build by several
	 *         factions it will appear several times in the list
	 */
	private List<TroopType> getTroopTypesToBlackMarket() {
		Logger.fine("getTroopTypesToBlackMarket() called");
		List<TroopType> troopTypes = new LinkedList<TroopType>();
		for (Faction aFaction : gameWorld.getFactions()) {
			Logger.finer("Faction: " + aFaction.getName());
			List<TroopType> factionTroopTypes = aFaction.getTroopTypes();
			Logger.finer("TroopTypes #: " + factionTroopTypes.size());
			for (TroopType aTroopType : factionTroopTypes) {
				Logger.finer("TT: " + aTroopType.getUniqueName());

				if (aTroopType.isReadyToUseInBlackMarket(this)) {
					if (!troopTypes.contains(aTroopType)) {
						troopTypes.add(aTroopType);
					}
				}
			}
		}
		return troopTypes;
	}

	public VIPType getRandomVIPType() {
		VIPType returnType = null;
		Logger.finer("getRandomVIPType()");
		int totFrequencySum = getTotalVIPFrequencySum();
		Logger.finer(String.valueOf(totFrequencySum));
		int freqValue = Functions.getRandomInt(0, totFrequencySum - 1);
		Logger.finer(String.valueOf(freqValue));
		returnType = getVipFromFrequency(freqValue);
		Logger.finer(returnType.getName());
		return returnType;
	}

	private int getTotalVIPFrequencySum() {
		int total = 0;
		for (VIPType aVipType : gameWorld.getVipTypes()) {
			if (aVipType.isReadyToUseInBlackMarket(this)) {
				total = total + aVipType.getFrequency().getFrequency();
			}
		}
		return total;
	}

	private VIPType getVipFromFrequency(int freqValue) {
		VIPType aVipType = null;
		int tmpFreqSum = 0;
		VIPType tmpVipType = null;
		int counter = 0;
		Logger.finer(String.valueOf(freqValue));
		while (aVipType == null) {
			tmpVipType = gameWorld.getVipTypes().get(counter);
			if (tmpVipType.isReadyToUseInBlackMarket(this)) {
				tmpFreqSum = tmpFreqSum + tmpVipType.getFrequency().getFrequency();
				Logger.finest("tmpFreqSum: " + tmpFreqSum);
				if (tmpFreqSum > freqValue) {
					aVipType = tmpVipType;
				}
			}
			counter++;
		}
		return aVipType;
	}

	// add Black Market message to all except aPlayer
	public void addBlackMarketMessages(Player aPlayer, String aMessage) {
		for (int i = 0; i < players.size(); i++) {
			Player tempPlayer = (Player) players.get(i);
			if ((aPlayer != tempPlayer) & (!tempPlayer.isDefeated())) {
				tempPlayer.addToLatestBlackMarketMessages(aMessage);
			}
		}
	}

	public String getStatus() {
		String retStr = "Active";
		if (!lastUpdateComplete) {
			retStr = "Error";
		} else if (turn == 0) {
			retStr = "Starting";
		} else if (gameEnded) {
			retStr = "Game Over";
		}
		return retStr;
	}

	public Faction findPlayerFaction(String playerName) {
		Faction foundFaction = null;
		int i = 0;
		while ((i < players.size()) & (foundFaction == null)) {
			Player tempPlayer = (Player) players.get(i);
			if (tempPlayer.getName().equalsIgnoreCase(playerName)) {
				foundFaction = tempPlayer.getFaction();
			} else {
				i++;
			}
		}
		return foundFaction;
	}

	public String getGameName() {
		return gameName;
	}

	public void setAutoBalance(boolean newAutoBalance) {
		autoBalance = newAutoBalance;
	}

	public boolean getAutoBalance() {
		return autoBalance;
	}

	public void setHasAutoUpdated(boolean newValue) {
		hasAutoUpdated = newValue;
	}

	public boolean hasAutoUpdated() {
		return hasAutoUpdated;
	}

	public void newPlayerPassword(String login, String newPassword) {
		for (Player aPlayer : players) {
			if (aPlayer.isPlayer(login)) {
				Logger.fine("New password for player: " + login);
				aPlayer.setPassword(newPassword);
			}
		}
	}

	/**
	 * Create highlights and general info to all other players
	 * 
	 * @param defeatedPlayer
	 *            the player who was defeated, skip him
	 */
	public void playerDefeated(Player defeatedPlayer) {
		for (Player aPlayer : players) {
			if (!aPlayer.isPlayer(defeatedPlayer)) {
				Logger.finest("Create messages about defeated player");
				aPlayer.addToHighlights(defeatedPlayer.getGovernorName(), HighlightType.TYPE_DEFEATED_OTHER_PLAYER);
				aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovernorName() + " has been defeated.");
				aPlayer.addToGeneral("");
			}
		}
	}

	/**
	 * Create highlights (and general info?) to all other players
	 * 
	 * @param defeatedPlayer
	 *            the player who was defeated, skip him
	 */
	public void govenorKilled(Player defeatedPlayer) {
		for (Player aPlayer : players) {
			if (!aPlayer.isPlayer(defeatedPlayer)) {
				Logger.finest("Create messages about killed governor");
				// aPlayer.addToHighlights(defeatedPlayer.getGovenorName(),Highlight.TYPE_GOVENOR_KILLED_OTHER_PLAYER);
				aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovernorName() + " has been killed.");
				aPlayer.addToGeneral("");
			}
		}
	}

	/**
	 * Create general info to all other players
	 * 
	 * @param defeatedPlayer
	 *            the player who was defeated, skip him
	 */
	public void noPlanetsOrShips(Player defeatedPlayer) {
		for (Player aPlayer : players) {
			if (!aPlayer.isPlayer(defeatedPlayer)) {
				Logger.finest("Create messages about a player who has no planets or ships");
				// aPlayer.addToHighlights(defeatedPlayer.getGovenorName(),Highlight.TYPE_NO_SHIPS_NO_PLANETS_OTHER_PLAYER);
				aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovernorName() + " has no planets or ships left.");
				aPlayer.addToGeneral("");
			}
		}
	}

	/**
	 * Create general info to all other players
	 * 
	 * @param defeatedPlayer
	 *            the player who was defeated, skip him
	 */
	public void playerAbandonsGame(Player defeatedPlayer) {
		Logger.finest("Create messages about a player who has abandoned this game");
		for (Player aPlayer : players) {
			if (!aPlayer.isPlayer(defeatedPlayer)) {
				aPlayer.addToHighlights(defeatedPlayer.getGovernorName(),
						HighlightType.TYPE_GAME_ABANDONED_OTHER_PLAYER);
				aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovernorName() + " has left this quadrant (player "
						+ defeatedPlayer.getName() + " has abandoned this game).");
				aPlayer.addToGeneral("");
			}
		}
	}

	/**
	 * Create general info to all other players
	 * 
	 * @param removedPlayer
	 *            the player who was defeated, skip him
	 */
	public void playerRemovedBrokeGame(Player removedPlayer) {
		Logger.finest(
				"Create messages about a player who has been removed from this game due to being broke for 5 turns");
		for (Player aPlayer : players) {
			if (!aPlayer.isPlayer(removedPlayer)) {
				aPlayer.addToHighlights(removedPlayer.getGovernorName(),
						HighlightType.TYPE_GAME_BROKE_REMOVED_OTHER_PLAYER);
				aPlayer.addToGeneral("Governor " + removedPlayer.getGovernorName() + " (player "
						+ removedPlayer.getName() + ") has been broke for 5 turns and have lost the game.");
				aPlayer.addToGeneral("");
			}
		}
	}

	public void setLastUpdateComplete(boolean lastUpdateComplete) {
		this.lastUpdateComplete = lastUpdateComplete;
	}

	public boolean getLastUpdateComplete() {
		return lastUpdateComplete;
	}

	public void addToLastLog(String addString) {
		if (lastLog == null) {
			lastLog = new StringBuffer(addString);
		} else {
			lastLog.append(addString);
		}
	}

	public void clearLastLog() {
		if (lastLog != null) {
			lastLog = null;
		}
	}

	public String getLastLog() {
		String retVal = "Log is empty";
		if (lastLog != null) {
			retVal = lastLog.toString();
		}
		return retVal;
	}

	public List<VIP> getAllDiplomatsOnNeutralPlanets() {
		List<VIP> allDiplomats = new LinkedList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.isDiplomat() & !tempVIP.getBoss().isAlien()) { // aliens can not use diplomacy
				Planet tempLocation = tempVIP.getPlanetLocation();
				if (tempLocation != null) { // tempVIP �r vid en planet
					if ((tempLocation.getPlayerInControl() == null) & !tempLocation.isRazed()) { // planeten �r neutral
						allDiplomats.add(tempVIP);
					}
				}
			}
		}
		return allDiplomats;
	}

	public List<VIP> getPlayerDiplomatsOnPlanet(Planet aPlanet, Player aPlayer) {
		List<VIP> diplomatsOnPlanet = new ArrayList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.isDiplomat() & !tempVIP.getBoss().isAlien()) { // aliens can not use diplomacy
				Planet tempLocation = tempVIP.getPlanetLocation();
				if (tempLocation == aPlanet) { // tempVIP is on aPlanet
					if (tempVIP.getBoss() == aPlayer) { // tempVIP belongs to aPlayer
						diplomatsOnPlanet.add(tempVIP);
					}
				}
			}
		}
		return diplomatsOnPlanet;
	}

	public List<VIP> getAllInfestatorsOnPlanet(Planet aPlanet, Player aPlayer) {
		Logger.finer("getAllInfestatorsOnPlanet() called");
		List<VIP> allInfestatorOnPlanet = new LinkedList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.isInfestator() & tempVIP.getBoss().isAlien()) {
				Planet tempLocation = tempVIP.getPlanetLocation();
				if (tempLocation == aPlanet) {
					if (tempVIP.getBoss() == aPlayer) {
						allInfestatorOnPlanet.add(tempVIP);
					}
				}
			}
		}
		return allInfestatorOnPlanet;
	}

	public List<VIP> getAllInfestatorsOnPlanets() {
		Logger.finer("getAllInfestatorsOnPlanets() called");
		List<VIP> allInfestators = new LinkedList<VIP>();
		Logger.finest("allVIPs.size(): " + allVIPs.size());
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			Logger.finest("tempVIP: " + tempVIP.getName());
			Logger.finest("tempVIP.isInfestator(): " + tempVIP.isInfestator());
			Logger.finest("tempVIP.getBoss().isAlien(): " + tempVIP.getBoss().isAlien());
			Logger.finest("tempVIP.getAlignment(): " + tempVIP.getAlignment());
			Logger.finest(
					"tempVIP.getBoss().getFaction().getAlignment(): " + tempVIP.getBoss().getFaction().getAlignment());
			if (tempVIP.isInfestator() & tempVIP.getBoss().isAlien()
					& tempVIP.getAlignment().equals(tempVIP.getBoss().getFaction().getAlignment())) { // only
																										// infestators
																										// with the same
																										// alignment as
																										// a player can
																										// use
																										// infestation
				Planet tempLocation = tempVIP.getPlanetLocation();
				if (tempLocation != null) { // tempVIP �r vid en planet
					Logger.finest("tempLocation != null ");
					Logger.finest("tempLocation: " + tempLocation.getName());
					Player owner = tempLocation.getPlayerInControl();
					if ((owner == null) || ((owner != tempVIP.getBoss()) & !owner.isAlien()
							& !(owner.getFaction().equals(tempVIP.getBoss().getFaction())))) { // planet is neutral or
																								// belongs to another
																								// non-alien player from
																								// another faction
						Logger.finest("can infest");
						if (tempLocation.getPopulation() > 0) { // can not infestate razed planets, troops are needed
																// for that
							Logger.finest("tempLocation.getPopulation() > 0");
							allInfestators.add(tempVIP);
						}
					}
				}
			}
		}
		return allInfestators;
	}

	public List<VIP> getAllGovsOnNeutralPlanets() {
		List<VIP> allGovs = new LinkedList<VIP>();
		for (VIP tempVIP : allVIPs) {
			if (tempVIP.isGovernor()) {
				Planet tempLocation = tempVIP.getPlanetLocation();
				if (tempLocation != null) { // Gov �r vid en planet
					if (tempLocation.getPlayerInControl() == null) { // planeten �r neutral
						allGovs.add(tempVIP);
					}
				}
			}
		}
		return allGovs;
	}

	public List<VIP> getAllDiplomatsNotOnNeutralPlanets() {
		List<VIP> allDips = new LinkedList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.isDiplomat()) {
				Planet tempLocation = tempVIP.getPlanetLocation();
				if (tempLocation != null) { // Gov �r vid en planet
					if (tempLocation.getPlayerInControl() != null) { // planeten �r inte neutral
						allDips.add(tempVIP);
					}
				} else {
					allDips.add(tempVIP);
				}
			}
		}
		return allDips;
	}

	public List<VIP> getAllInfestatorsNotOnActionPlanets() {
		List<VIP> allInfs = new LinkedList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.isInfestator()) {
				Planet tempLocation = tempVIP.getPlanetLocation();
				if (tempLocation == null) { // inf is not at a planet
					allInfs.add(tempVIP);
				} else if (tempLocation.getPopulation() == 0) { // razed cannot be infed
					allInfs.add(tempVIP);
				} else {
					Player owner = tempLocation.getPlayerInControl();
					if (owner == null) { // can inf neutral planet!
						// do nothing, ok to infestate!
					} else if (owner == tempVIP.getBoss()) { // cannot inf own planet
						allInfs.add(tempVIP);
					} else if (owner.isAlien()) { // cannot inf alien planets
						allInfs.add(tempVIP);
					} else if (owner.getFaction().equals(tempVIP.getBoss().getFaction())) { // cannot inf same factions
																							// planets
						allInfs.add(tempVIP);
					}
				}
			}
		}
		return allInfs;
	}

	public String getStartedByPlayer() {
		return startedByPlayer;
	}

	public void setStartedByPlayer(String startedByPlayer) {
		this.startedByPlayer = startedByPlayer;
	}

	public List<Spaceship> getHostileShipsAtPlanet(Planet aPlanet) {
		List<Spaceship> shipsAtPlanet = new ArrayList<Spaceship>();
		for (Iterator<Spaceship> iter = spaceships.iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if (aShip.getLocation() == aPlanet) {
				if (aShip.getOwner() != aPlanet.getPlayerInControl()) {
					shipsAtPlanet.add(aShip);
				}
			}
		}
		return shipsAtPlanet;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld aGameWorld) {
		gameWorld = aGameWorld;
	}

	public boolean isFTLMasterOnShip(Spaceship aShip) {
		boolean found = false;
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP tempVIP = (VIP) allVIPs.get(i);
			if (tempVIP.getShipLocation() == aShip) {
				if (tempVIP.isFTLbonus()) {
					Orders orders = aShip.getOwner().getOrders();
					if (orders.VIPWillStay(tempVIP)) {
						found = true;
					}
				}
			}
		}
		return found;
	}

	public List<Faction> getActiveFactions(Faction exceptionFaction) {
		List<Faction> allFactions = new ArrayList<>();
		for (Faction aFaction : gameWorld.getFactions()) {
			if (!aFaction.getName().equalsIgnoreCase(exceptionFaction.getName())) {
				allFactions.add(aFaction);
			}
		}
		return allFactions;
	}

	public boolean isGameOver() {
		return gameEnded;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isGroupSameFaction() {
		return groupSameFaction;
	}

	public void setGroupSameFaction(boolean groupSameFaction) {
		this.groupSameFaction = groupSameFaction;
	}

	public String getMapNameFull() {
		return mapFullName;
	}

	public int getNoSquadronsAssignedToCarrier(Spaceship aCarrier) {
		int count = 0;
		Player aPlayer = aCarrier.getOwner();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aCarrier.getLocation());
		for (Spaceship aSpaceship : shipsAtPlanet) {
			if (aSpaceship.getCarrierLocation() == aCarrier) {
				// check if sstemp has a move order
				if (aPlayer != null) {
					boolean moveToPlanetOrder = aPlayer.checkShipMove(aSpaceship);
					boolean moveToCarrierOrder = aPlayer.checkShipToCarrierMove(aSpaceship);
					// if not, inc counter
					if (!(moveToCarrierOrder | moveToPlanetOrder)) {
						count++;
					}
				} else {
					count++;
				}
			}
		}
		return count;
	}

	public int getNoTroopsAssignedToCarrier(Spaceship aCarrier) {
		int count = 0;
		Player aPlayer = aCarrier.getOwner();
		List<Troop> troopsAtPlanet = getPlayersTroopsOnPlanet(aPlayer, aCarrier.getLocation());
		for (Troop aTroop : troopsAtPlanet) {
			if (aTroop.getShipLocation() == aCarrier) {
				// check if sstemp has a move order
				if (aPlayer != null) {
					boolean moveOrder = aPlayer.checkTroopMove(aTroop);
					// if not, inc counter
					if (!moveOrder) {
						count++;
					}
				}
			}
		}
		return count;
	}

	public int getNoSquadronsMovingToCarrier(Spaceship aCarrier) {
		int count = 0;
		Player aPlayer = aCarrier.getOwner();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aCarrier.getLocation());
		for (Spaceship aSpaceship : shipsAtPlanet) {
			if (aSpaceship.isSquadron()) {
				// check if sstemp has a move order to the carrier
				if (aPlayer != null) {
					boolean moveToCarrierOrder = aPlayer.checkShipToCarrierMove(aSpaceship, aCarrier);
					if (moveToCarrierOrder) {
						count++;
					}
				}
			}
		}
		return count;
	}

	public int getNrTroopsMovingToCarrier(Spaceship aCarrier) {
		int count = 0;
		Player aPlayer = aCarrier.getOwner();
		List<Troop> troopsAtPlanet = getPlayersTroopsOnPlanet(aPlayer, aCarrier.getLocation());
		for (Troop aTroop : troopsAtPlanet) {
			// check if a has a move order to the carrier
			if (aPlayer != null) {
				boolean moveToCarrierOrder = aPlayer.checkTroopToCarrierMove(aTroop, aCarrier);
				if (moveToCarrierOrder) {
					count++;
				}
			}
		}
		return count;
	}

	public List<Spaceship> getCarriersWithFreeSlotsInSystem(Planet aLocation, Player aPlayer) {
		return getOtherCarriersWithFreeSlotsInSystem(aLocation, aPlayer, null); // call with null will return all
																				// carriers
	}

	/**
	 * 
	 * @param aLocation
	 * @param aPlayer
	 * @param aCarrier
	 *            use null if all carriers should be returned
	 * @return
	 */
	public List<Spaceship> getOtherCarriersWithFreeSlotsInSystem(Planet aLocation, Player aPlayer, Spaceship aCarrier) {
		List<Spaceship> carriersWithFreeSlots = new ArrayList<Spaceship>();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aLocation);
		for (Spaceship spaceship : shipsAtPlanet) {
			if (spaceship.isCarrier() & (spaceship != aCarrier)) {
				int maxSlots = spaceship.getSquadronCapacity();
				int slotsFull = getNoSquadronsAssignedToCarrier(spaceship);
				int sqdMovingToCarrier = getNoSquadronsMovingToCarrier(spaceship);
				if ((slotsFull + sqdMovingToCarrier) < maxSlots) {
					carriersWithFreeSlots.add(spaceship);
				}
			}
		}
		return carriersWithFreeSlots;
	}

	/**
	 * Return all carriers with at least minFreeSlots free slots for troops
	 * 
	 * @param aLocation
	 * @param aPlayer
	 * @param minFreeSlots
	 * @return
	 */
	public List<Spaceship> getCarriersWithFreeTroopSlotsInSystem(Planet aLocation, Player aPlayer, int minFreeSlots) {
		List<Spaceship> carriersWithFreeSlots = new ArrayList<Spaceship>();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aLocation);
		for (Spaceship spaceship : shipsAtPlanet) {
			if (spaceship.isTroopCarrier()) {
				int maxSlots = spaceship.getTroopCapacity();
				int slotsFull = getNoTroopsAssignedToCarrier(spaceship);
				int troopsMovingToCarrier = getNrTroopsMovingToCarrier(spaceship);
				if ((slotsFull + troopsMovingToCarrier + minFreeSlots) <= maxSlots) {
					carriersWithFreeSlots.add(spaceship);
				}
			}
		}
		return carriersWithFreeSlots;
	}

	public List<String> getSelectableFactionNames() {
		return selectableFactionNames;
	}

	public void setSelectableFactionNames(List<String> selectableFactionNames) {
		this.selectableFactionNames = selectableFactionNames;
		Logger.finest("this.selectableFactionNames: " + this.selectableFactionNames);
	}

	public void setAllFactionsSelectable() {
		selectableFactionNames = new LinkedList<>();
		for (Faction aFaction : gameWorld.getFactions()) {
			selectableFactionNames.add(aFaction.getName());
		}
	}

	public boolean isFactionSelectable(Faction aFaction) {
		boolean found = false;
		int i = 0;
		while (!found & (i < selectableFactionNames.size())) {
			if (aFaction.isFaction(selectableFactionNames.get(i))) {
				found = true;
			} else {
				i++;
			}
		}
		return found;
	}

	public boolean isRandomFaction() {
		return randomFaction;
	}

	public void setRandomFaction(boolean randomFaction) {
		this.randomFaction = randomFaction;
	}

	public String getFactionListString() {
		StringBuffer sb = new StringBuffer();
		if (selectableFactionNames.size() == gameWorld.getFactions().size()) {
			sb.append("All");
		} else {
			for (String selFactionName : selectableFactionNames) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(selFactionName);
			}
		}
		return sb.toString();
	}

	public void removeBuildingsDefeatedAlienPlayer(Player defeatedPlayer) {
		List<Planet> playerPlanets = getPlayersPlanets(defeatedPlayer);
		// PlanetInfos pi = defeatedPlayer.getPlanetInfos();
		for (Planet aPlanet : playerPlanets) {
			aPlanet.setBuildings(new ArrayList<Building>());
		}
	}

	public void removeBuildingsOnPlanet(Planet aPlanet) {
		aPlanet.setBuildings(new ArrayList<Building>());
	}

	public int getSteps() {
		return steps;
	}

	public List<Spaceship> getSquadronsNotInCarrier(Player aPlayer, Planet aPlanet) {
		List<Spaceship> playerSquadrons = new ArrayList<Spaceship>();
		List<Spaceship> playerShips = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
		for (Spaceship aShip : playerShips) {
			if (aShip.isSquadron()) {
				if (aShip.getCarrierLocation() == null) {
					playerSquadrons.add(aShip);
				}
			}
		}
		return playerSquadrons;
	}

	public boolean playerHasCarrierAtPlanet(Player aPlayer, Planet aPlanet) {
		boolean found = false;
		for (Iterator<Spaceship> iter = getSpaceships().iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if (aShip.getOwner() == aPlayer) {
				if (aShip.getLocation() == aPlanet) {
					if (aShip.isCarrier()) {
						found = true;
					}
				}
			}
		}
		return found;
	}

	public void addTroop(Troop aTroop) {
		troops.add(aTroop);
	}

	public List<TroopType> getTroopTypes() {
		return getGameWorld().getTroopTypes();
	}

	public boolean hasTroops() {
		return gameWorld.hasTroops();
	}

	public List<VIP> findLandBattleVIPs(Troop aTroop, boolean groupBonus) {
		List<VIP> VIPs = new LinkedList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++) {
			VIP aVIP = (VIP) allVIPs.get(i);

			if (aVIP.isLandBattleVIP() & (aVIP.getTroopLocation() != null
					&& aVIP.getTroopLocation().getUniqueName().equalsIgnoreCase(aTroop.getUniqueName()))) {
				// LoggingHandler.finest("findLandBattleVIPs; " + aVIP.getShortName() + " " +
				// aVIP.getTroopAttacksBonus());
				VIPs.add(aVIP);
			}
		}
		return VIPs;
	}

	public List<VIPType> getVIPType(Alignment findAlignment) {
		List<VIPType> vipTypes = new LinkedList<>();
		for (VIPType aVipType : gameWorld.getVipTypes()) {
			if (aVipType.getAlignment() == findAlignment) {
				vipTypes.add(aVipType);
			}
		}
		return vipTypes;
	}

	public Planet getPlanet(String planetName) {
		Logger.finer("getPlanet(String planetName) planetName : " + planetName);
		Planet aPlanet = null;
		for (Planet tempPlanet : planets) {
			if (tempPlanet.getName().equalsIgnoreCase(planetName)) {
				aPlanet = tempPlanet;
				break;
			}
		}
		return aPlanet;
	}

	public Building findBuilding(int buildingId, Player aPlayer) {
		List<Planet> planetList = getPlayersPlanets(aPlayer);

		for (Planet aPlanet : planetList) {
			Building aBuilding = aPlanet.getBuilding(buildingId);
			if (aBuilding != null) {
				return aBuilding;
			}
		}
		return null;
	}

	public boolean isOngoingGroundBattle(Planet aPlanet, Player aPlayer) {
		boolean playerHavetroop = false, nonPlayerTroop = false;
		for (Troop aTroop : troops) {
			if (aTroop.getPlanetLocation() == aPlanet) {
				if (aTroop.getOwner() == aPlayer) {
					playerHavetroop = true;
				} else {
					nonPlayerTroop = true;
				}
			}
		}
		return (playerHavetroop && nonPlayerTroop);

	}

	public boolean spaceshipTypeExist(SpaceshipType aSpaceshipType, Faction aFaction, Player aPlayer) {
		List<Spaceship> spaceshipsToCheck;
		if (aPlayer != null) {// playerUnique
			Logger.fine("spaceshipTypeExist: Player check");
			spaceshipsToCheck = getPlayersSpaceships(aPlayer);
		} else if (aFaction != null) {// factionUnique
			spaceshipsToCheck = new ArrayList<Spaceship>();
			for (Player tempPlayer : players) {
				if (tempPlayer.getFaction().getName().equals(aFaction.getName())) {
					spaceshipsToCheck.addAll(getPlayersSpaceships(tempPlayer));
				}
			}
		} else {// worldUnique
			spaceshipsToCheck = spaceships;
		}

		for (Spaceship spaceship : spaceshipsToCheck) {
			if (spaceship.getTypeName().equals(aSpaceshipType.getName())) {
				Logger.fine("Shio exist: " + aSpaceshipType.getName());
				return true;
			}
		}

		return false;
	}

	public boolean troopTypeExist(TroopType aTroopType, Faction aFaction, Player aPlayer) {
		boolean exist = false;
		List<Troop> troopsToCheck;
		if (aPlayer != null) {// playerUnique
			troopsToCheck = getPlayersTroops(aPlayer);
		} else if (aFaction != null) {// factionUnique
			troopsToCheck = new ArrayList<Troop>();
			for (Player tempPlayer : players) {
				if (tempPlayer.getFaction().getName().equals(aFaction.getName())) {
					troopsToCheck.addAll(getPlayersTroops(tempPlayer));
				}
			}
		} else {// worldUnique
			troopsToCheck = troops;
		}

		for (Troop tempTroop : troopsToCheck) {
			if (tempTroop.getTroopType().getUniqueName().equals(aTroopType.getUniqueName())) {
				exist = true;
			}
		}

		return exist;
	}

	public boolean vipTypeExist(VIPType aVIPType, Faction aFaction, Player aPlayer) {
		boolean exist = false;
		List<VIP> vipsToCheck;
		if (aPlayer != null) {// playerUnique
			vipsToCheck = getPlayersVips(aPlayer);
		} else if (aFaction != null) {// factionUnique
			vipsToCheck = new ArrayList<VIP>();
			for (Player tempPlayer : players) {
				if (tempPlayer.getFaction().getName().equals(aFaction.getName())) {
					vipsToCheck.addAll(getPlayersVips(tempPlayer));
				}
			}
		} else {// worldUnique
			vipsToCheck = allVIPs;
		}

		for (VIP tempVIP : vipsToCheck) {
			if (tempVIP.getTypeName().equals(aVIPType.getName())) {
				exist = true;
			}
		}

		return exist;
	}

	public boolean buildingTypeExist(BuildingType aBuildingType, Faction aFaction, Player aPlayer) {
		boolean exist = false;
		List<Planet> planetsToCheck;
		if (aPlayer != null) {// playerUnique
			planetsToCheck = getPlayersPlanets(aPlayer);
		} else if (aFaction != null) {// factionUnique
			planetsToCheck = new ArrayList<Planet>();
			for (Player tempPlayer : players) {
				if (tempPlayer.getFaction().getName().equals(aFaction.getName())) {
					planetsToCheck.addAll(getPlayersPlanets(tempPlayer));
				}
			}
		} else {// worldUnique
			planetsToCheck = planets;
		}
		for (Planet tempPlanet : planetsToCheck) {
			for (Building tempBuilding : tempPlanet.getBuildings()) {
				if (tempBuilding.getBuildingType().getName().equals(aBuildingType.getName())) {
					exist = true;
				}
			}
		}
		return exist;
	}

	public int getSingleVictory() {
		return singleVictory;
	}

	public void setSingleVictory(int singleVictory) {
		this.singleVictory = singleVictory;
	}

	public int getFactionVictory() {
		return factionVictory;
	}

	public void setFactionVictory(int factionVictory) {
		this.factionVictory = factionVictory;
	}

	public int getEndTurn() {
		return endTurn;
	}

	public void setEndTurn(int endTurn) {
		this.endTurn = endTurn;
	}

	public List<Faction> getLargestFactions() {
		List<Faction> largestFactions = new ArrayList<Faction>();
		int largestProd = 0;
		int tempLargest = 0;

		for (Player aPlayer : players) {
			tempLargest = aPlayer.getFaction().getTotalPop();
			if (largestProd < tempLargest) {
				largestProd = tempLargest;
			}
		}
		for (Player aPlayer : players) {
			if (largestProd == aPlayer.getFaction().getTotalPop()) {
				Faction tempFaction = aPlayer.getFaction();
				if (!largestFactions.contains(tempFaction)) {
					largestFactions.add(tempFaction);
				}
			}
		}

		return largestFactions;
	}

	public int getNumberOfStartPlanet() {
		return numberOfStartPlanet;
	}

	public void setNumberOfStartPlanet(int numberOfStartPlanet) {
		this.numberOfStartPlanet = numberOfStartPlanet;
	}

	public boolean getFactionGame() {
		boolean factionGame = false; // if any faction have more than one player
		List<Faction> foundFactions = new LinkedList<Faction>();
		for (Player aPlayer : players) {
			if (foundFactions.contains(aPlayer.getFaction())) {
				factionGame = true;
			} else {
				foundFactions.add(aPlayer.getFaction());
			}
		}
		return factionGame;
	}

	public boolean checkAllInConfederacyOrder(Player thePlayer, List<Player> confPlayers) {
		int confCounter = 0;
		Logger.fine("thePlayer: " + thePlayer);
		for (Player confPlayer : confPlayers) {
			Logger.fine("confPlayer: " + confPlayer);
			if (confPlayer.haveConfOrder(thePlayer)) {
				Logger.fine("confCounter: " + confCounter);
				confCounter++;
			}
		}
		return confCounter == confPlayers.size();
	}

	public boolean checkAllInConfederacyOffer(Player thePlayer, List<Player> confPlayers) {
		int confCounter = 0;
		Logger.finer("thePlayer: " + thePlayer);
		for (Player confPlayer : confPlayers) {
			Logger.finer("confPlayer: " + confPlayer);
			if (confPlayer.haveConfOffer(thePlayer)) {
				Logger.finer("confCounter: " + confCounter);
				confCounter++;
			}
		}
		return confCounter == confPlayers.size();
	}

	public boolean checkConfederacyOfferExist(Player thePlayer, List<Player> confPlayers) {
		int confCounter = 0;
		Logger.finer("thePlayer: " + thePlayer);
		for (Player confPlayer : confPlayers) {
			Logger.finer("confPlayer: " + confPlayer);
			if (confPlayer.haveConfOffer(thePlayer)) {
				Logger.finer("confCounter: " + confCounter);
				confCounter++;
			}
		}
		return confCounter > 0;
	}

	public List<DiplomacyState> getPostConflicts() {
		return postConflicts;
	}

	public void setPostConflicts(List<DiplomacyState> postConfList) {
		this.postConflicts = postConfList;
	}

	public void addPostConfList(DiplomacyState addToList) {
		postConflicts.add(addToList);
	}

	/**
	 * find the VIP with the highest bombardmentbonus on any of the sips in the list
	 *
	 *            a list of ships
	 */
	public VIP findHighestVIPbombardmentBonus(List<Spaceship> ships) {
		VIP foundVIP = null;
		int bombardmentBonus = 0;
		for (VIP aVIP : allVIPs) {
			if (aVIP.getBombardmentBonus() > 0) {
				if (aVIP.getShipLocation() != null) {
					Spaceship tempShip = aVIP.getShipLocation();
					if (ships.contains(tempShip)) {
						int tmpBonus = aVIP.getBombardmentBonus();
						if (tmpBonus > bombardmentBonus) {
							foundVIP = aVIP;
						}
					}
				}
			}
		}
		return foundVIP;
	}

	public boolean vipCanBeUsed(VIPType aVIPType) {
		boolean found = false;
		int index = 0;
		List<Player> activePlayers = getActivePlayers();
		while (!found & (index < activePlayers.size())) {
			Player aPlayer = activePlayers.get(index);
			if (aPlayer.getFaction().getAlignment().canHaveVip(aVIPType.getAlignment().getName())) {
				found = true;
			} else {
				index++;
			}
		}
		return found;
	}

	public boolean checkNoPlanet(Player aPlayer) {
		boolean noPlanet = true;
		for (int i = 0; i < planets.size(); i++) {
			Planet p = (Planet) planets.get(i);
			if (p.getPlayerInControl() == aPlayer) {
				noPlanet = false;
			}
		}
		return noPlanet;
	}

	public UniqueIdCounter getUniqueIdCounter(CounterType type){
		return uniqueIdCounters.stream().filter(counter -> counter.getCounterType() == type).findFirst().orElse(createUniqueIdCounter(type));
	}

	private UniqueIdCounter createUniqueIdCounter(CounterType type){
		UniqueIdCounter counter = new UniqueIdCounter(type);
		uniqueIdCounters.add(counter);
		return counter;
	}

}
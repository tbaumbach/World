package spaceraze.world;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.*;
import spaceraze.util.general.Logger;
import spaceraze.world.diplomacy.DiplomacyState;
import spaceraze.world.enums.DiplomacyGameType;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.enums.SpaceshipRange;

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

	@Enumerated(EnumType.STRING)
	private StatisticGameType statisticGameType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "galaxy")
	@Builder.Default
	private List<DiplomacyState> diplomacyStates = new ArrayList<>(); // current states between all players

	@Enumerated(EnumType.STRING)
	private DiplomacyGameType diplomacyGameType;

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
		diplomacyStates = new ArrayList<>();
		allStatistics = new ArrayList<>();
		currentOffers = new ArrayList<>();
		turn = 0;
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.SHIP));
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.VIP));
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.BLACK_MARKET));
		uniqueIdCounters.add(new UniqueIdCounter(CounterType.BUILDING));

		// create map data
		for (MapPlanet aPlanet : aMap.getPlanets()) {
			planets.add(new Planet(aPlanet));
		}
		for (MapPlanetConnection aConnection : aMap.getConnections()) {
			Planet p1 = findPlanet(aConnection.getPlanetOne().getName());
			Planet p2 = findPlanet(aConnection.getPlanetTwo().getName());
			planetConnections.add(new PlanetConnection(p1, p2, aConnection.isLongRange()));
		}
		maxNrStartPlanets = aMap.getMaxNrStartPlanets();
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

	public List<Troop> findAllTroopsOnShip(Spaceship aShip) {
		List<Troop> troopsOnShip = new LinkedList<Troop>();
		for (Troop troop : troops) {
			if (troop.getShipLocation() == aShip) {
				troopsOnShip.add(troop);
			}
		}
		return troopsOnShip;
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


	public SpaceshipType findSpaceshipType(String findname) {
		return gameWorld.getSpaceshipTypeByName(findname);
	}

	public List<Planet> getPlanets() {
		return planets;
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

	public Spaceship findSpaceshipByUniqueId(String uniqueId) {
		Spaceship ss = null;
		int i = 0;
		while ((ss == null) & (i < spaceships.size())) {
			Spaceship temp = spaceships.get(i);
			if (temp.getKey().equals(uniqueId)) {
				ss = temp;
			} else {
				i++;
			}
		}
		return ss;
	}

	public TroopType findTroopType(String ttname) {
		TroopType tt = null;
		int i = 0;
		while ((tt == null) & (i < gameWorld.getTroopTypes().size())) {
			TroopType aTT = gameWorld.getTroopTypes().get(i);
			if (aTT.getName().equals(ttname)) {
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
	public List<PlanetConnection> getPlanetConnections() {
		return planetConnections;
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

	public List<Player> getPlayers() {
		return players;
	}

	public List<Planet> getAllDestinations(Planet location, boolean longRange) {
		List<Planet> alldest = new ArrayList<>();
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
			PlanetConnection temppc = planetConnections.get(i);
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

	public List<Troop> getTroops() {
		return troops;
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

	public List<Spaceship> getSpaceships() {
		return spaceships;
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

	/**
	 * Get numbers of player in a faction
	 */
	public int getFactionMemberNr(Faction aFaction) {
		int nr = 0;
		for (int i = 0; i < players.size(); i++) {
			Player aPlayer = (Player) players.get(i);
			if (aPlayer.getFactionKey().equals(aFaction.getKey())) {
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
			if (aPlayer.getFactionKey().equals(aFaction.getKey()) && !aPlayer.isDefeated()) {
				nr++;
			}
		}
		return nr;
	}

	public List<Player> getFactionMember(Faction aFaction) {
		List<Player> factionPlayers = new ArrayList<Player>();
		for (int i = 0; i < players.size(); i++) {
			Player aPlayer = (Player) players.get(i);
			if (aPlayer.getFactionKey().equals(aFaction.getKey())) {
				factionPlayers.add(aPlayer);
			}
		}
		return factionPlayers;
	}

	public int getUndefeatedFactionMemberNr(Faction aFaction) {
		int nr = 0;
		for (int i = 0; i < players.size(); i++) {
			Player aPlayer = (Player) players.get(i);
			if (aPlayer.getFactionKey().equals(aFaction.getKey())) {
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

	public void removeBuildingsOnPlanet(Planet aPlanet) {
		aPlanet.setBuildings(new ArrayList<Building>());
	}

	public int getSteps() {
		return steps;
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

	public int getNumberOfStartPlanet() {
		return numberOfStartPlanet;
	}

	public void setNumberOfStartPlanet(int numberOfStartPlanet) {
		this.numberOfStartPlanet = numberOfStartPlanet;
	}

}
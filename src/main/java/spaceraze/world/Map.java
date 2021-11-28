package spaceraze.world;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "MAP")
public class Map implements Serializable, Comparable<Map>{
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //TODO check if we should use SEQUENCE or TABLE
	private Long id;

	private String key;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "map")
	@Builder.Default
	private List<MapPlanetConnection> connections = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "map")
	@Builder.Default
	private List<MapPlanet> planets = new ArrayList<>();

	private int maxNrStartPlanets;
	private String createdDate;
	private String changedDate;
	private String description;
	private String authorName; // real name of author
	private String author; // authors unique spaceraze login
	private String fileName; // unique name of map used in filename of properties file
	private String name; // real name of map
	private long versionId = 1; // should be increased every time a new version of a map is published

	@Enumerated(EnumType.STRING)
	private MapStatus status;

	public Map(String playerLogin,String mapFileName){
		this(playerLogin + "." + mapFileName);
		Logger.finer("new Map from draft, mapname: " + mapFileName + " playerLogin" + playerLogin);
	}
	
	// creating a map from a published map
	public Map(String mapFileName){
		Logger.finest("New Map from mapName: " + mapFileName);
		Properties props = PropertiesHandler.getInstance("maps." + mapFileName);
		initMap(mapFileName,props);
	}

	/** 
	 * Creating a map from a properties object.
	 * Used by SpaceRazeDroid.
	 * 
	 * @param mapFileName
	 * @param props
	 */
	public Map(String mapFileName, Properties props){
		initMap(mapFileName, props);
	}
	
	// creating a map from a published map
	public void initMap(String mapFileName, Properties props){
		this.key = UUID.randomUUID().toString();
		Logger.finest("Init Map from mapName: " + mapFileName);
		planets = getPlanets(props);
		connections = getConnections(props, List.copyOf(planets));
//		distanceTable = getDistanceTable(props);
		name = props.getProperty("mapName");
		String tmpVersion = props.getProperty("version");
		if (tmpVersion != null){
			versionId = Long.parseLong(props.getProperty("version"));
		}
		fileName = props.getProperty("mapFileName");
		author = props.getProperty("authorLogin");
		authorName = props.getProperty("author");
		description = props.getProperty("description");
		Logger.finest("description: " + description);
		createdDate = props.getProperty("createddate");		
		changedDate = props.getProperty("changeddate");
		Logger.finest("changeddate: " + changedDate);
		maxNrStartPlanets = Integer.parseInt(props.getProperty("maxNrStartPlanets"));
	}


	/**
	 * This constructor is only used during testing
	 * @param planets
	 * @param connections
	 * @param name
	 * @param maxNrPlayers
	 */
	public Map(List<MapPlanet> planets, List<MapPlanetConnection> connections, String name, String desc, int maxNrPlayers){
		this.planets = planets;
		this.connections = connections;
		this.name = name;
		this.description = desc;
//		createdDate = props.getProperty("createddate");		
//		changedDate = props.getProperty("changeddate");
		this.maxNrStartPlanets = maxNrPlayers;
	}
	
	/**
	 * Creates a new Map object from file of a published map
	 * @return
	 */
	@JsonIgnore
	public Map getCopyFromFile(){
		return new Map(fileName);
	}

	private List<MapPlanet> getPlanets(Properties props){
		List<MapPlanet> tmpPlanets = new LinkedList<>();
		int index = 0;
		String tmpValue = props.getProperty("planet" + index);
		while (tmpValue != null){
			MapPlanet tmpPlanet = new MapPlanet(tmpValue);
			tmpPlanets.add(tmpPlanet);
			index++;
			tmpValue = props.getProperty("planet" + index);
		}
		return tmpPlanets;
	}
	
	private List<MapPlanetConnection> getConnections(Properties props, List<MapPlanet> allPlanets){
		List<MapPlanetConnection> tmpConnections = new LinkedList<>();
		int index = 0;
		String tmpValue = props.getProperty("connection" + index);
		while (tmpValue != null){
			MapPlanetConnection tmpConnection = new MapPlanetConnection(tmpValue,allPlanets);
			tmpConnections.add(tmpConnection);
			index++;
			tmpValue = props.getProperty("connection" + index);
		}
		return tmpConnections;
	}


	public String getAuthorName() {
		return authorName;
	}

	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String newAuthorLogin){
		author = newAuthorLogin;
	}

	public String getChangedDate() {
		return changedDate;
	}
	
	public List<MapPlanetConnection> getConnections() {
		return connections;
	}

	public int getNrConnections() {
		return connections.size();
	}

	public int getNrConnectionsLong() {
		int nrLongConnections = 0;
		for (MapPlanetConnection aConnection : connections) {
			if (aConnection.isLongRange()){
				nrLongConnections++;
			}
		}
		return nrLongConnections;
	}

	public int getNrConnectionsShort() {
		int nrShortConnections = 0;
		for (MapPlanetConnection aConnection : connections) {
			if (!aConnection.isLongRange()){
				nrShortConnections++;
			}
		}
		return nrShortConnections;
	}

	public double getAverageNrConnections(){
		double ratio = (2.0*getNrConnections())/getNrPlanets();
		return ratio;
	}

	public String getAverageNrConnectionsString(){
		double ratio = getAverageNrConnections();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		return nf.format(ratio);
	}

	public String getCreatedDate() {
		return createdDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getMaxNrStartPlanets() {
		return maxNrStartPlanets;
	}
	
	public String getNameFull() {
		return name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String newFileName) {
		fileName = newFileName;
	}

	@JsonIgnore
	public List<String> getPlanetNames() {
		List<String> planetsName = new ArrayList<String>();
		for (MapPlanet planet : planets) {
			planetsName.add(planet.getName());
		}
		return planetsName;
	}

	public int getNrPlanets() {
		return planets.size();
	}

	public int compareTo(Map anotherMap) {
		int sortOrder = 1;
		if (anotherMap.getNrPlanets() > getNrPlanets()){
			sortOrder = -1;
		}
		return sortOrder;
	}

	@JsonIgnore
	private List<MapPlanet> getStarPlanets() {
		List<MapPlanet>  tempList = new ArrayList<>();
		for (MapPlanet aPlanet : planets) {
		  if(aPlanet.isPossibleStartPlanet()){
			  tempList.add(aPlanet);
		  }
		}
		return tempList;
	}

	public MapPlanet createNewPlanet(int x, int y){
		String newName = getFreeName("Unnamed - ");
		MapPlanet newPlanet = new MapPlanet(x, y, 0, newName, true);
		planets.add(newPlanet);
		return newPlanet;
	}
	
	@JsonIgnore
	public String getFreeName(String namePrefix){
		int index = 1;
		boolean freeFound = false;
		String curName = null;
		while (!freeFound){
			curName = namePrefix + index;
			boolean exists = false;
			int i = 0;
			while (!exists & (i < planets.size())){
				MapPlanet tmpPlanet = planets.get(i);
				if (tmpPlanet.getName().equals(curName)){
					exists = true;
				}else{
					i++;
				}
			}
			if (exists){
				index++;
			}else{
				freeFound = true;
			}
		}
		return curName;
	}
	
	public MapPlanet findClosestPlanet(int xLY,int yLY){
		MapPlanet closestPlanet = null;
		double closest = Double.MAX_VALUE;
		for (MapPlanet aPlanet : planets) {
			double xDiff = aPlanet.getX() - xLY;
			double yDiff = aPlanet.getY() - yLY;
			double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff,2));
			if (distance < closest){
				closest = distance;
				closestPlanet = aPlanet;
			}
		}
		return closestPlanet;
	}
	
	public void removePlanet(MapPlanet aPlanet){
		// remove all connections
		List<MapPlanetConnection> removeConns = new LinkedList<>();
		for (MapPlanetConnection aConn : connections) {
			if ((aConn.getPlanetOne() == aPlanet) | (aConn.getPlanetTwo() == aPlanet)){
				removeConns.add(aConn);
			}
		}
		// remove all removeConns
		for (MapPlanetConnection aConn2 : removeConns) {
			connections.remove(aConn2);
		}
		// remove the planet
		planets.remove(aPlanet);
	}

	public void addNewConnection(MapPlanet newP1, MapPlanet newP2, boolean longRange){
		MapPlanetConnection newConn = new MapPlanetConnection(newP1,newP2,longRange);
		connections.add(newConn);
	}

	/**
	 * copy of method i Galaxy
	 * @param findName
	 * @return
	 */
    public MapPlanet findPlanet(String findName){
        MapPlanet p = null;
        int i = 0;
        while ((p == null) & (i<planets.size())){
          MapPlanet temp = planets.get(i);
          if (temp.getName().equalsIgnoreCase(findName)){
            p = temp;
          }else{
            i++;
          }
        }
        return p;
    }
    
    public void deleteConnection(MapPlanet aPlanet1, MapPlanet aPlanet2){
    	MapPlanetConnection found = findConnection(aPlanet1,aPlanet2);
    	connections.remove(found);
    }
    
    public MapPlanetConnection findConnection(MapPlanet aPlanet1, MapPlanet aPlanet2){
    	int i = 0;
    	MapPlanetConnection found = null;
    	while ((found == null) & (i < connections.size())){
    		MapPlanetConnection tmpConn = connections.get(i);
    		if (tmpConn.isConnection(aPlanet1, aPlanet2)){
    			found = tmpConn;
    		}else{
    			i++;
    		}
    	}
    	return found;
    }

	// returns a String object that can be written to a file to create a Map file
    @JsonIgnore
	public String getMapData(){
		String s = "";
		List<MapPlanet> allPlanets = getPlanets();
		List<MapPlanetConnection> allConnections = getConnections();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		s = s + "# File created date: " + sdf.format(new Date()) + "\n";
		s = s + "# File created by: MapHandler.getMapData(Map)\n";
		s = s + "\n";
		s = s + "author = " + getAuthorName() + "\n";
		s = s + "authorLogin = " + getAuthor() + "\n";
		s = s + "\n";
		s = s + "description = " + getDescription() + "\n";
		s = s + "\n";
		s = s + "# Last date the map was changed (saved)\n";
		s = s + "changeddate = " + sdf.format(new Date()) + "\n";
		s = s + "\n";
		s = s + "# When the map was first created\n";
		s = s + "createddate = " + getCreatedDate() + "\n";
		s = s + "\n";
		// write name
		s = s + "mapName = " + getNameFull() + "\n";
		s = s + "mapFileName = " + getFileName() + "\n";
		s = s + "\n";
		// write version
		s = s + "version = " + getVersionId() + "\n";
		s = s + "\n";
		// write max nr start planets
		s = s + "maxNrStartPlanets = " + getMaxNrStartPlanets() + "\n";
		s = s + "\n";
		// write planets
		int counter = 0;
		for (MapPlanet aPlanet : allPlanets) {
			s = s + aPlanet.getSaveString(counter) + "\n";
			counter++;
		}
		s = s + "\n";
		// write connections
		counter = 0;
		for (MapPlanetConnection aConnection : allConnections) {
			s = s + aConnection.getSaveString(counter) + "\n";
			counter++;
		}
		s = s + "\n";
		return s; 	
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setMaxNrStartPlanets(int maxNrStartPlanets) {
		this.maxNrStartPlanets = maxNrStartPlanets;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public boolean checkPlanetNamesNoEmpty(){
		boolean noEmptyName = true;
		int counter = 0;
		while (noEmptyName & (counter < planets.size())) {
			MapPlanet aPlanet = planets.get(counter);
			if (aPlanet.getName().trim().equals("")){
				noEmptyName = false;
			}else{
				counter++;
			}
		}
		return noEmptyName;
	}

	public boolean checkPlanetNamesUnique(){
		boolean allNamesUnique = true;
		for (MapPlanet aPlanet1 : planets) {
			if (allNamesUnique){
				int counter = 0;
				for (MapPlanet aPlanet2 : planets) {
					if (aPlanet1.getName().equalsIgnoreCase(aPlanet2.getName())){
						counter++;
					}
				}
				if (counter > 1){
					allNamesUnique = false;
				}
			}
		}
		return allNamesUnique;
	}
	
	/**
	 * Called to validate a map that there are no planets without connections
	 * Returns the first planet without any connections. If all have 
	 * connections, null is returned.
	 */
	public MapPlanet checkConnectionToPlanets(){
		MapPlanet foundPlanet = null;
		int i = 0;
		while ((foundPlanet == null) && (i < planets.size())){
			MapPlanet tmpPlanet = planets.get(i);
			int j = 0;
			boolean foundConn = false;
			while ((!foundConn) && (j < connections.size())){
				MapPlanetConnection tmpConn = connections.get(j);
				Logger.finest( tmpPlanet.getName() + " " + tmpConn.getPlanetOne().getName() + " " + tmpConn.getPlanetTwo().getName());
				if (tmpConn.getPlanetOne().equals(tmpPlanet) | tmpConn.getPlanetTwo().equals(tmpPlanet)){
					foundConn = true;
				}else{
					j++;
				}
			}
			if (foundConn){
				i++;
			}else{
				foundPlanet = tmpPlanet;
			}
		}
		return foundPlanet;
	}

	public boolean checkPlanetsAllConnected(){
		boolean allComnected = true;
		List<MapPlanet> searchedPlanets = new LinkedList<>(); // add all serched planets + startplanet
		if (planets.size() > 1){ // 0 or 1 planets are by definition connected
			List<MapPlanet> edgePlanets = new LinkedList<>();
			List<MapPlanet> newEdgePlanets = new LinkedList<>();
			searchedPlanets.add(planets.get(0));
			edgePlanets.add(planets.get(0));
			List<MapPlanet> allNeighbours = new LinkedList<>();
			
	      	// Gå igenom alla edgePlanets (dvs bara startplaneten initialt)
	        for (int i = 0; i < edgePlanets.size(); i++){
	          MapPlanet tempPlanet = edgePlanets.get(i);
	          // Hämta alla grannar till tempPlanet
	          allNeighbours = getAllDestinations(tempPlanet,true);
	          // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
	          for (int j = 0; j < allNeighbours.size(); j++){
	            MapPlanet tempNeighbourPlanet = allNeighbours.get(j);
	            // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
	            if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
	              // lägg i newEdgePlanets
	              newEdgePlanets.add(tempNeighbourPlanet);
	            }
	          }
	        }
	        
	        while (newEdgePlanets.size() > 0){
	            // töm edgePlanets
	            edgePlanets.clear();
	            for (int l = 0; l < newEdgePlanets.size(); l++){
	              // kopiera över newEdgePlanets till edgePlanets
	              edgePlanets.add(newEdgePlanets.get(l));
	              // kopiera över newEdgePlanets till searchedPlanets
	              searchedPlanets.add(newEdgePlanets.get(l));
	            }
	            // töm newEdgePlanets
	            newEdgePlanets.clear();
	            // töm allNeighbours
	            allNeighbours.clear();

		      	// Gå igenom alla edgePlanets (dvs bara startplaneten initialt)
		        for (int i = 0; i < edgePlanets.size(); i++){
		          MapPlanet tempPlanet = edgePlanets.get(i);
		          // Hämta alla grannar till tempPlanet
		          allNeighbours = getAllDestinations(tempPlanet,true);
		          // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
		          for (int j = 0; j < allNeighbours.size(); j++){
		            MapPlanet tempNeighbourPlanet = allNeighbours.get(j);
		            // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
		            if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
		              // l�gg i newEdgePlanets
		              newEdgePlanets.add(tempNeighbourPlanet);
		            }
		          }
		        }
	        }
		}
		allComnected = planets.size() == searchedPlanets.size();
		return allComnected;
	}
	
	@JsonIgnore
    public List<MapPlanet> getAllDestinations(MapPlanet location, boolean longRange){
        List<MapPlanet> alldest = new LinkedList<>();
        MapPlanet tempPlanet = null;
        for (MapPlanetConnection temppc : connections){
          tempPlanet = temppc.getOtherEnd(location,longRange);
          if (tempPlanet != null){  // there is a connection within range
            alldest.add(tempPlanet);
          }
        }
        return alldest;
      }

	public long getVersionId() {
		return versionId;
	}

	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}

	public void incVersionId() {
		versionId++;
	}

}

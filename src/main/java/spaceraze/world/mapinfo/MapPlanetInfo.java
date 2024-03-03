package spaceraze.world.mapinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

import javax.persistence.*;

/**
 * Hanterar kart-informationen för en enskild planet för ett visst drag för en viss spelare
 * 
 * @author Paul Bodin
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "MAP_PLANET_INFO")
public class MapPlanetInfo implements Serializable {
	static final long serialVersionUID = 1L;
	public static final String NEUTRAL = "neutral";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_MAP_INFO_TURN")
	private MapInfoTurn mapInfoTurn;

	private String planetName;
	
	// värden som skall visas på kartan om en spelares nuvarande kunskap om planeten som denna instans gäller
	private String owner; // namnet på gov som äger planeten, "neutral" om neutral. Kan visas i lagpartier?
	@ElementCollection
	@CollectionTable(name = "BUILDINGS_HIDDEN")
	@Builder.Default
	private List<String> buildingsHidden = new ArrayList<>();
	@ElementCollection
	@CollectionTable(name = "BUILDINGS_VISIBLE")
	@Builder.Default
	private List<String> buildingsVisible = new ArrayList<>(); // alla buildings tillhör ägaren av planeten

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_OWN_VIP_DATA")
	private VIPData ownVIPs;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_OTHERS_VIP_DATA")
	private VIPData otherVIPs; // det kan aldrig vara vippar från mer än en annan spelare än sina egna vippar
	@ElementCollection
	@CollectionTable(name = "SHIPS_OWN")
	@Builder.Default
	private List<String> shipsOwn = new ArrayList<>(); // gäller endast egna skepp, varje skeppssträng kan börja med en siffra för hur många skepp det är av den typen

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mapPlanetInfo")
	@Builder.Default
	private List<FleetData> fleetsOther = new ArrayList<>(); // max size f�r neutrala och andra flottor, inkl civila skepp
	private boolean razed;
	@Column(name = "isOpen")
	private boolean open;
	private boolean besieged;
	private String prod;
	private String res;
	private String lastKnownProd;
	private String lastKnownRes;
	
	// planet notes sätts via order men lagras här
	private String notes; 
	
	// info about last known info
	private String lastKnownOwner; // null = neutral. Påverkar planetens färg.
	private String lastKnownMaxShipSize; // visa bara en grå storlek även om det fanns flera flottor vid planeten. Inkluderar info om civila skepp, t.ex. "small+civ"
	@ElementCollection
	@CollectionTable(name = "LAST_KNOWN_BUILDINGS_IN_ORBITE")
	@Builder.Default
	private List<String> lastKnownBuildingsInOrbit = new ArrayList<>();
	@ElementCollection
	@CollectionTable(name = "LAST_KNOWN_BUILDINGS_ON_SURFACE")
	@Builder.Default
	private List<String> lastKnownBuildingsOnSurface = new ArrayList<>();
	private boolean lastKnownRazed;
	private int lastInfoTurn = 0; // används även för att lagra nuvarande turn om det finns data om en planet


	public int getLastInfoTurn(){
		return lastInfoTurn;
	}

	public String getLastKnownOwner(){
		return lastKnownOwner;
	}

	public String getProd() {
		return prod;
	}

	public String getRes() {
		return res;
	}

	public boolean isRazed() {
		return razed;
	}

	public boolean isBesieged() {
		return besieged;
	}

	public String getNotes() {
		return notes;
	}

	public String getLastKnownMaxShipSize() {
		return lastKnownMaxShipSize;
	}

	public String getOwner() {
		return owner;
	}

	public List<String> getBuildingsHidden() {
		return buildingsHidden;
	}


	public List<String> getBuildingsVisible() {
		return buildingsVisible;
	}

	public VIPData getOwnVIPs() {
		return ownVIPs;
	}

	public VIPData getOtherVIPs() {
		return otherVIPs;
	}

	public List<String> getShipsOwn() {
		return shipsOwn;
	}

	public List<FleetData> getFleetsOther() {
		return fleetsOther;
	}

	public boolean isOpen() {
		return open;
	}

	public List<String> getLastKnownBuildingsInOrbit() {
		return lastKnownBuildingsInOrbit;
	}

	public List<String> getLastKnownBuildingsOnSurface() {
		return lastKnownBuildingsOnSurface;
	}

	public String getLastKnownRes() {
		return lastKnownRes;
	}

	public String getLastKnownProd() {
		return lastKnownProd;
	}

	public void setLastKnownRazed(boolean lastKnownRazed) {
		this.lastKnownRazed = lastKnownRazed;
	}

	public boolean isLastKnownRazed() {
		return lastKnownRazed;
	}

}

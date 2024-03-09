//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import spaceraze.util.general.Logger;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "FACTION")
public class Faction implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_GAME_WORLD")
    private GameWorld gameWorld;

    private String uuid;
    private String colorHexValue; // ex: red = "FF0000"
    private String name;
    private String shortName;

    @Column(length = 4000)
    private String description;

    @Column(length = 4000)
    private String history;

    @Column(length = 4000)
    private String shortDescription;

    @Column(length = 4000)
    private String advantages;

    @Column(length = 4000)
    private String disadvantages;

    @Column(length = 4000)
    private String howToPlay;

    @ElementCollection
    @CollectionTable(name = "FACTION_TO_SHIP_TYPE")
    @Builder.Default
    private List<String> spaceshipTypes = new ArrayList<>();

    @Builder.Default
    private int openPlanetBonus = 0;
    @Builder.Default
    private int closedPlanetBonus = 0;
    @Builder.Default
    private int resistanceBonus = 0;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_ALIGNMENT") //@JoinColumn(name = "FK_ALIGNMENT", insertable = false, updatable = false)
    private Alignment alignment; // must be set

    @OneToOne()//@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_VIP_TYPE_GOVERNOR") //@JoinColumn(name = "FK_VIP_TYPE_GOVERNOR", insertable = false, updatable = false)
    private VIPType governorVIPType;

    @ElementCollection
    @CollectionTable(name = "FACTION_TO_STARTING_VIP_TYPE")
    @Builder.Default
    private List<String> startingVIPTypes = new ArrayList<>();

    @Builder.Default
    private int nrStartingRandomVIPs = 1;

    @ElementCollection
    @CollectionTable(name = "FACTION_TO_STARTING_SHIP_TYPE")
    @Builder.Default
    private List<String> startingShipTypes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "FACTION_TO_STARTING_TROOP_TYPE")
    @Builder.Default
    private List<String> startingTroops = new ArrayList<>(); // players shall start with troops of the trooptypes in this list

    @ElementCollection
    @CollectionTable(name = "FACTION_TO_TROOP_TYPE")
    @Builder.Default
    private List<String> troopTypes = new ArrayList<>(); // these trooptypes is available to build by new players

    @Builder.Default
    private int techBonus = 0; // %
    private boolean alien;
    private boolean canReconstruct;
    @Builder.Default
    private int reconstructCostBase = 8;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "FACTION_TO_BUILDING_TYPE",
            joinColumns = @JoinColumn(name = "FK_FACTION"),
            inverseJoinColumns = @JoinColumn(name = "FK_BUILDNING_TYPE"))
    @Builder.Default
    private List<BuildingType> buildings = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "FACTION_TO_STARTING_BUILDNING_TYPE")
    @Builder.Default
    private List<String> startingBuildings = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CORRUPTION_POINT")
    CorruptionPoint corruptionPoint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faction", fetch = FetchType.EAGER)
    @Builder.Default
    private List<ResearchAdvantage> researchAdvantages = new ArrayList<>();

    private int numberOfSimultaneouslyResearchAdvantages = 1;

    public Faction(String newName, String colorHexValue, Alignment alignment) {
        this.uuid = UUID.randomUUID().toString();
        this.name = newName;
        this.colorHexValue = colorHexValue;
        this.alignment = alignment;
        spaceshipTypes = new ArrayList<>();
        researchAdvantages = new ArrayList<>();
        buildings = new ArrayList<>();
        numberOfSimultaneouslyResearchAdvantages = 1;
        startingBuildings = new ArrayList<>();
        troopTypes = new ArrayList<>();
        startingShipTypes = new ArrayList<>();
        startingVIPTypes = new ArrayList<>();
        startingTroops = new ArrayList<>();

    }

    public void setOpenPlanetBonus(int newBonus) {
        openPlanetBonus = newBonus;
    }

    public int getOpenPlanetBonus() {
        return openPlanetBonus;
    }

    public void setClosedPlanetBonus(int newBonus) {
        closedPlanetBonus = newBonus;
    }

    public int getClosedPlanetBonus() {
        return closedPlanetBonus;
    }

    public void setResistanceBonus(int newResistanceBonus) {
        resistanceBonus = newResistanceBonus;
    }

    public int getResistanceBonus() {
        return resistanceBonus;
    }

    public boolean isFaction(String aName) {
        return name.equalsIgnoreCase(aName);
    }

    public String getName() {
        return name;
    }

    public String getColorHexValue() {
        return colorHexValue;
    }

    public String getPlanetHexColor() {
        return getColorHexValue();
    }

    public String getColorValues() {
        String redHex = colorHexValue.substring(0, 2);
        String greenHex = colorHexValue.substring(2, 4);
        String blueHex = colorHexValue.substring(4);
        int red = getIntFromHexPair(redHex);
        int green = getIntFromHexPair(greenHex);
        int blue = getIntFromHexPair(blueHex);
        return red + " " + green + " " + blue;
    }

    private static int getIntFromHexPair(String hexPair) {
        int major = getIntFromHexValue(hexPair.substring(0, 1));
        int minor = getIntFromHexValue(hexPair.substring(1));
        return (major * 16) + minor;
    }

    private static int getIntFromHexValue(String hexValue) {
        int retVal = -1;
        try {
            retVal = Integer.parseInt(hexValue);
        } catch (NumberFormatException nfe) { // value is not <=10
            if ("A".equals(hexValue)) {
                retVal = 10;
            } else if ("B".equals(hexValue)) {
                retVal = 11;
            } else if ("C".equals(hexValue)) {
                retVal = 12;
            } else if ("D".equals(hexValue)) {
                retVal = 13;
            } else if ("E".equals(hexValue)) {
                retVal = 14;
            } else if ("F".equals(hexValue)) {
                retVal = 15;
            }
        }
        return retVal;
    }

    /**
     * Convert rgb ints to a color hex string, for example 255,0,0 -> FF0000
     *
     * @param red   0-255 color value for red
     * @param green 0-255 color value for green
     * @param blue  0-255 color value for blue
     * @return a six-digit color hex string
     */
    public static String getColorHexString(int red, int green, int blue) {
        Logger.finest(red + " " + green + " " + blue);
        String retVal = "";
        retVal = retVal + getHexPair(red);
        retVal = retVal + getHexPair(green);
        retVal = retVal + getHexPair(blue);
        Logger.finest(retVal);
        return retVal;
    }

    /**
     * @param aValue 0-255 color value
     * @return value as hex
     */
    private static String getHexPair(int aValue) {
        int major = aValue / 16;
        int minor = aValue % 16;
        Logger.finest("Major/minor: " + major + "/" + minor);
        String retVal = getHexValue(major);
        retVal = retVal + getHexValue(minor);
        return retVal;
    }

    /**
     * @param aValue 0-15
     * @return single hex letter
     */
    private static String getHexValue(int aValue) {
        String retVal = "";
        if (aValue < 10 & (aValue >= 0)) {
            retVal = String.valueOf(aValue);
        } else {
            switch (aValue) {
                case 10:
                    retVal = "A";
                    break;
                case 11:
                    retVal = "B";
                    break;
                case 12:
                    retVal = "C";
                    break;
                case 13:
                    retVal = "D";
                    break;
                case 14:
                    retVal = "E";
                    break;
                case 15:
                    retVal = "F";
                    break;
            }
        }
        return retVal;
    }

    public void addSpaceshipType(SpaceshipType sst) {
        spaceshipTypes.add(sst.getUuid());
    }

    public List<String> getSpaceshipTypes() {
        return spaceshipTypes;
    }

    public boolean equals(Faction anotherFaction) {
        return getName().equalsIgnoreCase(anotherFaction.getName());
    }

    @JsonProperty("alignment")
    public Alignment getAlignmentName() {
        return alignment;
    }

    @JsonIgnore
    public VIPType getGovernorVIPType() {
        return governorVIPType;
    }

    public String getGovernorVIPTypeName() {
        return governorVIPType.getName();
    }

    public void setGovernorVIPType(VIPType governorVIPType) {
        this.governorVIPType = governorVIPType;
    }

    public void addStartingShipType(SpaceshipType sst) {
        startingShipTypes.add(sst.getUuid());
    }

    public void addStartingBuildings(BuildingType bt) {
        startingBuildings.add(bt.getUuid());
    }

    public void addStartingTroop(TroopType tt) {
        startingTroops.add(tt.getUuid());
    }

    public List<String> getStartingTroops() {
        return startingTroops;
    }

    public void addTroopType(TroopType tt) {
        troopTypes.add(tt.getUuid());
    }

    public List<String> getTroopTypes() {
        return troopTypes;
    }

    public List<String> getStartingShipTypes() {
        return startingShipTypes;
    }

    public List<String> getStartingBuildings() {
        return startingBuildings;
    }

    public int getTechBonus() {
        return techBonus;
    }

    public void setTechBonus(int techBonus) {
        this.techBonus = techBonus;
    }

    public int getNrStartingRandomVIPs() {
        return nrStartingRandomVIPs;
    }

    public void setNrStartingRandomVIPs(int nrStartingRandomVIPs) {
        this.nrStartingRandomVIPs = nrStartingRandomVIPs;
    }


    public List<String> getStartingVIPTypes() {
        return startingVIPTypes;
    }

    public void addStartingVIPType(VIPType aStartingVIPType) {
        startingVIPTypes.add(aStartingVIPType.getUuid());
    }

    @JsonIgnore
    public String getTotalDescription() {
        String totalDescription = "";

        if (advantages != null && !advantages.equals("")) {
            totalDescription += "Advantages: " + advantages + "\n\n";
        }
        if (disadvantages != null && !disadvantages.equals("")) {
            totalDescription += "Disadvantages: " + disadvantages + "\n\n";
        }

        if (shortDescription != null && !shortDescription.equals("")) {
            totalDescription += "Short Description\n";
            totalDescription += shortDescription + "\n\n";
        }

        if (description != null && !description.equals("")) {
            totalDescription += "Description\n";
            totalDescription += description + "\n\n";
        }
        if (howToPlay != null && !howToPlay.equals("")) {
            totalDescription += "How to play\n";
            totalDescription += howToPlay + "\n\n";
        }
        if (history != null && !history.equals("")) {
            totalDescription += "History\n";
            totalDescription += history;
        }

        return totalDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getAdvantages() {
        return advantages;
    }

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getDisadvantages() {
        return disadvantages;
    }

    public void setDisadvantages(String disadvantages) {
        this.disadvantages = disadvantages;
    }

    public boolean isAlien() {
        return alien;
    }

    public void setAlien(boolean alien) {
        this.alien = alien;
    }

    public boolean isCanReconstruct() {
        return canReconstruct;
    }

    public void setCanReconstruct(boolean canReconstruct) {
        this.canReconstruct = canReconstruct;
    }

    public int getReconstructCostBase() {
        return reconstructCostBase;
    }

    public void setReconstructCostBase(int reconstructCostBase) {
        this.reconstructCostBase = reconstructCostBase;
    }

    public int getReconstructCost(Planet aPlanet) {
        return reconstructCostBase + aPlanet.getBasePopulation();
    }

    public String getHowToPlay() {
        return howToPlay;
    }

    public void setHowToPlay(String howToPlay) {
        this.howToPlay = howToPlay;
    }

//	public void setPlanetColor(Color planetColor) {
//		this.planetColor = planetColor;
//	}

    public String getShortName() {
        return shortName;
    }

    public void addBuilding(BuildingType buildingType){
        buildings.add(buildingType);
    }

    public BuildingType getBuildingTypeByName(String name){
        BuildingType found = null;
        for(int i= 0; i < buildings.size();i++){
            if(buildings.get(i).getName().equalsIgnoreCase(name)){
                found = buildings.get(i);
            }
        }
        return found;
    }

    @JsonIgnore
    public static String getHTMLHeaderRow() {
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        sb.append("<td>Name</td>");
        sb.append("<td>Alignment</td>");
        sb.append("<td>Open<br>Planet<br>Bonus</td>");
        sb.append("<td>Closed<br>Planet<br>Bonus</td>");
        sb.append("<td>Resistance<br>Bonus</td>");
        sb.append("<td>PsychWarfare<br>Bonus</td>");
		/*  sb.append("<td>Starting<br>Wharf<br>Size</td>");
		  sb.append("<td>Wharf<br>Build<br>Cost</td>");
		  sb.append("<td>Wharf<br>Upgrade<br>Costs<br>(m/l/h)</td>");*/
        sb.append("<td>Start<br>with<br>Space<br>Station</td>");
        sb.append("<td>Tech<br>Bonus</td>");
        sb.append("<td>Build<br>Space<br>Station<br>Base<br>Cost</td>");
        sb.append("<td>Build<br>Space<br>Station<br>Cost<br>Mulitplier</td>");
        sb.append("<td>Nr<br>Starting<br>Random<br>VIPs</td>");
        sb.append("<td>Nr<br>Starting<br>Specific<br>VIPTypes</td>");
        sb.append("<td>View Details</td>");
        sb.append("</tr>\n");
        return sb.toString();
    }

    @JsonIgnore
    public String getHTMLTableRow(String gameWorldName) {
        Logger.finer("getHTMLTableRow: " + gameWorldName);
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        sb.append("<td><font color=#\"" + colorHexValue + "\">" + name + "</font></td>");
        sb.append("<td>" + alignment.toString() + "</td>");
        sb.append("<td>" + openPlanetBonus + "</td>");
        sb.append("<td>" + closedPlanetBonus + "</td>");
        sb.append("<td>" + resistanceBonus + "</td>");
        sb.append("<td>" + techBonus + "</td>");
        sb.append("<td>" + nrStartingRandomVIPs + "</td>");
        sb.append("<td>" + startingVIPTypes.size() + "</td>");
        sb.append("<td><a href=\"faction.jsp?gameworldfilename=" + gameWorldName + "&factionname=" + name + "\">View Details</a></td>");
        sb.append("</tr>\n");
        return sb.toString();
    }

    @JsonIgnore
    public static String getHTMLHeaderRowNO() {
        StringBuffer sb = new StringBuffer();
        sb.append("<tr class='ListheaderRow'>");
        sb.append("<td class='ListHeader'></td>");
        sb.append("<td class='ListHeader'><div class='SolidText'>Name</div></td>");
        sb.append("<td class='ListHeader'><div class='SolidText'>Alignment</div></td>");
        sb.append("<td class='ListHeader'><div class='SolidText'>Open<br>Planet<br>Bonus</div></td>");
        sb.append("<td class='ListHeader'><div class='SolidText'>Closed<br>Planet<br>Bonus</div></td>");
        sb.append("<td class='ListHeader'><div class='SolidText'>Resistance<br>Bonus</div></td>");
        sb.append("<td class='ListHeader'><div class='SolidText'>Besiege<br>Bonus</div></td>");
		/*  sb.append("<td>Starting<br>Wharf<br>Size</td>");
		  sb.append("<td>Wharf<br>Build<br>Cost</td>");
		  sb.append("<td>Wharf<br>Upgrade<br>Costs<br>(m/l/h)</td>");*/
//		  sb.append("<td class='ListHeader'><div class='SolidText'>Start<br>with<br>Space<br>Station</div></td>");
        sb.append("<td class='ListHeader'><div class='SolidText'>Tech<br>Bonus</div></td>");
//		  sb.append("<td class='ListHeader'><div class='SolidText'>Build<br>Space<br>Station<br>Base<br>Cost</div></td>");
//		  sb.append("<td class='ListHeader'><div class='SolidText'>Build<br>Space<br>Station<br>Cost<br>Mulitplier</div></td>");
//		  sb.append("<td class='ListHeader'><div class='SolidText'>VIP's</div></td>");
//		  sb.append("<td class='ListHeader'><div class='SolidText'>Nr<br>Starting<br>Specific<br>VIPTypes</div></td>");
        //sb.append("<td class='ListHeader'><div class='SolidText'>View Details</div></td>");
        sb.append("</tr>\n");
        return sb.toString();
    }

    @JsonIgnore
    public String getHTMLTableRowNO(String gameWorldName, String RowName, int i) {
        Logger.finer("getHTMLTableRow: " + gameWorldName);
        StringBuffer sb = new StringBuffer();
        sb.append("<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',8,1);\" onMouseOut=\"TranparentRow('" + RowName + "',8,0);\">");
        sb.append("<td class='ListText' id='" + RowName + "1'></td>");
        sb.append("<td class='ListText' id='" + RowName + "2'><div class='SolidText'><font color=#\"" + colorHexValue + "\">" + name + "</font></div></td>");
        sb.append("<td class='ListText' id='" + RowName + "3'><div class='SolidText'>" + alignment.toString() + "</div></td>");
        sb.append("<td class='ListText' id='" + RowName + "4'><div class='SolidText'>" + openPlanetBonus + "</div></td>");
        sb.append("<td class='ListText' id='" + RowName + "5'><div class='SolidText'>" + closedPlanetBonus + "</div></td>");
        sb.append("<td class='ListText' id='" + RowName + "6'><div class='SolidText'>" + resistanceBonus + "</div></td>");
		/*  sb.append("<td>" + OrbitalWharf.getSizeString(startingWharfSize) + "</td>");
		  sb.append("<td>" + wharfBuildCost + "</td>");
		  sb.append("<td>" + getWharfUpgradeCostsString() + "</td>");*/
//		  sb.append("<td class='ListText' id='" + RowName + "8'><div class='SolidText'>" + Functions.getYesNo(startWithSS) + "</div></td>");
        sb.append("<td class='ListText' id='" + RowName + "8'><div class='SolidText'>" + techBonus + "</div></td>");
//		  sb.append("<td class='ListText' id='" + RowName + "9'><div class='SolidText'>" + buildOrbitalStructureCostBase + "</div></td>");
//		  sb.append("<td class='ListText' id='" + RowName + "9'><div class='SolidText'>" + buildOrbitalStructureCostMulitplier + "</div></td>");
//		  sb.append("<td class='ListText' id='" + RowName + "9'><div class='SolidText'>" + nrStartingRandomVIPs + "</div></td>");
//		  sb.append("<td class='ListText' id='" + RowName + "12'><div class='SolidText'>" + startingVIPTypes.size() + "</div></td>");
        //sb.append("<td class='ListText' id='" + RowName + "14'><div class='SolidText'><a href=\"faction.jsp?gameworldfilename=" + gameWorldName + "&factionname=" + name + "\">View Details</a></div></td>");
        sb.append("</tr>\n");
        return sb.toString();
    }

    @JsonIgnore
    public String getHTMLCheckbox() {
        return "<input type=\"checkbox\" checked name=\"faction_" + name + "\" value=\"yes\">" + name + "<br>";
    }

}
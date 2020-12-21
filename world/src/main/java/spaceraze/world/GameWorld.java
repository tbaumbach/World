/*
 * Created on 2005-sep-27
 */
package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import spaceraze.util.general.Logger;
import spaceraze.world.diplomacy.GameWorldDiplomacy;
import spaceraze.world.diplomacy.GameWorldDiplomacyRelation;
import spaceraze.world.enums.InitiativeMethod;

import javax.persistence.*;

/**
 * Contains all data for one distinct game world primarily used in individual games.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="GameWorld")
@Table(name = "GAME_WORLD")
public class GameWorld implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //TODO check if we should use SEQUENCE or TABLE
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gameWorld")
	@Builder.Default
	private List<SpaceshipType> shipTypes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gameWorld")
	@Builder.Default
	private List<VIPType> vipTypes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gameWorld")
	@Builder.Default
	private List<Faction> factions = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gameWorld")
	@Builder.Default
	private List<TroopType> troopTypes = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_NEUTRAL_SHIP_SMALL")
	private SpaceshipType neutralSize1;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_NEUTRAL_SHIP_MEDIUM")
	private SpaceshipType neutralSize2;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_NEUTRAL_SHIP_LARGE")
	private SpaceshipType neutralSize3;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_NEUTRAL_TROOP_TYPE")
	private TroopType neutralTroopType;

	private String description;
	private String history;
	private String shortDescription;
	private String howToPlay;
	private String fileName;
	private String fullName;
	private String createdByUser;
	private String createdDate;
	private String changedDate;
	private String battleSimDefaultShips1;
	private String battleSimDefaultShips2;
	private final boolean cumulativeBombardment = false; // cannot be changed anymore...
	private final boolean squadronsSurviveOutsideCarriers = false; // On Non-Friendly Planets, cannot be changed anymore...
	private InitiativeMethod initMethod = InitiativeMethod.WEIGHTED_1;
	private int closedNeutralPlanetChance = 60; // %
	private int razedPlanetChance = 0; // %

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gameWorld")
	@Builder.Default
	private List<Alignment> alignments = new ArrayList<>();

	private String versionId = "1"; // should be increased every time a new version of a GameWorld is published

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gameWorld")
	@Builder.Default
	private List<GameWorldDiplomacyRelation> gameWorldDiplomacyRelations = new ArrayList<>();
	private GameWorldDiplomacy diplomacy;
	private int baseBombardmentDamage = 1000; // default value (always kills the troop) 50% hit chance.
	private boolean adjustScreenedStatus = true;

	public void addShipType(SpaceshipType sst){
		shipTypes.add(sst);
	}

	public void addVipType(VIPType vt){
		vipTypes.add(vt);
	}

	public void addTroopType(TroopType tt){
		troopTypes.add(tt.clone());
	}
	
	public List<TroopType> getTroopTypes(){
		return troopTypes;
	}
	
	public boolean isTroopGameWorld(){
		return (troopTypes.size() > 0);
	}

    public SpaceshipType getSpaceshipTypeByName(String name){
    	SpaceshipType foundsst = shipTypes.stream().filter(spaceshipType -> spaceshipType.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    	
    	if (foundsst != null){
        	Logger.finest("GameWorld.getSpaceshipTypeByName, name:" + name + " -> " + foundsst);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en skeppstyp i gameworlden
        	Logger.severe("GameWorld(" + fullName + ").getSpaceshipTypeByName, name:" + name + " -> " + foundsst);
    		Thread.dumpStack();
    	}
    	return foundsst;
    }
    
    public SpaceshipType getSpaceshipTypeByShortName(String shortName){
    	SpaceshipType foundsst = shipTypes.stream().filter(spaceshipType -> spaceshipType.getShortName().equalsIgnoreCase(shortName)).findAny().orElse(null);
    	
    	if (foundsst != null){
        	Logger.finest("GameWorld.getSpaceshipTypeByShortName, shortName:" + shortName + " -> " + foundsst);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en skeppstyp i gameworlden
        	Logger.severe("GameWorld(" + fullName + ").getSpaceshipTypeByShortName, shortName:" + shortName + " -> " + foundsst);
    		Thread.dumpStack();
    	}
    	return foundsst;
    }

    public BuildingType getBuildingTypeByName(String btname){
		Faction faction = factions.stream().filter(faction1 -> faction1.getBuildingType(btname) != null).findFirst().orElse(null);
		BuildingType foundbt = faction.getBuildingType(btname);
    	if (foundbt != null){
        	Logger.finest("GameWorld.getBuildingTypeByName, btname:" + btname + " -> " + foundbt);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en buildingType i gameworlden
        	Logger.severe("GameWorld(" + fullName + ").getBuildingTypeByName, btname:" + btname + " -> " + foundbt);
    		Thread.dumpStack();
    	}
    	return foundbt;
    }

    public TroopType getTroopTypeByName(String ttname){
    	return getTroopTypeByName(ttname,true);
    }

    public TroopType getTroopTypeByName(String ttname, boolean errorDump){
    	TroopType foundtt = null;
    	int i = 0;
    	while ((i < troopTypes.size()) & (foundtt == null)){
    		TroopType temptt = troopTypes.get(i);
    		if (temptt.getUniqueName().equalsIgnoreCase(ttname)){
    			foundtt = temptt;
    		}else{
    			i++;
    		}
    	}
    	if (foundtt != null){
    		Logger.finest("GameWorld.getTroopTypeByName, ttname:" + ttname + " -> " + foundtt);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en trupptyp i gameworlden
    		if (errorDump){
    			Logger.severe("GameWorld(" + fullName + ").getTroopTypeByName, ttname:" + ttname + " -> " + foundtt);
    			Thread.dumpStack();
    		}
    	}
    	return foundtt;
    }

    public VIPType getVIPTypeByName(String name){
    	VIPType vip = vipTypes.stream().filter(vipType -> vipType.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    	
    	if (vip != null){
    	   	Logger.finest("GameWorld.getVIPTypeByName, vip:" + vip + " -> " + vip);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en viptyp i gameworlden
    	   	Logger.severe("GameWorld(" + fullName + ").getVIPTypeByName, vip:" + vip + " -> " + vip);
    		Thread.dumpStack();
    	}
    	return vip;
    }
    
    public VIPType getVIPTypeByShortName(String shortName){
    	VIPType vip = vipTypes.stream().filter(vipType -> vipType.getShortName().equalsIgnoreCase(shortName)).findAny().orElse(null);
    	
    	if (vip != null){
    	   	Logger.finest("GameWorld.getVIPTypeByShortName, vip:" + vip + " -> " + vip);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en viptyp i gameworlden
    	   	Logger.severe("GameWorld(" + fullName + ").getVIPTypeByShortName, vip:" + vip + " -> " + vip);
    		Thread.dumpStack();
    	}
    	return vip;
    }

    public TroopType getTroopTypeByShortName(String ttshortname){
    	TroopType foundtt = null;
    	int i = 0;
    	while ((i < troopTypes.size()) & (foundtt == null)){
    		TroopType temptt = troopTypes.get(i);
    		if (temptt.getUniqueShortName().equalsIgnoreCase(ttshortname)){
    			foundtt = temptt;
    		}else{
    			i++;
    		}
    	}
    	Logger.finer("GameWorld.getTroopTypeShortByName, ttname:" + ttshortname + " -> " + foundtt);
    	return foundtt;
    }

    
	public String getChangedDate() {
		return changedDate;
	}
	
	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}
	
	public String getCreatedByUser() {
		return createdByUser;
	}
	
	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public boolean isCumulativeBombardment() {
		return cumulativeBombardment;
	}
/*	Bombardment will always be non-cumulative
	public void setCumulativeBombardment(boolean cumulativeBombardment) {
		this.cumulativeBombardment = cumulativeBombardment;
	}
*/	
	@JsonIgnore
	public String getTotalDescription(){
		String totalDescription = "";
    	
    	if(shortDescription != null && !shortDescription.equals("")){
    		totalDescription += "Short Description\n";
        	totalDescription += shortDescription + "\n\n";
    	}
    	if(description != null && !description.equals("")){
    		totalDescription +="Description\n";
        	totalDescription += description + "\n\n";
    	}
    	if(howToPlay != null && !howToPlay.equals("")){
    		totalDescription +="Description\n";
        	totalDescription += howToPlay + "\n\n";
    	}
    	if(history != null && !history.equals("")){
    		totalDescription +="History\n";
        	totalDescription += history + "\n\n";
    	}
    	totalDescription +="Created by: " + createdByUser+ "\n";
    	totalDescription +="Created date: " + createdDate + "\n";
    	totalDescription +="Changed date: " + changedDate + "\n";
    	totalDescription +="Version: " + versionId + "\n";
    	
    	return totalDescription;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public String getHistory() {
		return history;
	}
	
	public void setHistory(String history) {
		this.history = history;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public List<Faction> getFactions() {
		return factions;
	}
	
	public List<SpaceshipType> getShipTypes() {
		return shipTypes;
	}
	
	public List<VIPType> getVipTypes() {
		return vipTypes;
	}

	public InitiativeMethod getInitMethod() {
		return initMethod;
	}
	
	@JsonIgnore
	public SpaceshipType getNeutralSize1() {
		return neutralSize1;
	}
	
	public void setNeutralSize1(SpaceshipType neutralSize1) {
		this.neutralSize1 = neutralSize1;
	}

	@JsonIgnore
	public SpaceshipType getNeutralSize2() {
		return neutralSize2;
	}

	public void setNeutralSize2(SpaceshipType neutralSize2) {
		this.neutralSize2 = neutralSize2;
	}

	@JsonIgnore
	public SpaceshipType getNeutralSize3() {
		return neutralSize3;
	}

	public void setNeutralSize3(SpaceshipType neutralSize3) {
		this.neutralSize3 = neutralSize3;
	}
	
	public String getNeutralSmallShip() {
		return neutralSize1.getName();
	}
	
	public String getNeutralMediumShip() {
		return neutralSize2.getName();
	}
	
	public String getNeutralLargeShip() {
		return neutralSize3.getName();
	}
	
	@JsonIgnore
	public String getBattleSimDefaultShips1() {
		return battleSimDefaultShips1;
	}

	public void setBattleSimDefaultShips1(String battleSimDefaultShips1) {
		this.battleSimDefaultShips1 = battleSimDefaultShips1;
	}

	@JsonIgnore
	public String getBattleSimDefaultShips2() {
		return battleSimDefaultShips2;
	}

	public void setBattleSimDefaultShips2(String battleSimDefaultShips2) {
		this.battleSimDefaultShips2 = battleSimDefaultShips2;
	}

	@JsonIgnore
	public List<VIPType> getBattleVIPtypes(){
		Logger.finer("getBattleVIPtypes()");
		List<VIPType> battleVips = new ArrayList<VIPType>();
		for (Object o : vipTypes) {
			VIPType aVIPtype = (VIPType)o;
//			LoggingHandler.finest("a vip: " + aVIPtype.getName());
			if (aVIPtype.isBattleVip()){
//				LoggingHandler.finest("  adding: " + aVIPtype.getName());
				battleVips.add(aVIPtype);
			}
		}
		return battleVips;
	}

	@JsonIgnore
	public List<VIPType> getLandBattleVIPtypes(){
		Logger.finer("getLandBattleVIPtypes()");
		List<VIPType> battleVips = new ArrayList<VIPType>();
		for (Object o : vipTypes) {
			VIPType aVIPtype = (VIPType)o;
			if (aVIPtype.isLandBattleVip()){
				battleVips.add(aVIPtype);
			}
		}
		return battleVips;
	}

	// TODO Remove
	@JsonIgnore
	public boolean hasSquadrons(){
		boolean hasSquadrons = false;
		int i = 0;
		while ((i < shipTypes.size()) && (!hasSquadrons)){
			SpaceshipType sst = (SpaceshipType)shipTypes.get(i);
			if (sst.isSquadron()){
				hasSquadrons = true;
			}
			i++;
		}
		return hasSquadrons;
	}
	
	public boolean hasTroops(){
		return troopTypes.size() > 0;
	}

	
	public Faction findFaction(String aFactionName){
		Faction found = null;
		for (Faction tmpf : factions) {
			if (tmpf.getName().equalsIgnoreCase(aFactionName)){
				found = tmpf;
			}
		}
		assert found != null : "Faction with name " + aFactionName + " does not exist";
		return found;
	}

	@JsonIgnore
	public boolean isSquadronsSurviveOutsideCarriers() {
		return squadronsSurviveOutsideCarriers;
	}
/*
	public void setSquadronsSurviveOutsideCarriers(boolean squadronsSurviveOutsideCarriers) {
		this.squadronsSurviveOutsideCarriers = squadronsSurviveOutsideCarriers;
	}
*/
	@JsonIgnore
	public int getClosedNeutralPlanetChance() {
		return closedNeutralPlanetChance;
	}

	public void setClosedNeutralPlanetChance(int closedNeutralPlanetChance) {
		this.closedNeutralPlanetChance = closedNeutralPlanetChance;
	}

	@JsonIgnore
	public int getRazedPlanetChance() {
		return razedPlanetChance;
	}

	public void setRazedPlanetChance(int razedPlanetChance) {
		this.razedPlanetChance = razedPlanetChance;
	}

	public List<Alignment> getAlignments() {
		return alignments;
	}

	public void setAlignments(List<Alignment> alignments) {
		this.alignments = alignments;
	}
	
	public String toString(){
		return "gameWorld: " + fullName + " (" + fileName + ")";
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	@JsonIgnore
	public TroopType getNeutralTroopType() {
		return neutralTroopType;
	}
	
	public String getNeutralTroopTypename() {
		return neutralTroopType.getUniqueName();
	}

	public void setNeutralTroopType(TroopType neutralTroopType) {
		this.neutralTroopType = neutralTroopType;
	}

	public GameWorldDiplomacy getDiplomacy() {
		return diplomacy;
	}

	public int getBaseBombardmentDamage() {
		return baseBombardmentDamage;
	}

	public void setBaseBombardmentDamage(int baseBombardmentDamage) {
		this.baseBombardmentDamage = baseBombardmentDamage;
	}

	public String getHowToPlay() {
		return howToPlay;
	}

	public void setHowToPlay(String howtoPlay) {
		this.howToPlay = howtoPlay;
	}

	public boolean isResearchWorld(){
		for (Faction faction : factions) {
			if(faction.getResearchAdvantages().size() != 0){
				return true;
			}
		}
		return false;
	}
	
	@JsonIgnore
	public boolean isAdjustScreenedStatus() {
		return adjustScreenedStatus;
	}

	public void setAdjustScreenedStatus(boolean adjustScreenedStatus) {
		this.adjustScreenedStatus = adjustScreenedStatus;
	}

}

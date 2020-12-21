package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import spaceraze.world.enums.TypeOfTroop;
import spaceraze.util.general.Functions;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "BUILDNING_TYPE")
public class BuildingType implements Serializable, Cloneable{
	static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private String shortName;
	private String advantages;
	private boolean inOrbit = false;
	private boolean autoDestructWhenConquered;
	private boolean selfDestructible = true;
	private boolean developed= true;
	private int openPlanetBonus = 0;
	private int closedPlanetBonus = 0;
	private int techBonus = 0;
	private int wharfSize = 0; // if = 0 cannot build ships
	private int troopSize = 0; // if = 0 cannot build troops
	// worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player, planetUnigue = one at planet.
	private boolean worldUnique = false;
	private boolean factionUnique = false;
	private boolean playerUnique = false;
	private boolean planetUnique = false;
	private int buildCost = 0;
	private boolean spaceport;
	int nrProduced;
	//  kan nog vara en ide...  om byggnaden ligger i orbit så fungerar det som för skepp men om byggnaden är på planeten så syns den inte fören en fi har trupper på planeten.
	private boolean visibleOnMap = true;
	
	// war buildings. shieldCapacity= ???, CannonDamage =  damge against enemy ships(one shot), cononRateOfFire(number of shot/turn) 
	private int resistanceBonus = 0;
	private int shieldCapacity = 0;
	private int cannonDamage = 0;
	private int cannonRateOfFire = 0;
	private int cannonHitChance = 50;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "BUILDNING_TYPE_TO_VIP_TYPE",
			joinColumns = @JoinColumn(name = "BUILDNING_ID"),
			inverseJoinColumns = @JoinColumn(name = "VIP_ID"))
	@Builder.Default
	private List<VIPType> buildVIPTypes = new ArrayList<>();

	@ElementCollection // TODO do we need to use (fetch = FetchType.EAGER) ?
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private List<TypeOfTroop> typeOfTroop = new ArrayList<>(); // infantry, armored or support
	private String parentBuildingTypeName;
	
	private boolean alienKiller;
	private int counterEspionage = 0;
	private int exterminator = 0;


	//TODO 2020-11-05 Dessa ser inte ut att användas?
	// GÖR OM skapa en klass o lägg dessa i den. 
	// så att troops får en egen klass + så att build troops klassen kan hämta troops bonus från denna klass
	// adding bonus to troops build on the planet
	private int troopHitBonus = 0; // % on troops bild on planet
	private int troopAttackBonus = 0; // % % on troops bild on planet
	private int troopDefenceBonus = 0; // % % on troops bild on planet
	private int troopSupportBonus = 0; // % % on troops bild on planet

	// adding bonus to troops in ground combat
	private int defenceBonus = 0; // %
	private int supportDefenceBonus = 0; // %

	/*
	//TODO (Tobbe) Dessa användes inte. Samma egenskaper som VIPar har och skall kanske användas i framtiden. Skall vara i % form.
	private int shipTechBonus = 0; // %  on ships bild on planet
	private int shipBuildBonus; // decreases build cost of ships
	private int troopBuildBonus; // decreases build cost of troops
	*/

	public BuildingType(BuildingType original, PlayerBuildingImprovement improvement){
		this.buildVIPTypes = new ArrayList<>();
		this.typeOfTroop = new ArrayList<>();
		this.setName(original.getName());
		this.setDescription(original.getDescription());
		this.setShortName(original.getShortName());
		this.setAdvantages(original.getAdvantages());
		this.setInOrbit(original.isInOrbit());
		this.setAutoDestructWhenConquered(original.isAutoDestructWhenConquered());
		this.setSelfDestructible(original.isSelfDestructible());
		this.setDeveloped(improvement.isDeveloped());
		this.worldUnique = original.isWorldUnique();
		this.factionUnique = original.isFactionUnique();
		this.playerUnique = original.isPlayerUnique();
		this.planetUnique = original.isPlanetUnique();

		this.setOpenPlanetBonus(original.getOpenPlanetBonus() + improvement.getOpenPlanetBonus());
		this.setClosedPlanetBonus(original.getClosedPlanetBonus() + improvement.getClosedPlanetBonus());
		this.setTechBonus(original.getTechBonus() + improvement.getTechBonus());
		this.setWharfSize(improvement.getWharfSize() > 0 ? improvement.getWharfSize() : original.getWharfSize());
		this.setTroopSize(improvement.getTroopSize() > 0 ? improvement.getTroopSize() : original.getTroopSize());
		this.setBuildCost(improvement.getBuildCost() > 0 ? improvement.getBuildCost() : original.getBuildCost(null));
		this.setSpaceport(improvement.isChangeSpaceport() ? improvement.isSpaceport() : original.isSpaceport());
		this.nrProduced = improvement.getNrProduced();
		this.setVisibleOnMap(improvement.isChangeVisibleOnMap() ? improvement.isVisibleOnMap() : original.isVisibleOnMap());

		this.setResistanceBonus(original.getResistanceBonus() + improvement.getResistanceBonus());
		this.setShieldCapacity(original.getShieldCapacity() + improvement.getShieldCapacity());
		this.setCannonDamage(original.getCannonDamage() + improvement.getCannonDamage());
		this.setCannonRateOfFire(original.getCannonRateOfFire() + improvement.getCannonRateOfFire());
		this.setCannonHitChance(original.getCannonHitChance());

		this.setBuildVIPTypes(original.getBuildVIPTypes());
		this.getTypeOfTroop().addAll(original.getTypeOfTroop());
		this.setParentBuildingTypeName(original.getParentBuildingName());

		this.setAlienKiller(improvement.isChangeAlienKiller() ? improvement.isAlienKiller() : original.isAlienKiller());
		this.setCounterEspionage(original.getCounterEspionage() + improvement.getCounterEspionage());
		this.setExterminator(original.getExterminator() + improvement.getExterminator());

		this.setTroopHitBonus(original.getTroopHitBonus() + improvement.getTroopHitBonus());
		this.setTroopAttackBonus(original.getTroopAttackBonus() + improvement.getTroopAttackBonus());
		this.setTroopDefenceBonus(original.getTroopDefenceBonus() + improvement.getTroopDefenceBonus());
		this.setTroopSupportBonus(original.getTroopSupportBonus() + improvement.getTroopSupportBonus());

		this.setDefenceBonus(original.getDefenceBonus() + improvement.getDefenceBonus());
		this.setSupportDefenceBonus(original.getSupportDefenceBonus() + improvement.getSupportDefenceBonus());

	}
	
	public BuildingType(String name, String shortName, int buildCost){
		setName(name);
		setShortName(shortName);
		setBuildCost(buildCost);
		nrProduced = 0;
		//nextBuildingSteps = new ArrayList<BuildingType>();	
		buildVIPTypes = new ArrayList<>();
		typeOfTroop = new ArrayList<>();
	}
	
	 public Building getBuilding(Planet planet, int uniqueId){
		 nrProduced++;
		 return new Building(this, nrProduced, uniqueId, planet);
	 }
	
	public void setParentBuildingTypeName(String parentBuildingTypeName){
		this.parentBuildingTypeName = parentBuildingTypeName;
	}

	public int getClosedPlanetBonus() {
		return closedPlanetBonus;
	}

	public void setClosedPlanetBonus(int closedPlanetBonus) {
		this.closedPlanetBonus = closedPlanetBonus;
	}

	public int getDefenceBonus() {
		return defenceBonus;
	}

	public void setDefenceBonus(int defenceBonus) {
		this.defenceBonus = defenceBonus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdvantages() {
		return advantages;
	}

	public void setAdvantages(String advantages) {
		this.advantages = advantages;
	}
	
	public boolean isDeveloped() {
		return developed;
	}

	public void setDeveloped(boolean developed) {
		this.developed = developed;
	}

	public boolean isInOrbit() {
		return inOrbit;
	}

	public void setInOrbit(boolean inOrbit) {
		this.inOrbit = inOrbit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String name) {
		this.shortName = name;
	}
/*
	public String getNextBuildingStep() {
		return nextBuildingStep;
	}

	public void setNextBuildingStep(String nextBuildingStep) {
		this.nextBuildingStep = nextBuildingStep;
	}
*/
	public int getOpenPlanetBonus() {
		return openPlanetBonus;
	}

	public void setOpenPlanetBonus(int openPlanetBonus) {
		this.openPlanetBonus = openPlanetBonus;
	}

	public String getParentBuildingName() {
		return parentBuildingTypeName;
	}

	/*public void setParentBuilding(BuildingType parentBuildingType) {
		this.parentBuildingType = parentBuildingType;
		//parentBuildingType.addNextBuildingType(this);
	}*/

	public int getResistanceBonus() {
		return resistanceBonus;
	}

	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}
/*
	public int getShipTechBonus() {
		return shipTechBonus;
	}

	public void setShipTechBonus(int shipTechBonus) {
		this.shipTechBonus = shipTechBonus;
	}
*/

	public int getSupportDefenceBonus() {
		return supportDefenceBonus;
	}

	public void setSupportDefenceBonus(int supportDefenceBonus) {
		this.supportDefenceBonus = supportDefenceBonus;
	}
	
	
	// set all troop bonus on same time
	public void setTroopBonus(int troopBonus){
		setTroopAttackBonus(troopBonus);
		setTroopDefenceBonus(troopBonus);
		setTroopHitBonus(troopBonus);
		setTroopSupportBonus(troopBonus);
		
	}

	public int getTroopAttackBonus() {
		return troopAttackBonus;
	}

	public void setTroopAttackBonus(int troopAttackBonus) {
		this.troopAttackBonus = troopAttackBonus;
	}

	public int getTroopDefenceBonus() {
		return troopDefenceBonus;
	}

	public void setTroopDefenceBonus(int troopDefenceBonus) {
		this.troopDefenceBonus = troopDefenceBonus;
	}

	public int getTroopHitBonus() {
		return troopHitBonus;
	}

	public void setTroopHitBonus(int troopHitBonus) {
		this.troopHitBonus = troopHitBonus;
	}

	public int getTroopSupportBonus() {
		return troopSupportBonus;
	}

	public void setTroopSupportBonus(int troopSupportBonus) {
		this.troopSupportBonus = troopSupportBonus;
	}
/*
	public boolean isReplaceParentBuilding() {
		return replaceParentBuilding;
	}

	public void setReplaceParentBuilding(boolean replaceParentBuilding) {
		this.replaceParentBuilding = replaceParentBuilding;
	}
	*/


	public int getBuildCost(VIP vipWithBonus){
      int tempBuildCost = buildCost;
      if (vipWithBonus != null){
    	  int vipBuildbonus = 100 - vipWithBonus.getBuildingBuildBonus();
    	  double tempBuildBonus = vipBuildbonus / 100.0;
    	  tempBuildCost = (int) Math.round(tempBuildCost * tempBuildBonus);
    	  if (tempBuildCost < 1){
    		  tempBuildCost = 1;
    	  }
      }
      return tempBuildCost;
    }

	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}

	public int getTechBonus() {
		return techBonus;
	}

	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}
	
	// Wharfs logic
	
	public static String getSizeString(int slots){		
		String size = "small";
		if (slots == 2){
			size = "medium";
		}else
		if (slots == 3){
			size = "large";
		}else
		if (slots == 5){
			size = "huge";
		}
		return size;
	}

	public String getSizeString(){
		
	      String size = "small";
	      if (wharfSize ==2){
	        size = "medium";
	      }else
	      if (wharfSize == 3){
	        size = "large";
	      }else
	      if (wharfSize == 5){
	        size = "huge";
	      }
	      return size;
	    }

//	    public static String getSizeString(int aTonnage){
//	        String size = "Small";
//	        if ((aTonnage >300) & (aTonnage <=600)){
//	          size = "Medium";
//	        }else
//	        if ((aTonnage >600) & (aTonnage <=900)){
//	          size = "Large";
//	        }else
//	        if (aTonnage >900){
//	          size = "Huge";
//	        }
//	        return size;
//	      }

		public int getWharfSize() {
			return wharfSize;
		}

		public void setWharfSize(int wharfSize) {
			this.wharfSize = wharfSize;
		}

		public boolean isSpaceport() {
			return spaceport;
		}

		public void setSpaceport(boolean spaceport) {
			this.spaceport = spaceport;
		}

	public boolean isAutoDestructWhenConquered() {
		return autoDestructWhenConquered;
	}

	public void setAutoDestructWhenConquered(boolean autoDestructWhenConquered) {
		this.autoDestructWhenConquered = autoDestructWhenConquered;
	}

	@JsonIgnore
	public List<VIPType> getBuildVIPTypes() {
		return buildVIPTypes;
	}
	
	public List<String> getBuildVIPTypesName() {
		List<String> vipTypesName = new ArrayList<String>();
		for (VIPType vip : buildVIPTypes) {
			vipTypesName.add(vip.getName());
		}
		return vipTypesName;
	}

	public void setBuildVIPTypes(List<VIPType> buildVIPTypes) {
		this.buildVIPTypes = buildVIPTypes;
	}
	
	public void addBuildVIPType(VIPType buildVIPType) {
		this.buildVIPTypes.add(buildVIPType);
	}

	public boolean isFactionUnique() {
		return factionUnique;
	}

	public boolean isPlanetUnique() {
		return planetUnique;
	}

	public void setPlanetUnique(boolean planetUnique) {
		this.planetUnique = planetUnique;
	}

	public boolean isPlayerUnique() {
		return playerUnique;
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.visibleOnMap = visibleOnMap;
	}

	public boolean isWorldUnique() {
		return worldUnique;
	}

	public void setWorldUnique(boolean worldUnique) {
		this.worldUnique = worldUnique;
	}
	
	public String getUniqueString(){
		String uniqueString = "";
  
		if(planetUnique){
			uniqueString = "Planet unique";
		}else
		if(playerUnique){
			uniqueString = "Player unique";
		}else
		if(factionUnique){
			uniqueString = "Faction unique";
		}else
		if(worldUnique){
			uniqueString = "World unique";
		}
  
		return uniqueString;
	}

	public int getShieldCapacity() {
		return shieldCapacity;
	}

	public void setShieldCapacity(int shieldCapacity) {
		this.shieldCapacity = shieldCapacity;
	}

	public int getTroopSize() {
		return troopSize;
	}

	public void setTroopSize(int troopSize) {
		this.troopSize = troopSize;
	}
	public boolean isVIPBuilder(){
		if(buildVIPTypes != null && buildVIPTypes.size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean isShipBuilder(){
		if(wharfSize > 0){
			return true;
		}
		return false;
	}
	
	public boolean isTroopBuilder(){
		if(troopSize > 0){
			return true;
		}
		return false;
	}

	public List<TypeOfTroop> getTypeOfTroop() {
		return typeOfTroop;
	}
	
	public void addTypeOfTroop(TypeOfTroop typeOfTroop) {
		this.typeOfTroop.add(typeOfTroop);
	}
	
	public boolean canBuildTypeOfTroop(TypeOfTroop typeOfTroop){
		
		for(int i=0; i < this.typeOfTroop.size();i++){
			if(this.typeOfTroop.get(i) == typeOfTroop){
				return true;
			}
		}
		return false;
	}

	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}

	@JsonIgnore
	public List<String> getAbilitiesStrings(){
	    List<String> allStrings = new LinkedList<String>();
	    
	    
	    if (worldUnique){
	        allStrings.add("Is World Unique");
	    }
	    if (factionUnique){
	        allStrings.add("Is Faction Unique");
	    }
	    if (playerUnique){
	        allStrings.add("Is Player Unique");
	    }
	    if (planetUnique){
	        allStrings.add("Is Planet Unique");
	    }
	    if (spaceport){
	        allStrings.add("Spaceport");
	    }
	    if (openPlanetBonus > 0){
	        allStrings.add("Open Planet Bonus: " + openPlanetBonus);
	    }
	    if (closedPlanetBonus > 0){
	        allStrings.add("Closed Planet Bonus: " + closedPlanetBonus);
	    }
	    if (techBonus > 0){
	        allStrings.add("Tech Bonus: " + techBonus + "%");
	    }
	    if (wharfSize > 0){
	        allStrings.add("Wharf Size: " + wharfSize);
	    }
	    if (troopSize > 0){
	    	allStrings.add("Build Troop Capacity: " + troopSize);
	    }
	    if (alienKiller){
	        allStrings.add("Alien Killer: prevent infestator to infestate the planet");
	    }
	    if (counterEspionage > 0){
	        allStrings.add("Counter-espionage: " + counterEspionage + "%");
	    }
	    if (exterminator > 0){
	    	allStrings.add("Exterminator: " + exterminator + "%");
	    }
	    if (resistanceBonus > 0){
	    	allStrings.add("Resistance bonus: " + resistanceBonus);
	    }
	    if (shieldCapacity > 0){
	    	allStrings.add("Shield Capacity : " + shieldCapacity);
	    }
	    if (cannonDamage > 0){
	    	allStrings.add("Cannon Damage: " + cannonDamage);
	    	allStrings.add("Cannon Rate Of Fire: " + cannonRateOfFire);
	    	allStrings.add("Cannon hit chance: " + cannonHitChance);
	    }
	    
	    if (troopSize > 0){
	    	String tmp = "Troop building:";
	    	boolean addComma = false;
	    	for (TypeOfTroop type : typeOfTroop) {
	    		if (addComma){
	    			tmp += ",";
	    		}
	    		tmp += " " + type;
	    		addComma = true;
			}
	    	allStrings.add(tmp);
	    }

	    if (buildVIPTypes.size() > 0){
	    	String tmp = "VIP building:";
	    	boolean addComma = false;
	    	for (VIPType vipType : buildVIPTypes) {
	    		if (addComma){
	    			tmp += ",";
	    		}
	    		tmp += " " + vipType.getName();
	    		addComma = true;
			}
	    	allStrings.add(tmp);
	    }

	    if(!visibleOnMap){
	    	allStrings.add("Visible On Map: " + Functions.getYesNo(visibleOnMap));
    	}
	
	    if (autoDestructWhenConquered){
	    	allStrings.add("Will Auto Destruct When Conquered");
	    }
	    
//	    allStrings.add("Description: " + description);
	    //private boolean inOrbit = false;
		//private boolean visibleOnMap = true;
		
		
//		private int shipTechBonus = 0; // %  on ships bild on planet
		
		/*
				
		// adding bonus to troops in ground combat
		private int defenceBonus = 0; // %
		private int airDefanceBonus = 0; // %
		private int suportDefanceBonus = 0; // %
		
	    */
	    
	    if (inOrbit){
	    	allStrings.add("In orbit: can be destroyed by enemy ships in orbit if undefended");
	    }else{
	    	allStrings.add("Placed on planets surface");
	    }

	    return allStrings;
	}

	public boolean isAlienKiller() {
		return alienKiller;
	}

	public void setAlienKiller(boolean ailienkiller) {
		this.alienKiller = ailienkiller;
	}

	public int getCounterEspionage() {
		return counterEspionage;
	}

	public void setCounterEspionage(int counterEspionage) {
		this.counterEspionage = counterEspionage;
	}

	public int getExterminator() {
		return exterminator;
	}

	public void setExterminator(int exterminator) {
		this.exterminator = exterminator;
	}

	public int getCannonHitChance() {
		return cannonHitChance;
	}
	public void setCannonHitChance(int iCannonHitChance) {
		this.cannonHitChance = iCannonHitChance;
	}
	
	public int getCannonDamage() {
		return cannonDamage;
	}

	public void setCannonDamage(int CannonDamage) {
		this.cannonDamage = CannonDamage;
	}

	public int getCannonRateOfFire() {
		return cannonRateOfFire;
	}

	public void setCannonRateOfFire(int CannonRateOfFire) {
		this.cannonRateOfFire = CannonRateOfFire;
	}

	public boolean isSelfDestructible() {
		return selfDestructible;
	}

	public void setSelfDestructible(boolean selfDestructible) {
		this.selfDestructible = selfDestructible;
	}


}

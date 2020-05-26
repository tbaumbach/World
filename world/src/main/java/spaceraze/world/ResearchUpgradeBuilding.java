package spaceraze.world;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import spaceraze.util.general.Functions;

@SuppressWarnings("unused")
public class ResearchUpgradeBuilding extends BuildingImprovement {

	public ResearchUpgradeBuilding(String name){
		super(name);
	}

	public String doResearch(PlayerBuildingImprovement improvement){
		  String text;
		  text = "\nThe building type " + getName() + " has been upgraded";
	    	
		  if(getBuildCost() > 0){
			  improvement.setBuildCost(improvement.getBuildCost() + getBuildCost());
	    	}
		  
		  	if(getWharfSize() > 0){
		  		improvement.setWharfSize(getWharfSize());
	    	}
		  	if(getTroopSize() > 0){
		  		improvement.setTroopSize(getTroopSize());
	    	}
	    	if(getExterminator() > 0){
	    		improvement.setExterminator(improvement.getExterminator() + getExterminator());
	    	}
	    	if(getResistanceBonus() > 0){
	    		improvement.setResistanceBonus(improvement.getResistanceBonus() + getResistanceBonus());
	    	}
	    	if(getShieldCapacity() > 0){
	    		improvement.setShieldCapacity(improvement.getShieldCapacity() + getShieldCapacity());
	    	}
	    	if(getCannonDamage() > 0){
	    		improvement.setCannonDamage(improvement.getCannonDamage() + getCannonDamage());
	    	}
	    	if(getCannonRateOfFire() > 0){
	    		improvement.setCannonRateOfFire(improvement.getCannonRateOfFire() + getCannonRateOfFire());
	    	}
	    	/*if(shipBuildBonus > 0){
	    		improvement.setShipBuildBonus(improvement.getShipBuildBonus() + shipBuildBonus);
	    	}
	    	if(troopBuildBonus > 0){
	    		improvement.setTroopBuildBonus(improvement.getTroopBuildBonus() + troopBuildBonus);
	    	}
	    	if(vipBuildBonus > 0){
	    		improvement.setVipBuildBonus(improvement.getVipBuildBonus() + vipBuildBonus);
	    	}
	    	if(buildingBuildBonus > 0){
	    		improvement.setBuildingBuildBonus(improvement.getBuildingBuildBonus() + buildingBuildBonus);
	    	}*/
	    	if(getTechBonus() > 0){
	    		improvement.setTechBonus(improvement.getTechBonus() + getTechBonus());
	    	}
	    	if(getOpenPlanetBonus() > 0){
	    		improvement.setOpenPlanetBonus(improvement.getOpenPlanetBonus() + getOpenPlanetBonus());
	    	}
	    	if(getClosedPlanetBonus() > 0){
	    		improvement.setClosedPlanetBonus(improvement.getClosedPlanetBonus() + getClosedPlanetBonus());
	    	}
	    	if(isChangeSpaceport()){
	    		improvement.setSpaceport(isSpaceport());
	    	}
	    	if(isChangeVisibleOnMap()){
	    		improvement.setVisibleOnMap(isVisibleOnMap());
	    	}
	    	/*
	    	if(aimBonus > 0){
	    		improvement.setAimBonus(improvement.getAimBonus() + aimBonus);
	    	}
	    	if(troopAttacksBonus > 0){
	    		improvement.setTroopAttacksBonus(improvement.getTroopAttacksBonus() + troopAttacksBonus);
	    	}
	    	if(landBattleGroupAttacksBonus > 0){
	    		improvement.setLandBattleGroupAttacksBonus(improvement.getLandBattleGroupAttacksBonus() + landBattleGroupAttacksBonus);
	    	}*/
	    	
	    	return text;
	    }
	
	@JsonIgnore
	public String getResearchText(){
    	String text;
    	
    	text= "Change properties for the Building type: " + getName();
    	
    	if(getBuildCost() > 0){
    		text+="\n-Build cost: " + addPlus(getBuildCost());
    	}
    	if(getWharfSize() > 0){
    		text+="\n-Wharf size: " + getWharfSize();
	  	}
	  	if(getTroopSize() > 0){
	  		text+="\n-Troop size: " + getTroopSize();
	  	}
    	if(getCounterEspionage() > 0){
    		text+="\n-CounterEspionage: " + addPlus(getCounterEspionage()) + "%";
    	}
    	if(getExterminator() > 0){
    		text+="\n-Exterminator: " + addPlus(getExterminator()) + "%";
    	}
    	if(getShieldCapacity() > 0){
    		text+="\n-Shield Capacity: " + addPlus(getShieldCapacity());
    	}
    	if(getCannonDamage() > 0){
    		text+="\n-Cannon Damage: " + addPlus(getCannonDamage());
    	}
    	if(getCannonRateOfFire() > 0){
    		text+="\n-Cannon Rate Of Fire: " + addPlus(getCannonRateOfFire());
    	}
    	if(getResistanceBonus() > 0){
    		text+="\n-Resistance Bonus: " + addPlus(getResistanceBonus());
    	}
    	if(getTechBonus() > 0){
    		text+="\n-Tech Bonus: " + addPlus(getTechBonus()) + "%";
    	}
    	if(getOpenPlanetBonus() > 0){
    		text+="\n-Open Planet Income Bonus: " + addPlus(getOpenPlanetBonus());
    	}
    	if(getClosedPlanetBonus() > 0){
    		text+="\n-Closed Planet Income Bonus: " + addPlus(getClosedPlanetBonus());
    	}
    	if(isChangeVisibleOnMap()){
    		text+="\n-Visible On Map: " + Functions.getYesNo(isVisibleOnMap());
    	}
    	if(isChangeSpaceport()){
    		text+="\n-Spaceport: " + Functions.getYesNo(isSpaceport());
    	}
    	/*
    	if(aimBonus > 0){
    		text+="\n-Aim Bonus: " + addPlus(aimBonus);
    	}
    	if(troopAttacksBonus > 0){
    		text+="\n-Troop Attacks Bonus: " + addPlus(troopAttacksBonus);
    	}
    	if(landBattleGroupAttacksBonus > 0){
    		text+="\n-Land Battle Group Attacks Bonus: " + addPlus(landBattleGroupAttacksBonus) + "%";
    	}*/
    	
    	return text;
    }
	
	private String addPlus(int number){
    	StringBuilder retVal = new StringBuilder();
    	if(number > 0){
    		retVal.append("+");
    	} else if(number < 0){
			retVal.append("-");
		}
    	retVal.append(number);
    	return retVal.toString();
    }

    @Override
	public void setSpaceport(boolean spaceport) {
		setChangeSpaceport(true);
		super.setSpaceport(spaceport);
	}

	@Override
	public void setVisibleOnMap(boolean visibleOnMap) {
		setChangeVisibleOnMap(true);
		super.setVisibleOnMap(visibleOnMap);
	}

	@Override
	public void setAlienKiller(boolean alienKiller){
		setChangeAlienKiller(true);
		super.setAlienKiller(alienKiller);
	}
}

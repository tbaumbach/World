package spaceraze.world;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "RESEARCH_UPGRADE_SHIP")
public class ResearchUpgradeShip extends SpaceshipImprovements {
	static final long serialVersionUID = 1L;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_RESEARCH_ADVANTAGE")
	private ResearchAdvantage researchAdvantage;

    
    public ResearchUpgradeShip(String typeId){
    	super(typeId);
    }

    public String doResearch(PlayerSpaceshipImprovement ship){
    	String text;
    	text = "\nThe ship model " + ship.getTypeId() + " have been upgrade";
    	
    	if(getShields() != 0){
    		ship.setShields(ship.getShields() + getShields());
    	}
    	if(getUpkeep() != 0){
    		ship.setUpkeep(ship.getUpkeep() + getUpkeep());
    	}
    	if(getBuildCost() != 0){
    		ship.setBuildCost(ship.getBuildCost() + getBuildCost());
    	}
    	if(getBombardment() != 0){
    		ship.setBombardment(ship.getBombardment() + getBombardment());
    	}
    	if(getIncreaseInitiative() > 0){
    		ship.setIncreaseInitiative(ship.getIncreaseInitiative() + getIncreaseInitiative());
    	}
    	if(getInitDefence() != 0){
    		ship.setInitDefence(ship.getInitDefence() + getInitDefence());
    	}
    	if(getPsychWarfare() != 0){
    		ship.setPsychWarfare(ship.getPsychWarfare() + getPsychWarfare());
    	}
    	if(getRange() != null){
    		ship.setRange(getRange());
    	}
    	if(isChangeNoRetreat()){
    		ship.setNoRetreat(isNoRetreat());
    	}
    	if(isChangeInitSupport()){
    		ship.setInitSupport(isInitSupport());
    	}
    	if(getWeaponsStrengthSmall() != 0){
    		ship.setWeaponsStrengthSmall(ship.getWeaponsStrengthSmall() + getWeaponsStrengthSmall());
    	}
    	if(getWeaponsStrengthMedium() != 0){
    		ship.setWeaponsStrengthMedium(ship.getWeaponsStrengthMedium() + getWeaponsStrengthMedium());
    	}
    	if(getWeaponsStrengthLarge() != 0){
    		ship.setWeaponsStrengthLarge(ship.getWeaponsStrengthLarge() + getWeaponsStrengthLarge());
    	}
    	if(getWeaponsStrengthHuge() != 0){
    		ship.setWeaponsStrengthHuge(ship.getWeaponsStrengthHuge() + getWeaponsStrengthHuge());
    	}
    	if(getWeaponsMaxSalvosMedium() != 0){
    		ship.setWeaponsMaxSalvosMedium(ship.getWeaponsMaxSalvosMedium() + getWeaponsMaxSalvosMedium());
    	}
    	if(getWeaponsMaxSalvosLarge() != 0){
    		ship.setWeaponsMaxSalvosLarge(ship.getWeaponsMaxSalvosLarge() + getWeaponsMaxSalvosLarge());
    	}
    	if(getWeaponsMaxSalvosHuge() != 0){
    		ship.setWeaponsMaxSalvosHuge(ship.getWeaponsMaxSalvosHuge() + getWeaponsMaxSalvosHuge());
    	}
    	if(getArmorSmall() != 0){
    		ship.setArmorSmall(ship.getArmorSmall() + getArmorSmall());
    	}
    	if(getArmorMedium() != 0){
    		ship.setArmorMedium(ship.getArmorMedium() + getArmorMedium());
    	}
    	if(getArmorLarge() != 0){
    		ship.setArmorLarge(ship.getArmorLarge() + getArmorLarge());
    	}
    	if(getArmorHuge() != 0){
    		ship.setArmorHuge(ship.getArmorHuge() + getArmorHuge());
    	}
    	if(getSupply() != null){
    		ship.setSupply(getSupply());
    	}
    	if(getWeaponsStrengthSquadron() != 0){
    		ship.setWeaponsStrengthSquadron(ship.getWeaponsStrengthSquadron() + getWeaponsStrengthSquadron());
    	}
    	if(getSquadronCapacity() != 0){
    		ship.setSquadronCapacity(ship.getSquadronCapacity() + getSquadronCapacity());
    	}
    	if(getIncEnemyClosedBonus() != 0){
    		ship.setIncEnemyClosedBonus(ship.getIncEnemyClosedBonus() + getIncEnemyClosedBonus());
    	}
    	if(getIncNeutralClosedBonus() != 0){
    		ship.setIncNeutralClosedBonus(ship.getIncNeutralClosedBonus() + getIncNeutralClosedBonus());
    	}
    	if(getIncFriendlyClosedBonus() != 0){
    		ship.setIncFriendlyClosedBonus(ship.getIncFriendlyClosedBonus() + getIncFriendlyClosedBonus());
    	}
    	if(getIncOwnClosedBonus() != 0){
    		ship.setIncOwnClosedBonus(ship.getIncOwnClosedBonus() + getIncOwnClosedBonus());
    	}
    	if(getIncEnemyOpenBonus() != 0){
    		ship.setIncEnemyOpenBonus(ship.getIncEnemyOpenBonus() + getIncEnemyOpenBonus());
    	}
    	if(getIncNeutralOpenBonus() != 0){
    		ship.setIncNeutralOpenBonus(ship.getIncNeutralOpenBonus() + getIncNeutralOpenBonus());
    	}
    	if(getIncFriendlyOpenBonus() != 0){
    		ship.setIncFriendlyOpenBonus(ship.getIncFriendlyOpenBonus() + getIncFriendlyOpenBonus());
    	}
    	if(getIncOwnOpenBonus() != 0){
    		ship.setIncOwnOpenBonus(ship.getIncOwnOpenBonus() + getIncOwnOpenBonus());
    	}
    	if(getDescription() != null){
    		ship.setDescription(getDescription());
    	}
    	if(getHistory() != null){
    		ship.setHistory(getHistory());
    	}
    /*	if(shortDescription != null){
    		ship.setShortDescription(shortDescription);
    	}
    	if(advantages != null){
    		ship.setAdvantages(advantages);
    	}
    	if(disadvantages != null){
    		ship.setDisadvantages(disadvantages);
    	}*/
    	if(isChangePlanetarySurvey()){
    		ship.setPlanetarySurvey(isPlanetarySurvey());
    	}
    	if(isCanAttackScreenedShips()){
    		ship.setCanAttackScreenedShips(isCanAttackScreenedShips());
    	}
    	if(isLookAsCivilian()){
    		ship.setLookAsCivilian(isLookAsCivilian());
    	}
    	if(isChangeCanBlockPlanet()){
    		ship.setCanBlockPlanet(isCanBlockPlanet());
    	}
    	if(isChangeVisibleOnMap()){
    		ship.setVisibleOnMap(isVisibleOnMap());
    	}
    	if(getTroopCarrier() != 0){
    		ship.setTroopCarrier(ship.getTroopCarrier() + getTroopCarrier());
    	}
    	
    	return text;
    }
    
    @JsonIgnore
    public String getResearchText(){
    	String text;
    	
    	text= "Change propertys for the ship model: " + getTypeId();
    	
    	if(getShields() != 0){
    		text+="\n-Shields: " + addplus(getShields());
    	}
    	if(getUpkeep() != 0){
    		text+="\n-Upkeep: " + addplus(getUpkeep());
    	}
    	if(getBuildCost() != 0){
    		text+="\n-Build cost: " + addplus(getBuildCost());
    	}
    	if(getBombardment() != 0){
    		text+="\n-Bombardment: " + addplus(getBombardment());
    	}
    	if(getIncreaseInitiative() != 0){
    		text+="\n-IncreaseInitiative: " + addplus(getIncreaseInitiative());
    	}
    	if(getInitDefence() != 0){
    		text+="\n-InitDefence: " + addplus(getInitDefence());
    	}
    	if(getPsychWarfare() != 0){
    		text+="\n-psychWarfare: " + addplus(getPsychWarfare());
    	}
    	if(getRange() != null){
    		text+="\n-Range: " + getRange().toString();
    	}
    	if(isChangeNoRetreat()){
    		text+="\n-No Retreat: " + addYesOrNo(isNoRetreat());
    	}
    	if(isChangeInitSupport()){
    		text+="\n-InitSupport: " + addYesOrNo(isInitSupport());
    	}
		if(getWeaponsStrengthSquadron() != 0){
			text+="\n-Weapons strength squadron: " + addplus(getWeaponsStrengthSquadron());
		}
    	if(getWeaponsStrengthSmall() != 0){
    		text+="\n-Weapons strength small: " + addplus(getWeaponsStrengthSmall());
    	}
    	if(getWeaponsStrengthMedium() != 0){
    		text+="\n-Weapons strength medium: " + addplus(getWeaponsStrengthMedium());
    	}
    	if(getWeaponsStrengthLarge() != 0){
    		text+="\n-Weapons strength large: " + addplus(getWeaponsStrengthLarge());
    	}
    	if(getWeaponsStrengthHuge() != 0){
    		text+="\n-Weapons strength huge: " + addplus(getWeaponsStrengthHuge());
    	}
    	if(getArmorSmall() != 0){
    		text+="\n-Armor Small: " + addplus(getArmorSmall());
    	}
    	if(getArmorMedium() != 0){
    		text+="\n-Armor Medium: " + addplus(getArmorMedium());
    	}
    	if(getArmorLarge() != 0){
    		text+="\n-Armor Large: " + addplus(getArmorLarge());
    	}
    	if(getArmorHuge() != 0){
    		text+="\n-Armor Huge: " + addplus(getArmorHuge());
    	}
    	if(getSupply() != null){
    		text+="\n-Supply ships size: " + getSupply().getDescription();
    	}

    	if(getSquadronCapacity() != 0){
    		text+="\n-Squadron carrier capacity: " + addplus(getSquadronCapacity());
    	}
    	if(getIncEnemyClosedBonus() != 0){
    		text+="\n-Incom enemy closed bonus: " + addplus(getIncEnemyClosedBonus());
    	}
    	if(getIncNeutralClosedBonus() != 0){
    		text+="\n-Incom neutral closed bonus: " + addplus(getIncNeutralClosedBonus());
    	}
    	if(getIncFriendlyClosedBonus() != 0){
    		text+="\n-Incom frendly closed bonus: " + addplus(getIncFriendlyClosedBonus());
    	}
    	if(getIncOwnClosedBonus() != 0){
    		text+="\n-Incom own closed bonus: " + addplus(getIncOwnClosedBonus());
    	}
    	if(getIncEnemyOpenBonus() != 0){
    		text+="\n-Incom enemy open bonus: " + addplus(getIncEnemyOpenBonus());
    	}
    	if(getIncNeutralOpenBonus() != 0){
    		text+="\n-Incom neutral open bonus: " + addplus(getIncNeutralOpenBonus());
    	}
    	if(getIncFriendlyOpenBonus() != 0){
    		text+="\n-Incom frendly open Bbnus: " + addplus(getIncFriendlyOpenBonus());
    	}
    	if(getIncOwnOpenBonus() != 0){
    		text+="\n-Incom own open bonus: " + addplus(getIncOwnOpenBonus());
    	}
    	if(isChangePlanetarySurvey()){
    		text+="\n-Planetary survey: " + addYesOrNo(isPlanetarySurvey());
    	}
    	if(isChangeCanAttackScreenedShips()){
    		text+="\n-Can attack screened ships: " + addYesOrNo(isCanAttackScreenedShips());
    	}
    	if(isChangeLookAsCivilian()){
    		text+="\n-Look as civilian: " + addYesOrNo(isLookAsCivilian());
    	}
    	if(isChangeCanBlockPlanet()){
    		text+="\n-Can block planet: " + addYesOrNo(isCanBlockPlanet());
    	}
    	if(isChangeVisibleOnMap()){
    		text+="\n-Visible on nap: " + addYesOrNo(isVisibleOnMap());
    	}
    	if(getTroopCarrier() != 0){
    		text+="\n-Troop Capacity: " + addplus(getTroopCarrier());
    	}
    	
    	
    	return text;
    }
    
    private String addplus(int number){
    	if(number > 0){
    		return "+" +number;
    	} else if (number < 0){
			return "-" +number;
		}
    	return Integer.toString(number);
    }
    private String addYesOrNo(boolean test){
    	if(test){
    		return "Yes";
    	}
    	return "No";
    }

    @Override
	public void setCanAttackScreenedShips(boolean canAttackScreenedShips) {
		this.setChangeCanAttackScreenedShips(true);
		super.setCanAttackScreenedShips(canAttackScreenedShips);
	}

	@Override
	public void setCanBlockPlanet(boolean canBlockPlanet) {
		this.setChangeCanBlockPlanet(true);
		super.setCanBlockPlanet(canBlockPlanet);
	}

	@Override
	public void setInitSupport(boolean initSupport) {
		this.setChangeInitSupport(true);
		super.setInitSupport(initSupport);
	}

	@Override
	public void setLookAsCivilian(boolean lookAsCivilian) {
		this.setChangeLookAsCivilian(true);
		super.setLookAsCivilian(lookAsCivilian);
	}

	@Override
	public void setNoRetreat(boolean noRetreat) {
		this.setChangeNoRetreat(true);
		super.setNoRetreat(noRetreat);
	}

	@Override
	public void setPlanetarySurvey(boolean planetarySurvey) {
		this.setChangePlanetarySurvey(true);
		super.setPlanetarySurvey(planetarySurvey);
	}

	@Override
	public void setVisibleOnMap(boolean visibleOnMap) {
		this.setChangeVisibleOnMap(true);
		super.setVisibleOnMap(visibleOnMap);
	}

}

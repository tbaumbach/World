package spaceraze.world;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.util.general.Functions;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "RESEARCH_UPGRADE_TROOP")
public class ResearchUpgradeTroop extends TroopImprovements{
	static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "FK_RESEARCH_ADVANTAGE")
	private ResearchAdvantage researchAdvantage;

    public ResearchUpgradeTroop(String typeId){
    	super(typeId);
    }
    
    public String doResearch(PlayerTroopImprovement improvement){
    	String text;
    	text = "\nThe troop type " + getTypeId() + " has been upgraded";
    	
    	if(getAttackInfantry() != 0){
			improvement.setAttackInfantry(improvement.getAttackInfantry() + getAttackInfantry());
    	}
    	if(getAttackArmored() != 0){
			improvement.setAttackArmored(improvement.getAttackArmored() + getAttackArmored());
    	}
    	if(getAttackArtillery() != 0){
			improvement.setAttackArtillery(improvement.getAttackArtillery() + getAttackArtillery());
    	}
    	if(getDamageCapacity() != 0){
			improvement.setDamageCapacity(improvement.getDamageCapacity() + getDamageCapacity());
    	}
    	if(getCostBuild() != 0){
			improvement.setCostBuild(improvement.getCostBuild() + getCostBuild());
    	}
    	if(getCostSupport() != 0){
			improvement.setCostSupport(improvement.getCostSupport() + getCostSupport());
    	}
    	if (isChangeSpaceshipTravel()){
			improvement.setSpaceshipTravel(isSpaceshipTravel());
    	}
    /*	if (changeAttackScreened){
    		aTroop.setAttackScreened(attackScreened);
    	}*/
    	if (isChangeVisible()){
			improvement.setVisible(isVisible());
    	}    	
    	return text;
    }
    
    @JsonIgnore
    public String getResearchText(){
    	String text;
    	
    	text= "Change properties for the troop type: " + getTypeId();
    	
    	if(getAttackInfantry() != 0){
    		text += "\n-Attack Infantry: " + addPlus(getAttackInfantry());
    	}
    	if(getAttackArmored() != 0){
    		text += "\n-Attack Armored: " + addPlus(getAttackArmored());
    	}
    	if(getAttackArtillery() != 0){
    		text += "\n-Attack Artillery: " + addPlus(getAttackArtillery());
    	}
    	if(getDamageCapacity() != 0){
    		text += "\n-Damage Capacity: " + addPlus(getDamageCapacity());
    	}
    	if(getCostBuild() != 0){
    		text += "\n-Cost Build: " + addPlus(getCostBuild());
    	}
    	if(getCostSupport() != 0){
    		text += "\n-Cost Support: " + addPlus(getCostSupport());
    	}
    	if (isChangeSpaceshipTravel()){
    		text += "\n-Spaceship Travel: " + Functions.getYesNo(isSpaceshipTravel());
    	}
    /*	if (changeAttackScreened){
    		text += "\n-Attack Screened: " + Functions.getYesNo(attackScreened);
    	}*/
    	if (isChangeVisible()){
    		text += "\n-Visible on map: " + Functions.getYesNo(isVisible());
    	}    	

    	return text;
    }
    
    private String addPlus(int number){
    	StringBuilder retVal = new StringBuilder();
    	if(number > 0){
    		retVal.append("+");
    	}else if (number < 0){
			retVal.append("-");
		}
    	retVal.append(number);
    	return retVal.toString();
    }

	@Override
	public void setSpaceshipTravel(boolean spaceshipTravel) {
		super.setSpaceshipTravel(spaceshipTravel);
		setChangeSpaceshipTravel(true);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setChangeVisible(true);
	}

}

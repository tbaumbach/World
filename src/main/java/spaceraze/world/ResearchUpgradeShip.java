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

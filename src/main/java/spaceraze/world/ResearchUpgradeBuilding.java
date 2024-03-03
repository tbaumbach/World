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
@Table(name = "RESEARCH_UPGRADE_BUILDNING")
public class ResearchUpgradeBuilding extends BuildingImprovement {

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_RESEARCH_ADVANTAGE")
	private ResearchAdvantage researchAdvantage;

	public ResearchUpgradeBuilding(String typeUuid){
		super(typeUuid);
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

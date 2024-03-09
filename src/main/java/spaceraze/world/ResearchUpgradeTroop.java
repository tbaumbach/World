package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

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

package spaceraze.world.report.spacebattle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "OWN_SPACESHIP")
public class OwnSpaceship extends SpaceshipState implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "FK_SPACE_BATTLE_REPORT")
	private SpaceBattleReport spaceBattleReport;
	
	private String name;
	
	public OwnSpaceship(String name, String type, boolean screened, int hullStatus) {
		super(hullStatus, screened, type);
		this.name = name;
	}
	
	public OwnSpaceship(String name, String type, boolean screened) {
		this(name, type, screened, 100);
	}
	
}

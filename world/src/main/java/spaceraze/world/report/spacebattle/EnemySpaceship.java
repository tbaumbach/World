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
@Table(name = "ENEMY_SPACESHIP")
public class EnemySpaceship extends SpaceshipState implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "FK_SPACE_BATTLE_REPORT")
	private SpaceBattleReport spaceBattleReport;
	
	private String type;
	private boolean screend;
	
	public EnemySpaceship(String type, boolean screened, int hullstatus) {
		super(hullstatus, screened, type);
		this.type = type;
		this.screend = screened;
	}
	
	public EnemySpaceship(String type, boolean screend) {
		this(type, screend, 100);
	}
	
	public String getType() {
		return type;
	}

	public boolean isScreend() {
		return screend;
	}

}

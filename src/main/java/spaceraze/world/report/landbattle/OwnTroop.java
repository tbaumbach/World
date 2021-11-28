package spaceraze.world.report.landbattle;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.world.enums.BattleGroupPosition;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "OWN_TROOP")
public class OwnTroop extends TroopState implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "FK_SPACE_BATTLE_REPORT")
	private LandBattleReport landBattleReport;
	
	private String name;
	
	public OwnTroop(String name, String type, int hitPoints, BattleGroupPosition position) {
		super(hitPoints, position, type);
		this.name = name;
	}
	
	public OwnTroop(String name, String type, BattleGroupPosition position) {
		this(name, type, 100, position);
	}

}

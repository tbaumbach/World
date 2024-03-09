package spaceraze.world.report.landbattle;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.world.enums.BattleGroupPosition;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "ENEMY_TROOP")
public class EnemyTroop extends TroopState implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "FK_SPACE_BATTLE_REPORT")
	private LandBattleReport landBattleReport;
	
	public EnemyTroop(String type, int hitpoints, BattleGroupPosition position) {
		super(hitpoints, position, type);
	}
	
	public EnemyTroop(String type, BattleGroupPosition position) {
		this(type, 100, position);
	}


}

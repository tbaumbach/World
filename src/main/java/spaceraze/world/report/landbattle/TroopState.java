package spaceraze.world.report.landbattle;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.world.enums.BattleGroupPosition;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class TroopState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;
	private int startHitPoints; //in %
	private int postBattleHitPoints; //in %
	private BattleGroupPosition position;
	
	public TroopState(int startHitPoints, BattleGroupPosition position, String type) {
		this.startHitPoints = startHitPoints;
		this.position = position;
		this.type = type;
	}
	
	public boolean isDestroyed() {
		return postBattleHitPoints == 0;
	}
	
}

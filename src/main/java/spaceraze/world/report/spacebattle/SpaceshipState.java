package spaceraze.world.report.spacebattle;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class SpaceshipState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int startHullStatus = 100; //in %
	private int postBattleHullState; //in %
	private boolean retreat;
	private String type;
	private boolean screened;

	
	public SpaceshipState(int startHullStatus, boolean screened, String type) {
		this.startHullStatus = startHullStatus;
		this.screened = screened;
		this.type = type;
	}

	public void setPostBattleHullState(int postBattleHullState) {
		this.postBattleHullState = postBattleHullState;
	}
	
	public boolean isStillBattleReady() {
		return !retreat && postBattleHullState > 0;
	}
	
	public boolean isDestroyed() {
		return postBattleHullState == 0;
	}

}

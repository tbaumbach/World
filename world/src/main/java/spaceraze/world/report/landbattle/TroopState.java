package spaceraze.world.report.landbattle;

import java.io.Serializable;

import spaceraze.world.enums.BattleGroupPosition;

public class TroopState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int startHitpoints; //in %
	private int postBattleHitpoints; //in %
	private final BattleGroupPosition position;
	
	public TroopState(int startHitpoints, BattleGroupPosition position) {
		this.startHitpoints = startHitpoints;
		this.position = position;
	}

	public int getPostBattleHitpoints() {
		return postBattleHitpoints;
	}

	public void setPostBattleHitpoints(int postBattleHitpoints) {
		this.postBattleHitpoints = postBattleHitpoints;
	}

	public int getStartHitpoints() {
		return startHitpoints;
	}

	public BattleGroupPosition getPosition() {
		return position;
	}
	
	public boolean isDestroyed() {
		return postBattleHitpoints == 0;
	}
	
}

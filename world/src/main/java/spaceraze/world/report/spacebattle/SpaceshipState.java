package spaceraze.world.report.spacebattle;

import java.io.Serializable;

public abstract class SpaceshipState implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int startHullstatus; //in %
	private int postBattleHullState; //in %
	private boolean retret = false;
	
	public SpaceshipState(int startHullstatus) {
		this.startHullstatus = startHullstatus;
	}
	
	public SpaceshipState() {
		this(100);
	}

	public int getPostBattleHullState() {
		return postBattleHullState;
	}

	public void setPostBattleHullState(int postBattleHullState) {
		this.postBattleHullState = postBattleHullState;
	}

	public boolean isRetret() {
		return retret;
	}

	public void setRetret(boolean retret) {
		this.retret = retret;
	}

	public int getStartHullstatus() {
		return startHullstatus;
	}
	
	public boolean isStillBattleReady() {
		return !retret && postBattleHullState > 0;
	}
	
	public boolean isDestroyed() {
		return postBattleHullState == 0;
	}

}

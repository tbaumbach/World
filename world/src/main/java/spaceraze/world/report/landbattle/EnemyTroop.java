package spaceraze.world.report.landbattle;

import java.io.Serializable;

import spaceraze.world.enums.BattleGroupPosition;

public class EnemyTroop extends TroopState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String type;
	
	public EnemyTroop(String type, int hitpoints, BattleGroupPosition position) {
		super(hitpoints, position);
		this.type = type;
	}
	
	public EnemyTroop(String type, BattleGroupPosition position) {
		this(type, 100, position);
	}

	public String getType() {
		return type;
	}

}

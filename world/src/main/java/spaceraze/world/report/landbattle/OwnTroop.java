package spaceraze.world.report.landbattle;

import java.io.Serializable;

import spaceraze.world.enums.BattleGroupPosition;

public class OwnTroop extends TroopState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final String type;
	
	public OwnTroop(String name, String type, int hitpoints, BattleGroupPosition position) {
		super(hitpoints, position);
		this.name = name;
		this.type = type;
	}
	
	public OwnTroop(String name, String type, BattleGroupPosition position) {
		this(name, type, 100, position);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}

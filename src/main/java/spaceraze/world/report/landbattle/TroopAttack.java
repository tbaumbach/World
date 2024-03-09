package spaceraze.world.report.landbattle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "TROOP_ATTACK")
public class TroopAttack implements Serializable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private boolean own;
	private String typeName;
	private int currentDamageCapacity;
	private int damageCapacity;
	private boolean artillery; //(aPosition == BattleGroupPosition.SUPPORT) & (aTroop.getTroop().getAttackArtillery() > 0)
	
	public TroopAttack(String typeName, int damageCapacity, int currentDamageCapacity, boolean isArtillery) {
		this.typeName = typeName;
		this.damageCapacity = damageCapacity;
		this.currentDamageCapacity = currentDamageCapacity;
		this.artillery = isArtillery;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public int getCurrentDamageCapacity() {
		return currentDamageCapacity;
	}

	public int getDamageCapacity() {
		return damageCapacity;
	}
	
	public boolean isArtillery() {
		return artillery;
	}


	public String getTroopName(){
		if(name != null){
			return name;
		}
		return typeName;
	}

}

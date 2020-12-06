package spaceraze.world.report.landbattle;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.world.Troop;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "TROOP_TARGET")
public class TroopTarget implements Serializable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private boolean own;
	private String typeName;
	private int currentDamageCapacity;
	private int damageCapacity;
	
	public TroopTarget(String typeName, int damageCapacity, int currentDamageCapacity) {
		this.typeName = typeName;
		this.damageCapacity = damageCapacity;
		this.currentDamageCapacity = currentDamageCapacity;
	}
	
	public TroopTarget(Troop troop) {
		this(troop.getTroopType().getUniqueName(), troop.getMaxDC(), troop.getCurrentDC());
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
	
	public String getTroopName(){
		if(name != null){
			return name;
		}
		return typeName;

	}

}

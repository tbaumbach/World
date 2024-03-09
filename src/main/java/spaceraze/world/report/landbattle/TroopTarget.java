package spaceraze.world.report.landbattle;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

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
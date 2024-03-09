package spaceraze.world.report.spacebattle;

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
@Table(name = "SPACE_SHIP_TARGET")
public class SpaceshipTarget implements Serializable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String typeName;
	private int currentShield;
	private int shield;
	private int currentDamageCapacity;
	private int damageCapacity;
	private boolean own;

	public String getSpaceshipName(){
		if(name != null){
			return name;
		}
		return typeName;
	}

}

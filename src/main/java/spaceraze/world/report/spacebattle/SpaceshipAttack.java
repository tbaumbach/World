package spaceraze.world.report.spacebattle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "SPACE_SHIP_ATTACK")
public class SpaceshipAttack implements Serializable {
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String typeName;
	private boolean scuttled;
	private String retreatsToPlanetName;
	private String name;
	private boolean own;

	public SpaceshipAttack(String typeName, boolean scuttled, String retreatsToPlanetName, boolean isOwn) {
		this.typeName = typeName;
		this.scuttled = scuttled;
		this.retreatsToPlanetName = retreatsToPlanetName;
		this.own = isOwn;
	}

	public SpaceshipAttack(String name, boolean scuttled, boolean isOwnAttack) {
		this(null, scuttled, null, isOwnAttack);
		this.name = name;
	}

	public SpaceshipAttack(String name, String typeName, boolean scuttled, boolean isOwnAttack , String retreatsToPlanetName) {
		this(typeName, scuttled, retreatsToPlanetName, isOwnAttack);
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getSpaceshipName(){
		if(name != null){
			return name;
		}
		return typeName;
	}

	public boolean isScuttled() {
		return scuttled;
	}

	public String getRetreatsToPlanetName() {
		return retreatsToPlanetName;
	}

}

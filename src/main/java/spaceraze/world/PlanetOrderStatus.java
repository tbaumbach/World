package spaceraze.world;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Denna klass hanterar orderstatus för en enskild planet, dvs hur denna planet skall hanteras.
 * Dessa statusar är bara krångligt att hantera som vanliga ordrar då de har en långvarig effekt,
 * till skillnad från vanliga ordrar som utförs, har sin effekt, och sedan är klara.
 * 
 * Denna klass skall alltid returneras när ett drag sparas.
 * 
 * Notes är inte med i denna klass då den hanteras som en order (av god anledning). 
 * 
 * @author Paul Bodin
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "PLANET_ORDER_STATUS")
public class PlanetOrderStatus implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_PLAYER")
	private Player player;

	private String planetName;
	private boolean attackIfNeutral;
	private boolean destroyOrbitalBuildings;
	private boolean doNotBesiege;
	private int maxBombardment;

    public boolean isAttackIfNeutral() {
		return attackIfNeutral;
	}

    public void setAttackIfNeutral(boolean attackIfNeutral) {
		this.attackIfNeutral = attackIfNeutral;
	}
	
    public boolean isDestroyOrbitalBuildings() {
		return destroyOrbitalBuildings;
	}
	
    public void setDestroyOrbitalBuildings(boolean destroyOrbitalBuildings) {
		this.destroyOrbitalBuildings = destroyOrbitalBuildings;
	}
	
    public boolean isDoNotBesiege() {
		return doNotBesiege;
	}
	
    public void setDoNotBesiege(boolean doNotBesiege) {
		this.doNotBesiege = doNotBesiege;
	}
	
    public int getMaxBombardment() {
		return maxBombardment;
	}
	
    public void setMaxBombardment(int maxBombardment) {
		this.maxBombardment = maxBombardment;
	}
}

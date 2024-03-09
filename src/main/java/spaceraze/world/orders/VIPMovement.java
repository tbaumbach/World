package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Planet;
import spaceraze.world.Spaceship;
import spaceraze.world.Troop;
import spaceraze.world.VIP;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "VIP_MOVMENT")
public class VIPMovement implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

    private String vipKey;
    private String planetDestination = null;
    private String shipDestination = null;
    private String troopDestination;

    public VIPMovement(VIP aVIP, Planet planetDestination) {
        this.vipKey = aVIP.getUuid();
        this.planetDestination = planetDestination.getName();
    }

    public VIPMovement(VIP aVIP, Spaceship shipDestination) {
        this.vipKey = aVIP.getUuid();
        this.shipDestination = shipDestination.getUuid();
    }

    public VIPMovement(VIP aVIP, Troop troopDestination) {
        this.vipKey = aVIP.getUuid();
        this.troopDestination = troopDestination.getUuid();
    }

}

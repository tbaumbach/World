package spaceraze.world.orders;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Spaceship;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "SHIP_CARRIER_MOVE")
public class ShipToCarrierMovement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_ORDERS")
    private Orders orders;

    private String spaceShipKey;
    private String destinationCarrierKey;

    public ShipToCarrierMovement(Spaceship ss, Spaceship destinationCarrier) {
        this.spaceShipKey = ss.getKey();
        this.destinationCarrierKey = destinationCarrier.getKey();
    }

}
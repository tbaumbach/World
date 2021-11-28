package spaceraze.world;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Builder
@Table(name = "CAN_BE_LOST_IN_SPACE")
public class CanBeLostInSpace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_REPORT_SHIP")
	private Report reportLostShips;

	@ManyToOne
	@JoinColumn(name = "FK_REPORT_TROOP")
	private Report reportLostTroops;

	private String lostInSpaceString;
	private String owner;

}

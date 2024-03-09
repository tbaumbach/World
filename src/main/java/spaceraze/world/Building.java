package spaceraze.world;

import java.io.Serializable;
import java.util.UUID;

import lombok.*;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "BUILDNING")
public class Building implements Serializable, Cloneable{
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_PLANET")
	private Planet location;

	private String uuid;
	private String typeUuid;
	private int productionNumber;
	private boolean visibleOnMap;

	@Builder.Default
	private int openPlanetBonus = 0;
	@Builder.Default
	private int closedPlanetBonus = 0;
	@Builder.Default
	private int techBonus = 0;
	@Builder.Default
	private int wharfSize = 0; // if = cannot build ships
	@Builder.Default
	private int troopSize = 0; // if = cannot build troops
	private boolean spaceport;

	@Builder.Default
	private int resistanceBonus = 0;
	@Builder.Default
	private int shieldCapacity = 0;
	private int cannonDamage;
	private int cannonRateOfFire;

	private boolean alienKiller;
	@Builder.Default
	private int counterEspionage = 0;
	@Builder.Default
	private int exterminator = 0;

    public Building(BuildingType buildingType, int productionNumber, Planet location){
    	this.uuid = UUID.randomUUID().toString();
		this.typeUuid = buildingType.getUuid();
		this.productionNumber = productionNumber;
		this.setLocation(location);

		this.openPlanetBonus = buildingType.getOpenPlanetBonus();
		this.closedPlanetBonus = buildingType.getClosedPlanetBonus();
		this.techBonus = buildingType.getTechBonus();
		this.wharfSize = buildingType.getWharfSize();
		this.troopSize = buildingType.getTroopSize();
		this.spaceport = buildingType.isSpaceport();
		this.visibleOnMap = buildingType.isVisibleOnMap();
		this.resistanceBonus = buildingType.getResistanceBonus();
		this.shieldCapacity = buildingType.getShieldCapacity();
		this.cannonDamage = buildingType.getCannonDamage();
		this.cannonRateOfFire = buildingType.getCannonRateOfFire();
		this.alienKiller = buildingType.isAlienKiller();
		this.counterEspionage = buildingType.getCounterEspionage();
		this.exterminator = buildingType.getExterminator();


    }

}

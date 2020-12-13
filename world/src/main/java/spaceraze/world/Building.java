package spaceraze.world;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "BUILDNING")
public class Building implements Serializable, Cloneable{
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;



	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_BUILDNING_TYPE", insertable = false, updatable = false)
	private BuildingType buildingType;

	@ManyToOne
	@JoinColumn(name = "FK_GALAXY")
	private Planet location;
	private int uniqueId; 
	@SuppressWarnings("unused")
	private int kills; // not used (yet)
    
    //  construktorn skall ej anropas direkt, utan BuildingType.getBuilding skall
	// användas istället    
    public Building(BuildingType buildingType, int nrProduced, Galaxy galaxy, Planet location){
    	this.buildingType = buildingType;
    	this.uniqueId = galaxy.getUniqueIdCounter(CounterType.BUILDING).getUniqueId();
		this.setLocation(location);
//		if (name != null) {
//			this.name = name;
//		} else {
//			this.name = uniqueName;
//		}
    	
    }
    
    @Override
	public Building clone(){
		Building clonedBuilding = null;
		try {
			clonedBuilding = (Building)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clonedBuilding;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public String getName() {
		return buildingType.getName();
	}

/*	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}*/
}

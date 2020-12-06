package spaceraze.world.mapinfo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Data om skepp fr�n en annan spelare/neutral
 * 
 * @author Paul Bodin
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "FLEET_DATA")
public class FleetData implements Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_MAP_PLANET_INFO")
	private MapPlanetInfo mapPlanetInfo;

	private String ownerGovName; // om owner == null, så är flottan neutral
	private int maxSize;
	private boolean civ;

	public FleetData(String ownerGovName, int maxSize, boolean civ){
		this.ownerGovName = ownerGovName;
		this.maxSize = maxSize;
		this.civ = civ;
	}

	public String getOwnerGovName() {
		return ownerGovName;
	}

	public int getMaxSize() {
		return maxSize;
	}
	
	public boolean isCiv() {
		return civ;
	}
	
	public String getMaxSizeString(){
		String sizeString = "small";
	    if (maxSize == 2){
	        sizeString = "medium";
	    }else
	    if (maxSize == 3){
	        sizeString = "large";
	    }else
	    if (maxSize == 5){
	        sizeString = "huge";
	    }
	    return sizeString;
	}
	
	public String getMapText(){
		StringBuffer sb = new StringBuffer(); 
		if (maxSize > 0){
			sb.append(getMaxSizeString());
		}
		if (isCiv()){
			if (maxSize > 0){
				sb.append("+");
			}
			sb.append("civ");
		}
		if (sb.length() > 0){ // n�got ska visas
			if (ownerGovName != null){ // �r ej neutral
				sb.append(" (");
				sb.append(ownerGovName);
				sb.append(")");
			}
		}
		return sb.toString();
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("fleetData, owner: " + ownerGovName);
		sb.append("\n");
		sb.append("\tmaxSize: " + getMaxSizeString());
		sb.append("\n");
		sb.append("\tciv: " + civ);
		sb.append("\n");
		return sb.toString();
	}
	
}

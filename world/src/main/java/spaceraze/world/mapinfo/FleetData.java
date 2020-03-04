package spaceraze.world.mapinfo;

import java.io.Serializable;

/**
 * Data om skepp fr�n en annan spelare/neutral
 * 
 * @author Paul Bodin
 */
public class FleetData implements Serializable  {
	private static final long serialVersionUID = 1L;
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

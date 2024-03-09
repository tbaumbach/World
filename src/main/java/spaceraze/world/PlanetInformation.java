package spaceraze.world;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.util.general.Logger;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "PLANET_INFORMATION")
public class PlanetInformation implements Serializable{
    static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_PLAYER")
	private Player player;

    private String name;
	private String  lastKnownOwner;
	private String lastKnownMaxShipSize;
	private String lastKnownBuildingsInOrbit;
	private String lastKnownBuildingsOnSurface;
    private boolean razed;
    private int lastInfoTurn = 0;
	private int res = -1;
	private int prod = -1;
	private int lastKnownTroopsNr;
    private String notes; // sätts via order men lagras här
    
    public PlanetInformation(Planet aPlanet, Player player){
      this.name = aPlanet.getName();
      Player tempp = aPlanet.getPlayerInControl();
      if (tempp != null){
        if (tempp == player){
          this.lastKnownOwner = tempp.getName();
        }else{
          this.lastKnownOwner = "Neutral";
        }
      }else{
        this.lastKnownOwner = "Neutral";
      }
      this.lastInfoTurn = 0;
    }

    public int getLastInfoTurn(){
      return lastInfoTurn;
    }

    public void setLastInfoTurn(int newvalue){
      lastInfoTurn = newvalue;
    }

    public String getLastKnownOwner(){
      return lastKnownOwner;
    }

    public void setLastKnownOwner(String newvalue){
      lastKnownOwner = newvalue;
    }

    public String getName(){
      return name;
    }

	public int getProd() {
		return prod;
	}
	
	public void setProd(int prod) {
		this.prod = prod;
	}
	
	public int getRes() {
		return res;
	}
	
	public void setRes(int res) {
		this.res = res;
	}

	public boolean isRazed() {
		return razed;
	}

	public void setRazed(boolean razed) {
		this.razed = razed;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String aNotes) {
		Logger.finest("PlanetInfo.setNotes: " + aNotes);
		if ((aNotes != null) && (aNotes.trim().equals(""))){
			this.notes = null;
		}else{
			this.notes = aNotes;
		}
		Logger.finest("PlanetInfo.setNotes2: " + this.notes);
	}

	public String getLastKnownMaxShipSize() {
		return lastKnownMaxShipSize;
	}

	public void setLastKnownMaxShipSize(String lastKnownMaxShipSize) {
		this.lastKnownMaxShipSize = lastKnownMaxShipSize;
	}

	public int getLastKnownTroopsNr() {
		return lastKnownTroopsNr;
	}

	public void setLastKnownTroopsNr(int lastKnownTroopsNr) {
		this.lastKnownTroopsNr = lastKnownTroopsNr;
	}

	public String getLastKnownBuildingsInOrbit() {
		return lastKnownBuildingsInOrbit;
	}

	public void setLastKnownBuildingsInOrbit(String lastKnownBuildingsInOrbit) {
		this.lastKnownBuildingsInOrbit = lastKnownBuildingsInOrbit;
	}

	public String getLastKnownBuildingsOnSurface() {
		return lastKnownBuildingsOnSurface;
	}

	public void setLastKnownBuildingsOnSurface(String lastKnownBuildingsOnSurface) {
		this.lastKnownBuildingsOnSurface = lastKnownBuildingsOnSurface;
	}
    
    public void pruneDroid(){
    	lastKnownOwner = null;
    	lastKnownMaxShipSize = null;
    	lastKnownBuildingsInOrbit = null;
    	lastKnownBuildingsOnSurface = null;
    }

}
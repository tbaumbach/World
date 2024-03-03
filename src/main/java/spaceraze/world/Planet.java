package spaceraze.world;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "PLANET")
public class Planet extends BasePlanet{
    static final long serialVersionUID = 1L;

	private int population = 0;
	private int resistance = 0;
	private int basePopulation = 0;
	private boolean startPlanet = false;
    @Column(name = "isOpen")
	private boolean open = false;
	private boolean besieged = false;


    @ManyToOne
    @JoinColumn(name = "FK_GALAXY")
    private Galaxy galaxy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLAYER_IN_CONTROL")
    private Player playerInControl;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_LAST_KNOWN_PLAYER_IN_CONTROL")
    private Player lastKnownPlayerInControl;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "REACH_FROM", insertable = false, updatable = false)
    private Planet reachFrom;
	private boolean hasNeverSurrendered = true;
    private int rangeToClosestFriendly; // used in Galaxy when computing startplanet location

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    @Builder.Default
    private List<Building> buildings = new ArrayList<>();

    public Planet(MapPlanet planet){
        super(planet);
		this.buildings = new ArrayList<>();
        population = 0;
        basePopulation = 0;
        resistance = 0;
        startPlanet = false;
        open = false;
    }
    
    public Planet(double x, double y, double z, String name, int population, int resistance, boolean startPlanet, boolean isPossibleStartPlanet){
        super(x, y, z, name, isPossibleStartPlanet);
		this.buildings = new ArrayList<>();
        this.population = population;
        basePopulation = population;
        this.resistance = resistance;
        this.startPlanet = startPlanet;
        open = true;
        if (startPlanet){ // A players start planet is always closed in the start of the game.
          open = false;
        }
    }

    public void setProd(int newProd){
        population = newProd;
    }

    public void setResistance(int newRes){
      resistance = newRes;
    }

    public Player getPlayerInControl(){
      return playerInControl;
    }

    public void setPlayerInControl(Player p){
      playerInControl = p;
    }

    public boolean isStartPlanet(){
      return startPlanet;
    }

    public int getPopulation(){
        return population;
    }

    public void setBesieged(boolean value){
    	besieged = value;
    }

    public boolean isBesieged(){
        return besieged;
    }

    public int getResistance(){
      return resistance;
    }

    public Player getLastKnownPlayerInControl(){
      return lastKnownPlayerInControl;
    }

    public boolean isOpen(){
      return open;
    }

    public void setReachFrom(Planet newReachFrom){
      reachFrom = newReachFrom;
    }

    public Planet getReachFrom(){
      return reachFrom;
    }

	public int getBasePopulation(){
		return basePopulation;
	}
	
	public String toString(){
		return "Planet " + getName() + " (" + population + "/" + resistance + ")";
	}

	public int getRangeToClosestFriendly() {
		return rangeToClosestFriendly;
	}

	public void setRangeToClosestFriendly(int rangeToClosestFriendly) {
		this.rangeToClosestFriendly = rangeToClosestFriendly;
	}

	public boolean isHasNeverSurrendered(){
		return hasNeverSurrendered;
	}
	
	public void setHasNeverSurrendered(boolean newHasNeverSurrendered){
		hasNeverSurrendered = newHasNeverSurrendered;
	}

	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

}
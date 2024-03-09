package spaceraze.world;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "MAP_PLANET_CONNECTION")
public class MapPlanetConnection implements Serializable {
    static final long serialVersionUID = 1L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_MAP")
    private Map map;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLANET_ONE", insertable = false, updatable = false)
    private MapPlanet planetOne;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLANET_TWO", insertable = false, updatable = false)
    private MapPlanet planetTwo;

    @Column(name = "LONG_RANGE")
    private boolean longRange;

    public MapPlanetConnection(String dataString, List<MapPlanet> allPlanets) {
        StringTokenizer st = new StringTokenizer(dataString, "\t");
        String planetName1 = st.nextToken();
        String planetName2 = st.nextToken();
        longRange = st.nextToken().equalsIgnoreCase("true");
        planetOne = findPlanet(planetName1, allPlanets);
        planetTwo = findPlanet(planetName2, allPlanets);
    }

    private MapPlanet findPlanet(String aPlanetName, List<MapPlanet> allPlanets) {
        MapPlanet found = null;
        int index = 0;
        while ((found == null) && (index < allPlanets.size())) {
            MapPlanet tempPlanet = allPlanets.get(index);
            if (tempPlanet.getName().equals(aPlanetName)) {
                found = tempPlanet;
            } else {
                index++;
            }
        }
        return found;
    }

    public MapPlanetConnection(MapPlanet planetOne, MapPlanet planetTwo, boolean longRange) {
        this.planetOne = planetOne;
        this.planetTwo = planetTwo;
        this.longRange = longRange;
    }


    @JsonIgnore
    public MapPlanet getOtherEnd(MapPlanet aPlanet, boolean isLongRange) {
        MapPlanet returnPlanet = null;
        if ((longRange == false) | (isLongRange == longRange)) {
            if (aPlanet == planetOne) {
                returnPlanet = planetTwo;
            } else if (aPlanet == planetTwo) {
                returnPlanet = planetOne;
            }
        }
        return returnPlanet;
    }

    @JsonIgnore
    public String getSaveString(int index) {
        String retStr = "connection" + index + " = ";
        retStr = retStr + planetOne.getName();
        retStr = retStr + "\t" + planetTwo.getName();
        retStr = retStr + "\t" + longRange;
        return retStr;
    }

    @JsonIgnore
    public boolean isConnection(MapPlanet aPlanet1, MapPlanet aPlanet2) {
        boolean found = false;
        if ((planetOne == aPlanet1) & (planetTwo == aPlanet2)) {
            found = true;
        } else if ((planetTwo == aPlanet1) & (planetOne == aPlanet2)) {
            found = true;
        }
        return found;
    }

    @JsonIgnore
    public String toString() {
        return planetOne + " <--> " + planetTwo;
    }

}
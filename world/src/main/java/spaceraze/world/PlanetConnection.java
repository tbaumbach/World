package spaceraze.world;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "PLANET_CONNECTION")
public class PlanetConnection implements Serializable {
    static final long serialVersionUID = 1L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO should galaxy have the map instead?
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_GALAXY")
    private Galaxy galaxy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_MAP")
    private Map map;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLANET_ONE", insertable = false, updatable = false)
    private BasePlanet planetOne;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLANET_TWO", insertable = false, updatable = false)
    private BasePlanet planetTwo;

    @Column(name = "LONG_RANGE")
    private boolean longRange;

    public PlanetConnection(String dataString, List<BasePlanet> allPlanets) {
        StringTokenizer st = new StringTokenizer(dataString, "\t");
        String planetName1 = st.nextToken();
        String planetName2 = st.nextToken();
        longRange = st.nextToken().equalsIgnoreCase("true");
        planetOne = findPlanet(planetName1, allPlanets);
        planetTwo = findPlanet(planetName2, allPlanets);
    }

    private BasePlanet findPlanet(String aPlanetName, List<BasePlanet> allPlanets) {
        BasePlanet found = null;
        int index = 0;
        while ((found == null) && (index < allPlanets.size())) {
            BasePlanet tempPlanet = allPlanets.get(index);
            if (tempPlanet.getName().equals(aPlanetName)) {
                found = tempPlanet;
            } else {
                index++;
            }
        }
        return found;
    }

    public PlanetConnection(BasePlanet planetOne, BasePlanet planetTwo, boolean longRange) {
        this.planetOne = planetOne;
        this.planetTwo = planetTwo;
        this.longRange = longRange;
    }


    @JsonIgnore
    public BasePlanet getOtherEnd(BasePlanet aPlanet, boolean isLongRange) {
        BasePlanet returnPlanet = null;
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
    public boolean isConnection(BasePlanet aPlanet1, BasePlanet aPlanet2) {
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

    public String getPlanetOneName(){
        return planetOne.getName();
    }

    public String getPlanetOneTwo(){
        return planetTwo.getName();
    }

}
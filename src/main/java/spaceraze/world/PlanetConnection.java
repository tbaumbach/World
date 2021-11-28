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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLANET_ONE", insertable = false, updatable = false)
    private Planet planetOne;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLANET_TWO", insertable = false, updatable = false)
    private Planet planetTwo;

    @Column(name = "LONG_RANGE")
    private boolean longRange;

    public PlanetConnection(Planet planetOne, Planet planetTwo, boolean longRange) {
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

}
package spaceraze.world;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "MAP_PLANET")
public class MapPlanet extends BasePlanet{

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_MAP")
    private Map map;


    /**
     * Used when reading map data from file
     * @param dataString containing a tab separated list of data: name\tx\ty\tz
     */
    public MapPlanet(String dataString){
        super(dataString);
    }

    public MapPlanet(double x, double y, double z, String name, boolean possibleStartPlanet){
        super(x, y, z, name, possibleStartPlanet);
    }
}

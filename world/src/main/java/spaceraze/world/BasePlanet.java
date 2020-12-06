package spaceraze.world;

import java.io.Serializable;
import java.util.StringTokenizer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "BASE_PLANET")
public class BasePlanet implements Serializable{
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_MAP")
    private Map map;

    private double x;
    private double y;
    private double z;
    private String name;
    private boolean possibleStartPlanet = true;

    /**
     * Used when reading map data from file
     * @param dataString containing a tab separated list of data: name\tx\ty\tz
     */
    public BasePlanet(String dataString){
        StringTokenizer st = new StringTokenizer(dataString,"\t");
        name = st.nextToken();
        x = Double.parseDouble(st.nextToken());
        y = Double.parseDouble(st.nextToken());
        z = Double.parseDouble(st.nextToken());
        if(st.hasMoreTokens()){
            possibleStartPlanet = Boolean.valueOf(st.nextToken());
        }
    }

    public BasePlanet(BasePlanet basePlanet){
        this(basePlanet.getX(), basePlanet.getY(), basePlanet.getZ(), basePlanet.getName(), basePlanet.isPossibleStartPlanet());
    }

    public BasePlanet(double x, double y, double z, String name, boolean possibleStartPlanet){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.possibleStartPlanet = possibleStartPlanet;
    }

    public String getSaveString(int index){
        String retStr = "planet" + index + " = ";
        retStr = retStr + getName();
        retStr = retStr + "\t" + getX();
        retStr = retStr + "\t" + getY();
        retStr = retStr + "\t" + getZ();
        retStr = retStr + "\t" + isPossibleStartPlanet();
        return retStr;
    }

    public BasePlanet clonePlanet(){
        return new BasePlanet(x, y, z, name, possibleStartPlanet);
    }

    /**
     * Transform x & y coors for use on map web pages
     */
    public void changeScale(double scaleMod){
        x = (int)Math.round(x * scaleMod);
        y = (int)Math.round(y * scaleMod);
    }
}

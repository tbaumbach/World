package spaceraze.world;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "VIP")
public class VIP implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_GALAXY")
    private Galaxy galaxy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLANET", insertable = false, updatable = false)
    private Planet planetLocation = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_SPACESHIP", insertable = false, updatable = false)
    private Spaceship shipLocation = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_TROOP", insertable = false, updatable = false)
    private Troop troopLocation = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLAYER", insertable = false, updatable = false)
    private Player boss = null;

    private String key;
    private String typeKey;
    private int govCounter = -1; // used by diplomats and infestators
    private int govLastTurn = -1;
    private int kills = 0;
    private boolean hasKilled = false;
    private int buildCost = 0;
    private int upkeep = 0;

    public VIP(VIPType aVIPType, boolean isFanatic) {
        this.key = UUID.randomUUID().toString();
        this.typeKey = aVIPType.getKey();
        if (!isFanatic) {
            buildCost = aVIPType.getBuildCost();
            upkeep = aVIPType.getUpkeep();
        }
    }

    public void setBoss(Player newBoss) {
        boss = newBoss;
    }

    public Player getBoss() {
        return boss;
    }

    public Planet getPlanetLocation() {
        return planetLocation;
    }

    public Spaceship getShipLocation() {
        return shipLocation;
    }

    public Troop getTroopLocation() {
        return troopLocation;
    }

    public int getGovCounter() {
        return govCounter;
    }

    public int getGovLastTurn() {
        return govLastTurn;
    }

    public void setLastTurn(int i) {
        govLastTurn = i;
    }

    public int getKills() {
        return kills;
    }

    public boolean getHasKilled() {
        return hasKilled;
    }

    public int getBuildCost() {
        return buildCost;
    }

    public int getUpkeep() {
        return upkeep;
    }

}

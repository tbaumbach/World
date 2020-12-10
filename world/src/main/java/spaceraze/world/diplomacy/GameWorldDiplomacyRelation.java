package spaceraze.world.diplomacy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.Faction;
import spaceraze.world.GameWorld;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "GAME_WORLD_DIPLOMACY_RELATION")
public class GameWorldDiplomacyRelation extends DiplomacyRelation  {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FK_GAME_WORLD")
    private GameWorld gameWorld;

    public GameWorldDiplomacyRelation(Faction aFaction1, Faction aFaction2){
        super(aFaction1, aFaction2);
    }


}

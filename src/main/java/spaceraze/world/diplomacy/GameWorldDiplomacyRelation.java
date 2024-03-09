package spaceraze.world.diplomacy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spaceraze.world.GameWorld;

import jakarta.persistence.*;

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

    public GameWorldDiplomacyRelation(String factionOne, String factionTwo){
        super(factionOne, factionTwo);
    }


}

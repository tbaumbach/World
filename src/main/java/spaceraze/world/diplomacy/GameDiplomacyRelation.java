package spaceraze.world.diplomacy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "GAME_DIPLOMACY_RELATION")
public class GameDiplomacyRelation extends DiplomacyRelation {

}

package spaceraze.world;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "RESEARCH_PROGRESS")
public class ResearchProgress implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_PLAYER")
    private Player player;

    private boolean developed;
    private int researchedTurns = 0;
    private String ResearchAdvantageName;
}

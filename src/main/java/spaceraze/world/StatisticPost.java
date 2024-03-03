package spaceraze.world;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "STATISTIC")
public class StatisticPost implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_GALAXY")
    private Statistics statistics;

    private String uuid;//TODO, change to save uuid from the user instead of name

    @ElementCollection
    @CollectionTable(name = "STATISTIC_POST_VALUES")
    @Column(name = "VALUE")
    @Builder.Default
    private List<Integer> values = new ArrayList<>();

}

package spaceraze.world;

import lombok.*;

import jakarta.persistence.*;
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
    @JoinColumn(name = "FK_STATISTICS_GALAXY")
    private Statistics statistics;

    private String uuid;//TODO, change to save uuid from the user instead of name

    @ElementCollection
    @CollectionTable(name = "STATISTIC_POST_VALUES", joinColumns = @JoinColumn(name = "statistic_post_id", referencedColumnName = "id"))
    @Column(name = "value")
    @Builder.Default
    private List<Integer> values = new ArrayList<>();

}

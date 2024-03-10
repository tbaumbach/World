package spaceraze.world;

import lombok.*;

import java.io.Serializable;
import java.util.*;


import jakarta.persistence.*;

/**
 * Handles one type of statistics in a game
 * Player name "Neutral" is used for statistics for neutral planets
 *
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "STATISTICS_GALAXY")
public class Statistics implements Serializable{
    static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_GALAXY")
	private Galaxy galaxy;

	@Enumerated(EnumType.STRING)
	private StatisticType statisticType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "statistics")
	private List<StatisticPost> statisticPosts = new ArrayList<>();

	public Statistics(StatisticType statisticType){
		this.statisticPosts = new ArrayList<>();
		this.statisticType = statisticType;
	}
	
}

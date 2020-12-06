package spaceraze.world.report;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.world.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "PLAYER_REPORT")
public class PlayerReport extends EventReport {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "FK_PLAYER")
	private Player player;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "playerReport")
	@Builder.Default
	private List<PlanetReport> planetReports = new ArrayList<>();

	@Override
	public String getReport() {
		return "";
	}

	@Override
	public EventReport getParent() {
		return null;
	}

	public String getFullReport() {
		StringBuilder stringBuilderReport = new StringBuilder(getReport());
		planetReports.stream().forEach(report -> stringBuilderReport.append("\n").append(report.getFullReport()));
		return stringBuilderReport.toString();
	}

	public List<EventReport> getChildReports(){
		return Collections.unmodifiableList(planetReports);
	}

}

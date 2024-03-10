package spaceraze.world.report;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spaceraze.world.report.landbattle.LandBattleReport;
import spaceraze.world.report.spacebattle.SpaceBattleReport;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity()
@Table(name = "PLANET_REPORT")
public class PlanetReport extends EventReport{

	@ManyToOne
	@JoinColumn(name = "FK_PLAYER_REPORT")
	private PlayerReport playerReport;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "planetReport")
	private List<SpaceBattleReport> spaceBattleReports = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "planetReport")
	private List<LandBattleReport> landBattleReports = new ArrayList<>();

	private String planetName;

	public PlanetReport(String planetName) {
		super();
		this.planetName = planetName;
		this.spaceBattleReports= new ArrayList<>();
		this.landBattleReports= new ArrayList<>();
	}
	
	@Override
	public String getReport() {
		return "Report from " + planetName + "\n";
	}

	@Override
	public EventReport getParent() {
		return playerReport;
	}

	public String getBattleReports() {
		StringBuilder report = new StringBuilder();
		spaceBattleReports.forEach(battleReport -> report.append(battleReport.getFullReport()));
		landBattleReports.forEach(battleReport -> report.append(battleReport.getFullReport()));
		return report.toString();
	}

	public String getFullReport() {
		StringBuilder stringBuilderReport = new StringBuilder(getReport());
		stringBuilderReport.append(getBattleReports());
		return stringBuilderReport.toString();
	}

	public List<EventReport> getChildReports(){
		List<EventReport> eventReports = new ArrayList<>(spaceBattleReports);
		eventReports.addAll(landBattleReports);

		return eventReports;
	}

}

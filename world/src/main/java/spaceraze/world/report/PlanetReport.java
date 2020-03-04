package spaceraze.world.report;

import spaceraze.world.report.landbattle.LandBattleReport;
import spaceraze.world.report.spacebattle.SpaceBattleReport;

public class PlanetReport extends EventReport{

	private static final long serialVersionUID = 1L;
	private final String planetName;

	public PlanetReport(String planetname) {
		super();
		this.planetName = planetname;
	}
	
	@SuppressWarnings("unused")
	private PlanetReport() {
		this(null);
	};
	
	@Override
	public String getReport() {
		return "Report from " + planetName + "\n";
	}
	
	public String getSpaceBattleReports() {
		StringBuilder report = new StringBuilder(getReport());
		getChildeReportsOfType(SpaceBattleReport.class).forEach(battleReport -> report.append(battleReport.getReports()));
		getChildeReportsOfType(LandBattleReport.class).forEach(battleReport -> report.append(battleReport.getReports()));
		return report.toString();
	}

	public String getPlanetName() {
		return planetName;
	}

}

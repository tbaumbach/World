package spaceraze.world.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EventReport implements Serializable{
	static final long serialVersionUID = 1L;
	
	private List<EventReport> reports = new ArrayList<>();
	private final EventReport parent;
	private int index;
	
	public EventReport(EventReport parent) {
		this.parent = parent;
	}
	
	public EventReport() {
		this(null);
	}
	
	public EventReport getParent() {
		return parent;
	}
	
	public abstract String getReport();
	
	public String getReports() {
		StringBuilder stringBuilderReport = new StringBuilder(getReport());
		stringBuilderReport.append(getChildereports());
		return stringBuilderReport.toString();
	}
	
	public List<EventReport> getChildeReports(){
		return Collections.unmodifiableList(reports);
	}
	
	public String getChildereports() {
		StringBuilder stringBuilderReport = new StringBuilder();
		getChildeReports().stream().map(EventReport::getReports).forEach(report -> stringBuilderReport.append("\n").append(report));
		return stringBuilderReport.toString();
	}
	
	/*TODO 2019-12-30 Testa om <T extends EventReport> Set<T> getChildeReportsOfType(Class<T> inClass) fungerar eller om den kan slänga fel objekt. Mycket möjligt att vi måste casta till rätt typ d.v.s retunera Set<EventReport>
	public Set<EventReport> getChildeReportsOfType(Class<?> inClass){
		return Collections.unmodifiableSet(reports.stream().filter(report -> inClass.isInstance(report)).collect(Collectors.toSet()));
	}*/
	/*
	public Set<? extends EventReport> getChildeReportsOfType(Class<?> inClass){
		return Collections.unmodifiableSet(reports.stream().filter(report -> inClass.isInstance(report)).collect(Collectors.toSet()));
	}
	*/
	@SuppressWarnings("unchecked")
	public <T extends EventReport> List<T> getChildeReportsOfType(Class<T> inClass){
		return (List<T>) Collections.unmodifiableList(reports.stream().filter(report -> inClass == report.getClass()).collect(Collectors.toList()));
	}
	
	public void addReport(EventReport eventReport) {
		reports.add(eventReport);
	}
	
	public void addReports(List<EventReport> eventReports) {
		reports.addAll(eventReports);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}

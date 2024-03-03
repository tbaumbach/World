package spaceraze.world.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class EventReport implements Serializable, ReportInterface{
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int sortOrder;

	public String getFullReport() {
		StringBuilder stringBuilderReport = new StringBuilder(getReport());
		getChildReports().stream().map(EventReport::getChildReports).forEach(report -> stringBuilderReport.append("\n").append(report));
		return stringBuilderReport.toString();
	}

	@SuppressWarnings("unchecked")
	public <T extends EventReport> List<T> getChildReportsOfType(Class<T> inClass){
		return (List<T>) Collections.unmodifiableList(getChildReports().stream().filter(report -> inClass == report.getClass()).collect(Collectors.toList()));
	}

}

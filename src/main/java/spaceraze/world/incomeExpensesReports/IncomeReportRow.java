package spaceraze.world.incomeExpensesReports;

import java.io.Serializable;

import lombok.*;
import spaceraze.util.general.Logger;

import jakarta.persistence.*;

/**
 * Contains information about one specific income
 *
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "INCOME_REPORT_ROW")
public class IncomeReportRow implements Serializable {
    static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_INCOME_REPORT")
	IncomeReport incomeReport;

	private IncomeType type;
	private String description;
	private String location;
	private int counter;
	private int value;
	
	public IncomeReportRow(IncomeType type, String description, String location, int value, int counter){
		this.type = type;
		this.description = description;
		this.location = location;
		this.value = value;
		this.counter = counter;
		Logger.finer("counter: " + counter);
	}

	public String getDescription() {
		return description;
	}

	public IncomeType getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public int getCounter() {
		return counter;
	}

	public String getLocation() {
		return location;
	}

}

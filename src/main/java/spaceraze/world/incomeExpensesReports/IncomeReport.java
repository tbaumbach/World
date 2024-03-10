package spaceraze.world.incomeExpensesReports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import spaceraze.world.TurnInfo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all information about a players incomes for a specific turn
 *
 */
@Setter
@Getter
@AllArgsConstructor
@Entity()
@Table(name = "INCOME_REPORT")
public class IncomeReport implements Serializable {
    static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "FK_TURN_INFO")
	TurnInfo turnInfo;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "incomeReport")
	private List<IncomeReportRow> incomeRows = new ArrayList<>();

	private int counter; 
	
	public IncomeReport (){
		this.incomeRows = new ArrayList<>();
		counter = 1;
	}
	
	public void addNewRow(IncomeType type, String desc, String location, int value){
		IncomeReportRow newRow = new IncomeReportRow(type, desc, location, value, counter);
		incomeRows.add(newRow);
		counter++;
	}
	
	public List<IncomeReportRow> getRows(){
		return incomeRows;
	}
	
}

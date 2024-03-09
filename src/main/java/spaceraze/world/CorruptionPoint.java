package spaceraze.world;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Handles one breakpoint for corruption.
 * Breakpoint are not cumulative
 * @author WMPABOD
 *
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "CORRUPTION_POINT")
public class CorruptionPoint implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false)
	private Long id;

	private int incomeLimit;
	private int corruptionPercentage;
	private double incomePercentage;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_CURRUPTION_PONT_NEXT_BREAKPOINT")
	private CorruptionPoint nextBreakpoint;

	public CorruptionPoint(int incomeLimit,int corruptionPercentage){
		this.incomeLimit = incomeLimit;
		this.corruptionPercentage = corruptionPercentage;
		this.incomePercentage = (100 - corruptionPercentage)/100.0;
	}
	
	public String getDescription(){
		StringBuffer sb = new StringBuffer();
		sb.append(incomeLimit);
		sb.append("=");
		sb.append(corruptionPercentage);
		sb.append("%");
		if (nextBreakpoint != null){
			sb.append(",");
			sb.append(nextBreakpoint.getDescription());
		}
		return sb.toString();
	}

	public int getCorruptionPercentage() {
		return corruptionPercentage;
	}

	public int getIncomeLimit() {
		return incomeLimit;
	}

	public void setNextBreakpoint(CorruptionPoint tmpNextBreakpoint){
		nextBreakpoint = tmpNextBreakpoint;
	}
	
	/**
	 * Compute income after corruption between this and the next breakpoint.
	 * If there is another breakpoint, call it and add it's income to the returned result.
	 * @param income
	 * @return
	 */
	public int getIncomeAfterCorruption(int income){
		int incomeAfterCorruption = 0;
		if (nextBreakpoint != null){
			int nextLimit = nextBreakpoint.getIncomeLimit();
			if (income < nextLimit){
				double tmpInc = (income - incomeLimit) * incomePercentage;
				incomeAfterCorruption = (int)Math.round(tmpInc);
			}else{
				double tmpInc = (nextLimit - incomeLimit) * incomePercentage;
				incomeAfterCorruption = (int)Math.round(tmpInc);
				incomeAfterCorruption += nextBreakpoint.getIncomeAfterCorruption(income);
			}
		}else{
			double tmpInc = (income - incomeLimit) * incomePercentage;
			incomeAfterCorruption = (int)Math.round(tmpInc);
		}
		return incomeAfterCorruption;
	}

	public CorruptionPoint getNextBreakpoint() {
		return nextBreakpoint;
	}
}

package spaceraze.world;

import java.io.Serializable;

/**
 * handles corruption levels for a faction
 * @author WMPABOD
 *
 */
public class Corruption implements Serializable{
	private static final long serialVersionUID = 1L;
	CorruptionPoint firstBreakpoint,lastBreakPoint;
	
	/**
	 * Deep clone of a Corruption instance
	 */
	@Override
	public Corruption clone(){
		Corruption deepClone = new Corruption();
		// clone any breakpoint in this Corruption instance
		CorruptionPoint tmpCp = firstBreakpoint;
		while (tmpCp != null){
			deepClone.addBreakpoint(tmpCp.getIncomeLimit(), tmpCp.getCorruptionPercentage());
			tmpCp = tmpCp.getNextBreakpoint();
		}
		return deepClone;
	}
	
	public void addBreakpoint(int incomeLimit, int corrutionPercentage){
		CorruptionPoint tmpCp = new CorruptionPoint(incomeLimit,corrutionPercentage);
		if (firstBreakpoint == null){
			firstBreakpoint = tmpCp;
			lastBreakPoint = tmpCp;
		}else{
			lastBreakPoint.setNextBreakpoint(tmpCp);
			lastBreakPoint = tmpCp;
		}
	}
	
	public int getIncomeAfterCorruption(int anIncome){
		int tmpIncomeAfterCorr = 0;
		if (firstBreakpoint == null){
			tmpIncomeAfterCorr = anIncome;
		}else{
			int firstLimit = firstBreakpoint.getIncomeLimit();
			if (anIncome > firstLimit){
				tmpIncomeAfterCorr = firstLimit;
				// call breakpoints
				tmpIncomeAfterCorr += firstBreakpoint.getIncomeAfterCorruption(anIncome);
			}else{
				tmpIncomeAfterCorr = anIncome;
			}
		}
		return tmpIncomeAfterCorr;
	}

	public String getCorruptionDescription(){
		String desc = "";
		if (firstBreakpoint != null){
			desc = firstBreakpoint.getDescription();
		}
		return desc;
	}

	public CorruptionPoint getCorruptionPoint(){
		return this.clone().firstBreakpoint;
	}
}

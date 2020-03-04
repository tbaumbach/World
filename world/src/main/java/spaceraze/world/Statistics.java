package spaceraze.world;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.StatisticType;

/**
 * Handles one type of statistics in a game
 * Player name "Neutral" is used for statistics for neutral planets
 * @author bodinp
 *
 */
public class Statistics implements Serializable{
    static final long serialVersionUID = 1L;
	private StatisticType statisticType;
	private Map<String,List<Integer>> playersStatistics; // String = player name, List<Integer> values, and index = turn -1
	private Galaxy g;
	private static final int WIN_LOSE_PROD_PERCENTAGE = 66;
	
	public Statistics(StatisticType statisticType, Galaxy g){
		this.statisticType = statisticType;
		this.g = g;
		playersStatistics = new HashMap<String,List<Integer>>();
	}
	
	public StatisticType getStatisticType(){
		return statisticType;
	}

	public void addStatistics(String aPlayerName, int value){
		addStatistics(aPlayerName, value, false);
	}
	
	public void addStatistics(String aPlayerName, int value, boolean cumulative){
		String playerName = aPlayerName;
		List<Integer> values = (List<Integer>)playersStatistics.get(playerName);
		if (values == null){ // assumes this is turn 1 and a new player should be added
			List<Integer> newList = new LinkedList<Integer>();
			newList.add(value);
			playersStatistics.put(playerName,newList);
		}else{
			if (cumulative){
				int lastValue = values.get(values.size()-1);
				values.add(lastValue + value);
			}else{
				values.add(value);
			}
		}
		Logger.finest(statisticType.toString() + " " + aPlayerName + " " + value);
	}
	
	public int getLastTurn(){
		Set<String> keys = playersStatistics.keySet();
		Object[] keysArray = keys.toArray();
		String playerName = (String)keysArray[0];
		List<Integer> list = playersStatistics.get(playerName);
		return list.size();
	}
	
	/**
	 * Does not count the "Neutral" player
	 * @return
	 */
	public int getMaxValue(){
		int maxValue = 0;
		Set<String> keys = playersStatistics.keySet();
		Object[] keysArray = keys.toArray();
		for (int i = 0; i < keysArray.length; i++) {
			String playerName = (String)keysArray[i];
			if (!playerName.equalsIgnoreCase("Neutral")){
				List<Integer> list = playersStatistics.get(playerName);
				for (Integer aValue : list) {
					if (aValue > maxValue){
						maxValue = aValue;
					}
				}
			}
		}
		return maxValue;
	}
	
	public List<Integer> getStatList(String aPlayerName){
		return playersStatistics.get(aPlayerName);
	}

	public List<Integer> getWinLimit(){
		List<Integer> winLimitList = new LinkedList<Integer>();
		for (int i = 0; i < getLastTurn(); i++){
			int sum = 0;
			Set<String> keys = playersStatistics.keySet();
			Object[] keysArray = keys.toArray();
			for (Object object : keysArray) {
				List<Integer> aList = playersStatistics.get(object);
				sum += aList.get(i);
			}
			winLimitList.add((int)Math.round(sum*WIN_LOSE_PROD_PERCENTAGE/100.0));
		}
		return winLimitList;
	}
	
}

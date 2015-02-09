 /*
  * 
  * 
  * @conraddepeuter
  */


import java.util.ArrayList;

public class Possession {

	public String offense;
	public String defense;
	public int startTime;
	public int endTime;
	public int time;
	public int points;
	public String scorer;
	public boolean turnover;
	public int oBoards;
	public int dFouls;
	public boolean assist;
	public int missedShots;
	public boolean steal;
	public String start;
	public String date;
	public int quarter;
	public int shotDistance;
	public String action;
	public String away;
	public String home;
	public String missDistances;
	

	public void printPossession() {
		System.out.print(offense+" "+away+" "+points+" "+time+" secs end"+endTime+", qtr"+quarter+"  D: "+defense+", oRebs: "+oBoards+
				", miss "+missedShots+"  "+missDistances+", dfls "+dFouls+", ast "+assist+", trv "+turnover+", stl "+steal+", who "+scorer+
				", from"+shotDistance+", "+action+"\n\n");
		//System.out.print(offense+" score "+points+"  Start time: "+startTime+" End Time: "+endTime+"\n");
	}
	
	public ArrayList<String> getStringArray(){
		ArrayList<String> returnList = new ArrayList<String>();
		returnList.add(offense);
		returnList.add(defense);
		returnList.add(String.valueOf(startTime));
		returnList.add(String.valueOf(endTime));
		returnList.add(String.valueOf(time));
		returnList.add(String.valueOf(points));
		try{
			String finalScorer = scorer.replace("'", "");
			returnList.add(finalScorer.trim());
		}
		catch(NullPointerException e){
			returnList.add(scorer);
		}
		
		if(turnover){
			returnList.add(String.valueOf(1));
		}
		else{
			returnList.add(String.valueOf(0));
		}
		
		returnList.add(String.valueOf(oBoards));
	
		returnList.add(String.valueOf(dFouls));
		if(assist){
			returnList.add(String.valueOf(1));
		}
		else{
			returnList.add(String.valueOf(0));
		}
		returnList.add(String.valueOf(missedShots));
		if(steal){
			returnList.add(String.valueOf(1));
		}
		else{
			returnList.add(String.valueOf(0));
		}
		returnList.add(start);
		returnList.add(date);
		returnList.add(String.valueOf(quarter));
		returnList.add(String.valueOf(shotDistance));
		
		returnList.add(action);
		returnList.add(away);
		returnList.add(home);
		returnList.add(missDistances);
		
		return returnList;
	}

}

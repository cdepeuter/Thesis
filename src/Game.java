import java.util.ArrayList;


public class Game {

	public String away;
	public String home;
	public String date;
	public int awayScore;
	public int homeScore;
	public int awayTOP;
	public int homeTOP;
	public int awayTurnovers;
	public int homeTurnovers;
	public int awayOBoards;
	public int homeOBoards;
	public int awayDFouls;
	public int homeDFouls;
	public int awayAssists;
	public int homeAssists;
	public int awayMisses;
	public int homeMisses;
	public int awaySteals;
	public int homeSteals;
	public int awayPossessions;
	public int homePossessions;
	
	public void printGame(){
		System.out.print(away+" vs "+home+" on"+date+", "+String.valueOf(awayScore)+"-"+String.valueOf(homeScore));
	}
	
	
	public ArrayList<String> getStringArray(){
		ArrayList<String> returnList = new ArrayList<String>();
		returnList.add(away);
		returnList.add(home);
		returnList.add(date);
		returnList.add(String.valueOf(awayScore));
		returnList.add(String.valueOf(homeScore));
		returnList.add(String.valueOf(awayTOP));
		returnList.add(String.valueOf(homeTOP));
		returnList.add(String.valueOf(awayTurnovers));
		returnList.add(String.valueOf(homeTurnovers));
		returnList.add(String.valueOf(awayOBoards));
		returnList.add(String.valueOf(homeOBoards));
		returnList.add(String.valueOf(awayDFouls));
		returnList.add(String.valueOf(homeDFouls));
		returnList.add(String.valueOf(awayAssists));
		returnList.add(String.valueOf(homeAssists));
		returnList.add(String.valueOf(awayMisses));
		returnList.add(String.valueOf(homeMisses));
		returnList.add(String.valueOf(awaySteals));
		returnList.add(String.valueOf(homeSteals));
		returnList.add(String.valueOf(awayPossessions));
		returnList.add(String.valueOf(homePossessions));
		
		return returnList;
	}
}

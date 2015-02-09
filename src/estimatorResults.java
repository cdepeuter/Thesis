import java.util.ArrayList;


public class estimatorResults {
	public String away;
	public String home;
	public int awayScore;
	public int homeScore;
	public int awayW;
	public int awayL;
	public int homeW;
	public int homeL;
	public double awayPythag;
	public double homePythag;
	public boolean awayWin;
	public boolean pythagCorrect;
	public boolean recordCorrect;
	public String date;
	
	
	public  void printGame(){
		System.out.print(away+" "+String.valueOf(awayScore)+"-"+home+":"+String.valueOf(homeScore)+", awayPythag:"+String.valueOf(awayPythag)+", homePythag"+String.valueOf(homePythag)+
				", awayRecord:"+String.valueOf(awayW)+"-"+String.valueOf(awayL)+", homeRecord:"+String.valueOf(homeW)+"-"+String.valueOf(homeL)+
				", pythag:"+String.valueOf(pythagCorrect)+", record:"+String.valueOf(recordCorrect)+"\n\n");
	}
	
	public ArrayList<String> getArrayList(){
		ArrayList<String> returnList =new ArrayList<String>();
		
		returnList.add(away);
		returnList.add(home);
		returnList.add(String.valueOf(awayScore));
		returnList.add(String.valueOf(homeScore));
		returnList.add(String.valueOf(awayW));
		returnList.add(String.valueOf(awayL));
		returnList.add(String.valueOf(homeW));
		returnList.add(String.valueOf(homeL));
		returnList.add(String.valueOf(awayPythag));
		returnList.add(String.valueOf(homePythag));
		if(awayWin){
			returnList.add(String.valueOf(1));
		}
		else{
			returnList.add(String.valueOf(0));
		}
		
		if(pythagCorrect){
			returnList.add(String.valueOf(1));
		}
		else{
			returnList.add(String.valueOf(0));
		}
		
		
		if(recordCorrect){
			returnList.add(String.valueOf(1));
		}
		else{
			returnList.add(String.valueOf(0));
		}
		
		returnList.add(date);
		
		
		
		return  returnList;
	}
}

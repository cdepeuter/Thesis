import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class getEstimators {
	public static HashMap<Integer, String> teamMap = new HashMap<Integer,String>() {{
		put(1, "Hawks"); put(2, "Celtics"); put(3, "Hornets"); put(4, "Bulls"); put(5, "Cavaliers"); put(6, "Mavericks"); put(7, "Nuggets");
		put(8, "Pistons"); put(9, "Warriors"); put(10, "Rockets"); put(11, "Pacers"); put(12, "Clippers"); put(13, "Lakers"); put(14, "Heat"); put(15, "Bucks");
		put(16, "Timberwolves"); put(17, "Nets"); put(18, "Knicks"); put(19, "Magic"); put(20, "76ers"); put(21, "Suns"); put(22, "Trail Blazers"); put(23, "Kings"); 
		put(24, "Spurs"); put(25, "Thunder"); put(26, "Jazz"); put(27, "Wizards"); put(28, "Raptors"); put(29, "Grizzlies"); put(30, "Bobcats");
	}};
	
	
	
	static int[] recordStart = {0,0};
	
	
	public static HashMap<String, int[]> recordMap = new HashMap<String,int[]>(){{
		put("Atlanta Hawks", recordStart); put("Boston Celtics", recordStart); put("New Orleans Hornets", recordStart); put("Chicago Bulls", recordStart);
		put("Cleveland Cavaliers", recordStart); put("Dallas Mavericks", recordStart); put("Denver Nuggets", recordStart); put("Detroit Pistons", recordStart);
		put("Golden State Warriors", recordStart); put("Houston Rockets", recordStart); put("Indiana Pacers", recordStart); put("Los Angeles Clippers", recordStart);
		put("Los Angeles Lakers",recordStart); put("Miami Heat", recordStart); put("Milwaukee Bucks", recordStart); put("Minnesota Timberwolves", recordStart);
		put("Brooklyn Nets", recordStart); put("New York Knicks", recordStart); put("Orlando Magic", recordStart); put("Philadelphia 76ers", recordStart);
		put("Phoenix Suns", recordStart); put("Portland Trail Blazers", recordStart); put("Sacramento Kings", recordStart); put("San Antonio Spurs", recordStart);
		put("Oklahoma City Thunder", recordStart); put("Utah Jazz", recordStart); put("Washington Wizards", recordStart); put("Toronto Raptors", recordStart);
		put("Memphis Grizzlies", recordStart); put("Charlotte Bobcats", recordStart);
	}};
	
	public static HashMap<String, String> rMap = new HashMap<String,String>(){{
		put("Atlanta Hawks", "hawks"); put("Boston Celtics", "celtics"); put("New Orleans Hornets", "hornets"); put("Chicago Bulls", "bulls");
		put("Cleveland Cavaliers", "cavaliers"); put("Dallas Mavericks", "mavericks"); put("Denver Nuggets", "nuggets"); put("Detroit Pistons", "pistons");
		put("Golden State Warriors", "warriors"); put("Houston Rockets", "rockets"); put("Indiana Pacers", "pacers"); put("Los Angeles Clippers", "clippers");
		put("Los Angeles Lakers","lakers"); put("Miami Heat", "heat"); put("Milwaukee Bucks", "bucks"); put("Minnesota Timberwolves", "timberwolves");
		put("Brooklyn Nets", "nets"); put("New York Knicks", "knicks"); put("Orlando Magic", "magic"); put("Philadelphia 76ers", "sixers");
		put("Phoenix Suns", "suns"); put("Portland Trail Blazers", "blazers"); put("Sacramento Kings", "kings"); put("San Antonio Spurs", "spurs");
		put("Oklahoma City Thunder", "thunder"); put("Utah Jazz", "jazz"); put("Washington Wizards", "wizards"); put("Toronto Raptors", "raptors");
		put("Memphis Grizzlies", "grizzlies"); put("Charlotte Bobcats", "bobcats");
	}};
	
	public static HashMap<String, int[]> pythagMap = new HashMap<String,int[]>(){{
		put("Atlanta Hawks", recordStart); put("Boston Celtics", recordStart); put("New Orleans Hornets", recordStart); put("Chicago Bulls", recordStart);
		put("Cleveland Cavaliers", recordStart); put("Dallas Mavericks", recordStart); put("Denver Nuggets", recordStart); put("Detroit Pistons", recordStart);
		put("Golden State Warriors", recordStart); put("Houston Rockets", recordStart); put("Indiana Pacers", recordStart); put("Los Angeles Clippers", recordStart);
		put("Los Angeles Lakers",recordStart); put("Miami Heat", recordStart); put("Milwaukee Bucks", recordStart); put("Minnesota Timberwolves", recordStart);
		put("Brooklyn Nets", recordStart); put("New York Knicks", recordStart); put("Orlando Magic", recordStart); put("Philadelphia 76ers", recordStart);
		put("Phoenix Suns", recordStart); put("Portland Trail Blazers", recordStart); put("Sacramento Kings", recordStart); put("San Antonio Spurs", recordStart);
		put("Oklahoma City Thunder", recordStart); put("Utah Jazz", recordStart); put("Washington Wizards", recordStart); put("Toronto Raptors", recordStart);
		put("Memphis Grizzlies", recordStart); put("Charlotte Bobcats", recordStart);
	}};
	
	public static WebDriver instance = null;

	public static WebDriver getInstance() {
		if (instance == null) {
			instance = new FirefoxDriver();
		}
		return instance;

	}
	
	public static void closeInstance(){
		if(!(instance == null)){
			instance.close();
		}
	}
	
	
	
	public static void getRInputs(){
		
		String fullSeason = "http://www.basketball-reference.com/leagues/NBA_2013_games.html";
		
		WebDriver driver = getInstance();
		driver.get(fullSeason);
		
		WebDriverWait wait = new WebDriverWait(driver, 10); // wait for max of 5 seconds
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr")));
		
		List<WebElement> games = driver.findElements(By.cssSelector("tr"));
		int numGames = games.size();
		String gameHref;
		WebElement currentElement;
		String currentGame;
		
		for(int i=0;i<numGames; i++){
			currentElement=games.get(i);
			currentGame=currentElement.getText();
			//System.out.print(currentGame+"\n");
			if(currentGame.contains("Box Score")){//the game has been played
				String awayTeam="";
				String homeTeam="";
				int awayScore=0;
				int homeScore = 0;
				currentGame = currentGame.replace("at London, England", "");
				currentGame = currentGame.replace("3OT", "");
				currentGame = currentGame.replace("2OT", "");
				currentGame = currentGame.replace("OT", "");
				String[] gameSplit1 = currentGame.split("Box Score");
				String date = gameSplit1[0].trim();
				String[] gameSplit2 = gameSplit1[1].split(" ");
				for(int j=0; j<gameSplit2.length; j++){
					try{
						awayScore = Integer.parseInt(gameSplit2[j]);
					}
					catch(NumberFormatException e){
						//not a number
					}
					
					if(awayScore != 0){//got away score now get away team
						int awayScoreLoc = j;
						for(int k=0; k<j; k++){
							awayTeam += gameSplit2[k]+" ";
							
						}
						
						homeScore= Integer.parseInt(gameSplit2[gameSplit2.length-1]);
						for(int q=awayScoreLoc+1; q<gameSplit2.length-1; q++){
							homeTeam += gameSplit2[q]+" ";
							
						}
					}
					if(homeScore != 0){//got all game info process game
						homeTeam=homeTeam.trim();
						awayTeam = awayTeam.trim();
						String homeRMap = rMap.get(homeTeam);
						String awayRMap = rMap.get(awayTeam);
						try{
							if(!homeRMap.equals(null) && !awayRMap.equals(null)){
								//String printString="simFourFactors("+awayRMap+"FirstShotDist, "+awayRMap+"Turnovers, "+awayRMap+"FTS, "+awayRMap+"RBS, "+awayRMap+"AwayFS, "
							//+awayRMap+"FSD, "+homeRMap+"FirstShotDist, "+homeRMap+"Turnovers, "+homeRMap+"FTS, "+homeRMap+"RBS, "+homeRMap+"HomeFS, "+homeRMap+"FSD)"; 
								String printString = "simFourFactors("+awayRMap+"FirstShotDist, "+awayRMap+"Turnovers, "+awayRMap+"FTS, "+awayRMap+"RBS, "+
										awayRMap+"AwayFS, "+awayRMap+"FSD,"+homeRMap+"FirstShotDist, "+homeRMap+"Turnovers, "+homeRMap+"FTS, "+homeRMap+"RBS, "+
												homeRMap+"HomeFS, "+homeRMap+"FSD)";
								System.out.println(printString);
							}
						}
						catch(NullPointerException e){
							
						}
						
					}
					
				}
				
			}
		}
		
	}
	
	
	
	public static void getOtherEstimators(){
		String fullSeason = "http://www.basketball-reference.com/leagues/NBA_2013_games.html";
		
		WebDriver driver = getInstance();
		driver.get(fullSeason);
		
		WebDriverWait wait = new WebDriverWait(driver, 10); // wait for max of 5 seconds
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr")));
		
		List<WebElement> games = driver.findElements(By.cssSelector("tr"));
		int numGames = games.size();
		String gameHref;
		WebElement currentElement;
		String currentGame;
		ArrayList<estimatorResults> returnList = new ArrayList<estimatorResults>();
		
		for(int i=0; i<numGames; i++){
			currentElement=games.get(i);
			currentGame=currentElement.getText();
			System.out.print(currentGame+"\n");
			if(currentGame.contains("Box Score")){//the game has been played
				int awayScore=0;
				int homeScore=0;
				String awayTeam="";
				String homeTeam="";
				currentGame = currentGame.replace("at London, England", "");
				currentGame = currentGame.replace("3OT", "");
				currentGame = currentGame.replace("2OT", "");
				currentGame = currentGame.replace("OT", "");
				String[] gameSplit1 = currentGame.split("Box Score");
				String date = gameSplit1[0].trim();
				String[] gameSplit2 = gameSplit1[1].split(" ");
				for(int j=0; j<gameSplit2.length; j++){
					try{
						awayScore = Integer.parseInt(gameSplit2[j]);
					}
					catch(NumberFormatException e){
						//not a number
					}
					
					if(awayScore != 0){//got away score now get away team
						int awayScoreLoc = j;
						for(int k=0; k<j; k++){
							awayTeam += gameSplit2[k]+" ";
						}
						
						homeScore= Integer.parseInt(gameSplit2[gameSplit2.length-1]);
						for(int q=awayScoreLoc+1; q<gameSplit2.length-1; q++){
							homeTeam += gameSplit2[q]+" ";
						}
					}
					
					if(homeScore != 0){//got all game info process game
						estimatorResults playedGame = new estimatorResults();
						awayTeam = awayTeam.trim();
						homeTeam = homeTeam.trim();
						
						playedGame.away = awayTeam;
						playedGame.home = homeTeam;
						playedGame.awayScore = awayScore;
						playedGame.homeScore = homeScore;
						playedGame.date = date;
						
						int[] awayScoring = pythagMap.get(awayTeam);
						int[] homeScoring = pythagMap.get(homeTeam);
						int[] awayRecord = recordMap.get(awayTeam);
						int[] homeRecord = recordMap.get(homeTeam);
						playedGame.awayW = awayRecord[0];
						playedGame.awayL = awayRecord[1];
						playedGame.homeW = homeRecord[0];
						playedGame.homeL = homeRecord[1];
						
						if((awayScoring[0] != 0) && (homeScoring[0] !=0)){//have info to predict this game
							double awayWPercent = Math.pow(awayRecord[0],1)/(Math.pow(awayRecord[0], 1)+Math.pow(awayRecord[1], 1));
							double homeWPercent = Math.pow(homeRecord[0],1)/(Math.pow(homeRecord[0], 1)+Math.pow(homeRecord[1], 1));
							System.out.print("Away%: "+String.valueOf(awayWPercent)+"  Home%:"+String.valueOf(homeWPercent)+"\n");
							
							double awayPythag = Math.pow(awayScoring[0],14)/(Math.pow(awayScoring[0], 14)+Math.pow(awayScoring[1], 14));
							double homePythag = Math.pow(homeScoring[0],14)/(Math.pow(homeScoring[0], 14)+Math.pow(homeScoring[1], 14));
							System.out.print("AwayPy: "+String.valueOf(awayPythag)+"  HomePy:"+String.valueOf(homePythag)+"\n");
							playedGame.awayPythag = awayPythag;
							playedGame.homePythag = homePythag;
							
							if(awayScore > homeScore){//away team won
								playedGame.awayWin = true;
								//did the record predict the game right
								if(awayWPercent > homeWPercent){
									playedGame.recordCorrect = true;
								}
								else{
									playedGame.recordCorrect = false;
								}
								
								//did pythag predict the game right
								if(awayPythag>homePythag){
									playedGame.pythagCorrect = true;
								}
								else{
									playedGame.pythagCorrect = false;
								}
								
							}
							else{//home team won
								playedGame.awayWin=false;
								//did win% predict result
								if(homeWPercent>awayWPercent){
									playedGame.recordCorrect = true;
								}
								else{
									playedGame.recordCorrect = false;
								}
								
								//did pythag predict result
								if(homePythag>awayPythag){
									playedGame.pythagCorrect = true;
								}
								else{
									playedGame.pythagCorrect = false;
								}
							}
							
						}
						int[] newAwayPythag = {awayScoring[0]+awayScore, awayScoring[1]+homeScore};
						int[] newHomePythag = {homeScoring[0]+homeScore, homeScoring[1]+awayScore};
						//now update map info
						pythagMap.put(awayTeam, newAwayPythag);
						pythagMap.put(homeTeam, newHomePythag);
						
						int[] newAwayRecord = new int[2];
						int[] newHomeRecord = new int[2];
						if(awayScore>homeScore){
							newAwayRecord[0] = awayRecord[0]+1;
							newAwayRecord[1] = awayRecord[1];
							newHomeRecord[0] = homeRecord[0];
							newHomeRecord[1] = homeRecord[1]+1;							
						}
						else{
							newAwayRecord[0] = awayRecord[0];
							newAwayRecord[1] = awayRecord[1]+1;
							newHomeRecord[0] = homeRecord[0]+1;
							newHomeRecord[1] = homeRecord[1];		
						}
						recordMap.put(awayTeam, newAwayRecord);
						recordMap.put(homeTeam, newHomeRecord);
						
						
						playedGame.printGame();
						returnList.add(playedGame);
						break;
					}
					
				}
			}
			
			
		}
		for(int c=0; c<returnList.size(); c++){
			
			returnList.get(c).printGame();
			if(Database.gameInDB(returnList.get(c))){
				System.out.println("game in db");
			}
			else{
				Database.insertPredictedGame(returnList.get(c));
				System.out.println("game not in db");
			}
		}
		
	}
	
public static void main(String[] args) {
		getRInputs();
		closeInstance();
	}
	
	
}

/**
 *
 * @author conraddepeuter
 */

import java.util.ArrayList;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetStats {

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
	
	public static void testWebDriver(){
		WebDriver driver= getInstance();
		driver.get("http://espn.go.com/nba/recap?id=311226027");
		WebElement dateElement = driver.findElement(By.cssSelector(".game-time-location p"));
		String dateText1 = dateElement.getText();
		String [] dateArray = dateText1.split(",");
		String finalDate = dateArray[1].trim()+", "+dateArray[2].trim();
		System.out.print(finalDate);
	}
	
	public static ArrayList<String> getCompleteSeason(String url){

		WebDriver driver = getInstance();
		driver.get(url);
		
		ArrayList<String> urls = new ArrayList<String>();
		List<WebElement> games = driver.findElements(By.cssSelector(".score a"));
	
		int numGames = games.size();
		
		String gameUrl;
		String gameHref;
		String gameId="";
		for (int i = 0; i<numGames; i++){
			gameHref = games.get(i).getAttribute("href");
			//System.out.print(gameHref+"\n");
			String[] gameSplit = gameHref.split("id=");
			gameId = gameSplit[1];
			gameUrl= "http://espn.go.com/nba/playbyplay?gameId="+gameId+"&period=0";
			urls.add(gameUrl);			
			//System.out.print("GetStats.findPossessions("+"\"http://espn.go.com/nba/playbyplay?gameId="+gameId+"&period=0\""+", \""+away+"\", \""+home+"\", \""+date+"\")\n");
		}
		//System.out.print(String.valueOf(numGames)+" games\n");
		
		//System.out.print("GetStats.findPossessions("+"\"http://espn.go.com/nba/playbyplay?gameId="+gameId+"&period=0\""+", \""+away+"\", \""+home+"\", \""+date+"\")");
		return urls;
	}

	public static void findPossessions(String url) {
		
		WebDriver driver = getInstance();
		driver.get(url);
		
		WebDriverWait wait = new WebDriverWait(driver, 10); // wait for max of 5 seconds
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".team.away h3")));
		WebElement awayTeamTop = driver.findElement(By.cssSelector(".team.away h3"));
		WebElement awayTeamScore = driver.findElement(By.cssSelector(".team.away h3 span"));
		String awayRemove = awayTeamScore.getText();
		String awayKeep = awayTeamTop.getText();
		String finalAway = awayKeep.replace(" "+awayRemove, "").trim();
	
		
		WebElement homeTeamTop = driver.findElement(By.cssSelector(".team.home h3"));
		WebElement homeTeamScore = driver.findElement(By.cssSelector(".team.home h3 span"));
		String homeRemove = homeTeamScore.getText();
		String homeKeep = homeTeamTop.getText();
		String finalHome = homeKeep.replace(homeRemove, "").trim();
		
		WebElement dateElement = driver.findElement(By.cssSelector(".game-time-location p"));
		String dateText1 = dateElement.getText();
		String [] dateArray = dateText1.split(",");
		String finalDate = dateArray[1].trim()+", "+dateArray[2].trim();
	
		
		String awayTeam = finalAway;
		String homeTeam = finalHome;
		String date = finalDate;
		
		
		if (Database.checkGame(awayTeam, homeTeam, date)){
			System.out.print("Game already recorded in database\n\n");
			return;
		}
		else{
			System.out.print("Game not in db beginning entering possessions\n");
		}
		// driver.get("http://scores.espn.go.com/ncb/playbyplay?gameId=330300154");
		
		//make sure game hasnt already been recorded
		
		List<WebElement> evenPlays = driver.findElements(By.cssSelector(".mod-data tr"));
		int numEven = evenPlays.size();
		ArrayList<String> allPlays = new ArrayList<String>();
		
		for (int i = 0; i < numEven; i++) {
			String ePlay = evenPlays.get(i).getText();
			allPlays.add(ePlay);
		}

		int numRows = allPlays.size();

		/*
		 * for(int i=0; i<numRows; i++){ //System.out.print(i+":  ");
		 * System.out.print(allPlays.get(i)+"\n"); }
		 */

		int i = 0;
		boolean possessionOver;
		int newTime = 720;
		ArrayList<Possession> allPos = new ArrayList<Possession>();
		
		//start running through the play by play
		//keep log of score for the game
		int awayPoints = 0;
		int homePoints = 0;
		int gameQuarter = 0;
		String startAction = "tip";
		

		while (i < numRows - 1) {
			possessionOver = false;
			
			// fill out a possession
			Possession p = new Possession();
			p.away = awayTeam;
			p.home = homeTeam;
			p.oBoards = 0;
			p.turnover = false;
			p.points = 0;
			p.dFouls = 0;
			p.startTime = newTime;
			p.steal = false;
			p.start = startAction;
			p.missDistances = "";
		
			while (!possessionOver) {
				String play = "";
				String[] playParts = play.split(" ");
				try {
					play = allPlays.get(i);
					playParts = play.split(" ");
				} catch (IndexOutOfBoundsException e) {
					driver.quit();
					return;
				}

				// figure out current time
				// System.out.print(playParts[0]);
				try {
					Integer playMins = Integer
							.parseInt(playParts[0].split(":")[0]);
					Integer playSecs = Integer
							.parseInt(playParts[0].split(":")[1]);
					Integer clockTime = playMins * 60 + playSecs;
					if (clockTime.equals(0)) {
						possessionOver = true;
					}
					// System.out.print(playMins+":"+playSecs+"    "+clockTime+"\n");
					String restOfPlay = play.substring(play.indexOf(':') + 3)
							.trim();
					if (restOfPlay.contains("Start of the")){
						gameQuarter++;
					}
					String numbers = "1234567890";
					String firstChar = String.valueOf(restOfPlay.charAt(0));
					String score = "";
					String[] splitPlay = restOfPlay.split(" ");
					String typePlay = "";
					if (numbers.contains(firstChar)) {
						typePlay = "Home Play";
						score = splitPlay[0];
					} else {
						typePlay = "Away Play";
						score = splitPlay[splitPlay.length - 1];
					}
					String[] twoScores = score.split("-");
					try{
						Integer awayScore = Integer.parseInt(twoScores[0]);
						Integer homeScore = Integer.parseInt(twoScores[1]);
					}
					catch(ArrayIndexOutOfBoundsException e){
					}
					String playAction = restOfPlay.replace(score, "").trim();
					String act = "";
					String actionPlayer = "";
					if (!playAction.equals("")) {
						String[] splitAction = playAction.split(" ");
						int j = 0;
						boolean notPlayer = true;

						while (notPlayer) {
							
							if(j>=splitAction.length){
								break;
							}
							Character playerChar = Character.valueOf(splitAction[j].charAt(0));
							
							
							// The player who does the play is always the first
							// few words of playAction that dont start with
							// lower case
							if (Character.isLowerCase(playerChar)) {

								for (int k = 0; k < j; k++) {
									actionPlayer += splitAction[k] + " ";
								}
								notPlayer = false;
							}
							j++;
						}
						actionPlayer.trim();
						act = playAction.replace(actionPlayer, "").trim();

					}

					// System.out.print(clockTime.toString()+":    "+actionPlayer+"       "+act+"     Score:     "+score+"     "+typePlay+"\n");
					// everything is split up, now analyze the act string to
					// figure out what happened

					if (act.contains("loose ball foul")
							|| act.contains("shooting foul")
							|| act.contains("personal foul")
							|| act.contains("shooting block foul")) {
						p.dFouls++;
					}
					if (act.contains("steals")){
						p.steal=true;
					}
	
					if (act.contains("turnover")) {
						p.turnover = true;
						possessionOver = true;
						
						if (typePlay.equals("Away Play")) {
							p.offense = awayTeam;
							p.defense = homeTeam;
						} else {
							p.offense = homeTeam;
							p.defense = awayTeam;
						}
					}

					if (act.contains("traveling")) {
						p.turnover = true;
						possessionOver = true;
						if (typePlay.equals("Away Play")) {
							p.offense = awayTeam;
							p.defense = homeTeam;
						} else {
							p.offense = homeTeam;
							p.defense = awayTeam;
						}
					}

					if (act.contains("bad pass")) {
						p.turnover = true;
						possessionOver = true;
						if (typePlay.equals("Away Play")) {
							p.offense = awayTeam;
							p.defense = homeTeam;
						} else {
							p.offense = homeTeam;
							p.defense = awayTeam;
						}
					}

					if (act.contains("defensive team rebound")) {
						possessionOver = true;
						if (typePlay.equals("Away Play")) {
							p.offense = homeTeam;
							p.defense = awayTeam;
						} else {
							p.offense = awayTeam;
							p.defense = homeTeam;
						}
					}

					if (act.contains("defensive rebound")) {
						possessionOver = true;
						if (typePlay.equals("Away Play")) {
							p.offense = homeTeam;
							p.defense = awayTeam;
						} else {
							p.offense = awayTeam;
							p.defense = homeTeam;
						}
					}

					if (act.contains("offensive rebound")) {
						p.oBoards++;
					}
					//add missed shots
					if (act.contains("misses")) {
						if (!act.contains("free throw")) {
							p.missedShots++;
						}
						if (act.contains("-foot")){
							int missedShotIndex = act.indexOf("-foot")-2;
							String shotDistance = act.substring(missedShotIndex, missedShotIndex+2).trim();;
							
							p.missDistances += shotDistance+",";
						}
						else if (act.contains("layup") || act.contains("tip shot") ){
							p.missDistances += "1,";
						}
						else if (act.contains("hook shot")){
							p.missDistances += "2,";
						}
						else if (act.contains("three point jumper")){
							p.missDistances += "24,";
						}
						else if (act.contains("running jumper")){
							p.missDistances += "6,";
						}
						else if (act.contains("jumper") || act.contains("jumpshot")){
							p.missDistances += "14,";
						}
						
					}

					if (act.contains("makes")) {// made basket
						// who scored
						if (actionPlayer.contains("'")){
							actionPlayer.replaceAll("'", "");
						}
						p.scorer = actionPlayer;
						
						//was it assisted
						if (act.contains("assists")) {
							p.assist = true;
						}
						

						if (!act.contains("free throw")) {// made shot not ft so
															// possession over
							
							//how far was the shot
							if (act.contains("-foot")){
								int shotIndex = act.indexOf("-foot")-2;
								String shotDistance = act.substring(shotIndex, shotIndex+2).trim();;
								Integer shotLength = Integer.valueOf(shotDistance);
								p.shotDistance = shotLength;
							}
							else if(act.contains("layup") || act.contains("dunk") ||  act.contains("tip shot")){
								p.shotDistance = 1;
							}
							possessionOver = true;
							if (typePlay.equals("Away Play")) {
								p.offense = awayTeam;
								p.defense = homeTeam;
							} else {
								p.offense = homeTeam;
								p.defense = awayTeam;
							}
							
							

							if (act.contains("three point jumper")) {
								p.shotDistance = 24;
								p.points += 3;
							} else {
								if (act.contains("jumper")){
									p.shotDistance = 14;
								}
								if (act.contains("hook shot")){
									p.shotDistance = 3;
								}
								if (act.contains("two point shot")){
									p.shotDistance=13;
								}
								if (act.contains("running jumper")){
									p.shotDistance = 6;
								}
								
								p.points += 2;
							}
						} else {// shooting fts
							p.points++;
							if (!act.contains("technical") && act.length()>3) {
								int ofLoc = act.indexOf("of");
								char attemptNumChar = '0';
								if(!(ofLoc == -1)){
									attemptNumChar = act.charAt(ofLoc - 2);
								}
								
								int attemptNum = Character
										.getNumericValue(attemptNumChar);
								char totNumChar = act.charAt(ofLoc + 3);
								int totNum = Character
										.getNumericValue(totNumChar);
								if (attemptNum == totNum) {// made last ft

									possessionOver = true;
									if (typePlay.equals("Away Play")) {
										p.offense = awayTeam;
										p.defense = homeTeam;
									} else {
										p.offense = homeTeam;
										p.defense = awayTeam;
									}

								}

							}
						}

					}
					if (possessionOver) {
						int actionParenthLoc = act.indexOf("(");
						if (actionParenthLoc > 0){
							act = act.substring(0, actionParenthLoc).trim();
						}
						p.action = act;
						p.endTime = clockTime;
						if(p.offense != null){
							if(p.offense.equals(awayTeam)){
								awayPoints += p.points;
							}
							else{
								homePoints += p.points;
							}	
						}
						
					}
				} catch (NumberFormatException e) {

				}

				i++;

			}// end of single possession while
				// System.out.print("\n");
			
			p.date = date;
			
			//set the next possessions starting action to what happened on this possession
			startAction = p.action;
			
			//set the start time of the next possession
			if (p.endTime == 0) {// if qtr over
				newTime = 720;
			} else {
				newTime = p.endTime;
			}

			p.time = p.startTime - p.endTime;
			
			//if first possession of the quarter
			
			p.quarter = gameQuarter;
			if (p.missDistances.endsWith(",")){
				p.missDistances = p.missDistances.substring(0, p.missDistances.length()-1);
			}
			if (p.time < 100 && p.time > 0 && p.offense != null) {
				// p.printPossession();
				allPos.add(p);
			}

			// make sure its a valid possession

			// save this possession as a unique instance

		}


		//get the game stats together
		//do this after you weed out bad possessions
		
		Game game = new Game();
		game.away = awayTeam;
		game.home = homeTeam;
		game.date = date;
		int awayOBoards = 0;
		int homeOBoards = 0;
		int awayDFouls = 0;
		int homeDFouls = 0;
		int awayMisses = 0;
		int homeMisses = 0;
		int awayTOP = 0;
		int homeTOP = 0;
		int awayTurnovers = 0;
		int homeTurnovers = 0;
		int awayAssists = 0;
		int homeAssists = 0;
		int awaySteals = 0;
		int homeSteals = 0;

		Possession pos;
		
		//run through possessions keeping stats
		for (int q = 0; q < allPos.size(); q++) {
			pos = allPos.get(q);
			if(pos.offense.equals(awayTeam)){//away team on offense
				awayOBoards += pos.oBoards;
				homeDFouls += pos.dFouls;
				awayMisses += pos.missedShots;
				awayTOP += pos.time;
				if(pos.turnover){
					awayTurnovers++;
				}
				if(pos.assist){
					awayAssists++;
				}
				if(pos.steal){
					homeSteals++;
				}
				
			}
			else{//home team on offense
				
				homeOBoards += pos.oBoards;
				awayDFouls += pos.dFouls;
				homeMisses += pos.missedShots;
				homeTOP += pos.time;
				if(pos.turnover){
					homeTurnovers++;
				}
				if(pos.assist){
					homeAssists++;
				}
				if(pos.steal){
					awaySteals++;
				}
			}
		}
		game.awayOBoards = awayOBoards;
		game.homeOBoards = homeOBoards;
		game.awayDFouls = awayDFouls;
		game.homeDFouls = homeDFouls;
		game.awayMisses = awayMisses;
		game.homeMisses = homeMisses;
		game.awayTOP = awayTOP;
		game.homeTOP = homeTOP;
		game.awayTurnovers = awayTurnovers;
		game.homeTurnovers = homeTurnovers;
		game.awayAssists = awayAssists;
		game.homeAssists = homeAssists;
		game.awaySteals = awaySteals;
		game.homeSteals = homeSteals;
		game.awayScore = awayPoints;
		game.homeScore = homePoints;
		
		game.printGame();
		

		// Possession testPos = allPos.get(212);
		// db.insertPoss(testPos);

		Database.insertPossessions(allPos);
		Database.insertGame(game);
		
		// System.out.print("\noBoards: "+oBoards+", dFouls: "+dFouls+", misses: "+misses+", turnovers: "+turnovers+", assists: "+assists+"\n");

		System.out.print("\n Database insertion complete\n\n\n");
		// Close the browser
		
	}

}

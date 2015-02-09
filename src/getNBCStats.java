import java.util.ArrayList;
import java.util.HashMap;
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


public class getNBCStats {
	
	public static HashMap<Integer, String> teamMap = new HashMap<Integer,String>() {{
		put(1, "Hawks"); put(2, "Celtics"); put(3, "Hornets"); put(4, "Bulls"); put(5, "Cavaliers"); put(6, "Mavericks"); put(7, "Nuggets");
		put(8, "Pistons"); put(9, "Warriors"); put(10, "Rockets"); put(11, "Pacers"); put(12, "Clippers"); put(13, "Lakers"); put(14, "Heat"); put(15, "Bucks");
		put(16, "Timberwolves"); put(17, "Nets"); put(18, "Knicks"); put(19, "Magic"); put(20, "76ers"); put(21, "Suns"); put(22, "Trail Blazers"); put(23, "Kings"); 
		put(24, "Spurs"); put(25, "Thunder"); put(26, "Jazz"); put(27, "Wizards"); put(28, "Raptors"); put(29, "Grizzlies"); put(30, "Bobcats");
	}};
	
	
	public static HashMap<String, String> acrMap = new HashMap<String,String>() {{
		put("Atl", "Hawks"); put("Bos", "Celtics"); put("NO", "Hornets"); put("Chi", "Bulls"); put("Cle", "Cavaliers"); put("Dal", "Mavericks"); put("Den", "Nuggets");
		put("Det", "Pistons"); put("GS", "Warriors"); put("Hou", "Rockets"); put("Ind", "Pacers"); put("LAC", "Clippers"); put("LAL", "Lakers"); put("Mia", "Heat"); put("Mil", "Bucks");
		put("Min", "Timberwolves"); put("Bkn", "Nets"); put("NY", "Knicks"); put("Orl", "Magic"); put("Phi", "76ers"); put("Pho", "Suns"); put("Por", "Trail Blazers"); put("Sac", "Kings"); 
		put("SA", "Spurs"); put("OKC", "Thunder"); put("Uta", "Jazz"); put("Was", "Wizards"); put("Tor", "Raptors"); put("Mem", "Grizzlies"); put("Cha", "Bobcats");
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
	
	public static void testWebDriver(){
		WebDriver driver= getInstance();
		driver.get("http://espn.go.com/nba/recap?id=311226027");
		
	}
	
	public static ArrayList<String> getCompleteNCAASeason(int teamNum){
		
		WebDriver driver = getInstance();
		ArrayList<String> urls = new ArrayList<String>();
		
		String seasonSched = "http://scores.nbcsports.msnbc.com/cbk/teamstats.asp?team="+String.valueOf(teamNum)+"&report=schedule";
		
		driver.get(seasonSched);
		WebDriverWait wait = new WebDriverWait(driver, 10); // wait for max of 10 seconds
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".shsTable tr a")));
	
		
		List<WebElement> games = driver.findElements(By.cssSelector(".shsTable tr a"));
		
		int numGames = games.size();	
		
		
		String gameHref;
		String gameUrl;
		//loop within the month pages to find the individual games
		for (int i=0; i<numGames; i++){
			gameHref = games.get(i).getAttribute("href");
			
			if (gameHref.contains("boxscore.asp?gamecode=")){
				String[] hrefSplit = gameHref.split("gamecode=");
				//System.out.print(hrefSplit[1]+"\n");
				//System.out.print(gameHref+"\n");
				
				gameUrl = "http://scores.nbcsports.msnbc.com/cbk/pbp.asp?gamecode="+hrefSplit[1];
				//System.out.print(gameUrl+"\n");
				urls.add(gameUrl);
				System.out.print(gameUrl+"\n");	
			}
		
		
			
		}
		
		
		
		return urls;
		
	}
	
	public static ArrayList<String> getCompleteSeason(int teamNum){

		WebDriver driver = getInstance();
		
		
		ArrayList<String> urls = new ArrayList<String>();
		
		String gameHref;
		String gameUrl;
		int month = 11;
		
		String nbcUrl = "http://scores.nbcsports.msnbc.com/nba/teamstats.asp?teamno=";
		nbcUrl += String.valueOf(teamNum)+"&type=schedule&month=";
		String finalUrl;
		while(month != 4){
			
			if (month<10){
				finalUrl = nbcUrl+"0"+String.valueOf(month);
			}
			else{
				finalUrl = nbcUrl +String.valueOf(month);
			}
			
			driver.get(finalUrl);
			WebDriverWait wait = new WebDriverWait(driver, 10); // wait for max of 10 seconds
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".shsTable tr a")));
			
			List<WebElement> games = driver.findElements(By.cssSelector(".shsTable tr a"));
			
			int numGames = games.size();	
		
			//loop within the month pages to find the individual games
			for (int i=0; i<numGames; i++){
				gameHref = games.get(i).getAttribute("href");
				
				if (gameHref.contains("boxscore.asp?gamecode=")){
					String[] hrefSplit = gameHref.split("gamecode=");
					//System.out.print(hrefSplit[1]+"\n");
					//System.out.print(gameHref+"\n");
					
					gameUrl = "http://scores.nbcsports.msnbc.com/nba/pbp.asp?gamecode="+hrefSplit[1];
					//System.out.print(gameUrl+"\n");
					urls.add(gameUrl);
				}
			
				
				
			}
			
			
			if (month == 12){
				month = 1;
			}
			else{
				month++;
			}
			
		}
		//driver.close();
		for (int j = 0; j< urls.size(); j++){
			System.out.print(urls.get(j)+"\n");
		}
		
		return urls;
	}
	
	public static void findPossessions(String url){
		WebDriver driver = getInstance();
		driver.get(url);
		
		WebDriverWait wait = new WebDriverWait(driver, 10); // wait for max of 5 seconds
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".shsPBPRow")));
		
		String[] infoSplit = url.split("&");
		String gameCode = infoSplit[0].replace("http://scores.nbcsports.msnbc.com/nba/pbp.asp?gamecode=", "");
		//System.out.print("GameCode = "+gameCode+"\n");
		String homeNum = infoSplit[1].replace("home=", "");
		Integer homeInt = Integer.valueOf(homeNum);
		String homeTeam = teamMap.get(homeInt);
		String awayNum = infoSplit[2].replace("vis=", "");
		Integer awayInt = Integer.valueOf(awayNum);
		String awayTeam = teamMap.get(awayInt);
		
		System.out.print("GameCode = "+gameCode+"  Away="+awayTeam+"  Home="+homeTeam+"\n");
		
		if (Database.checkGame(awayTeam, homeTeam, gameCode)){
			System.out.print("Game already recorded in database\n\n");
			return;
		}
		else{
			System.out.print("Game not in db beginning entering possessions\n");
		}
		
		ArrayList<String> allPlays = new ArrayList<String>();
		
		
		List<WebElement> lastPlays = driver.findElements(By.cssSelector(".shsPBPRow"));
		int numLast = lastPlays.size();
		List<WebElement> firstPlays = driver.findElements(By.cssSelector(".shsMorePBPRow"));
		
		
		for (int i = 0; i < numLast; i++) {
			String ePlay = lastPlays.get(i).getText();
			allPlays.add(ePlay);
		}
		for (int q=0; q<firstPlays.size(); q++){
			String fPlay = firstPlays.get(q).getText();
			allPlays.add(fPlay);
		}

		int numPlays = allPlays.size();
		
		
		for (int p = numPlays-1; p>=0; p--){
			System.out.print(allPlays.get(p)+"\n");
		}
		
		
		int i = numPlays-1;
		boolean possessionOver;
		int newTime = 720;
		ArrayList<Possession> allPos = new ArrayList<Possession>();
		
		//start running through the play by play
		//keep log of score for the game
		int awayPoints = 0;
		int homePoints = 0;
		String startAction = "tip";
		
		
		
		while(i >= 0){
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
			p.date = gameCode;
			
			
			while(!possessionOver){
				String play = allPlays.get(i).trim();
				String[] playParts = play.split(" ");
				if (playParts[0].equals("Qtr")){
					i--;
					continue;
				}
				Integer quarter = Integer.parseInt(playParts[0]);
				Integer playMins = Integer.parseInt(playParts[1].split(":")[0]);
				Integer playSecs = Integer.parseInt(playParts[1].split(":")[1]);
				Integer clockTime = playMins * 60 + playSecs;
				
				String actionTeam = acrMap.get(playParts[2]);
				
				int playPartsSize = playParts.length;
				
				Integer homeScore = Integer.parseInt(playParts[playPartsSize-1]);
				Integer awayScore = Integer.parseInt(playParts[playPartsSize-2]);
				
				String actionWPlayer="";
				for(int r=3; r<playPartsSize-2; r++){
					actionWPlayer += playParts[r]+" ";
				}
				
				//reasons to skip this play
				if(actionWPlayer.contains("Substitution:") || actionWPlayer.contains("timeout")){
					i--;
					continue;
				}
				
				
				
				//is the possession over
				//end of qtr/defensive rebound/turnover/made shot
				
				//end of qtr
				if(actionWPlayer.contains("End of")){
					possessionOver=true;
					p.action = "end of quarter";
					//cant list details about home or away team
				}
				//defensive rebound
				if(actionWPlayer.contains("defensive rebound")){
					
					possessionOver=true;
					p.action = "defensive rebound";
					if(actionTeam.equals(awayTeam)){
						p.offense = homeTeam;
						p.defense = awayTeam;
					}
					else{
						p.offense = awayTeam;
						p.defense = homeTeam;
					}
				}
				//turnover
				if(actionWPlayer.contains("turnover")){
					
					p.turnover=true;
					possessionOver=true;
					p.action = "turnover";
					if(actionTeam.equals(awayTeam)){
						p.offense = awayTeam;
						p.defense = homeTeam;
					}
					else{
						p.offense = homeTeam;
						p.defense = awayTeam;
					}
				}
				if(actionWPlayer.contains("steals")){
					p.turnover=true;
					p.steal=true;
					possessionOver=true;
					p.action = "steal";
					if(actionTeam.equals(awayTeam)){
						p.offense = awayTeam;
						p.defense = homeTeam;
					}
					else{
						p.offense = homeTeam;
						p.defense = awayTeam;
					}
				}
				
				//made shot, may not end up in end of poss
				if(actionWPlayer.contains("makes")){
					
					if(actionWPlayer.contains("assist")){
						p.assist=true;
					}
					//free throws
					if(actionWPlayer.contains("free throw")){
						p.points++;
						if(!actionWPlayer.contains("technical")){
							//find the free throw number
							int ofLoc = actionWPlayer.indexOf("of");
							char attemptNumChar = '0';
							if(!(ofLoc == -1)){
								attemptNumChar = actionWPlayer.charAt(ofLoc - 2);
							}
							
							int attemptNum = Character
									.getNumericValue(attemptNumChar);
							char totNumChar = actionWPlayer.charAt(ofLoc + 3);
							int totNum = Character
									.getNumericValue(totNumChar);
							//last ft
							if (attemptNum == totNum){
								possessionOver=true;
								p.action = "free throw";
								String[] makeSplit = actionWPlayer.split(" makes");
								p.scorer = makeSplit[0];
								if(actionTeam.equals(awayTeam)){
									p.offense = awayTeam;
									p.defense = homeTeam;
								}
								else{
									p.offense = homeTeam;
									p.defense = awayTeam;
								}
								
							}
						}
						else{
							//just a technical free throw
							p.points++;
						}
					}
					//regular shot
					else{
						possessionOver=true;
						p.action = "made shot";
						//who made it
						String[] makeSplit = actionWPlayer.split(" makes");
						p.scorer = makeSplit[0];
						if(actionTeam.equals(awayTeam)){
							p.offense = awayTeam;
							p.defense = homeTeam;
						}
						else{
							p.offense = homeTeam;
							p.defense = awayTeam;
						}
						
						//3 pointer
						
						if(actionWPlayer.contains("3-point")){
							p.points+=3;
							System.out.print("Three Pointer!!!!!!\n");
							//how far was it
							String[] shotDistanceArray = actionWPlayer.split(" feet out");
							String[] shotDistanceArray2 = shotDistanceArray[0].split(" ");
							String distance = shotDistanceArray2[shotDistanceArray2.length-1];
							Integer shotD = Integer.parseInt(distance);
							p.shotDistance = shotD;
							System.out.print(String.valueOf(shotD)+"\n");
						}
						else{
							p.points+=2;
							System.out.print("two pointer...\n");
							
							//does it have the distance
							if(actionWPlayer.contains(" feet out") || actionWPlayer.contains(" foot out")){
								String[] shotDistanceArray;
								if(actionWPlayer.contains(" foot out")){
									shotDistanceArray = actionWPlayer.split(" foot out");
								}
								else{
									shotDistanceArray = actionWPlayer.split(" feet out");
								}
								String[] shotDistanceArray2 = shotDistanceArray[0].split(" ");
								String distance = shotDistanceArray2[shotDistanceArray2.length-1].trim();
								Integer shotD = Integer.parseInt(distance);
								p.shotDistance = shotD;
								System.out.print(String.valueOf(shotD)+"\n");
							}
						}
					}
				}
				
				//fill out the rest of the stats
				if(actionWPlayer.contains("offensive rebound")){
					p.oBoards++;
				}
				if(actionWPlayer.contains("Shooting foul") || actionWPlayer.contains("Personal foul") || actionWPlayer.contains("Personal Take Foul") || actionWPlayer.contains("Illegal Defense foul")){
					p.dFouls++;
				}
				if(actionWPlayer.contains("misses")){
					//does it give the range
					if((actionWPlayer.contains(" feet out") || actionWPlayer.contains(" foot out")) && (!actionWPlayer.contains("free throw"))){
						p.missedShots++;
						String[] shotDistanceArray;
						if(actionWPlayer.contains(" foot out")){
							shotDistanceArray = actionWPlayer.split(" foot out");
						}
						else{
							shotDistanceArray = actionWPlayer.split(" feet out");
						}
						String[] shotDistanceArray2 = shotDistanceArray[0].split(" ");
						String distance = shotDistanceArray2[shotDistanceArray2.length-1].trim();
						p.missDistances += distance+",";
					}
				}
				
				System.out.print(String.valueOf(quarter)+"  "+String.valueOf(clockTime)+"   "+actionTeam+" "+String.valueOf(awayScore)+"-"+String.valueOf(homeScore)+"      "+actionWPlayer+"\n");
				
				if(possessionOver){
					//finalize the possession
					p.endTime = clockTime;
					p.quarter = quarter;
					if(p.offense != null){
						if(p.offense.equals(awayTeam)){
							awayPoints += p.points;
						}
						else{
							homePoints += p.points;
						}	
					}
				}
				
				
				i--;
			}
			System.out.print("End of possession loop \n\n");
			
			//set the next possessions starting action to what happened on this possession
			
			startAction = p.action;
			
			//set the start time of the next possession
			if (p.endTime == 0) {// if qtr over
				newTime = 720;
			} else {
				newTime = p.endTime;
			}

			p.time = p.startTime - p.endTime;
			//take the last comma out of miss distances
			if (p.missDistances.endsWith(",")){
				p.missDistances = p.missDistances.substring(0, p.missDistances.length()-1);
			}
			
			p.printPossession();
			
			if (p.time < 100 && p.time > 0 && p.offense != null) {
				// p.printPossession();
				allPos.add(p);
			}
			
		}
		
		
		Game game = new Game();
		game.away = awayTeam;
		game.home = homeTeam;
		game.date = gameCode;
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
		int awayPoss=0;
		int homePoss=0;

		Possession pos;
		
		//run through possessions keeping stats
		for (int q = 0; q < allPos.size(); q++) {
			pos = allPos.get(q);
			if(pos.offense.equals(awayTeam)){//away team on offense
				awayPoss++;
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
				homePoss++;
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
		game.awayPossessions = awayPoss;
		game.homePossessions = homePoss;
		
		game.printGame();
		
		//insert into DB
		Database.insertPossessions(allPos);
		Database.insertGame(game);
		
		
		System.out.print("\n Database insertion complete\n\n\n");

	}
	
}

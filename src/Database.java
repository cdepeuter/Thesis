 /*
  * 
  * 
  * @conraddepeuter
  */



import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
	static String Poss_database = "NBC_POSS_TEST";
	static String Game_database = "NBC_GAMES_TEST";
	static String predict_database = "predicted_games_comp";
	static String[] teams = {"Heat", "Rockets","Nets","Wizards","Grizzlies","Warriors","Pistons","Suns","Bulls",
			"Cavaliers","Lakers","Clippers","Jazz","Hornets","Trail Blazers","Thunder","Kings","Pacers","Nuggets","Celtics","Bobcats", 
			"Mavericks","Bucks","Spurs", "Hawks", "76ers", "Knicks", "Magic", "Timberwolves", "Raptors"};
	
	public static HashMap<String, String> monthMap = new HashMap<String, String>(){{
		put("Jan", "01"); put("Feb", "02"); put("Mar", "03"); put("Nov", "11"); put("Dec", "12");
		
	}};
	
	public static HashMap<String, String> teamMap = new HashMap<String,String>(){{
		put("hawks", "Atlanta Hawks"); put("celtics", "Boston Celtics"); put("hornets", "New Orleans Hornets"); put("bulls", "Chicago Bulls");
		put("cavaliers", "Cleveland Cavaliers"); put("mavericks", "Dallas Mavericks"); put("nuggets", "Denver Nuggets"); put("pistons", "Detroit Pistons");
		put("warriors", "Golden State Warriors"); put("rockets","Houston Rockets"); put("pacers", "Indiana Pacers"); put("clippers", "Los Angeles Clippers");
		put("lakers", "Los Angeles Lakers"); put("heat", "Miami Heat"); put("bucks","Milwaukee Bucks"); put("timberwolves", "Minnesota Timberwolves");
		put("nets", "Brooklyn Nets"); put("knicks", "New York Knicks"); put("magic", "Orlando Magic"); put("sixers", "Philadelphia 76ers");
		put( "suns", "Phoenix Suns"); put("blazers", "Portland Trail Blazers"); put("kings", "Sacramento Kings"); put( "spurs", "San Antonio Spurs");
		put( "thunder", "Oklahoma City Thunder"); put( "jazz", "Utah Jazz"); put("wizards", "Washington Wizards"); put("raptors","Toronto Raptors");
		put("grizzlies","Memphis Grizzlies"); put("bobcats", "Charlotte Bobcats");
	}};
	
	public static HashMap<String, String> db2dbMap = new HashMap<String,String>(){{
		put("Atlanta Hawks", "Hawks"); put("Boston Celtics", "Celtics"); put("New Orleans Hornets", "Hornets"); put("Chicago Bulls", "Bulls");
		put("Cleveland Cavaliers", "Cavaliers"); put("Dallas Mavericks", "Mavericks"); put("Denver Nuggets", "Nuggets"); put("Detroit Pistons", "Pistons");
		put("Golden State Warriors", "Warriors"); put("Houston Rockets", "Rockets"); put("Indiana Pacers", "Pacers"); put("Los Angeles Clippers", "Clippers");
		put("Los Angeles Lakers","Lakers"); put("Miami Heat", "Heat"); put("Milwaukee Bucks", "Bucks"); put("Minnesota Timberwolves", "Timberwolves");
		put("Brooklyn Nets", "Nets"); put("New York Knicks", "Knicks"); put("Orlando Magic", "Magic"); put("Philadelphia 76ers", "76ers");
		put("Phoenix Suns", "Suns"); put("Portland Trail Blazers", "Trail Blazers"); put("Sacramento Kings", "Kings"); put("San Antonio Spurs", "Spurs");
		put("Oklahoma City Thunder", "Thunder"); put("Utah Jazz", "Jazz"); put("Washington Wizards", "Wizards"); put("Toronto Raptors", "Raptors");
		put("Memphis Grizzlies", "Grizzlies"); put("Charlotte Bobcats", "Bobcats");
		
	}};
	
	
	
	public static void compareGames(){
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            Statement st3 = con.createStatement();
            //for each possession insert into DB
          
            String sql = "SELECT * FROM "+predict_database+" WHERE 1";
        	
        	
            ResultSet result = st.executeQuery(sql);
            String date;
            String away;
            String home;
            float awayPoss;
            float homePoss;
            float awayTOP;
            float homeTOP;
            
            while(result.next()){
            	
            	date = result.getString(18);
            	System.out.println(date);
            	away = result.getString(1);
            	home = result.getString(2);
            	String [] dateArray =date.split(",");
            	String newDate = dateArray[2].trim();
            	String[] dayMonthSplit = dateArray[1].trim().split(" ");
            	String day = dayMonthSplit[1];
            	if(day.length()==1){
            		day="0"+day;
            	}
            	String month = monthMap.get(dayMonthSplit[0]);
            	
            	newDate += month + day;
            	String awayTeam = db2dbMap.get(away);
            	String homeTeam = db2dbMap.get(home);
            	System.out.println(result.getString(1)+" "+result.getString(2)+" "+result.getString(3)+" "+result.getString(4)+" "+result.getString(18));
            	System.out.println(awayTeam+" "+homeTeam+" "+newDate);
            	
            	
            	ResultSet results2 = st2.executeQuery("SELECT * FROM "+Game_database+" WHERE away='"+awayTeam+"' AND home='"+homeTeam+"' AND date LIKE '"+newDate+"%'");
            	results2.next();
            	try{
            		 awayTOP = results2.getFloat(6);
     	            homeTOP = results2.getFloat(7);
     	            awayPoss = results2.getFloat(20);
     	            homePoss = results2.getFloat(21);
            	}
            	catch (java.sql.SQLException h){
            		break;
            	}
	           
            	String update = "UPDATE "+predict_database+" SET awayPoss="+String.valueOf(awayPoss)+", homePoss="+String.valueOf(homePoss)+", awayTOP="+String.valueOf(awayTOP)+
            			", homeTOP="+homeTOP+"  WHERE date='"+date+"' AND away='"+away+"' AND home='"+home+"'";
	            System.out.println(update);
	            
	            int ret3 = st3.executeUpdate(update);
	            System.out.println(ret3);
            }
        }
        catch (ClassNotFoundException f) {
            System.out.println("Driver not found");
            return;
        }
        catch (SQLException g) {
            g.printStackTrace();
            return;
        }
	}
	
	
 
	public static void updateResults(ArrayList<String> games){
		String gameString;
		String[] gameInfo;
		float spread;
		String awayTeam;
		String homeTeam;
		String awayScore;
		String homeScore;
		String awayPossessions;
		String homePossessions;
		String awayTOP;
		String homeTOP;
		
		
		for(int i=0; i<games.size(); i++){
			gameString=games.get(i);
			gameInfo=gameString.split(",");
			
			spread = Float.parseFloat(gameInfo[0].trim());
			awayTeam = teamMap.get(gameInfo[1].trim().replace("AwayMulti", ""));
			//System.out.print(gameInfo[1]+"\n");
		    //System.out.print(gameInfo[1].replace("AwayMulti", "")+"\n");
			//System.out.print(awayTeam+"\n");
			awayScore = gameInfo[2].trim();
			homeTeam = teamMap.get(gameInfo[3].trim().replace("HomeMulti", ""));
			homeScore = gameInfo[4].trim();
			awayPossessions = gameInfo[5].trim();
			awayTOP = gameInfo[6].trim();
			homePossessions = gameInfo[7].trim();
			homeTOP = gameInfo[8].trim();
			String sql;
			int sqlResult;
			
			
			try {
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	            Statement st = con.createStatement();
	            
	            
	            if(spread>0){//away team won in the model
	            	sql="UPDATE "+predict_database+"    SET  "+"multiCorrect=1, multiAwayScore="+awayScore+
	                		 ", multiHomeScore="+homeScore+", multiAwayPoss="+awayPossessions+", multiHomePoss="+homePossessions+
	                		 ", multiAwayTOP="+awayTOP+", multiHomeTOP="+homeTOP+"     WHERE awayWin=1 AND away='"+awayTeam+"' AND home='"+homeTeam+"'";
	            	//try statement
	            	
	            	 System.out.print("Querying "+sql+" into DB\n");
	                 int ret = st.executeUpdate(sql);
	                 System.out.print(ret+"\n");
	                 
	                 sql="UPDATE "+predict_database+"    SET  "+"multiCorrect=0, multiAwayScore="+awayScore+
	                		 ", multiHomeScore="+homeScore+", multiAwayPoss="+awayPossessions+", multiHomePoss="+homePossessions+
	                		 ", multiAwayTOP="+awayTOP+", multiHomeTOP="+homeTOP+"     WHERE awayWin=0 AND away='"+awayTeam+"' AND home='"+homeTeam+"'";
		            	//try statement
		            	
	                 System.out.print("Querying "+sql+" into DB\n");
		             ret = st.executeUpdate(sql);
		             System.out.print(ret+"\n");
	            }
	            else{//spread<0
	            	 sql="UPDATE "+predict_database+"    SET  "+"multiCorrect=1, multiAwayScore="+awayScore+
	                		 ", multiHomeScore="+homeScore+", multiAwayPoss="+awayPossessions+", multiHomePoss="+homePossessions+
	                		 ", multiAwayTOP="+awayTOP+", multiHomeTOP="+homeTOP+"     WHERE awayWin=0 AND away='"+awayTeam+"' AND home='"+homeTeam+"'";
	            	 
	            	 
	            	 System.out.print("Querying "+sql+" into DB\n");
	                 int ret = st.executeUpdate(sql);
	                 System.out.print(ret+"\n");
	                 
	                 sql="UPDATE "+predict_database+"    SET  "+"multiCorrect=0, multiAwayScore="+awayScore+
	                		 ", multiHomeScore="+homeScore+", multiAwayPoss="+awayPossessions+", multiHomePoss="+homePossessions+
	                		 ", multiAwayTOP="+awayTOP+", multiHomeTOP="+homeTOP+"     WHERE awayWin=1 AND away='"+awayTeam+"' AND home='"+homeTeam+"'";
	                 System.out.print("Querying "+sql+" into DB\n");
	                 ret = st.executeUpdate(sql);
	                 System.out.print(ret+"\n");
	            }
	        }
	        catch (ClassNotFoundException f) {
	            System.out.println("Driver not found");
	            return;
	        }
	        catch (SQLException g) {
	            g.printStackTrace();
	            return;
	        }
	        
		}
	}
	
	public static boolean gameInDB(estimatorResults e){
		try{
			 Class.forName("com.mysql.jdbc.Driver");
	            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	            Statement check = con.createStatement();
	            
	            ResultSet result = check.executeQuery("Select * from "+predict_database+" WHERE date='"+e.date+"' AND home='"+e.home+"' AND away='"+e.away+"'");
	            if(result.next()){
	            	return true;
	            }
	            return false;
		}
		
		catch (ClassNotFoundException f) {
	            System.out.println("Driver not found");
	            return true;
	        }
	     catch (SQLException g) {
	            g.printStackTrace();
	            return true;
	      }
	}
	
	public static void insertPredictedGame(estimatorResults e){
		ArrayList<String> gameAttributes = e.getArrayList();
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
         
            
            
            Statement st = con.createStatement();
            
            
            //for each possession insert into DB
          
            String sql = "INSERT INTO "+predict_database+" (away, home, awayScore, homeScore, awayW, awayL, homeW, homeL, awayPythag, homePythag, awayWin, pythagCorrect, recordCorrect, date)"+
        				"VALUES (";
        	int j=0;
        	for(j=0;j<gameAttributes.size()-1;j++){
        		sql += "'"+gameAttributes.get(j)+"', ";
        	}
        	sql += "'"+gameAttributes.get(j)+"')";
        	System.out.print("Querying "+sql+" into DB\n");
            int ret = st.executeUpdate(sql);
            System.out.print(ret+"\n");
        }
        catch (ClassNotFoundException f) {
            System.out.println("Driver not found");
            return;
        }
        catch (SQLException g) {
            g.printStackTrace();
            return;
        }
		
	}
	
	public static void insertPossessions(ArrayList<Possession> Possessions){
		Possession p;
		
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
            Statement st = con.createStatement();
          
            //for each possession insert into DB
            for(int i=0; i<Possessions.size();i++){
            	p = Possessions.get(i);
            	ArrayList<String> possessionStrings = p.getStringArray();
            	String sql = "INSERT INTO "+Poss_database+" (offense, defense, startTime, endTime, time, points, scorer, turnover, oBoards, dFouls, assist, missedShots, steal, start, date, qtr, shotDistance, action, away, home, missDistances)"+
        				"VALUES (";
        		int j=0;
        		for(j=0;j<possessionStrings.size()-1;j++){
        			sql += "'"+possessionStrings.get(j)+"', ";
        		}
        		sql += "'"+possessionStrings.get(j)+"')";
        		 System.out.print("Querying "+sql+" into DB\n");
                 int ret = st.executeUpdate(sql);
                 System.out.print(ret+"\n");
            }
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
	}
	
	public static void insertPossession(Possession p){
		String sql = "INSERT INTO "+Poss_database+" (offense, defense, startTime, endTime, time, points, scorer, turnover, oBoards, dFouls, assist, missedShots, start)"+
				"VALUES (";
		ArrayList<String> possessionStrings = p.getStringArray();
		
		int i;
		for(i=0;i<possessionStrings.size()-1;i++){
			sql += "'"+possessionStrings.get(i)+"', ";
		}
		sql += "'"+possessionStrings.get(i)+"')";
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
            Statement st = con.createStatement();
            System.out.print("Querying "+sql+" into DB\n");
            int ret = st.executeUpdate(sql);
            System.out.print(ret+"\n");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
	}
	
	
	
	public static void insertGame(Game game){
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
            Statement st = con.createStatement();
          
            //for each possession insert into DB
            
            	ArrayList<String> gameStrings = game.getStringArray();
            	String sql = "INSERT INTO "+Game_database+" (away, home, date, awayScore, homeScore, awayTOP, homeTOP, awayTurnovers, homeTurnovers, awayOBoards, homeOBoards, awayDFouls, homeDFouls, awayAssists, homeAssists, awayMisses, homeMisses, awaySteals, homeSteals, awayPoss, homePoss)"+
        				"VALUES (";
        		int j=0;
        		for(j=0;j<gameStrings.size()-1;j++){
        			sql += "'"+gameStrings.get(j)+"', ";
        		}
        		sql += "'"+gameStrings.get(j)+"')";
        		 System.out.print("Querying "+sql+" into DB\n");
                 int ret = st.executeUpdate(sql);
                 System.out.print(ret+"\n");
            
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
	}
	
	public static boolean checkGame(String away, String home, String date){
		String sql = "select * from "+Game_database+" WHERE away='"+away+"' AND home='"+home+"' AND date='"+date+"'"; 
		System.out.print(sql+"\n");
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
            System.out.println("Connection established.");
            Statement st = con.createStatement();
            ResultSet result = st.executeQuery(sql);

            System.out.println("SQL query executed.");
            System.out.print(result+"\n");
            try{
            	  result.next();
            	  if(result.getString("date").length()>4){//if game in db return true
                  	return true;
                  }
                  else{
                	  System.out.print("Game not in DB\n");
                  	return false;
                  }
            }
            catch(SQLException e){//empty result set
            	return false;
            }
          
            
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	
	public static void testDBConnection(){
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
            System.out.println("Connection established.");
            Statement st = con.createStatement();
            ResultSet result = st.executeQuery("select * from "+Poss_database);
            System.out.println("SQL query executed.");
            while (result.next()) {
                System.out.printf("%s\t%s\t%s\t%s%n",
                        result.getString(1),
                        result.getString(2),
                        result.getString(4),
                        result.getString(5));
            }
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
	}
	
	public static void findData(){
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
            System.out.println("Connection established.");
            Statement st = con.createStatement();
            for(String team:teams){
            	ResultSet result = st.executeQuery("SELECT points FROM NBC_POSS_TEST	 WHERE defense='"+team+"' AND turnover=0 and oBoards=0");
                //System.out.println("SQL query executed.");
                
                //System.out.println(team);
                float[] homePoints = {0,0,0,0, 0,0,0,0,0,0,0};
                int totalPoss=0;
                while(result.next())   
                {    
                  homePoints[result.getInt(1)]++;
                  totalPoss++;
                  
                }  
                String printString = team+", ";
                for(int i=0; i<4;i++){
                	homePoints[i]=homePoints[i]/totalPoss;
                	printString+= String.valueOf(homePoints[i])+", ";
                }
                System.out.println(String.valueOf(printString));
                
                
                /*
                ResultSet result2 = st.executeQuery("SELECT points FROM NBC_POSS_TEST	 WHERE offense='"+team+"' AND away='"+team+"' AND turnover=0 and oBoards=0");
                //System.out.println("SQL query executed.");
                
                
                float noTurnoverSize =0;  
                if (result2 != null)   
                {  
                  result2.beforeFirst();  
                  result2.last();  
                  noTurnoverSize = result2.getRow();  
                }
                //System.out.println(String.valueOf(noTurnoverSize));
                float tovPercent=oTurnoverSize/(oTurnoverSize+noTurnoverSize);
                */  
                //System.out.print(team+" oFT%: "+String.valueOf(tovPercent)+"\n\n");
            }
            
            
        }
        catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return;
        }
	}
 
    public static void main(String[] args) {
        //testDBConnection();
        compareGames();
        
       
       
    } 
}
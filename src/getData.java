import java.sql.*;
import java.util.HashMap;
import java.util.Set;



public class getData {
	
	static String predict_database = "predicted_games_comp";
	static String poss_database = "NBC_POSS_TEST";
	
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
	
	public static HashMap<String, String> teamMap = new HashMap<String,String>(){{
		put("Atlanta Hawks", "Hawks"); put("Boston Celtics", "Celtics"); put("New Orleans Hornets", "Hornets"); put("Chicago Bulls", "Bulls");
		put("Cleveland Cavaliers", "Cavaliers"); put("Dallas Mavericks", "Mavericks"); put("Denver Nuggets", "Nuggets"); put("Detroit Pistons", "Pistons");
		put("Golden State Warriors", "Warriors"); put("Houston Rockets", "Rockets"); put("Indiana Pacers", "Pacers"); put("Los Angeles Clippers", "Clippers");
		put("Los Angeles Lakers","Lakers"); put("Miami Heat", "Heat"); put("Milwaukee Bucks", "Bucks"); put("Minnesota Timberwolves", "Timberwolves");
		put("Brooklyn Nets", "Nets"); put("New York Knicks", "Knicks"); put("Orlando Magic", "Magic"); put("Philadelphia 76ers", "76ers");
		put("Phoenix Suns", "Suns"); put("Portland Trail Blazers", "Trail Blazers"); put("Sacramento Kings", "Kings"); put("San Antonio Spurs", "Spurs");
		put("Oklahoma City Thunder", "Thunder"); put("Utah Jazz", "Jazz"); put("Washington Wizards", "Wizards"); put("Toronto Raptors", "Raptors");
		put("Memphis Grizzlies", "Grizzlies"); put("Charlotte Bobcats", "Bobcats");
		
	}};
	
	static Set<String> teams = teamMap.keySet();
	
	public static void getTOPData(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        String sql = "Select awayTOP, homeTOP, multiAwayTOP, multiHomeTOP  FROM "+predict_database+" WHERE 1";
	        ResultSet result = st.executeQuery(sql);
	        
	        float multiTOP=0;
	        float realTOP = 0;
	        float games = 0;
	        
	        while(result.next()){
	        	games++;
	        	realTOP += result.getFloat(1)+result.getFloat(2);
	        	multiTOP += result.getFloat(3)+result.getFloat(4);
	        }
	            
	            
	        realTOP = realTOP/games;
	        multiTOP = multiTOP/games;
	 
		
	      System.out.print(String.valueOf(realTOP)+"    "+String.valueOf(multiTOP));
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	
	public static void getDefenseDist(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        Statement st2 = con.createStatement();
	      
	        String teamString="";
	        String dataString="";
	        for(String team: teams){
	            String sql = "SELECT points FROM `"+poss_database+"` WHERE `defense`='"+teamMap.get(team)+"' AND `away`='"+teamMap.get(team)+"' AND (action='made shot' or action='defensive rebound') and oBoards=0 and action!='free throw' and dFouls=0";
	            
	            
	            ResultSet result = st.executeQuery(sql);
	            float awayZero=0;
	            float awayOne=0;
	            float awayTwo=0;
	            float awayThree=0;
	            
	            while(result.next()){
	            	
	            	if(result.getInt(1)==0){
	            		awayZero++;
	            	}
	            	if(result.getInt(1)==1){
	            		awayOne++;
	            	}
	            	if(result.getInt(1)==2){
	            		awayTwo++;
	            	}
	            	if(result.getInt(1)==3){
	            		awayThree++;
	            	}
	            }
	            float allAway=awayZero+awayOne+awayTwo+awayThree;
	            
	            awayZero = awayZero/allAway;
	            awayOne = awayOne/allAway;
	            awayTwo = awayTwo/allAway;
	            awayThree = awayThree/allAway;
	            
	            sql = "SELECT points FROM `"+poss_database+"` WHERE `defense`='"+teamMap.get(team)+"' AND (action='made shot' or action='defensive rebound') and oBoards=0 and action!='free throw' and dFouls=0";
	            
	          
	            System.out.println(teamMap.get(team).toLowerCase()+"FSD<-c("+String.valueOf(awayZero)+","+String.valueOf(awayTwo)+","+String.valueOf(awayThree)+")");
	        }
	      
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getPointDist(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        Statement st2 = con.createStatement();
	      
	        String teamString="";
	        String dataString="";
	        for(String team: teams){
	            String sql = "SELECT points FROM `"+poss_database+"` WHERE `offense`='"+teamMap.get(team)+"' AND `away`='"+teamMap.get(team)+"' AND (action='made shot' or action='defensive rebound') and oBoards=0 and action!='free throw' and dFouls=0";
	            
	            
	            ResultSet result = st.executeQuery(sql);
	            float awayZero=0;
	            float awayOne=0;
	            float awayTwo=0;
	            float awayThree=0;
	            
	            while(result.next()){
	            	
	            	if(result.getInt(1)==0){
	            		awayZero++;
	            	}
	            	if(result.getInt(1)==1){
	            		awayOne++;
	            	}
	            	if(result.getInt(1)==2){
	            		awayTwo++;
	            	}
	            	if(result.getInt(1)==3){
	            		awayThree++;
	            	}
	            }
	            float allAway=awayZero+awayOne+awayTwo+awayThree;
	            
	            awayZero = awayZero/allAway;
	            awayOne = awayOne/allAway;
	            awayTwo = awayTwo/allAway;
	            awayThree = awayThree/allAway;
	            
	            sql = "SELECT points FROM `"+poss_database+"` WHERE `offense`='"+teamMap.get(team)+"' AND `home`='"+teamMap.get(team)+"' AND (action='made shot' or action='defensive rebound') and oBoards=0 and action!='free throw' and dFouls=0";
	            
	            ResultSet result2 = st2.executeQuery(sql);
	            float homeZero=0;
	            float homeOne=0;
	            float homeTwo=0;
	            float homeThree=0;
	            
	            while(result2.next()){
	            	if(result2.getInt(1)==0){
	            		homeZero++;
	            	}
	            	if(result2.getInt(1)==1){
	            		homeOne++;
	            	}
	            	if(result2.getInt(1)==2){
	            		homeTwo++;
	            	}
	            	if(result2.getInt(1)==3){
	            		homeThree++;
	            	}
	            }
	            float allHome=(homeZero+homeOne+homeTwo+homeThree);
	            homeZero = homeZero/allHome;
	            homeOne = homeOne/allHome;
	            homeTwo = homeTwo/allHome;
	            homeThree = homeThree/allHome;
	            
	            System.out.println(teamMap.get(team).toLowerCase()+"AwayFS<-c("+String.valueOf(awayZero)+","+String.valueOf(awayTwo)+","+String.valueOf(awayThree)+")");
	            System.out.println(teamMap.get(team).toLowerCase()+"HomeFS<-c("+String.valueOf(homeZero)+","+String.valueOf(homeTwo)+","+String.valueOf(homeThree)+")");
	        }
	      
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getTurnovers(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        Statement st2 = con.createStatement();
	      
	        String teamString="";
	        String dataString="";
	        for(String team: teams){
	            String sql = "Select turnover  FROM "+poss_database+" WHERE offense='"+teamMap.get(team)+"' AND oBoards=0";
	            
	            
	            ResultSet result = st.executeQuery(sql);
	            float turn=0;
	            float noTurn=0;
	            while(result.next()){
	            	if(result.getInt(1)==1.00){
	            		turn=turn+1;
	            	}
	            	else{
	            		noTurn=noTurn+1;
	            	}
	            }
	            
	            float teamTurn = turn/(turn+noTurn);
	            
	            sql = "Select turnover  FROM "+poss_database+" WHERE defense='"+teamMap.get(team)+"' AND oBoards=0";
	            
	            
	            ResultSet result2 = st2.executeQuery(sql);
	            float dTurn=0;
	            float dNoTurn=0;
	            while(result2.next()){
	            	if(result2.getInt(1)==1.00){
	            		dTurn=dTurn+1;
	            	}
	            	else{
	            		dNoTurn=dNoTurn+1;
	            	}
	            }
	            
	            float dTeamTurn = dTurn/(dTurn+dNoTurn);
	            
	            
	            
	            System.out.println(teamMap.get(team).toLowerCase()+"Turnovers<-c("+String.valueOf(teamTurn)+","+String.valueOf(dTeamTurn)+")");
	        }
	      
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getFTData(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        Statement st2 = con.createStatement();
	      
	        String teamString="";
	        String dataString="";
	        for(String team: teams){
	            String sql = "Select * FROM "+poss_database+" WHERE offense='"+teamMap.get(team)+"' AND action='free throw'";
	            
	            
	            ResultSet result = st.executeQuery(sql);
	            float turn=0;
	            float noTurn=0;
	            while(result.next()){
	            	if(result.getInt(1)==1.00){
	            		turn=turn+1;
	            	}
	            	else{
	            		noTurn=noTurn+1;
	            	}
	            }
	            
	            float teamTurn = turn/(turn+noTurn);
	            
	           
	            
	            
	        }
	      
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	
	public static void getPossessionData(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        String realPoss="";
	        String multiString="";
	        for(String team: teams){
	            String sql = "Select away, matrixAwayPoss FROM "+predict_database+" WHERE away='"+team+"'";
	            float poss = 0;
	            float multiPoss=0;
	            float multiWDPoss=0;
	            float ffPoss=0;
	            float games=0;
	            
	            ResultSet result = st.executeQuery(sql);
	            while(result.next()){
	            	games++;
	            	//poss += result.getFloat(2);
	            	multiPoss += result.getFloat(2);
	            	//multiWDPoss += result.getFloat(4);
	            	//ffPoss += result.getFloat(5);
	            }
	            
	            String sql2 = "Select home, matrixHomePoss  FROM "+predict_database+" WHERE home='"+team+"'";
	       
	            ResultSet result2 = st.executeQuery(sql);
	            while(result2.next()){
	            	games++;
	            	//poss += result2.getFloat(2);
	            	multiPoss += result2.getFloat(2);
	            	//multiWDPoss += result2.getFloat(4);
	            	//ffPoss += result2.getFloat(5);
	            }
	            
	            //poss = poss/games;
	            multiPoss = multiPoss/games;
	            //multiWDPoss = multiWDPoss/games;
	            //ffPoss=ffPoss/games;
	            //realPoss += String.valueOf(poss)+", ";
	            multiString += String.valueOf(multiPoss)+", ";
	            System.out.println(teamMap.get(team)+"	"+String.valueOf(multiPoss));
	            
	        }
	        
	        System.out.println(realPoss);
	        System.out.println(multiString);
	        
	 
		
	      
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}
	
	
	public static void getRecords(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        String awayWPer="";
	        String multiAwayWPer="";
	        String multiAwayWDPer="";
	        String teamAwayString="";
	        
	        String homeWPer="";
	        String multiHomeWPer="";
	        String multiHomeWDPer="";
	        String teamHomeString="";
	        
	        for(String team: teams){
	            int awayWins=0;
	            int awayLosses=0;
	            int homeWins=0;
	            int homeLosses = 0;
	            int multiAwayWins=0;
	            int multiAwayLosses = 0;
	            int multiHomeWins=0;
	            int multiHomeLosses = 0;
	            int multiWDAwayWins=0;
	            int multiWDAwayLosses=0;
	            int multiWDHomeWins=0;
	            int multiWDHomeLosses=0;
	            
	            String sql = "SELECT awayWin, matrixCorrect, multiWDCorrect FROM "+predict_database+" WHERE away='"+team+"'";
	            ResultSet result = st.executeQuery(sql);
	            while(result.next()){
	            	if(result.getInt(1)==1){//away team won
	            		awayWins++;
	            		if(result.getInt(2)==1){
	            			multiAwayWins++;
	            		}
	            		else{
	            			multiAwayLosses++;
	            		}
	            		if(result.getInt(3)==1){
	            			multiWDAwayWins++;
	            		}
	            		else{
	            			multiWDAwayLosses++;
	            		}
	            	}
	            	else{
	            		awayLosses++;
	            		if(result.getInt(2)==0){
	            			multiAwayWins++;
	            		}
	            		else{
	            			multiAwayLosses++;
	            		}
	            		if(result.getInt(3)==0){
	            			multiWDAwayWins++;
	            		}
	            		else{
	            			multiWDAwayLosses++;
	            		}
	            	}
	            	
	            	
	            }
	            
	            sql = "SELECT awayWin, matrixCorrect, multiWDCorrect FROM "+predict_database+" WHERE home='"+team+"'";
	            result = st.executeQuery(sql);
	            while(result.next()){
	            	if(result.getInt(1)==0){//home team won
	            		homeWins++;
	            		if(result.getInt(2)==1){//if multi got it right
	            			multiHomeWins++;
	            		}
	            		else{
	            			multiHomeLosses++;
	            		}
	            		if(result.getInt(3)==1){ //if multiWD got it right
	            			multiWDHomeWins++;
	            		}
	            		else{
	            			multiWDHomeLosses++;
	            		}
	            	}
	            	else{
	            		homeLosses++;
	            		if(result.getInt(2)==0){
	            			multiHomeWins++;
	            		}
	            		else{
	            			multiHomeLosses++;
	            		}
	            		if(result.getInt(3)==0){
	            			multiWDHomeWins++;
	            		}
	            		else{
	            			multiWDHomeLosses++;
	            		}
	            	}
	            	
	            	
	            }
	            
	            float teamAwayWins = awayWins;
	            float teamAwayLosses = awayLosses;
	            float mAwayWins = multiAwayWins;
	            float mAwayLosses = multiAwayLosses;
	            float mdAwayWins = multiWDAwayWins;
	            float mdAwayLosses = multiWDAwayLosses;
	            float awayWPerc = teamAwayWins/(teamAwayWins+teamAwayLosses);
	            float mwAwayPer = mAwayWins/(mAwayWins+mAwayLosses);
	            float mwdAwayPer = mdAwayWins/(mdAwayWins+mdAwayLosses);
	            
	            
	            float teamHomeWins = homeWins;
	            float teamHomeLosses = homeLosses;
	            float mHomeWins = multiHomeWins;
	            float mHomeLosses = multiHomeLosses;
	            float mdHomeWins = multiWDHomeWins;
	            float mdHomeLosses = multiWDHomeLosses;
	            float homeWPerc = teamHomeWins/(teamHomeWins+teamHomeLosses);
	            float mwHomePer = mHomeWins/(mHomeWins+mHomeLosses);
	            float mwdHomePer = mdHomeWins/(mdHomeWins+mdHomeLosses);
	            float winPer = (teamHomeWins+teamAwayWins)/(teamHomeWins+teamAwayWins+teamHomeLosses+teamAwayLosses);
	            float multiDWPer = (mdHomeWins+mdAwayWins)/(mdHomeWins+mdAwayWins+mdHomeLosses+mdAwayLosses);
	            float multiWinPerc = (mHomeWins+mAwayWins)/(mHomeWins+mHomeLosses+mAwayWins+mAwayLosses);
	            //System.out.println(teamMap.get(team)+" away win%: "+String.valueOf(awayWPerc)+"  mwPer: "+String.valueOf((mwAwayPer+mwHomePer)/2)+"  mwdPer: "+String.valueOf(mwdAwayPer));
	            //System.out.println(teamMap.get(team)+" home win%: "+String.valueOf(homeWPerc)+"  mwPer: "+String.valueOf(mwHomePer)+"  mwdPer: "+String.valueOf(mwdHomePer));
	            
	            
	            System.out.print(String.valueOf(winPer)+", ");
	            
	            awayWPer += String.valueOf(awayWPerc)+", ";
	            multiAwayWPer += String.valueOf(mwAwayPer)+", ";
	            multiAwayWDPer += String.valueOf(mwdAwayPer)+", ";
	            teamAwayString += "'"+teamMap.get(team)+" away', ";
	            
	            homeWPer += String.valueOf(homeWPerc)+", ";
	            multiHomeWPer += String.valueOf(mwHomePer)+", ";
	            multiHomeWDPer += String.valueOf(mwdHomePer)+", ";
	            teamHomeString += "'"+teamMap.get(team)+"', ";
	            
	        }
	        
	        /*
	        System.out.println(awayWPer);
	        System.out.println(multiAwayWPer);
	        System.out.println(multiAwayWDPer);
	        System.out.println(teamAwayString);
	        System.out.println(homeWPer);
	        System.out.println(multiHomeWPer);
	        System.out.println(multiHomeWDPer);
	        System.out.println(teamHomeString);
	      */
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public static void getMissingRInputs(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        String sql="SELECT away, home FROM predicted_games_comp WHERE multiCorrect is null";
	        ResultSet result = st.executeQuery(sql);
	        
	        
	        while(result.next()){
	        	 String homeRMap=rMap.get(result.getString(2));
	 			String awayRMap=rMap.get(result.getString(1));
	 			String printString = "getMultiSim("+awayRMap+"TimeDist, "+awayRMap+"AwayMulti, "+homeRMap+"TimeDist,"+homeRMap+"HomeMulti)";
	 			System.out.println(printString);
	        }
	        
	       
		} 
		catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} 
		catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
	}
	
	public static void getTOP(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	       
	        for(String team : teams){
	        	String sql = "SELECT time FROM NBC_POSS_TEST WHERE offense='"+teamMap.get(team)+"'";
	        	float TOP = 0;
	        	float poss=0;
	        	ResultSet result = st.executeQuery(sql);
	        	
	        	while(result.next()){
	        		poss++;
	        		TOP += result.getFloat(1);
	        	}
	        	float meanTOP = TOP/poss;
	        	System.out.println(String.valueOf(teamMap.get(team)));
	        }
	 
		
	      
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public static void main(String[] args) {
		getMissingRInputs();
	}

}

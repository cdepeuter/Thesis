import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;


public class getTransitionMatrix {

	static String predict_database = "predicted_games_comp";
	static String poss_database = "NBC_POSS_TEST";
	
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
	
	static ArrayList<String> actions = new ArrayList<String>(
			Arrays.asList("defensive rebound", "made shot", "turnover", "steal", "free throw", "end of quarter"));
	
	public static void getOMatrix(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/NBA", "root", "root");
	        Statement st = con.createStatement();
	        Statement st2 = con.createStatement();
	      
	        String teamString="";
	        String dataString="";
	        for(String team: teams){
	        	String teamReturn="";
	        	teamReturn+=teamMap.get(team).toLowerCase()+"TransD<-matrix(c(";
	        	System.out.println("\n\n"+teamMap.get(team));
	        	int row=0;
	        	for(String start: actions){
	        		//System.out.println(action);
	        		double[][] results = 
	        		{{0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
	        		{0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, 
	        		{0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};
	        		String sql = "SELECT points, action FROM `"+poss_database+"` WHERE `defense`='"+teamMap.get(team)+"'  AND start='"+start+"'";
	        		ResultSet result = st.executeQuery(sql);
	        		row++;
	        		while(result.next()){
	        			
	        			String act=result.getString(2).trim();
	        			//System.out.println(start+"-->"+act);
	        			
	        			if(act.equals("defensive rebound")){
	        				results[row][0]++;
	        			}
	        			if(act.equals("made shot")){
	        				if(result.getInt(1)==2){
	        					results[row][1]++;
	        				}
	        				if(result.getInt(1)==3){
	        					results[row][2]++;
	        				}
	        			}
	        			
	        			if(act.equals("turnover")){
	        				results[row][3]++;
	        			}
	        			
	        			if(act.equals("steal")){
	        				results[row][4]++;
	        			}
	        			
	        			if(act.equals("free throw")){
	        				if(result.getInt(1)==1){
	        					results[row][5]++;
	        				}
	        				if(result.getInt(1)==2){
	        					results[row][6]++;
	        				}
	        				if(result.getInt(1)==3){
	        					results[row][7]++;
	        				}
	        			}
	        			if(act.equals("end of quarter")){
	        				results[row][8]++;
	        			}
	        			
	        			
	        		}
	        		
	        		double sumRow = 0.0;
	        		
	        		for(int i=0; i<9; i++){
	        			sumRow+=results[row][i];
	        		}
	        		
	        		for(int i=0; i<9; i++){
	        			results[row][i]=results[row][i]/sumRow;
	        			teamReturn+=String.valueOf(results[row][i])+",";
	        			System.out.print(results[row][i]+"	");
	        		}
	        		System.out.println();
	        	
	        	}
	        	//System.out.println(teamReturn.substring(0,teamReturn.length()-1)+"), nrow=6, ncol=9, byrow=TRUE)\n");
	            
	            
	          
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
		getOMatrix();

	}

}

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//getEstimators webDriver = new getEstimators();
		/*
	 
		
		*/
		
		ArrayList<String> games = new ArrayList<String>();
		File file = new File("results/missingMultiResults.txt");
		try{
			Scanner input =new Scanner(file);
			while(input.hasNext()){
				String num = input.nextLine();
				//System.out.println(num);
				games.add(num.trim().substring(0, num.length()-2));
			}
		}
		catch(Exception e){
			
		}
		Database.updateResults(games);
		/*
		for(int i=0;i<games.size();i++){
			System.out.println(games.get(i));
		}
		*/
		
		//GetStats.findPossessions("http://espn.go.com/nba/playbyplay?gameId=320109018&period=0");
		
		//webDriver.closeInstance();
		System.out.print("\n\nDone\n\n");
	}

}

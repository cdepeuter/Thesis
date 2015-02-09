import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class otherTools {

	/**
	 * @param args
	 */
	
	public static void excelToLatex(String text){
		String[] rows = text.split("\n");
		for(String row: rows){
			System.out.println(row.replace("	", "&")+"\\\\");
		}
		
	}
	
	public static void excelToR(String text){
		String[]  rows = text.split("\n");
		String ret="";
		for(String row:rows){
			ret+=row.split("	")[1]+", ";
		}
		System.out.print(ret);
	}
	
	public static void riffRaff(String s){
		StringBuilder ret = new StringBuilder();
		for (int i= 0; i<s.length(); i++){
			if (s.charAt(i)=='i' || s.charAt(i)=='I'){
				ret.append('i');
			}
			else{
				ret.append(String.valueOf(s.charAt(i)).toUpperCase());
			}
			
		}
		System.out.print(ret.toString());
	}
	
	
	public static void main(String[] args) {
		
		
		String ret="";
		File file = new File("meanPPP.txt");
		try{
			Scanner input =new Scanner(file);
			while(input.hasNext()){
				String num = input.nextLine();
				ret+=num+"\n";
			}
		}
		catch(Exception e){
			
		}
		excelToLatex(ret);
		
	
	
	}

}

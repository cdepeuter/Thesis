import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class spam {

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
	
	public static void spamDukeCrushes(){
		ArrayList<String> spamList = new ArrayList<String>();
		
		//for(String s : spamList){
		
		
		File file = new File("romeo.txt");
		try{
			Scanner input =new Scanner(file);
			while(input.hasNext()){
				String num = input.nextLine();
				if(num.trim().length()>5){
					spamList.add(num);
				}
				
			}
		}
		catch(Exception e){
			
		}
		for(String s : spamList){	
			WebDriver driver= getInstance();
			driver.get("http://www.surveymonkey.com/s/7W6GR87");
			
			WebDriverWait wait = new WebDriverWait(driver, 10); // wait for max of 10 seconds
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("text_504884651_0")));
			WebElement textBox=driver.findElement(By.id("text_504884651_0"));
			textBox.click();
			textBox.sendKeys(s);
			WebElement nextButton = driver.findElement(By.id("NextButton"));
			nextButton.click();
			
		}
		
		
		
		
	}
	

	
	
	
	
	
	public static void main(String[] args) {
		spamDukeCrushes();
		closeInstance();
		
	}

}

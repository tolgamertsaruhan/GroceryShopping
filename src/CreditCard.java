import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class CreditCard {

	private String nameOnCard;
	private String numberCreditCard;
	private String ccv;
	private String validThru;
	private Customer customer;
	private String savingName;
	public static String[] cardArray = new String[1];
	
	public CreditCard(String nameOnCard, String numberCreditCard, String ccv, String validThru, Customer customer, String savingName) throws IOException
	{
		this.nameOnCard = nameOnCard;
		this.numberCreditCard = numberCreditCard;
		this.ccv = ccv;
		this.validThru = validThru;
		this.customer = customer;
		this.savingName = savingName;
		creditCardWriter();
	}
	
	public CreditCard(Customer customer){
		this.customer = customer;
	}
	
	private void creditCardWriter() throws IOException {//write to txt new credit cards info
		File file = new File("creditCards.txt");
		if (!file.exists()) {
	         file.createNewFile();
	     }
		 
	    FileWriter fileWriter = new FileWriter(file, true);
	    BufferedWriter bWriter = new BufferedWriter(fileWriter);
	    bWriter.newLine();
	    bWriter.write(customer.getUsername() + "," + customer.getPasswordStr() + "," + savingName + "/" + nameOnCard + "/" + numberCreditCard + "/" + validThru + "/" + ccv);
	    bWriter.close();
	}
	
	public void cardList(Customer customer) throws IOException {//adding list every cards
		File file = new File("creditCards.txt");
	    String line = null;
	    String[] splitLine = null;
		BufferedReader bf;
		bf = new BufferedReader(new FileReader(file));

		int index = 0;
		while((line = bf.readLine()) != null) {
			splitLine = line.split(",");

			String[] splitForCard = splitLine[2].split("/");
			if(cardArray[0] != null)
			{
				cardArray = changeArraySizePlus(cardArray);
				cardArray[index] = splitLine[0];
				cardArray = changeArraySizePlus(cardArray);
				cardArray[index + 1] = splitLine[1];
				cardArray = changeArraySizePlus(cardArray);
				cardArray[index + 2] = splitForCard[0];
				index = index + 3;
			}
			else {
				cardArray[0] = splitLine[0];
				cardArray = changeArraySizePlus(cardArray);
				cardArray[index + 1] = splitLine[1];
				cardArray = changeArraySizePlus(cardArray);
				cardArray[index + 2] = splitForCard[0];
				index = index + 3;
			}
		}
		bf.close();
	}
	
	private static String[] changeArraySizePlus(String[] oldArray) {
		return Arrays.copyOf(oldArray, oldArray.length + 1);
	}
}

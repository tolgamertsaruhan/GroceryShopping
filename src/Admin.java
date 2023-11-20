import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Admin {
	private String username;
	private char[] password;
	private String passwordStr;
	private int index1 = 0;
	
	public Admin(String username, char[] password){
		this.username = username;
		this.password = password;
		for(int i = 0; i < password.length; i++)
		{
			if(passwordStr != null)
				passwordStr = passwordStr + password[i];
			else
				passwordStr = "" + password[i];
		}
	}

	public String getUsername() {
		return username;
	}
	
	public String getPasswordStr() {
		return passwordStr;
	}
	
	public char[] getpassword() {
		return password;
	}

	public void addProduct(Product newProduct) throws IOException {
		File file = new File("products.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fileWriter = new FileWriter(file, true);
		BufferedWriter bWriter = new BufferedWriter(fileWriter);

		//writing the new product to the file
		bWriter.newLine();
		//writing with separator
		bWriter.write(newProduct.getCategory() + "/" + newProduct.getProductName() + "/" + newProduct.getProductPrice() + "/" + newProduct.getProductIngredients() );
		bWriter.close();
	}

	@SuppressWarnings("resource")
	public int removeProduct(Product product) throws IOException {
		String line1 = null, line2 = null;
		BufferedReader bf1, bf2;
		bf1 = new BufferedReader(new FileReader("products.txt"));
		bf2 = new BufferedReader(new FileReader("products.txt"));
		int lineCounter = 0;
		
		while((line1 = bf1.readLine()) != null) {//counter for the number of products to old text
			lineCounter++;
		}
		
		int index=0;
		String[] array = new String[lineCounter];//array for keep the reading products
		lineCounter = 0;
		//The elements other than the element to be deleted are transferred to the array and the element to be deleted is excluded.
		while((line2 = bf2.readLine()) != null) {
			String[] a = line2.split("/");
			if(product.getProductName().equalsIgnoreCase(a[1])) {
				index1=index;
			}else {
				array[lineCounter] = line2;
				lineCounter++;
			}
			index++;
		}
		//The length of the array is 1 more than necessary as it comes from the previous txt and includes the element to be deleted.
		//I am using the changeArraySizeMinus function to fix this situation.
		array = changeArraySizeMinus(array);
		
		//Using this array as a source, the txt is rewritten.
		FileWriter fileWriter = new FileWriter("products.txt", false);
		BufferedWriter bWriter = new BufferedWriter(fileWriter);
		for(int i = 0; i < lineCounter; i++) {
			bWriter.write(array[i]);
			if(lineCounter != i + 1)
				bWriter.write("\n");
		}
		
		bWriter.close();
		return index1;
	}
	
	private static String[] changeArraySizeMinus(String[] oldArray) {//Returns the size of the given array by 1 index.
		return Arrays.copyOf(oldArray, oldArray.length - 1);
	}
	
	
	
	
	
	
	
}

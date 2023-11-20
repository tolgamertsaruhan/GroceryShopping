import java.io.*;

public class Customer {
	
	private String username;
	private char[] password;
	private String passwordStr;
	private String[] customerCart;
	
	public Customer(String username, char[] password){
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
	
	public String getPasswordStr() {
		return passwordStr;
	}
	
	public String getUsername() {
		return username;
	}

	public char[] getPassword() {
		return password;
	}
	
	public void setCustomerCart(String[] cart) {
		customerCart = cart;
	}
	
	public String[] getCustomerCart() {
		return customerCart;
	}
	//if the 'admin.txt' and 'customer.txt' files does not have the information about the person who is trying to log in
	public void addCustomer(String userName, String password) throws IOException {
	     File file = new File("customer.txt");
	     if (!file.exists()) {
	         file.createNewFile();
	     }

	     FileWriter fileWriter = new FileWriter(file, true);
	     BufferedWriter bWriter = new BufferedWriter(fileWriter);
	     bWriter.write("\n" + userName + "," + password);
	     bWriter.close();
	}
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import javax.swing.JList;

public class Product {

	private String category;
	private String productName;
	private String productIngredients;
	private String productPrice;
	private static List<Object> productList = new ArrayList<>();
	
	public Product(String category, String productName, String productIngredients, String productPrice){
		this.category = category;
		this.productName = productName;
		this.productIngredients = productIngredients;
		this.productPrice = productPrice;
	}
	
	public Product() {
		
	}
	
	public void addProductList(Product product) {
		productList.add(product);
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getProductName() {
		return productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public String getProductIngredients() {
		return productIngredients;
	}
	
	public List<Object> getProductList(){
		return productList;
	}
	
	public boolean searchProductName(String name) {
		boolean searchFlag = false;
		Iterator<Object> iterator = productList.iterator();
		while(iterator.hasNext()){
			String str = ((Product) iterator.next()).getProductName();
		    if(str.equals(name) == true) {
		  	  searchFlag = true;
		   	  break;
		    }
		}
		return searchFlag;
	}
	
	public void removeProductList(int index) {
		productList.remove(index);
	}
}

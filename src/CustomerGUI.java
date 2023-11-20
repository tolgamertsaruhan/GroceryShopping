import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class CustomerGUI extends JFrame {

	private JPanel customerPage;
	static String productName;
	String product;
	String productIngredients;
	static String productNameCart;
	List<Object> productList = new ArrayList<Object>();
	Object[] productListArray = new Object[productList.size()];
	Object[] productListArray2;
	GUI frame = new GUI();
	String[] cart = new String[1];
	String[] productTexts;
	boolean flagControl = false;
	double discount = 0;//for show discount quantity
	boolean backgroundFlag = false;
	Color background = null;
	static String amountStrForBack = "0";
	static String amountStrForBack2 = "0";
	
	//While deleting from the cart, right after selecting and deleting another product,
	//when you press the key without being selected from the cart,
	//it behaves as if it is selected in the background and deletes the flag to prevent this situation.
	static boolean flag = false;           
	int cartIndex = 0; //For index tracking when adding to the cart array
	public static String[] cartText;//To return text-based output to the receipt
	private int discountNumber = 0;//select discount option

	public CustomerGUI(Customer customer) throws IOException {
		setTitle("Grocery Shopping - Customer version - Shopping Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 65, 800, 750);
		customerPage = new JPanel();
		customerPage.setBackground(new Color(222, 222, 222));
		customerPage.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(customerPage);
		customerPage.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(265, 159, 258, 130);
		customerPage.add(scrollPane);

		productList = readFile();//reading products and keeps object format
		productTexts = new String[1];//it will keep product all attribute line format
		String[] productCategories = new String[1];//The productCategories array to be filled in to access the categories of the products separately
		productListArray = productList.toArray();// convert array for array operations
		productListArray2 = productListArray;
		boolean categoryFlag = false; //flag for is there an element from the same category
		int categoryIndex = 0; //for index tracking of productCategories array
		for(int i = 0; i < productList.size(); i++) { //category variants are populated into productCategories array
			String temp = ((Product) productListArray[i]).getCategory();
			if (productCategories.length == 1) 
			{
				productCategories[categoryIndex] = temp;
				productCategories = changeArraySizePlus(productCategories);//I am using my function to increase array size by 1 index
				categoryIndex++;
			}
			else {
				for(int j = 0; j < productCategories.length; j++)
				{
					if (temp.equals(productCategories[j]))
					{
						categoryFlag = true;
						break;
					}
				}
				if(categoryFlag == false)
				{
					productCategories = changeArraySizePlus(productCategories);
					productCategories[categoryIndex] = temp;
					categoryIndex++;
				}
			}
			categoryFlag = false;
		}
		//I use my array size reduction function by 1 index because the last index of the array,
		//which I previously increased by 1 index to add, is not filled.
		productCategories = changeArraySizeMinus(productCategories);
		JList list_1 = new JList();
		
		scrollPane.setViewportView(list_1);
		
		JLabel lblNewLabel = new JLabel("Cart:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBorder(new LineBorder(new Color(0, 191, 255), 7, true));
		lblNewLabel.setBounds(55, 449, 71, 41);
		customerPage.add(lblNewLabel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(265, 403, 258, 130);
		customerPage.add(scrollPane_1);
		
		JList<String> list = new JList<String>();
		
		scrollPane_1.setViewportView(list);//cart part
		
		JLabel lblNewLabel_1 = new JLabel("Products List:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1.setBorder(new LineBorder(new Color(0, 191, 255), 7, true));
		lblNewLabel_1.setBounds(55, 200, 156, 41);
		customerPage.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Select a Category:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1_1.setBorder(new LineBorder(new Color(0, 191, 255), 7, true));
		lblNewLabel_1_1.setBounds(55, 53, 202, 41);
		customerPage.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_4 = new JLabel("0 TL");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_4.setBounds(376, 543, 360, 28);
		customerPage.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5_1 = new JLabel("");
		lblNewLabel_5_1.setForeground(new Color(255, 0, 0));
		lblNewLabel_5_1.setBounds(265, 686, 258, 21);
		customerPage.add(lblNewLabel_5_1);
		
		JButton deleteProductButton = new JButton("Delete Product");
		deleteProductButton.setBounds(548, 455, 122, 35);
		deleteProductButton.setBackground(new Color(255, 128, 0));
		deleteProductButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				deleteProductButton.setBackground(new Color(0, 191, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				deleteProductButton.setBackground(new Color(255, 128, 0));
			}
		});
		customerPage.add(deleteProductButton);
		
		deleteProductButton.addActionListener(new ActionListener() {//delete product to cart
			public void actionPerformed(ActionEvent e) {
				if ((productNameCart != null) && (flag == true)) {//If flag is not clicked it does not delete it when it is not selected
					String[] temp = new String[2];
					int number = 0;
					if (cart.length > 1)
					{
						for(int i = 0; i < cart.length; i++)
						{
							if (cart[i].contains("-")) //Extracting "-x4" spelling of multiple elements in a cart array
							{
								temp = cart[i].split("-");
								if (temp[0].equals(productNameCart))//if the selected element and the element kept in the cart are the same it will be deleted
								{
									temp[1] = temp[1].replace("x", "");
									number = Integer.parseInt(temp[1]);
									number--;
									if (number == 1) //if x5 -> x4, if x2 -> nothing write
										cart[i] = productNameCart;
									else
										cart[i] = productNameCart + "-x" + number;
									break;
								}
							}
							else {//if element is only one, delete directly
								if (cart[i].equals(productNameCart))
								{
									cart = deleteArrayIndex(cart, i);
									cartIndex--;
									break;
								}
							}
						}
					}
					else {//same operations for multiple different products
						if (cart[0].contains("-"))
						{
							temp = cart[0].split("-");
							if (temp[0].equals(productNameCart))
							{
								temp[1] = temp[1].replace("x", "");
								number = Integer.parseInt(temp[1]);
								number--;
								if (number == 1)
									cart[0] = productNameCart;
								else
									cart[0] = productNameCart + "-x" + number;
							}
						}
						else {
							cart[0] = null;
							cartIndex = cart.length - 1;
						}
					}
					flag = false; //selected element is deleted flag value is false again
					list.setListData(cart);//The final state of the cart array is being set again
					
					String amountStr = lblNewLabel_4.getText();//total price calculation part
					String[] split = amountStr.split(" ");//Existing price is put into amount as int
					int amount = Integer.parseInt(split[0]);
					String numStrDel = null;
					int priceDel = 0;
					for(int j = 0; j < productListArray.length; j++)
					{
						String tempNameDel = ((Product) productListArray[j]).getProductName();
						//in order to reach the price of the element to be deleted,
						//the whole list is browsed and if the element name is the same,
						//the price is withdrawn and subtracted from the amount.
						if(productNameCart.equals(tempNameDel))
						{
							numStrDel = ((Product) productListArray[j]).getProductPrice();
							numStrDel = numStrDel.replace("TL", "");
							priceDel = Integer.parseInt(numStrDel);
							amount = amount - priceDel;
							break;
						}
					}
					
					//if the discountNumber value activates the discount options and the conditions are met,
					//the discount is applied
					if(discountNumber == 1)
					{
						if(amount > 200)
						{
							Product dsc=new Discount();
							amountStrForBack = String.valueOf(amount);
							double dscnt=((Discount) dsc).getDiscount(discountNumber, amount);
							double netAmaount=amount-dscnt;
							String netAmount2 =  String.format("%.2f", netAmaount);
							String dscnt2 =  String.format("%.2f", dscnt);
							amountStr = String.valueOf(amount);
							lblNewLabel_5_1.setText("");
							lblNewLabel_4.setText(amountStr + " TL"+"\n Discount: -"+dscnt2+" TOTALLY "+netAmount2+" TL");
						}
						else {
							amountStrForBack = String.valueOf(amount);
							lblNewLabel_5_1.setText("amount under 200TL");
							amountStr = String.valueOf(amount);
							lblNewLabel_4.setText(amountStr + " TL");
						}
					}
					else if (discountNumber == 2)
					{
						if(amount > 500)
						{
							Product dsc=new Discount();
							amountStrForBack = String.valueOf(amount);
							double dscnt=((Discount) dsc).getDiscount(discountNumber, amount);
							double netAmaount=amount-dscnt;
							String netAmount2 =  String.format("%.2f", netAmaount);
							String dscnt2 =  String.format("%.2f", dscnt);
							amountStr = String.valueOf(amount);
							lblNewLabel_5_1.setText("");
							lblNewLabel_4.setText(amountStr + " TL"+"\n Discount: -"+dscnt2+" TOTALLY "+netAmount2+" TL");
						}
						else {
							amountStrForBack = String.valueOf(amount);
							lblNewLabel_5_1.setText("amount under 500TL");
							amountStr = String.valueOf(amount);
							lblNewLabel_4.setText(amountStr + " TL");
						}
					}
					else {
						amountStrForBack = String.valueOf(amount);
						amountStr = String.valueOf(amount);
						lblNewLabel_4.setText(amountStr + " TL");
						lblNewLabel_5_1.setText("");
					}
				}
			}
		});
		
		list.getSelectionModel().addListSelectionListener(e -> {
			if (list.getSelectedValue() != null) //understanding what is clicked in the scroll pane of this section
			{
				productNameCart = (String) list.getSelectedValue();//clicked name in productNameCart
				String[] temp = new String[2];
				if(productNameCart.contains("-"))//Extracting the -x4 spelling of multiple added elements in the cart section
				{
					temp = productNameCart.split("-");
					productNameCart = temp[0];
				}
				flag = true;//for clicking
			}
		});
		
		JButton addProductButton = new JButton("Add to Cart");
		addProductButton.setBounds(548, 210, 122, 35);
		addProductButton.setBackground(new Color(255, 128, 0));
		addProductButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				addProductButton.setBackground(new Color(0,191,255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				addProductButton.setBackground(new Color(255, 128, 0));
			}
		});
		customerPage.add(addProductButton);
		
		JScrollPane scrollPane_2 = new JScrollPane();//ingredients part
		scrollPane_2.setBounds(265, 325, 258, 49);
		customerPage.add(scrollPane_2);
		
		JLabel lblNewLabel_2 = new JLabel();//ingredients will be set here
		scrollPane_2.setViewportView(lblNewLabel_2);
		
		JLabel lblNewLabel_1_2 = new JLabel("Ingredients:");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1_2.setBorder(new LineBorder(new Color(0, 191, 255), 7, true));
		lblNewLabel_1_2.setBounds(55, 327, 146, 41);
		customerPage.add(lblNewLabel_1_2);
		
		JComboBox comboBox = new JComboBox(productCategories);//category selected combo box
		comboBox.setSelectedItem(null);
		comboBox.setBounds(316, 53, 156, 41);
		customerPage.add(comboBox);
		
		JLabel lblNewLabel_3 = new JLabel("Cart Amount:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_3.setBounds(265, 543, 102, 28);
		customerPage.add(lblNewLabel_3);
		
		//when the pay button is pressed, the cartText function for the receipt part runs and a new window is opened
		JButton payBtn = new JButton("Pay");
		payBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cart[0] != null)
				{
					customer.setCustomerCart(cart);
					cartText = cartText();
					dispose();
					try {
						amountStrForBack2 = amountStrForBack;
						PayGUI payGUI = new PayGUI(customer);
						payGUI.setVisible(true);//to paying page
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else//if there is not element in cart, it isn't go to paying page
					JOptionPane.showMessageDialog(frame, "Please add items to your cart.");
			}
		});
		payBtn.setBackground(new Color(255, 128, 0));
		payBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				payBtn.setBackground(new Color(0, 191, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				payBtn.setBackground(new Color(255, 128, 0));
			}
		});
		payBtn.setBounds(548, 581, 122, 35);
		customerPage.add(payBtn);
		
		String[] discountArr = {"Discount 1", "Discount 2"};
		JComboBox comboBox_1 = new JComboBox(discountArr);//discount selection combo box
		comboBox_1.setSelectedItem(null);
		comboBox_1.setBounds(265, 581, 130, 35);
		customerPage.add(comboBox_1);
		
		JLabel lblNewLabel_5 = new JLabel("You have not selected any discounts.");
		lblNewLabel_5.setBounds(265, 641, 258, 21);
		customerPage.add(lblNewLabel_5);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBackground(new Color(255, 128, 0));
		btnBack.setBounds(548, 53, 122, 35);
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnBack.setBackground(new Color(0, 191, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnBack.setBackground(new Color(255, 128, 0));
			}
		});
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				GUI gui = new GUI();
				gui.setVisible(true);
			}
		});
		customerPage.add(btnBack);
		
		JButton btnOldCart = new JButton("Old Cart");
		btnOldCart.setBounds(548, 120, 122, 35);
		if(customer.getCustomerCart() == null)
		{
			btnOldCart.setBackground(new Color(192, 192, 192));
			btnOldCart.setEnabled(false);
		}
		else {
			if(!backgroundFlag) {
				btnOldCart.setBackground(new Color(255, 128, 0));
				background = btnOldCart.getBackground();
			}
			if(background == btnOldCart.getBackground()) {
				btnOldCart.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						if(!backgroundFlag) {
							btnOldCart.setBackground(new Color(0, 191, 255));
						}
					}
					@Override
					public void mouseExited(MouseEvent e) {
						if(!backgroundFlag) {
							btnOldCart.setBackground(new Color(255, 128, 0));
						}	
					}
				});
				btnOldCart.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cart = customer.getCustomerCart();
						cartIndex = cart.length;
						list.setListData(cart);
						lblNewLabel_4.setText(amountStrForBack2 + " TL");
						lblNewLabel_5_1.setText("");
						btnOldCart.setBackground(new Color(192, 192, 192));
						btnOldCart.setEnabled(false);
						backgroundFlag = true;
					}
				});
			}
			else {
				btnOldCart.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btnOldCart.setBackground(new Color(192, 192, 192));
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnOldCart.setBackground(new Color(192, 192, 192));
					}
				});
			}
			
		}
		customerPage.add(btnOldCart);
		
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox_1.getSelectedItem().equals("Discount 1"))
				{
					discountNumber = 1;
					lblNewLabel_5.setText("3% discount if the cart amount is over 200 TL");
					
					int intAmount = Integer.parseInt(amountStrForBack);
					String amountStrlbl = "";
					if(intAmount > 200)
					{
						Product dsc=new Discount();
						double dscnt=((Discount) dsc).getDiscount(discountNumber, intAmount);
						double netAmaount=intAmount-dscnt;
						String netAmount2 =  String.format("%.2f", netAmaount);
						String dscnt2 =  String.format("%.2f", dscnt);
						amountStrlbl = String.valueOf(intAmount);
						lblNewLabel_5_1.setText("");
						lblNewLabel_4.setText(amountStrlbl + " TL"+"\n Discount: -"+dscnt2+" TOTALLY "+netAmount2+" TL");
					}
					else {
						lblNewLabel_5_1.setText("amount under 200TL");
						
						amountStrlbl = String.valueOf(intAmount);
						lblNewLabel_4.setText(amountStrlbl + " TL");
					}
				}
				else if(comboBox_1.getSelectedItem().equals("Discount 2"))
				{
					discountNumber = 2;
					lblNewLabel_5.setText("5% discount if the cart amount is over 500 TL");
					
					int intAmount = Integer.parseInt(amountStrForBack);
					String amountStrlbl = "";
					if(intAmount > 500)
					{
						Product dsc=new Discount();
						double dscnt=((Discount) dsc).getDiscount(discountNumber, intAmount);
						double netAmaount=intAmount-dscnt;
						String netAmount2 =  String.format("%.2f", netAmaount);
						String dscnt2 =  String.format("%.2f", dscnt);
						amountStrlbl = String.valueOf(intAmount);
						lblNewLabel_5_1.setText("");
						lblNewLabel_4.setText(amountStrlbl + " TL"+"\n Discount: -"+dscnt2+" TOTALLY "+netAmount2+" TL");
					}
					else {
						lblNewLabel_5_1.setText("amount under 500TL");
						
						amountStrlbl = String.valueOf(intAmount);
						lblNewLabel_4.setText(amountStrlbl + " TL");
					}
				}
			}
		});
		
		comboBox.addActionListener(new ActionListener() {//category combo box
			public void actionPerformed(ActionEvent e) {
				int counter = 0;
				for(int i = 0; i < productList.size(); i++) {
					String temp = ((Product) productListArray[i]).getCategory();
					//Products with category selected from combo box are thrown into productTexts
					if((i != 0) && (comboBox.getSelectedItem().equals(temp))) 
					{
						productTexts = changeArraySizePlus(productTexts);
						productTexts[counter] = ((Product) productListArray[i]).getProductName() + "-" + ((Product) productListArray[i]).getProductPrice();
						counter++;
					}
					else if (comboBox.getSelectedItem().equals(temp))
					{
						productTexts[counter] = ((Product) productListArray[i]).getProductName() + "-" + ((Product) productListArray[i]).getProductPrice();
						productTexts = changeArraySizePlus(productTexts);
						counter++;
					}	
				}
				productTexts = changeArraySizeMinus(productTexts);
				String[] tempArray = new String[1];
				list_1.setListData(productTexts);//productTexts updated according to selected category
				boolean flag0 = false;
				for(int i = 0; i < productTexts.length; i++)//productTexts updated according to selected category
				{
					if (productTexts[i].equals(productName))
						flag0 = true;
				}
				if(flag0 == false)
					productName = null;
				flag0 = false;
				//Since it is defined globally, after it is set, it is brought to the appropriate form for the first step.
				productTexts = tempArray;
			}
		});
		addProductButton.addActionListener(new ActionListener() {//add product button
			public void actionPerformed(ActionEvent e) {
				if (productName != null)
				{
					int count = 0;
					String[] temp = new String[2];
					String tempNumber;
					if(cart[0] != null) {
						for(int i = 0; i < cart.length; i++)
						{
							temp = cart[i].split("-");
							if(temp[0].equals(productName))//To add x2, x3, x4 if the product is already added while adding
							{
								if(temp.length == 1)
								{
									cart[i] = productName + "-x2";
									break;
								}
								else {
									tempNumber = temp[1].replace("x","");
									count = Integer.parseInt(tempNumber);
									count++;
									cart[i] = productName + "-x" + count;
									break;
								}
							}
							if ((i + 1) == cart.length)//first adding selected product
							{
								cart = changeArraySizePlus(cart);
								cart[cartIndex] = productName;
								cartIndex++;
								break;
							}
						}
					}
					else {//first product adding to cart
						cart[cartIndex] = productName;
						cartIndex++;
					}
					list.setListData(cart);
					
					int amount = 0;//Calculating total cart amount
					String amountStr = null;
					for(int i = 0; i < cart.length; i++)
					{
						String[] temp0;
						String numStr = null;
						int price = 0;
						int num = 0;
						if(cart[0] != null)
						{
							if(cart[i].contains("-"))
							{
								temp0 = cart[i].split("-");
								for(int j = 0; j < productListArray.length; j++)
								{
									//Searching for the product in the productListArray and getting its price
									String tempName = ((Product) productListArray[j]).getProductName();
									if(temp0[0].equals(tempName))
									{
										numStr = ((Product) productListArray[j]).getProductPrice();
										numStr = numStr.replace("TL", "");
										price = Integer.parseInt(numStr);
										String tempStr = temp0[1].replace("x", "");
										num = Integer.parseInt(tempStr);
										price = price * num;
										amount = amount + price;
										break;
									}
								}
							}
							else {
								for(int j = 0; j < productListArray.length; j++)
								{
									//Searching for the product in the productListArray and getting its price
									String tempName = ((Product) productListArray[j]).getProductName();
									if(cart[i].equals(tempName))
									{
										numStr = ((Product) productListArray[j]).getProductPrice();
										numStr = numStr.replace("TL", "");
										price = Integer.parseInt(numStr);
										amount = amount + price;
										break;
									}
								}
							}
						}
					}
					//there is the same discount part again because the cart amount changes in the add and remove operations
					//if the discountNumber value activates the discount options and the conditions are met,
					//the discount is applied
					if(discountNumber == 1)
					{
						if(amount > 200)
						{
							Product dsc=new Discount();
							amountStrForBack = String.valueOf(amount);
							double dscnt=((Discount) dsc).getDiscount(discountNumber, amount);
							double netAmaount=amount-dscnt;
							String netAmount2 =  String.format("%.2f", netAmaount);
							String dscnt2 =  String.format("%.2f", dscnt);
							amountStr = String.valueOf(amount);
							lblNewLabel_5_1.setText("");
							lblNewLabel_4.setText(amountStr + " TL"+"\n Discount: -"+dscnt2+" TOTALLY "+netAmount2+" TL");
						}
						else {
							amountStrForBack = String.valueOf(amount);
							lblNewLabel_5_1.setText("amount under 200TL");
							amountStr = String.valueOf(amount);
							lblNewLabel_4.setText(amountStr + " TL");
						}
					}
					else if (discountNumber == 2)
					{
						if(amount > 500)
						{
							Product dsc=new Discount();
							amountStrForBack = String.valueOf(amount);
							double dscnt=((Discount) dsc).getDiscount(discountNumber, amount);
							double netAmaount=amount-dscnt;
							String netAmount2 =  String.format("%.2f", netAmaount);
							String dscnt2 =  String.format("%.2f", dscnt);
							amountStr = String.valueOf(amount);
							lblNewLabel_5_1.setText("");
							lblNewLabel_4.setText(amountStr + " TL"+"\n Discount: -"+dscnt2+" TOTALLY "+netAmount2+" TL");
						}
						else {
							amountStrForBack = String.valueOf(amount);
							lblNewLabel_5_1.setText("amount under 500TL");
							amountStr = String.valueOf(amount);
							lblNewLabel_4.setText(amountStr + " TL");
						}
					}
					else {
						amountStrForBack = String.valueOf(amount);
						amountStr = String.valueOf(amount);
						lblNewLabel_4.setText(amountStr + " TL");
						lblNewLabel_5_1.setText("");
					}
				}
			}
		});
		//Capturing the clicked item in the scroll pane of the product list
		list_1.getSelectionModel().addListSelectionListener(e -> {
			if(list_1.getSelectedValue() != null)
			{
				productListArray = productList.toArray();
				productListArray2 = productListArray;
				product = (String) list_1.getSelectedValue();
				String[] temp = new String[2];
				temp = product.split("-");
				int index = 0;
				while (!temp[0].equals(((Product) productListArray2[index]).getProductName()))
					index++;
				productName = ((Product) productListArray2[index]).getProductName();
				productIngredients = ((Product) productListArray2[index]).getProductIngredients();
				lblNewLabel_2.setText(productIngredients);
			}
		});
	}
	
	private static String[] changeArraySizePlus(String[] oldArray) {//auto increase array size by 1
		return Arrays.copyOf(oldArray, oldArray.length + 1);
	}
	
	private static String[] changeArraySizeMinus(String[] oldArray) {//auto reduce array size by 1
		return Arrays.copyOf(oldArray, oldArray.length - 1);
	}
	
	private String[] deleteArrayIndex(String[] array, int index) {//delete selected array index
		if (array != null)
		{
			if (array.length == 1)
			{
				String[] temp = new String[0];
				return temp;
			}
			else {
				String[] temp = new String[array.length - 1];
				for(int i = 0; i < array.length; i++)
				{
					if (index < i)
						temp[i - 1] = array[i];
					else if (index != i)
						temp[i] = array[i];
				}
				return temp;
			}
		}
		else
			return array;
	}
	
	private List<Object> readFile(){//It reads the txt and creates an object and returns it as a list.
		List<Object> listProduct = new ArrayList();
		Product product = new Product("", "", "", "");
        try (BufferedReader br = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split("/");
                 product = new Product(values[0], values[1], values[3], values[2]);
                if(!product.searchProductName(values[1])) {
                	 product.addProductList(product);
                     listProduct = product.getProductList();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        listProduct = product.getProductList();
        return listProduct;
	}
	
	public String[] cartText() {//Returns the card as text for the receipt part
		String[] rtrn = new String[cart.length + 2];
		int amount = 0;
		String amountStr = null;
		for(int i = 0; i < cart.length; i++)
		{
			String[] temp0;
			String numStr = null;
			int price = 0;
			int num = 0;
			if(cart[0] != null)
			{
				if(cart[i].contains("-"))
				{
					temp0 = cart[i].split("-");
					for(int j = 0; j < productListArray.length; j++)
					{
						String tempName = ((Product) productListArray[j]).getProductName();
						if(temp0[0].equals(tempName))
						{
							numStr = ((Product) productListArray[j]).getProductPrice();
							numStr = numStr.replace("TL", "");
							price = Integer.parseInt(numStr);
							String tempStr = temp0[1].replace("x", "");
							num = Integer.parseInt(tempStr);
							price = price * num;
							amount = amount + price;
							rtrn[i] = "      " + cart[i] + "          " + num + " * " + numStr + "  >>  " + price + " TL";
							break;
						}
					}
				}
				else {
					for(int j = 0; j < productListArray.length; j++)
					{
						String tempName = ((Product) productListArray[j]).getProductName();
						if(cart[i].equals(tempName))
						{
							numStr = ((Product) productListArray[j]).getProductPrice();
							numStr = numStr.replace("TL", "");
							price = Integer.parseInt(numStr);
							amount = amount + price;
							rtrn[i] = "      " + cart[i] + "          " + numStr + " TL";
							break;
						}
					}
				}
			}
		}
		double newAmount = 0;
		if(discountNumber == 1)
		{
			if(amount > 200)
			{
				newAmount = amount - ((3.0 / 100.0) * amount);
				rtrn[rtrn.length - 2] = "Discount 1 active (%3 discount)";
				rtrn[rtrn.length - 1] = Integer.toString(amount) + " >> " + newAmount + " TL";
			}
			else {
				rtrn[rtrn.length - 2] = "Discount 1 selected but amount under 200 TL.";
				rtrn[rtrn.length - 1] = Integer.toString(amount) + " TL";
			}
		}
		else if(discountNumber == 2)
		{
			if(amount > 500)
			{
				newAmount = amount - ((5.0 / 100.0) * amount);
				rtrn[rtrn.length - 2] = "Discount 2 active (%5 discount)";
				rtrn[rtrn.length - 1] = Integer.toString(amount) + " >> " + newAmount + " TL";
			}
			else {
				rtrn[rtrn.length - 2] = "Discount 2 selected but amount under 500 TL.";
				rtrn[rtrn.length - 1] = Integer.toString(amount) + " TL";
			}
		}
		else {
			rtrn[rtrn.length - 2] = "No discount has been selected.";
			rtrn[rtrn.length - 1] = Integer.toString(amount) + " TL";
		}
		return rtrn;
	}
}

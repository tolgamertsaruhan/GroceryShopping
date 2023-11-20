import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class AdminGUI extends JFrame {
	private JPanel contentPane;
	private JButton addProductButton;
	private JButton removeProductButton;
	private DefaultListModel<String> productList;
	private List<Object> pList;
	private JList<String> pl;

	public AdminGUI(Admin admin) throws IOException {
		
		setTitle("Grocery Shopping - Admin version - Product Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(325, 290, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(222, 222, 222));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		//to display the products
		productList = new DefaultListModel<>();
		pList = readFile();//read products.txt
		
		//keep these in pList and add productList
		for(int i = 0; i < pList.size(); i++) {
			productList.addElement(((Product)pList.get(i)).getCategory() + "/" + ((Product)pList.get(i)).getProductName() + "/" + ((Product)pList.get(i)).getProductPrice()  + "/" + ((Product)pList.get(i)).getProductIngredients());  
		}

		addProductButton = new JButton("Add Product");
		addProductButton.setBounds(288, 104, 127, 23);
		addProductButton.setBackground(new Color(113, 184, 255));
		addProductButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		addProductButton.setBackground(new Color(255, 128, 0));
		addProductButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				addProductButton.setBackground(new Color(0, 191, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				addProductButton.setBackground(new Color(255, 128, 0));
			}
		});
		addProductButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//for adding a new product
				//the attributes of the newly created product are taken as input from the user
				String newProductCategory = JOptionPane.showInputDialog(contentPane, "The category of the product you want to add: ");
				String newProductName = JOptionPane.showInputDialog(contentPane, "The name of the product you want to add: ");
				String newProductPrice = JOptionPane.showInputDialog(contentPane, "The price of the product you want to add: ");
				String newProductIngr = JOptionPane.showInputDialog(contentPane, "The ingredients of the product you want to add: \n(Please seperate with commas) ");
				try {//if there is no empty attribute, the prouct is created.
					if( !(newProductCategory == null || newProductName == null || newProductPrice == null  || newProductIngr == null ) ) {
						Product newProduct = new Product(newProductCategory, newProductName, newProductIngr, newProductPrice);
						admin.addProduct(newProduct);
						productList.addElement(newProduct.getCategory() + "/" + newProduct.getProductName() + "/" + newProduct.getProductPrice() + "/" + newProduct.getProductIngredients());
						newProduct.addProductList(newProduct);//the newly added product is also added to the productList
					}   
					else {//if one of the attributes of new product are blank
						JOptionPane.showMessageDialog(contentPane,"Please enter the product name, price and ingredients properly.");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		contentPane.add(addProductButton);
		
		removeProductButton = new JButton("Remove Product");
		removeProductButton.setBounds(288, 150, 127, 23);
		removeProductButton.setBackground(new Color(113, 184, 255));
		removeProductButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		removeProductButton.setBackground(new Color(255, 128, 0));
		removeProductButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				removeProductButton.setBackground(new Color(0, 191, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				removeProductButton.setBackground(new Color(255, 128, 0));
			}
		});
		removeProductButton.addActionListener(new ActionListener() {//for remove selected product
			public void actionPerformed(ActionEvent e) {
				String selectedValue = pl.getSelectedValue();//keep selected product line String format
				if(selectedValue != null) {
					String[] a = selectedValue.split("/");//split this line format
					//It may seem strange to add a new element here,
					//but this element is created to capture the element to be deleted and this element is not thrown into the productList,
					//it is not written to the text file,
					//it is also used to catch the element to be deleted in the productList.
					//It can be thought of as a duplicate element that is not added to the txt and the list,
					//so that the deletion process is carried out without any problems.
					Product p = new Product(a[0],a[1], a[3], a[2]);
					try {
						int removeIndex=admin.removeProduct(p);
						productList.remove(removeIndex);
						p.removeProductList(removeIndex);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		contentPane.add(removeProductButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 57, 180, 175);
		contentPane.add(scrollPane);

		pl = new JList<>(productList);
		scrollPane.setViewportView(pl);
		pl.setBackground(new Color(255, 255, 255));
		pl.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(288, 57, 127, 23);
		btnBack.setBackground(new Color(113, 184, 255));
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnBack.setBackground(new Color(255, 128, 0));
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
		contentPane.add(btnBack);
	}

	private List<Object> readFile(){
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
}

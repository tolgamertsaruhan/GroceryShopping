import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private static boolean flagAdmin = false;//this is used in verifyAccont function to see who logged in, admin or customer 
	private static boolean flagCustomer = false;//then these flags are used for which frame will show up
	private JPanel frame;
	private JTextField userNameField;	
	private JPasswordField passwordField;
	private JLabel userNameLabel;
	private JLabel passwordLabel;
	private JLabel message;
	private JButton signinButton;
	private JButton loginButton;
	static Customer customer;
	static Admin admin;

	public GUI() {
		setTitle("Grocery Shopping");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(244, 256, 612, 368);
		frame = new JPanel();
		frame.setBackground(new Color(222, 222, 222));
		frame.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(frame);
		frame.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(" Grocery Shopping");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 25));
		lblNewLabel.setBorder(new LineBorder(new Color(255, 128, 0), 10, true));
		lblNewLabel.setToolTipText("");
		lblNewLabel.setBounds(157, 39, 262, 69);
		frame.add(lblNewLabel);
		
		userNameLabel = new JLabel("Username :");
		userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		userNameLabel.setForeground(new Color(0, 0, 0));
		userNameLabel.setLabelFor(userNameField);
		userNameLabel.setBounds(173, 137, 91, 17);
		frame.add(userNameLabel);
		
		userNameField = new JTextField();
		userNameField.setBounds(298, 136, 91, 24);
		frame.add(userNameField);
		userNameField.setColumns(10);
		
		passwordLabel = new JLabel("Password :");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordLabel.setBounds(173, 187, 91, 29);
		frame.add(passwordLabel);
		
		message = new JLabel("                             Please enter your user name and password if you have an account.");
		message.setBounds(10, 286, 578, 14);
		frame.add(message);
		
		signinButton = new JButton("Sign In");
		signinButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		signinButton.setBackground(new Color(255, 128, 0));
		signinButton.setBounds(298, 242, 89, 23);
		signinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				signinButton.setBackground(new Color(0,191,255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				signinButton.setBackground(new Color(255, 128, 0));
			}
		});
		
		loginButton = new JButton("Log In");
		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unlikely-arg-type")
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == loginButton) {//if log in button is pressed
					//If the fields are empty, warn the user
					if( (e.getSource() == loginButton) && (userNameField.getText().equals("") || passwordField.getPassword().equals("") ) ) {
						JOptionPane.showMessageDialog(frame, "Please enter the user name and password properly.");
					}
					else {
						try {
							if(e.getSource() == loginButton) {
								verifyAccount(e);								
								//new frames
								if(flagCustomer) {//If flagCustomer is true than the user who logged in is a customer
									dispose();
									flagCustomer = false;
									CustomerGUI customerGUI = new CustomerGUI(customer);
									customerGUI.setVisible(true);
									
								}
								else if(flagAdmin) {//If flagAdmin is true than the user who logged in is an admin
									dispose();
									flagAdmin = false;
									AdminGUI adminGUI = new AdminGUI(admin);
									adminGUI.setVisible(true);
								}
								
							}
							
							
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} 
			}
		});
		loginButton.setForeground(new Color(0, 0, 0));
		loginButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		loginButton.setBackground(new Color(255, 128, 0));
		loginButton.setBounds(173, 242, 89, 23);
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setBackground(new Color(0,191,255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setBackground(new Color(255, 128, 0));
			}
		});
		frame.add(loginButton);
		
		signinButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unlikely-arg-type")
			public void actionPerformed(ActionEvent e) {//sign in button process	
				try {//verifyAccount(e) == 2 means the user who is trying to enter is neither an admin nor a customer
					if( !(userNameField.getText().equals("") && passwordField.getPassword().equals("") )  && (e.getSource() == signinButton && verifyAccount(e) == 2) ) {
						try {//It will automatically write into customer file
							addCustomer(userNameField.getText(), new String(passwordField.getPassword()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		frame.add(signinButton);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(298, 192, 91, 24);
		frame.add(passwordField);
	}
	//adding the user to the customer text if they signed in
	private void addCustomer(String name,String psw) throws IOException {
		customer.addCustomer(name, psw);
	}
	//decision process of all login or sign in operations
	@SuppressWarnings({ "resource", "unlikely-arg-type" })
	private int verifyAccount(ActionEvent e) throws IOException {
		String adminFile = "admin.txt";
		String customerFile = "customer.txt";
		String line = null;
		BufferedReader bf, bf1;
		bf = new BufferedReader(new FileReader(adminFile));
		bf1 = new BufferedReader(new FileReader(customerFile));
		if(userNameField.getText().equals("") || passwordField.getPassword().equals("")) {//fields are expected to be filled
			JOptionPane.showMessageDialog(frame,"Please enter the user name and password properly.");
		}
		while((line = bf.readLine()) != null) {//reading adminFile
			String[] a = line.split(",");
			if(a[0].equals(userNameField.getText()) && a[1].equals(new String(passwordField.getPassword())) ) {
				flagAdmin = true;//if flagAdmin is true, the 'admin.txt' file has this person's informations
				break;
			}
		}
		if(!flagAdmin) {
			while((line = bf1.readLine()) != null) {//reading customerFile
				String[] a = line.split(",");
				if(a[0].equals(userNameField.getText()) && a[1].equals(new String(passwordField.getPassword())) ) {
					flagCustomer = true;//if flagCustomer is true, the 'customer.txt' file has this person's informations
					break;
				}
			}
		}
		
		int flagCount = 0;
		//if the two flags are false, then the person trying to login neither are customer nor admin.
		if(!flagAdmin && !flagCustomer && !(userNameField.getText().equals("") || passwordField.getPassword().equals("")) && e.getSource() == signinButton) {
			JOptionPane.showMessageDialog(frame,"You do not have an account and now you were added to our customer list." + "\n" + "You can log in with same username and password.");
			customer = new Customer(userNameField.getText(), passwordField.getPassword());
			flagCount = 2;
		}else if (!flagAdmin && !flagCustomer && !(userNameField.getText().equals("") || passwordField.getPassword().equals("")) && e.getSource() == loginButton) {
			JOptionPane.showMessageDialog(frame,"You do not have an account. Please sign in first.");
		}
		else if(flagAdmin && e.getSource() == loginButton) {//if flagAdmin is true than the person who is trying to log in is admin
			JOptionPane.showMessageDialog(frame,"Successfully logged in as an admin.");
			admin = new Admin(userNameField.getText(), passwordField.getPassword());
			flagCount = 1;
		}
		else if(flagCustomer && e.getSource() == loginButton) {//if flagCustomer is true than the person who is trying to log in is customer
			JOptionPane.showMessageDialog(frame,"Successfully logged in as a customer.");
			customer = new Customer(userNameField.getText(), passwordField.getPassword());
			flagCount = 1;
		}		
		
		//If the user is in the customer or admin file and pressed 'Sign in' button, warn them to press 'Log in' button
		if(flagCustomer && e.getSource() == signinButton) {
			JOptionPane.showMessageDialog(frame,"You are our customer. Please press 'Log in' button.");
		}
		else if (flagAdmin && e.getSource() == signinButton) {
			JOptionPane.showMessageDialog(frame,"You are an admin. Please press 'Log in' button.");
		}
		
		return flagCount;
	}
}

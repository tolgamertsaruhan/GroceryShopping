import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Color;

@SuppressWarnings("serial")
public class PayGUI extends JFrame{
	private JTextField nameField;
	private JTextField numberField;
	private JTextField ccvField;
	private JTextField validField;
	private JTextField savingNameField;
	private CreditCard card;
	private String cardName;
	private String[] array = new String[1];

	public PayGUI(Customer customer) throws IOException {
		getContentPane().setBackground(new Color(222, 222, 222));
		setTitle("Grocery Shopping - Customer version - Payment Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 140, 700, 600);
		getContentPane().setLayout(null);
		
		JLabel nameOnCardlbl = new JLabel("Name On The Card:");
		nameOnCardlbl.setBounds(74, 89, 112, 13);
		getContentPane().add(nameOnCardlbl);
		
		nameField = new JTextField();
		nameField.setBounds(246, 86, 96, 19);
		getContentPane().add(nameField);
		nameField.setColumns(10);
		
		JLabel numberCreditCardlbl = new JLabel("Number of Credit Card:");
		numberCreditCardlbl.setBounds(74, 145, 162, 13);
		getContentPane().add(numberCreditCardlbl);
		
		numberField = new JTextField();
		numberField.setColumns(10);
		numberField.setBounds(246, 142, 96, 19);
		getContentPane().add(numberField);
		
		JLabel ccvlbl = new JLabel("CCV: ");
		ccvlbl.setBounds(74, 193, 112, 13);
		getContentPane().add(ccvlbl);
		
		ccvField = new JTextField();
		ccvField.setColumns(10);
		ccvField.setBounds(246, 190, 96, 19);
		getContentPane().add(ccvField);
		
		JLabel validThrulbl = new JLabel("Valid Thru:");
		validThrulbl.setBounds(74, 240, 112, 13);
		getContentPane().add(validThrulbl);
		
		validField = new JTextField();
		validField.setColumns(10);
		validField.setBounds(246, 237, 96, 19);
		getContentPane().add(validField);
		
		JButton saveBtn = new JButton("Save");
		saveBtn.setBounds(152, 351, 85, 21);
		saveBtn.setBackground(new Color(255, 128, 0));
		saveBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				saveBtn.setBackground(new Color(0,191,255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				saveBtn.setBackground(new Color(255, 128, 0));
			}
		});
		getContentPane().add(saveBtn);
		
		JLabel savingNamelbl = new JLabel("Saving Name of Credit Card:");
		savingNamelbl.setBounds(74, 285, 162, 13);
		getContentPane().add(savingNamelbl);
		
		savingNameField = new JTextField();
		savingNameField.setColumns(10);
		savingNameField.setBounds(246, 282, 96, 19);
		getContentPane().add(savingNameField);
		
		JLabel selectCardlbl = new JLabel("Select Credit Card:");
		selectCardlbl.setBounds(459, 122, 112, 13);
		getContentPane().add(selectCardlbl);
		
		JButton continueBtn = new JButton("Continue to Receipt");
		continueBtn.setBackground(new Color(255, 128, 0));
		continueBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				continueBtn.setBackground(new Color(0,191,255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				continueBtn.setBackground(new Color(255, 128, 0));
			}
		});
		continueBtn.addActionListener(new ActionListener() {//button that takes you to the receipt screen
			public void actionPerformed(ActionEvent e) {
				if (cardName != null)
				{
					dispose();
					Receipt receipt = new Receipt(card, customer, cardName);
					try {
						ReceiptGUI receiptGUI = new ReceiptGUI(receipt);
						receiptGUI.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		continueBtn.setBounds(443, 236, 152, 21);
		getContentPane().add(continueBtn);
		
		card = new CreditCard(customer);//card validation part
		card.cardList(customer);
		int index = 2;
		int index2 = 0;
		for(int i = 0; i < card.cardArray.length; i = i + 3)
		{
			if(array[0] != null)
			{
				if(customer.getUsername().equals(card.cardArray[i]) && customer.getPasswordStr().equals(card.cardArray[i + 1]))
				{
					array = changeArraySizePlus(array);
					array[index2] = card.cardArray[index];
					index2++;
				}
			}
			else {
				if(customer.getUsername().equals(card.cardArray[i]) && customer.getPasswordStr().equals(card.cardArray[i + 1]))
				{
					array[0] = card.cardArray[index];
					index2++;
				}
			}
			index = index + 3;
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(459, 145, 121, 73);
		getContentPane().add(scrollPane);
		
		JList list = new JList(array);
		scrollPane.setViewportView(list);
		
		list.getSelectionModel().addListSelectionListener(e -> {
			if (list.getSelectedValue() != null) 
			{
				cardName = (String) list.getSelectedValue();
			}
		});
		
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.setBackground(new Color(255, 128, 0));
		refreshBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				refreshBtn.setBackground(new Color(0,191,255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				refreshBtn.setBackground(new Color(255, 128, 0));
			}
		});
		refreshBtn.addActionListener(new ActionListener() {// refresh for new saving cards to list
			public void actionPerformed(ActionEvent e) {
				String[] tempArray = new String[1];
				int index = 2;
				int index2 = 0;
				for(int i = 0; i < card.cardArray.length; i = i + 3)
				{
					if(tempArray[0] != null)
					{
						if(customer.getUsername().equals(card.cardArray[i]) && customer.getPasswordStr().equals(card.cardArray[i + 1]))
						{
							tempArray = changeArraySizePlus(tempArray);
							tempArray[index2] = card.cardArray[index];
							index2++;
						}
					}
					else {
						if(customer.getUsername().equals(card.cardArray[i]) && customer.getPasswordStr().equals(card.cardArray[i + 1]))
						{
							tempArray[0] = card.cardArray[index];
							index2++;
						}
					}
					index = index + 3;
				}
				
				list.setListData(tempArray);
				array = tempArray;
			}
		});
		refreshBtn.setBounds(479, 85, 85, 21);
		getContentPane().add(refreshBtn);
		
		JLabel message1 = new JLabel("");
		message1.setForeground(new Color(255, 0, 0));
		message1.setBounds(74, 112, 163, 13);
		getContentPane().add(message1);
		
		JLabel message2 = new JLabel("");
		message2.setForeground(Color.RED);
		message2.setBounds(74, 168, 163, 13);
		getContentPane().add(message2);
		
		JLabel message3 = new JLabel("");
		message3.setForeground(Color.RED);
		message3.setBounds(74, 216, 163, 13);
		getContentPane().add(message3);
		
		JLabel message4 = new JLabel("");
		message4.setForeground(Color.RED);
		message4.setBounds(74, 262, 163, 13);
		getContentPane().add(message4);
		
		JLabel message5 = new JLabel("");
		message5.setForeground(Color.RED);
		message5.setBounds(74, 312, 163, 13);
		getContentPane().add(message5);
		
		JLabel message6 = new JLabel("");
		message6.setForeground(Color.RED);
		message6.setBounds(74, 382, 268, 13);
		getContentPane().add(message6);
		
		JLabel lblNewLabel = new JLabel("(Letter only)");
		lblNewLabel.setBounds(246, 112, 104, 13);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("(Ex: 0000 1111 2222 3333)");
		lblNewLabel_1.setBounds(246, 167, 163, 13);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("(Ex: 999)");
		lblNewLabel_2.setBounds(246, 214, 73, 13);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("(Ex: m/year >> 11/2222)");
		lblNewLabel_3.setBounds(246, 262, 131, 13);
		getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("(Letter only)");
		lblNewLabel_4.setBounds(246, 311, 73, 13);
		getContentPane().add(lblNewLabel_4);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBackground(new Color(255, 128, 0));
		btnBack.setBounds(479, 40, 85, 21);
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnBack.setBackground(new Color(0,191,255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnBack.setBackground(new Color(255, 128, 0));
			}
		});
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				try {
					CustomerGUI customerGUI = new CustomerGUI(customer);
					customerGUI.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		getContentPane().add(btnBack);
		
		
		saveBtn.addActionListener(new ActionListener() {//card save
			public void actionPerformed(ActionEvent e) {
				boolean flagName = false;
				boolean flagNumber = false;
				boolean flagccv = false;
				boolean flagValid = false;
				boolean flagSavingName = false;
				
				String alphabet = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz ";
				String temp;
				temp = nameField.getText();
				for(int i = 0; i < temp.length(); i++)
				{
					String str = "" + temp.charAt(i);
					if(!alphabet.contains(str))
					{
						message1.setText("error");
						flagName = false;
						nameField.setText("");
						break;
					}
					else 
						flagName = true;
				}
				String numbers = "0123456789";
				temp = numberField.getText();
				if(temp.length() == 19)
				{
					for(int i = 0; i < temp.length(); i++)
					{
						String str = "" + temp.charAt(i);
						
						if((i == 4) || (i == 9) || (i == 14))
						{
							if(str.equals(" "))
								flagNumber = true;
							else {
								flagNumber = false;
								message2.setText("error");
								numberField.setText("");
								break;
							}
						}else {
							if(!numbers.contains(str))
							{
								message2.setText("error");
								flagName = false;
								numberField.setText("");
								break;
							}
							else
								flagName = true;
						}
					}
				}
				else {
					flagNumber = false;
					message2.setText("error");
					numberField.setText("");
				}
				
				temp = ccvField.getText();
				if(temp.length() == 3)
				{
					for(int i = 0; i < temp.length(); i++)
					{
						String str = "" + temp.charAt(i);
						
						if(!numbers.contains(str))
						{
							flagccv = false;
							message3.setText("error");
							ccvField.setText("");
							break;
						}else
							flagccv = true;
					}
				}
				else {
					flagccv = false;
					message3.setText("error");
					ccvField.setText("");
				}
				
				temp = validField.getText();
				if(temp.indexOf("/") != 2)
				{
					flagValid = false;
					message4.setText("error");
					validField.setText("");
				}
				else {
					for(int i = 0; i < temp.length(); i++)
					{
						String str = "" + temp.charAt(i);
						
						if(i != 2)
						{
							if(!numbers.contains(str))
							{
								flagValid = false;
								message4.setText("error");
								validField.setText("");
								break;
							}
							else
								flagValid = true;
						}
					}
				}
				
				temp = savingNameField.getText();
				for(int i = 0; i < temp.length(); i++)
				{
					String str = "" + temp.charAt(i);
					if(!alphabet.contains(str))
					{
						message5.setText("error");
						flagSavingName = false;
						savingNameField.setText("");
						break;
					}
					else 
						flagSavingName = true;
				}
				
				if((flagName == true) && (flagNumber == true) && (flagccv == true) && (flagValid == true) && (flagSavingName == true))
				{
					try {
						card = new CreditCard(nameField.getText(), numberField.getText(), ccvField.getText(), validField.getText(), customer, savingNameField.getText());
						card.cardList(customer);
						nameField.setText("");
						numberField.setText("");
						ccvField.setText("");
						validField.setText("");
						savingNameField.setText("");
						message1.setText("");
						message2.setText("");
						message3.setText("");
						message4.setText("");
						message5.setText("");
						message6.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
					message6.setText("error");
			}
		});
		
		JPanel payingPage = new JPanel();
		payingPage.setBorder(new EmptyBorder(5, 5, 5, 5));
	}
	
	private static String[] changeArraySizePlus(String[] oldArray) {
		return Arrays.copyOf(oldArray, oldArray.length + 1);
	}
}

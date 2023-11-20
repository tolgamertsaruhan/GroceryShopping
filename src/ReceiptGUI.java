import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class ReceiptGUI extends JFrame{

	public ReceiptGUI(Receipt receipt) throws IOException{
		getContentPane().setBackground(new Color(222, 222, 222));
		setTitle("Grocery Shopping - Customer version - Receipt");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 215, 500, 500);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(67, 104, 373, 305);
		getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea(receipt.receiptWrite());
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
		JLabel lblNewLabel = new JLabel("Receipt:");
		lblNewLabel.setBounds(67, 49, 83, 38);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBorder(new LineBorder(new Color(0, 191, 255), 7, true));
		getContentPane().add(lblNewLabel);
		JPanel receiptPage = new JPanel();
		receiptPage.setBorder(new EmptyBorder(5, 5, 5, 5));
		
	}
}

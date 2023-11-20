import java.io.IOException;

public class Receipt {
	private Customer customer;
	private CreditCard card;
	private String cardName;

	public Receipt(CreditCard card, Customer customer, String cardName) {
		this.card = card;
		this.customer = customer;
		this.cardName = cardName;
	}
	
	public String receiptWrite() throws IOException {//all receipt output
		CustomerGUI cstmrG = new CustomerGUI(customer);
		String rtrn1 = "Customer Username: " + customer.getUsername() + "\n"
		+ "Saved Name of Paid Credit Card: " + cardName + "\n"
		+ "Cart:" + "\n";
		String[] rtrn2 = cstmrG.cartText;
		String rtrn3 = "Total amount = " + rtrn2[rtrn2.length - 1] + "\n" + "The order has been successfully completed.";
		String rtrn = rtrn1;
		for(int i = 0; i < rtrn2.length - 1; i++)
			rtrn = rtrn + rtrn2[i] + "\n";
		rtrn = rtrn + rtrn3;
		
		return rtrn;
	}
	
}

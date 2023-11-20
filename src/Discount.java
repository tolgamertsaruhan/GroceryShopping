
public class Discount extends Product{
	private double discount;
	private boolean flag = false;
	
	public Discount() {
		
	}
	
	public double getDiscount(int number, int amount){
		if(number == 1)
		{
			discount = (3.0 / 100.0) * amount;
		}
		else if(number == 2)
		{
			discount = (5.0 / 100.0) * amount;
		}
		
		return discount;
	}

	public double getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}

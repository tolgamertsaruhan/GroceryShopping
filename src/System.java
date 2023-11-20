import javax.swing.JOptionPane;

public class System {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GUI program = new GUI();
		program.setVisible(true);
		JOptionPane.showMessageDialog(program,"Admin profiles are default in this program to prevent anyone from registering as admin." + "\n" + "If you want to visit the admin page, you can use the username: admin, password: 123456 profile.");
	}

}

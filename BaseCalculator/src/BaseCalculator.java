import javax.swing.JFrame;

public class BaseCalculator {
	public static void main(String[] args) {
		createAndShowGUI();
	}
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Base Calculator");
		frame.add(new BasePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 250);
		frame.setVisible(true);
	}
}

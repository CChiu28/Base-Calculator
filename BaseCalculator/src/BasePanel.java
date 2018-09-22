import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
/**
 * 
 * @author Chang Chiu
 *
 * <p>This class provides all the necessary code for the calculator's GUI. There are two JTextArea which allows one to display
 * the equation and the other displays the result of the calculation. An array of both JButton and String are used for efficiency to initiate
 * the button keys for the calculator. This includes the 0-F numerical/hex keys, and all operator keys. 
 * <p>{@link BasePanel#numLayout},  {@link BasePanel#opLayout}, {@link BasePanel#hexLayout} are GridLayout panels used to fit 
 * all the JButton keys. They are then strategically placed around the {@link BasePanel()} BorderLayout for a clean uniform layout.
 * <p>{@link BasePanel#topPanel} is used as a BoxLayout to stack both JTextArea displays on top of each other.
 * <p>{@link BasePanel#base}  is the JSlider used to switch between base 2 to 16. The JTextArea displays will change accordingly.
 * <p>{@link BasePanel#baseLabel} is the label used to let the user know what base the calculator is currently in.
 * <p>{@link #keyChange} - The BasePanel class will call upon this method to enable/disable the JButtons as the base changes.
 * <p>There are also two nested classes for the numerical/hex keys and operator key ActionListeners:
 * <p>{@link NumPerform#NumPerform()} and {@link OpPerform#OpPerform()}
 */
public class BasePanel extends JPanel {
	private BaseCalcState calc = new BaseCalcState();
	private JButton[] digKeys = new JButton[23]; //Array to store all buttons
	private String[] digHex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","+","-","*","/","=","Clr","Mem"}; //labels for buttons
	static JTextArea display; //Shows input and equation results
	static JTextArea display2; //Shows equation
	private JPanel numLayout = new JPanel(), //Panels used for GUI
	opLayout = new JPanel(),
	hexLayout = new JPanel(),
	eastPanel = new JPanel(),
	westPanel = new JPanel(),
	topPanel = new JPanel(),
	southPanel = new JPanel(),
	centerPanel = new JPanel();
	private JSlider base; //For base changing
	private JLabel baseLabel; //To denote base changigng
	private String keyPress; //For ActionListeneres
/**
 * 
 */
	BasePanel() {
		this.setLayout(new BorderLayout()); //Set overall layout
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		numLayout.setLayout(new GridLayout(4,3)); //Initialize grid layouts for button size uniformity
		opLayout.setLayout(new GridLayout(4,2));
		hexLayout.setLayout(new GridLayout(3,2));
		display = new JTextArea("0"); //Set display sizes
		display2 = new JTextArea("0");
		display.setEditable(false); //Set displays to take no keyboard input
		display2.setEditable(false);
		for (int i=0; i<digKeys.length; i++) { //Initialize keys 0-F, and operators
			digKeys[i] = new JButton();
			digKeys[i].setText(digHex[i]); //Set String key labels to buttons
			if (i<16) {
				digKeys[i].addActionListener(new NumPerform()); //Initialize listeners for numerical/hex keys
			} else if (i>=16) {
				digKeys[i].addActionListener(new OpPerform()); //Initialize listeners for operator keys
			}
			if (i>=10&&i<16) { //hex keys disabled by default
				digKeys[i].setEnabled(false);
			}
		}
		for (int i=1; i<=15; i++) { //Add numerical keys
			numLayout.add(digKeys[i]);
		}
		for (int i=0; i==0; i++) { //Add 0 key
			numLayout.add(digKeys[i]);
		}
		for (int i=10; i<=15; i++) { //Add hex keys
			hexLayout.add(digKeys[i]);
		}
		for (int i=16; i<=22; i++) { //Add operator keys
			opLayout.add(digKeys[i]);
		}
		base = new JSlider(2,16,10); //Create slider for base 2-16; Default at base10
		base.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent x) {
				baseLabel.setText("Base: "+((JSlider)x.getSource()).getValue()); //Sets label to whatever base
				keyChange(base.getValue()); //Method to enable/disable keys depending on base
				calc.displayChange(base.getValue()); //Method to change the number in display according to base
			}
		});
		baseLabel = new JLabel();
		baseLabel.setText("Base: "+base.getValue()); //Set default label
		topPanel.add(display); //Add text displays to panel
		topPanel.add(display2);
		eastPanel.add(opLayout); //Add operator panel to panel
		westPanel.add(numLayout); //Add digit key panel
		centerPanel.add(hexLayout); //Add hex key panel
		southPanel.add(baseLabel); //Add label to panel
		southPanel.add(base); //Add slider to panel
		add(topPanel, BorderLayout.NORTH); //Add panels to overall frame panel
		add(eastPanel, BorderLayout.EAST);
		add(westPanel, BorderLayout.WEST);
		add(southPanel, BorderLayout.SOUTH);
		add(centerPanel, BorderLayout.CENTER);
	}
	/**
	 * <b>keyChange</b>
	 * <p>This method is called in the ChangeListener for the JSlider. As soon as there is a ChangeEvent, this method will be called
	 * to reflect the change of base. It will enable and disable the numerical/hex keys accordingly. Hex keys are disabled by default
	 * as the calculator's default state is in base 10.
	 * @param x - This method only takes one parameter which is the value of JSlider base.
	 */
	public void keyChange(int x) { //Method to enable/disable keys for base
		for (int i=15; i>=x; i--) { //Disable keys as base decreases
			digKeys[i].setEnabled(false);
		}
		for (int i=2; i<x; i++) { //Enable keys as base increases
			digKeys[i].setEnabled(true);
		}
	}
	
	/**
	 * <b>NumPerform</b>
	 * <p>This class is the ActionListener for the numerical/hex keys. As each JButton is clicked, the resulting event is the updating
	 * of one of the JTextArea and a call to calc.memNumber method in BaseCalcState,
	 *
	 */
	class NumPerform implements ActionListener { //Listener for numerical/hex keys
		public void actionPerformed (ActionEvent event) {
			if (display.getText().equals("0")) //Empty display field if '0' is present
				display.setText("");
			keyPress = event.getActionCommand(); //Takes string of the current event command
			if (!display.getText().equals("0")) {
				display.append(keyPress); //Appends the button that was pressed to the display
				calc.memNumber(keyPress, base.getValue()); //Calls calc.memNumber method to store current display value
			}
		}
	}
	/**
	 * <b>OpPerform</b>
	 * <p>This class is the ActionListener for the operator keys. As each JButton is pressed, a method in BaseCalcState is called accordingly.
	 * This method uses a String variable keyPress which will get the string of the event command that just occurred.
	 * <p>{@link BaseCalcState#Calculate(String, int)} will be called when operator keys are pressed. Takes keyPress and the value of JSlider base as parameters.
	 * <p>{@link BaseCalcState#equal(int)} will be called when the "=" button is pressed. Takes the JSlider base value as a parameter.
	 * <p>{@link BaseCalcState#clear()} will be called when the "Clr" button is pressed. Takes no parameters.
	 * <p>{@link BaseCalcState#recallMem(int)} will be called when "Mem" is pressed. It takes the JSlider base as a parameter.
	 *
	 */
	class OpPerform implements ActionListener { //Listener for operator keys
		public void actionPerformed (ActionEvent event) {
			keyPress = event.getActionCommand(); //Takes string of the current event command
			if (!keyPress.equals("=")&&!keyPress.equals("Clr")&&!keyPress.equals("Mem")) { //if key pressed is NOT equal or clear, then call calc.Calculate() for operations
				calc.Calculate(keyPress, base.getValue());
			}
			else if (keyPress.equals("=")) { //Call this if equal is pressed to do calculation
				calc.equal(base.getValue());
			}
			else if (keyPress.equals("Clr")) { //Call this if clear is pressed to clear all BaseCalcState variables
				calc.clear();
			}
			else if (keyPress.equals("Mem")) { //Call this if mem is pressed to cycle through the results of calculations
				calc.recallMem(base.getValue());
			}
		}
	}
}

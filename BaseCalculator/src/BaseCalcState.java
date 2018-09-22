import java.util.ArrayList;

/**
 * <b>BaseCalcState</b>
 * <p>This class contains all the necessary methods to ensure correct calculations of values. It holds two values (value1 and value2) for a simple
 * calculation. It stores the result into an ArrayList for memory usage.
 * opCheck is used as a flag in {@link #Calculate(String, int)} for the operator keys to determine which operation is required. memNumber is a string variable that
 * is used in {@link #displayChange(int)} to store the incoming String command in the JSlider event and converts to an int.
 * There are six methods in this class:
 * <p>{@link #memNumber(String, int)} - This method is called whenever a numerical/hex key is pressed. It updates the value variable
 * each time a new numerical/hex key is pressed.
 * <p>{@link #displayChange(int)} - This method is called when the JSlider is moved to change base. It updates the display field
 * accordingly to reflect the change of base.
 * <p>{@link #Calculate(String, int)} - This method is called when an operator key that is NOT equal or Clr is pressed. It flags
 * the corresponding operator for final calculation.
 * <p>{@link #equal(int)} - This method is called when equal is pressed. It conducts the actual calculation based on the operator
 * that was pressed previously.
 * <p>{@link #clear()} - This method is called when Clr is pressed. It sets all variables excluding the result array to 0.
 * <p>{@link #recallMem(int)} - This method is called when Mem is pressed. It will recall the results of previous calculations beginning
 * with the most recent one.
 */
public class BaseCalcState {
	private int opCheck, value1, value2, memCheck; // value of current calculation
	private ArrayList<Integer> result; //Holds the results in an arraylist for future use
	private String memNumber; //Variable used for "history" purposes
	BaseCalcState() { //Constructor
		value1 = 0; //Variable for first input
		value2 = 0; //Variable for second input
		opCheck = 0; //Variable for operator flag
		memCheck = 0; //Variable for result ArrayList
		memNumber = "0"; //String used to update text display
		result = new ArrayList<Integer>(); //ArrayList to hold the results of calculations
	}
	/**<b>memNumber</b>
	 * This method is called whenever a numerical/hex key is pressed. The ActionListener retrieves the String representation of the key that
	 * is pressed and sends that as a parameter to this method.
	 * @param s - The string representation of the key in the ActionListener. It is converted to an int via the memNumber string variable.
	 * @param base - The base from the JSlider is used when converting the variable memNumber to an int.
	 */
	void memNumber(String s, int base) { //Continuously adds each button input to string variable for history memory
		if (memNumber.equals("0")) //Clear string if only '0'
			memNumber="";
		memNumber+=s; //Concats incoming string input to current existing string value
		if (opCheck==0)
			value1 = Integer.parseInt(memNumber, base); //Stores in this variable if first input
		else value2 = Integer.parseInt(memNumber, base); //Otherwise store in second
	}
	/**
	 * This method is called in the JSlider ChangeListener. As a ChangeEvent occurs, this method is called and the text in the display
	 * field will update accordingly to reflect the base change.
	 * @param base - It takes an int parameter from the value of the JSlider.
	 */
	void displayChange(int base) { //Changes the value in display textfield to reflect base
		if (opCheck==5) {
			BasePanel.display.setText(Integer.toString(result.get(result.size()-1), base)); //Updates result according to base change
		}
		else if (!BasePanel.display.equals("")&&opCheck==0) {
			BasePanel.display.setText(Integer.toString(value1, base)); //If opCheck hasn't been flagged yet, display the first value
		} else if (!BasePanel.display.equals("")&&opCheck>0) {
			BasePanel.display.setText(Integer.toString(value2, base)); //Otherwise display second value
		}
	}
	/**
	 * This method is called whenever an operator key NOT equal or Clr is called. The String representation of the operator key is taken
	 * abd updated in the text display accordingly through a switch case. An int variable is used as a flag variable to determine
	 * what operator is required.
	 * @param key - The String representation of the key pressed in the {@link BasePanel#NumPerform} ActionListener.
	 * @param base - The value of the JSlider base.
	 */
	void Calculate(String key, int base) {
		if (!BasePanel.display.getText().equals("")) {
			switch(key) { //switch case takes the string representation of the operator keys
			case "+":
				opCheck = 1; //Sets opCheck accordingly to determine operation needed
				break;
			case "-":
				opCheck = 2;
				break;
			case "*":
				opCheck = 3;
				break;
			case "/":
				opCheck = 4;
				break;
			}
			BasePanel.display.setText("");
			if (BasePanel.display2.getText().equals("0")) {
				BasePanel.display2.setText("");
			}
			BasePanel.display2.append(Integer.toString(value1, base)+key); //Appends second display to show equation
			memNumber = ""; //Resets string variable for next value input
		}
	}
	/**
	 * This method is called when equal is pressed. It does the actual calculation based on the variable opCheck used in {@link #Calculate(String, int)}.
	 * It stores the answer in the {@link #result} ArrayList for memory usage.
	 * @param base - The value of the JSlider base.
	 */
	void equal(int base) {
		if (opCheck!=0) {
			switch(opCheck) { //Uses opCheck to determine which operation is needed
			case 1: result.add(value1+value2);
					break;
			case 2: result.add(value1-value2);
					break;
			case 3: result.add(value1*value2);
					break;
			case 4: 
				if (value2!=0) {
					result.add(value1/value2);
				}
				else BasePanel.display.setText("Error: Cannot divide by zero");
				break;
			}
			BasePanel.display2.setText(BasePanel.display2.getText()+BasePanel.display.getText()); //Displays full equation in second display
			BasePanel.display.setText(Integer.toString(result.get(result.size()-1), base)); //Displays answer in first display
		}
		opCheck = 5; //Used to update result base change in displayChange()
	}
	/**
	 * This method is called when Clr is pressed. It sets all int variables to 0.
	 */
	void clear() {
		value1 = value2 = opCheck = 0;
		memNumber = "0";
		BasePanel.display.setText("0");
		BasePanel.display2.setText("0");
	}
	/**
	 * This method is called when the Mem key is pressed. It uses a variable {@link #memCheck} to determine how the ArrayList will go
	 * through its elements. It catches an Array index exception if Mem is continuously pressed.
	 * @param base - This is the value of the JSlider base
	 */
	void recallMem(int base) {
		try {
		BasePanel.display.setText(Integer.toString(result.get(result.size()-1-memCheck), base)); //Cycles through arraylist starting with most recent element
		memCheck++;
		} catch (ArrayIndexOutOfBoundsException e) { clear(); }; //Throws exception if memCheck exceeds size of arraylist
	}
}

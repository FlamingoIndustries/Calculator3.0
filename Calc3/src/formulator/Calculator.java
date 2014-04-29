package formulator;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;


public class Calculator {
	public  HashMap<String, FormulaElement> formulas;
	
	public Calculator()
	{
		formulas=new HashMap<String, FormulaElement>();
	}
}

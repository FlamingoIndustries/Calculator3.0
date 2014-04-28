package formulator;
import java.util.HashMap;
import java.util.Vector;

//shouldn't catch the minus

public class Main {
	public static HashMap<String, FormulaElement> formulas = new HashMap<String, FormulaElement>();
	
	public static void main(String[] args){
		//Sample input Strings to try:
		//"Y^3-6X(Z+5(Y+2^2))"
		//"3 + 4.6 + cos(1)"
		
		//testing PARSEFORMULA
		String sampleInput1= "-1+3";
		String sampleInput2 = "(2.3 + X + 4.5 + 3X)(2X - (Y^3 + 7) + cos(2^X))";
		FormulaElement test = FormulaElement.parseFormula(sampleInput2);
		if(test!=null)
			System.out.println("Parsed formula: "+test.toString());
		else
			System.out.println("String wasn't parsed correctly");
	
		//testing BASIC EVALUATION
		FormulaElement evalEx = FormulaElement.parseFormula("2x+2+x^2-y");
		int x_val=2;
		int y_val=3;
		evalEx.setVariableValue("x", x_val);
		evalEx.setVariableValue("y", y_val);
		System.out.println("Evaluating: "+evalEx.toString());
		System.out.println("Fully grounded: "+evalEx.isFullyGrounded());
		if(!evalEx.isFullyGrounded())
			System.out.println("Not all variables in the formula have been assigned a value");
		else
			System.out.println("Evaluation: "+evalEx.evaluate());
		

		Vector<String> variables = evalEx.identifyVars();
		for(String var: variables){
			evalEx.setVariableValue(var, 2);
		}
		System.out.println("Eval: "+evalEx.evaluate());
		
		
		//INPUT PARSING
		String input = "h(x, y) = y(2x)";
		formulas.put(input.substring(0,1), FormulaElement.parseInitialFormula(input));
		//System.out.println(formulas.get("h"));
		
		//testing ADVANCED EVALUATION
		FormulaElement F = FormulaElement.parseFormula("y(2x)");
		formulas.put("f", F);
		FormulaElement G = FormulaElement.parseFormula("x+3");
		formulas.put("g", G);
		String input2 = "f(x=g(3) y=2)";
		System.out.println("Evaluation: "+EvalFormula.evaluateFor(input2));
		
		//PARTIAL EVALUATION for derivatives
		G.setDValue("x", new MultipleFunctionElement(new VariableElement("y"), new ConstantElement(4)));
		System.out.println(G.dEval());
		
		ReadWriteFormulae.WriteFormulae();
		System.out.println(ReadWriteFormulae.ReadFormulae());
	}
}

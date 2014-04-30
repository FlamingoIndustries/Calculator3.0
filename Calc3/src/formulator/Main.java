package formulator;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.widgets.Display;

public class Main
{
	public static void main(String[] args){
//		try {
//			Display display = Display.getDefault();
//			CalculatorUI shell = new CalculatorUI(display);
//			shell.open();
//			shell.layout();
//			while (!shell.isDisposed()) {
//				if (!display.readAndDispatch()) {
//					display.sleep();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
		

//		}		
//		FormulaElement form=FormulaElement.parseFormula("(x-1)/(y+3)", new HashMap<String, FormulaElement>(), false);
//		Differentiation diff=new Differentiation();
//		System.out.println(form.symbolicDiff("x", 1).getXMLformat(""));
//		System.out.println(form.symbolicDiff("x", 1));
//		Calculator calc=new Calculator();
//		System.out.println(calc.branch("save"));
//		System.out.println(calc.branch("load"));
//		EvalFormula ev=new EvalFormula();
//		//Sample input Strings to try:
//		//"Y^3-6X(Z+5(Y+2^2))"
//		//"3 + 4.6 + cos(1)"
//		Calculator calc=new Calculator();
		
		//testing PARSEFORMULA
//		String sampleInput1= "f(x)+3";
//		String sampleInput2 = "(2.3 + X + 4.5 + 3X)(2X - (Y^3 + 7) + cos(2^X))";
//		FormulaElement test = FormulaElement.parseFormula(sampleInput1);
//		if(test!=null)
//			System.out.println("Parsed formula: "+test.toString());
//		else
//			System.out.println("String wasn't parsed correctly");
//	
//		//testing BASIC EVALUATION
//		FormulaElement evalEx = FormulaElement.parseFormula("2x+2+x^2-y");
//		int x_val=2;
//		int y_val=3;
//		evalEx.setVariableValue("x", x_val);
//		evalEx.setVariableValue("y", y_val);
//		System.out.println("Evaluating: "+evalEx.toString());
//		System.out.println("Fully grounded: "+evalEx.isFullyGrounded());
//		if(!evalEx.isFullyGrounded())
//			System.out.println("Not all variables in the formula have been assigned a value");
//		else
//			System.out.println("Evaluation: "+evalEx.evaluate());
//		
//
//		Vector<String> variables = evalEx.identifyVars();
//		for(String var: variables){
//			evalEx.setVariableValue(var, 2);
//		}
//		System.out.println("Eval: "+evalEx.evaluate());
//		
//		
//		//INPUT PARSING
//		String input = "h(x, y) = y(2x)";
//		calc.formulas.put(input.substring(0,1), FormulaElement.parseInitialFormula(input));
//		//System.out.println(formulas.get("h"));
//		
//		//testing ADVANCED EVALUATION
//		FormulaElement F = FormulaElement.parseFormula("y(2x)");
//		calc.formulas.put("f", F);
//		FormulaElement G = FormulaElement.parseFormula("x+3");
//		calc.formulas.put("g", G);
//		String input2 = "f(x=g(3) y=2)";
//		System.out.println("Evaluation: "+ev.evaluateFor(input2));
//		
//		//PARTIAL EVALUATION for derivatives
//		G.setDValue("x", new MultipleFunctionElement(new VariableElement("y"), new ConstantElement(4)));
//		G.setDValue("x", new MultipleFunctionElement(new VariableElement("x"), new ConstantElement(4)));
//		System.out.println(G.dEval());
//		//ReadWriteFormulae.WriteFormulae();
//		//System.out.println(ReadWriteFormulae.ReadFormulae());
	}
}

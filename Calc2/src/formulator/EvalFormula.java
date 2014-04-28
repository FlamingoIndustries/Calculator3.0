package formulator;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public class EvalFormula {
	
	//parser
	public static double evaluateFor(String input){
		//find formula element and identify its variables
		FormulaElement formula = Main.formulas.get(input.substring(0, 1));
		Vector<String> varKeys = formula.identifyVars();
		HashMap<String, Double> vars = new HashMap<String, Double>();
		for(String key: varKeys)
			vars.put(key, null);
		//chop off first part of the string input so the parsing bit starts after the open bracket
		String toParse = input.substring(2);
		//create a string tokenizer to identify variables, equal signs, and variable values
		Vector<String> tokens = new Vector<String>();
		StringTokenizer tokenizer = new StringTokenizer(toParse, "(= ),", true);
		while(tokenizer.hasMoreTokens()){
			String currToken = tokenizer.nextToken();
			if(!currToken.equals(" "))
				tokens.add(currToken);
		}

		//evaluating a function with a SINGLE VARIABLE - only one value to assign
		//System.out.println(formula.variables.keySet());
		if(vars.size()==1){
			//id the only key
			String key = vars.keySet().iterator().next();
			//with a nested formula in the brackets
			if(tokens.size()>2){
				String recInput=toParse.substring(0, 4);
				double varValue = EvalFormula.evaluateFor(recInput);
				vars.put(key, varValue);
			}
			else{
			//with just a single value in the brackets
				vars.put(key, Double.parseDouble(tokens.elementAt(0)));
			}
		}
		
		//evaluating a function with MULTIPLE VARIABLES
		else{
			//used to indicate that the next token will be a variable value
			boolean next=false;
			//to indicate what variable the next value belongs to; necessary as variables may be out of order
			String varKey="";
			for(int i=0; i<tokens.size(); i++){
				String token=tokens.get(i);
				if(token.equals("="))
					next=true;
				//assign a value to the current variable
				else if(next){
					//recursion
					if(isVariable(token)){
						String recInput = token;
						recInput+=tokens.remove(i+1);
						recInput+=tokens.remove(i+1);
						recInput+=tokens.remove(i+1);
						double varValue = EvalFormula.evaluateFor(recInput);
						vars.put(varKey, varValue);
					}
					else
						vars.put(varKey, Double.parseDouble(tokens.elementAt(i)));
					next=false;
				}
				//if token is a variable, identify which one so the correct value can be assigned
				else if(isVariable(token))
					varKey = token;
				else if(tokens.equals(")"))
					break;
			}
		}
		
		//testing that variables were assigned the correct values
//		for(String key: vars.keySet()){
//			System.out.print(key+": ");
//			System.out.println(vars.get(key));
//		}
		
		//assign respective values to all variables in the formula using the created variables vector
		for(String key: vars.keySet()){
			formula.setVariableValue(key, vars.get(key));
		}
		return formula.evaluate();
	}
	
	
	public static boolean isVariable(String s){
		if(s.equals("cos") || s.equals("sin"))
			return false;
		char[] chars = s.toCharArray();
		for(char c: chars){
			if(!Character.isLetter(c))
				return false;
		}
		return true;
	}

}

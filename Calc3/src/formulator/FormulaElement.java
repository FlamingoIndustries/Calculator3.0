/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public abstract class FormulaElement
{	
	public abstract double evaluate();
	public abstract FormulaElement partialEval();
	public abstract String getXMLformat(String tabbing);
	public abstract FormulaElement symbolicDiff(String respect, int degree);
	public abstract FormulaElement getSimplifiedCopy();
	
	//assigns the specified value to all instances of the specified variable in the formula by recursively searching
	public void setVariableValue(String varName, double value){
		if(this instanceof VariableElement){
			VariableElement current = (VariableElement) this;
			if(current.getName().equals(varName)){
				current.setValue(value);
			}
		}
		else if(this instanceof FunctionElement){
			for(FormulaElement elem: ((FunctionElement)this).getArguments()){
				elem.setVariableValue(varName, value);
			}
		}
	}
	
	public void unSetValue(String varName){
		if(this instanceof VariableElement){
			VariableElement current = (VariableElement) this;
			if(current.getName().equals(varName)){
				current.unSetValue();
			}
		}
		else if(this instanceof FunctionElement){
			for(FormulaElement elem: ((FunctionElement)this).getArguments()){
				elem.unSetValue(varName);
			}
		}
	}
	
	public void setPartialValue(String varName, FormulaElement value){
		if(this instanceof VariableElement){
			VariableElement current = (VariableElement) this;
			if(current.getName().equals(varName)){
				current.setPartialValue(value);
			}
		}
		else if(this instanceof FunctionElement){
			for(FormulaElement elem: ((FunctionElement)this).getArguments()){
				elem.setPartialValue(varName, value);
			}
		}
	}

	//identify all the variables in a formula and put them in the variables hash map
	public Vector<String> identifyVars(){
		Vector<String> vars = new Vector<String>();
		if(this instanceof VariableElement){
			VariableElement current = (VariableElement) this;
			vars.add(current.getName());
		}
		else if(this instanceof FunctionElement){
			for(FormulaElement elem: ((FunctionElement)this).getArguments()){
				vars.addAll(elem.identifyVars());
			}
		}
		return vars;
	}
	
	//method that checks if all variables in a formula have been assigned a value
	public boolean isFullyGrounded(){
		if(this instanceof VariableElement){
			VariableElement current = (VariableElement) this;
			if(!current.valueAssigned)
				return false;
		}
		else if(this instanceof FunctionElement){
			for(FormulaElement elem: ((FunctionElement)this).getArguments()){
				if(!elem.isFullyGrounded())
					return false;
			}
		}
		return true;
	}
	
	public boolean equals(FormulaElement comp)
	{
		return false;
	}
	
	public VariableElement findVariable(String varName)
	{
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static FormulaElement parseFormula(String formula, HashMap<String, FormulaElement> formulas, boolean symbolic)
	{
		//will chop the string into substring tokens wherever it sees a delimiter
		StringTokenizer tokenizer = new StringTokenizer(formula, "+-/()^* \t'", true);
		Vector tokens = new Vector();
		String token="";

		//put each token except for spaces onto the end of the tokenVector
		while(tokenizer.hasMoreTokens())
		{
			token = tokenizer.nextToken();
			String numToken="";
			if(!token.equals(" ") && !token.equals("\t"))
			{
				char start = token.charAt(0);
				char end = token.charAt(token.length()-1);
				while(Character.isDigit(start)&&Character.isLetter(end)){
					numToken+=token.charAt(0);
					token=token.substring(1);
					start = token.charAt(0);
					end = token.charAt(token.length()-1);
				}
				if(!numToken.isEmpty())
					tokens.add(numToken);
				if(!token.isEmpty())
					tokens.add(token);
			}
			
		}
		
		//testing
		//System.out.println("Initial: "+tokens.toString());

		//LOOKING FOR EVALUATION OR DIFFERENTIATION
		for(int i=0; i<tokens.size(); i++){
			String key = (String) tokens.get(i);
			String input="";
			if(formulas.containsKey(key)){
				input += (String)tokens.remove(i);
				if(i!=tokens.size()){
					String current = (String) tokens.get(i);
					input+=current;
					int degree=0;
					String respect="";
					while(current.equals("'")){
						degree++;
						current = (String) tokens.remove(i);
						input+=current;
					}
					if(current.equals("(")){
						current=(String)tokens.remove(i);
						while(!current.equals(")")){
							if(!current.equals("(")){
								respect+=current;
							}
							current = (String) tokens.remove(i);
							input+=current;
						}
						if(degree!=0){
							FormulaElement temp;
							if(symbolic)
								temp = formulas.get(key).symbolicDiff(respect, degree-1);
							else
								temp = formulas.get(key).numericDiff(respect, degree-1);
							tokens.add(i, temp);
						}
						else{
							if(i!=tokens.size()){
								if(((String)tokens.get(i)).equals(")"))
									input+=tokens.remove(i);
							}
							FormulaElement result;	
							result = evaluateFor(input, formulas);
							tokens.add(i, result);	
						}
					}
					else{
						tokens.add(i, formulas.get(key));
					}
				}
				else{
					tokens.add(i, formulas.get(key));
				}
			}
		}
		
		//1st pass: convert integers to constant elements and variables to variable elements
		for(int i=0; i<tokens.size(); i++){
			if(tokens.get(i) instanceof String){
				String current = (String) tokens.get(i);
				if(Character.isDigit(current.charAt(0))){
					//check for double dots; if so the formula is badly formed
					if(current.contains("..")){
						System.out.println("There are two dots between numbers; badly formed decimal.");
						return null;
					}
					tokens.remove(i);
					tokens.add(i, new ConstantElement(Double.parseDouble(current)));
				}
				else if(Character.isLetter(current.charAt(0))&&!current.equals("cos")&&!current.equals("sin")&&!current.equals("abs")){
					tokens.remove(i);
					tokens.add(i, new VariableElement(current));
				}
			}
		}
		//Testing
		//System.out.println("1st pass: "+tokens.toString());
				
		//passes 2-6 should happen recursively for each formula section in parentheses
		
		return parseFormulaTwo(tokens);
	}
		
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static FormulaElement parseFormulaTwo(Vector tokens){
		
		//2nd pass: convert adjacent variable/constant elements to multiple function elements
		for(int i=0; i<tokens.size()-1; i++){
			if(tokens.get(i) instanceof FormulaElement && tokens.get(i+1) instanceof FormulaElement)
			{
				FormulaElement arg1 = (FormulaElement) tokens.remove(i);
				FormulaElement arg2 = (FormulaElement) tokens.remove(i);
				tokens.add(i, new MultipleFunctionElement(arg1, arg2));
				i--;
			}
		}
		//testing
		//System.out.println("2nd pass: "+tokens.toString());
		
		//3rd pass: calling parseFunction recursively for all sections in parentheses
		//parseFunction will return a FormulaElement, which will replace the section in parentheses in tokens
		Vector<Object> temp = new Vector<Object>(); //stores the elements in parentheses to be passed to parseFunction
		int brackets=0;
		for(int i=0; i<tokens.size(); i++)
		{
			if(tokens.get(i).equals("("))
				brackets++;
			if(tokens.get(i).equals(")"))
			{
				brackets--;
				//if the brackets are matched a complete section in parentheses has been found
				//all the elements in parentheses will be in the temp vector, which is then passed to parseFunction
				//the FormulaElement that is returned is then added to the tokens vector
				if(brackets==0)
				{
					tokens.remove(i);
					temp.remove(0);
					FormulaElement returnValue = parseFormulaTwo(temp);
					tokens.add(i, returnValue);
					temp.clear();
				}
			}
			if(brackets>0)
			{
				//take elements in parentheses out of tokens and add them to temp to be passed to parseFunction
				temp.add(tokens.remove(i));
				i--;
			}
		}
		//testing
		//System.out.println("3rd pass: "+tokens.toString());
		
		//4th pass: finding sin, cosine and absolute value functions and using the next formula element as the argument
		//the part in brackets has already been reduced to one formula element; the item after sin/cos/abs in tokens
		for(int i=0; i<tokens.size(); i++)
		{
			if(tokens.get(i).equals("sin") || tokens.get(i).equals("cos") || tokens.get(i).equals("abs")) 
			{
				String type = (String) tokens.remove(i);
				FormulaElement arg = (FormulaElement) tokens.remove(i);
				FormulaElement func;
				if(type.equals("cos"))
					func = new CosineFunctionElement(arg);
				else if(type.equals("sin"))
					func = new SineFunctionElement(arg);
				else
					func = new AbsValueFunctionElement(arg);
				tokens.add(i, func);
			}
		}
		//testing
		//System.out.println("4th pass: "+tokens.toString());
		
		//5th pass: find powers and replace the symbol and its 2 arguments with a power function element
		for(int i=0; i<tokens.size()-1; i++)
		{
			if(tokens.get(i+1).equals("^"))
			{
				tokens.remove(i+1);
				FormulaElement arg1 = (FormulaElement) tokens.remove(i);
				FormulaElement arg2 = (FormulaElement) tokens.remove(i);
				tokens.add(i, new PowerFunctionElement(arg1, arg2));
				i--;
			}
		}
		//testing
		//System.out.println("5th pass: "+tokens.toString());
		
		//6th pass: find multiplication and division and replace with appropriate function elements
		for(int i=1; i<tokens.size(); i++){
			if(tokens.get(i-1)instanceof FormulaElement && tokens.get(i)instanceof FormulaElement){
				FormulaElement arg1=(FormulaElement)tokens.remove(i-1);
				FormulaElement arg2=(FormulaElement)tokens.remove(i-1);
				tokens.add(i-1, new MultipleFunctionElement(arg1, arg2));
				i--;
			}
			else if(tokens.get(i).equals("*")){
				tokens.remove(i);
				FormulaElement arg1=(FormulaElement)tokens.remove(i-1);			
				FormulaElement arg2=(FormulaElement)tokens.remove(i-1);
				tokens.add(i-1, new MultipleFunctionElement(arg1,arg2));
				i--;
			}
			else if(tokens.get(i).equals("/")){
				tokens.remove(i);
				FormulaElement arg1=(FormulaElement)tokens.remove(i-1);			
				FormulaElement arg2=(FormulaElement)tokens.remove(i-1);
				tokens.add(i-1, new DivideFunctionElement(arg1,arg2));
				i--;
			}
		}
		//testing
		//System.out.println("6th pass: "+tokens.toString());
		
		//7th pass: find addition and subtraction and replace with appropriate function elements
		for(int i=0; i<tokens.size()-1; i++){
			if(tokens.get(i).equals("+")){
				tokens.remove(i);
				FormulaElement arg1=(FormulaElement)tokens.remove(i-1);
				FormulaElement arg2=(FormulaElement)tokens.remove(i-1);
				tokens.add(i-1, new PlusFunctionElement(arg1, arg2));
				i--;
			}
			else if(tokens.get(i).equals("-") && i==0){
				tokens.remove(i);
				FormulaElement arg1=(FormulaElement)tokens.remove(i);
				if(arg1 instanceof ConstantElement)
					tokens.add(i, new ConstantElement(0-((ConstantElement)arg1).getValue()));
				else
					tokens.add(i, new MinusFunctionElement(new ConstantElement(0), arg1));
			}
			else if(tokens.get(i).equals("-")){
				tokens.remove(i);
				FormulaElement arg1=(FormulaElement)tokens.remove(i-1);
				FormulaElement arg2=(FormulaElement)tokens.remove(i-1);
				tokens.add(i-1, new MinusFunctionElement(arg1, arg2));
				i--;
			}
		}
		if(!tokens.isEmpty())
			return (FormulaElement) tokens.get(0);
		else
			return null;
	}
	
	//This method estimates the differentiate of the formula by using (f(x+dx)-f(x))/dx
	public FormulaElement numericDiff(String respect, int degree)
	{
		if(degree<1)		//Stop recursion if degree is less than 1
			return this;
		ConstantElement dx=new ConstantElement(0.0000001);
		FormulaElement replace=new PlusFunctionElement(dx, new VariableElement(respect));
		this.setPartialValue(respect, replace);
		MinusFunctionElement minus=new MinusFunctionElement(this.partialEval(),this);
		DivideFunctionElement div=new DivideFunctionElement(minus,dx);
		return div.numericDiff(respect, degree-1); //Return the differentiate of the result with 1 lower degree
	}
	
	//main functionality is parsing input for which values are assigned to which variables
	public static FormulaElement evaluateFor(String input, HashMap<String, FormulaElement> formulas){
		//find formula element and identify its variables
		FormulaElement formula = formulas.get(input.substring(0, 1));
		Vector<String> varKeys = formula.identifyVars();
		HashMap<String, FormulaElement> vars = new HashMap<String, FormulaElement>();
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
		if(vars.size()==1){
			//id the only key
			String key = vars.keySet().iterator().next();
			//with a nested formula in the brackets
			if(tokens.size()>2){
				String recInput=toParse.substring(0, 4);
				FormulaElement varValue = evaluateFor(recInput, formulas);
				if(!(varValue instanceof ConstantElement))
					formula.unSetValue(key);
				vars.put(key, varValue);
			}
			else{
				//with just a single value in the brackets, either a number or a formula element
				FormulaElement value = FormulaElement.parseFormula(tokens.get(0), formulas, true);
				if(!(value instanceof ConstantElement))
					formula.unSetValue(key);
				vars.put(key, value);
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
					if(formulas.containsKey(varKey)){
						String recInput = token;
						recInput+=tokens.remove(i+1);
						recInput+=tokens.remove(i+1);
						recInput+=tokens.remove(i+1);
						FormulaElement varValue = evaluateFor(recInput, formulas);
						if(!(varValue instanceof ConstantElement))
							formula.unSetValue(varKey);
						vars.put(varKey, varValue);
					}
					else{
						FormulaElement varValue = FormulaElement.parseFormula(tokens.get(i), formulas, true);
						if(!(varValue instanceof ConstantElement))
							formula.unSetValue(varKey);
						vars.put(varKey, varValue);
					}
					next=false;
				}
				//if token is a variable, identify which one so the correct value can be assigned
				else if(isVariable(token))
					varKey = token;
				else if(tokens.equals(")"))
					break;
			}
		}
		
		//assign respective values to all variables in the formula using the created variables vector
		for(String key: vars.keySet()){
			if(vars.get(key)!=null){
				if(vars.get(key) instanceof ConstantElement)
					formula.setVariableValue(key, ((ConstantElement)vars.get(key)).getValue());
				else
					formula.setPartialValue(key, vars.get(key));
			}
		}
		if(formula.isFullyGrounded())
			return new ConstantElement(formula.evaluate());
		return formula.partialEval();
		
	}
	
	public static boolean isVariable(String s){
		if(s.equals("cos") || s.equals("sin") || s.equals("abs"))
			return false;
		char[] chars = s.toCharArray();
		for(char c: chars){
			if(!Character.isLetter(c))
				return false;
		}
		return true;
	}
}


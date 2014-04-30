package formulator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public abstract class FormulaElement
{	
	public abstract double evaluate();
	public abstract FormulaElement dEval();
	public abstract String getXMLformat(String tabbing);
	//TO DO
	//parse for nested formulas in formula declaration
	
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
	
	public void setDValue(String varName, FormulaElement value){
		if(this instanceof VariableElement){
			VariableElement current = (VariableElement) this;
			if(current.getName().equals(varName)){
				current.setdVal(value);
			}
		}
		else if(this instanceof FunctionElement){
			for(FormulaElement elem: ((FunctionElement)this).getArguments()){
				elem.setDValue(varName, value);
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
	
	@SuppressWarnings("unchecked")
	public static FormulaElement parseFormula(String formula, HashMap<String, FormulaElement> formulas)
	{
		//will chop the string into substring tokens wherever it sees a delimiter
		StringTokenizer tokenizer = new StringTokenizer(formula, "+-/()^ \t'", true);
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
		//Used for debugging:
		//System.out.println("Initial: "+tokens.toString());
		
		//Before continuing, perform some basic checks to ensure that the formula has a proper format
		if(!checkFormula(tokens))
			return null;
		
		
		
		//FORMULAS USED TO DEFINE FORMULAS
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
						tokens.remove(i);
						while(!current.equals(")")){
							if(!current.equals("(")){
								respect+=current;
								input+=current;
							}
							current = (String) tokens.remove(i);
						}
						if(degree!=0){
							Differentiation diff = new Differentiation();
							FormulaElement temp = diff.symbolicDiff(formulas.get(key), respect, degree);
							tokens.add(i, temp);
						}
						else{
							Double result = EvalFormula.evaluateFor(input, formulas);
							tokens.add(i, new ConstantElement(result));	
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
				else if(Character.isLetter(current.charAt(0))&&!current.equals("cos")&&!current.equals("sin")){
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
		
		//4th pass: finding sin and cosine functions and using the next formula element as the argument
		//the part in brackets has already been reduced to one formula element; the item after sin/cos in tokens
		for(int i=0; i<tokens.size(); i++)
		{
			if(tokens.get(i).equals("sin") || tokens.get(i).equals("cos"))
			{
				String type = (String) tokens.remove(i);
				FormulaElement arg = (FormulaElement) tokens.remove(i);
				FormulaElement func;
				if(type.equals("cos"))
					func = new CosineFunctionElement(arg);
				else
					func = new SineFunctionElement(arg);
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
	
	//This method returns false if the formula doesn't have the correct format
	public static boolean checkFormula(Vector tokens){
		
		//1st check: For brackets that don't match up
		int openBrackets=0;
		int closeBrackets=0;
		for(int i=0; i<tokens.size(); i++){
			if(tokens.get(i).equals("("))
				openBrackets++;
			else if(tokens.get(i).equals(")")){
				closeBrackets++;
				if(closeBrackets>openBrackets){
					System.out.println("Closing bracket appears before opening bracket.");
					return false;
				}
			}
		}
		if(openBrackets!=closeBrackets){
			System.out.println("The brackets in this formula aren't matched.");
			return false;
		}
		
		//2nd check: Symbols that occur twice in a row
		for(int i=0; i<tokens.size()-1; i++){
			if(tokens.get(i).equals(tokens.get(i+1)) && !tokens.get(i).equals(")") && !tokens.get(i).equals("(")){
				System.out.println("Badly formed formula; a symbol occurs twice.");
				return false;
			}
		}
		
		//3rd check: Incomplete formulas
		for(int i=0; i<tokens.size()-1; i++){
			if(tokens.get(i).equals("(") || i==0){
				String cur;
				if(i==0)
					cur = (String) tokens.get(i);
				else
					cur = (String) tokens.get(i+1);
				if(!Character.isDigit(cur.charAt(0)) && !Character.isLetter(cur.charAt(0)) && !cur.equals("(") && !cur.equals("sin") && !cur.equals("cos")){
					System.out.println("Badly formed formula.");
					return false;
				}
			}
			else if(tokens.get(i+1).equals(")") || i==tokens.size()-2){
				String cur;
				if(i==tokens.size()-2)
					cur = (String) tokens.get(i+1);
				else
					cur = (String) tokens.get(i);
				if(!Character.isDigit(cur.charAt(0)) && !Character.isLetter(cur.charAt(0)) && !cur.equals(")")){
					System.out.println("Badly formed formula.");
					return false;
				}
			}
		}
		
		return true;
	}
	
	public Boolean equals(FormulaElement comp)
	{
		return false;
	}
	
	public VariableElement findVariable(String varName)
	{
		return null;
	}
	
	public static void main(String[] args){
		HashMap<String, FormulaElement> formulas = new HashMap<String, FormulaElement>();
		formulas.put("f", new PlusFunctionElement(new VariableElement("x"), new ConstantElement(3)));
		formulas.put("g", new PlusFunctionElement(new VariableElement("y"), new ConstantElement(2)));
		FormulaElement test = FormulaElement.parseFormula("(f)(g)", formulas);
		MultipleFunctionElement test2 = new MultipleFunctionElement();
		VariableElement x = new VariableElement("x");
		ConstantElement zero = new ConstantElement(3);
		ConstantElement zero2 = new ConstantElement(4);
		test2.addArgument(zero);
		test2.addArgument(zero2);
		System.out.println(test.toString());
	}
}


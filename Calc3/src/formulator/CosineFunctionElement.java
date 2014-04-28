package formulator;

public class CosineFunctionElement extends FunctionElement{
	
	//constructor that allows argument to be added immediately
	public CosineFunctionElement(FormulaElement arg){
		addArgument(arg);
	}
	
	//empty constructor; argument can be added manually
	public CosineFunctionElement(){	
	}
	
	//override addArgument method so that you can't add more than 1 argument
	public void addArgument(FormulaElement arg){
		if(arguments.size()==1)
			System.out.println("You can't add any more arguments; the cosine function only accepts one");
		else
			arguments.add(arg);
	}
	
	public String toString(){
		String retString = "cos(" + getArguments().get(0).toString() +")";
		return retString;
	}
	
	public double evaluate(){
		return Math.cos(getArguments().get(0).evaluate());
	}

	@Override
	public FormulaElement dEval() {
		return new CosineFunctionElement(getArguments().get(0).dEval());
	}
	
}

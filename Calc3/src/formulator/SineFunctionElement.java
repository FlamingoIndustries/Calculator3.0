package formulator;

public class SineFunctionElement extends FunctionElement {
	
	//constructor that allows argument to be added immediately
	public SineFunctionElement(FormulaElement arg){
		addArgument(arg);
	}
	
	//empty constructor; argument can be added manually
	public SineFunctionElement(){
	}
	
	//override addArgument method so that you can't add more than 1 argument
	public void addArgument(FormulaElement arg){
		if(arguments.size()==1)
			System.out.println("You can't add any more arguments; the cosine function only accepts one");
		else
			arguments.add(arg);
	}
	
	public String toString(){
		String retString = "sin(" + getArguments().get(0).toString() +")";
		return retString;
	}
	
	public double evaluate(){
		return Math.sin(getArguments().get(0).evaluate());
	}

	@Override
	public FormulaElement dEval() {
		return new SineFunctionElement(getArguments().get(0).dEval());
	}
	

}

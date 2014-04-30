package formulator;

import java.util.Vector;

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
	
	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree==0)
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new MultipleFunctionElement();
		FormulaElement first=elements.elementAt(0);
		elem.addArgument(new ConstantElement(-1));
		elem.addArgument(new SineFunctionElement(first));
		elem.addArgument(first.symbolicDiff(respect, degree));
		System.out.println(first);//this.symbolicDiff(first, respect, degree));
		return elem.symbolicDiff(respect, degree-1);
	}
}

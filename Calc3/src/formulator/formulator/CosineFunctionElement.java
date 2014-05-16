/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

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
			System.out.println("Can't have more than one argument.");
		else
			arguments.add(arg);
	}
	
	@Override
	public String toString(){
		if(getArguments().get(0) instanceof ConstantElement)
			return new ConstantElement(evaluate()).toString();
		String retString = "cos(" + getArguments().get(0).toString() +")";
		return retString;
	}
	
	public double evaluate(){
		return Math.cos(getArguments().get(0).evaluate());
	}

	@Override
	public FormulaElement partialEval() {
		return new CosineFunctionElement(getArguments().get(0).partialEval());
	}
	
	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree<1)			//Stop recursion when degree is less than 1
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new MultipleFunctionElement();
		FormulaElement first=elements.elementAt(0);
		elem.addArgument(new ConstantElement(-1));
		elem.addArgument(new SineFunctionElement(first));
		elem.addArgument(first.symbolicDiff(respect, degree));
		return elem.symbolicDiff(respect, degree-1);	//Recursively call function with 1 less degree
	}
	
	@Override
	public FormulaElement getSimplifiedCopy()
	{
		Vector<FormulaElement> v=this.getArguments();
		FormulaElement out=new CosineFunctionElement(v.firstElement().getSimplifiedCopy());
		return out;
	}
}

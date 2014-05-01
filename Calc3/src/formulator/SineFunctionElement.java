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
	
	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree<1)
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new MultipleFunctionElement();
		FormulaElement first=elements.elementAt(0);
		elem.addArgument(new CosineFunctionElement(first));
		elem.addArgument(first.symbolicDiff(respect, degree));
		return elem.symbolicDiff(respect, degree-1);
	}
}

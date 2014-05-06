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

public class AbsValueFunctionElement extends FunctionElement {
	
	//constructor that allows argument to be added immediately
	public AbsValueFunctionElement(FormulaElement arg){
		addArgument(arg);
	}
	
	//empty constructor; argument can be added manually
	public AbsValueFunctionElement(){
	}
	
	//override addArgument method so that you can't add more than 1 argument
	public void addArgument(FormulaElement arg){
		if(arguments.size()==1)
			System.out.println("You can't add any more arguments; the cosine function only accepts one");
		else
			arguments.add(arg);
	}
	
	public String toString(){
		if(getArguments().get(0) instanceof ConstantElement)
			return new ConstantElement(evaluate()).toString();
		String retString = "|" + getArguments().get(0).toString() +"|";
		return retString;
	}
	
	public double evaluate(){
		return Math.abs(getArguments().get(0).evaluate());
	}

	@Override
	public FormulaElement partialEval() {
		return new AbsValueFunctionElement(getArguments().get(0).partialEval());
	}
	
	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree==0)
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FormulaElement first=elements.elementAt(0);
		return new AbsValueFunctionElement(first.symbolicDiff(respect, degree));
	}
	
	@Override
	public FormulaElement getSimplifiedCopy()
	{
		AbsValueFunctionElement out=new AbsValueFunctionElement();
		out.addArgument(this.getArguments().firstElement().getSimplifiedCopy());
		return out;
	}
}

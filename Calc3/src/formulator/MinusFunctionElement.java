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

public class MinusFunctionElement extends FunctionElement{
	
	//constructor that allows two arguments to be added immediately
	public MinusFunctionElement(FormulaElement arg1, FormulaElement arg2){
		addArgument(arg1);
		addArgument(arg2);
	}
	
	//empty constructor; arguments can be added manually
	public MinusFunctionElement(){
	}
	
	//override addArgument method so that you can't add more than 2 arguments
	public void addArguments(FormulaElement arg){
		if(arguments.size()==2)
			System.out.println("The minus function can't have more than 2 arguments");
		else
			arguments.add(arg);
	}
	
	public String toString(){
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		//generate result if both arguments are constants
		if(arg1 instanceof ConstantElement && arg2 instanceof ConstantElement){
			double retNum = ((ConstantElement) arg1).getValue() - ((ConstantElement) arg2).getValue();
			return ""+retNum;
		}
		//create string with minus symbol if 1+ arguments are variables
		else{
			if(arg2 instanceof PlusFunctionElement){
				return arg1.toString() + " - "+"("+arg2.toString()+")";
			}
			return arg1.toString() + " - " + arg2.toString();
		}
	}
	
	public double evaluate(){
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		return arg1.evaluate()-arg2.evaluate();
	}
	
	public FormulaElement dEval(){
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		return new MinusFunctionElement(arg1.dEval(), arg2.dEval());
	}
	
	public boolean equals(FormulaElement comp)
	{
		if(this.getClass().getSimpleName().equals(comp.getClass().getSimpleName()))
		{
			FunctionElement comp1=(FunctionElement) comp;
			Vector<FormulaElement> compelements=comp1.getArguments();
			if(this.getArguments().elementAt(0).equals(compelements.elementAt(0))&&this.getArguments().elementAt(1).equals(compelements.elementAt(1)))
				return true;
		}
		return false;
	}

	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree<1)
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new MinusFunctionElement();
		for(int i=0;i<elements.size();i++)
			elem.addArgument(elements.elementAt(i).symbolicDiff(respect, degree));
		return elem.symbolicDiff(respect, degree-1);
	}
}

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


public class PowerFunctionElement extends FunctionElement {
	
	public PowerFunctionElement(FormulaElement arg1, FormulaElement arg2){
		addArgument(arg1);
		addArgument(arg2);
	}
	
	public PowerFunctionElement(){
	}

	//override addArgument method so that you can't add more than 2 arguments
	public void addArguments(FormulaElement arg){
		if(arguments.size()==2)
			System.out.println("The power function can't have more than 2 arguments");
		else
			arguments.add(arg);
	}
	
	public String toString(){
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		if(arg1 instanceof ConstantElement && arg2 instanceof ConstantElement){
			double retNum = Math.pow(((ConstantElement)arg1).getValue(), ((ConstantElement)arg2).getValue());
			if(retNum%1==0)
				return ""+(int)retNum;
			else
				return ""+retNum;
		}
		else{
			String ret1="", ret2="";
			if(!(arg1 instanceof VariableElement || arg1 instanceof ConstantElement))
				ret1="("+arg1.toString()+")";
			else
				ret1=arg1.toString();
			if(!(arg2 instanceof VariableElement || arg2 instanceof ConstantElement))
				ret2="("+arg2.toString()+")";
			else
				ret2=arg2.toString();
			return ret1+"^"+ret2;
		}
	}
	
	public double evaluate(){
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		return Math.pow(arg1.evaluate(), arg2.evaluate());
	}

	@Override
	public FormulaElement dEval() {
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		return new PowerFunctionElement(arg1.dEval(), arg2.dEval());
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
		if(degree==0)
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new MultipleFunctionElement();
		FormulaElement first=elements.elementAt(0);
		FormulaElement second=elements.elementAt(1);
		elem.addArgument(second);
		MinusFunctionElement minus=new MinusFunctionElement(second,new ConstantElement(1));
		PowerFunctionElement pow=new PowerFunctionElement(first,minus);
		elem.addArgument(pow);
		return elem.symbolicDiff(respect, degree-1);
	}
}

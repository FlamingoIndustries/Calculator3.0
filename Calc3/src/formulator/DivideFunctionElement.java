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

public class DivideFunctionElement extends FunctionElement{
	
	//constructor that allows two arguments to be added immediately
	public DivideFunctionElement(FormulaElement arg1, FormulaElement arg2){
		addArgument(arg1);
		addArgument(arg2);
	}
	
	//empty constructor; arguments can be added manually
	public DivideFunctionElement(){
	}
	
	//override addArgument method so that you can't add more than 2 arguments
	public void addArgument(FormulaElement arg){
		if(arguments.size()==2){
			System.out.println("The divide function can't have more than 2 arguments");
		}
		else
			arguments.add(arg);
	}

	public String toString(){
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		//check if both arguments are constants; in which case division is performed to yield 1 value
		if(arg1 instanceof ConstantElement && arg2 instanceof ConstantElement){
			double retNum=((ConstantElement) arg1).getValue() / ((ConstantElement) arg2).getValue();
			if(retNum%1==0)
				return ""+(int)retNum;
			return ""+retNum;
		}
		else{
			String argStr1="", argStr2="";
		
			//checking if parentheses are needed around first argument
			if(arg1 instanceof PlusFunctionElement || arg1 instanceof MinusFunctionElement)
				argStr1 = "("+arg1.toString()+")";
			else
				argStr1 = arg1.toString();
			
			//check if parentheses are needed around second argument
			if(arg2 instanceof PlusFunctionElement || arg2 instanceof MinusFunctionElement)
				argStr2 = "("+arg2.toString()+")";
			else
				argStr2 = arg2.toString();
			
			return argStr1+"/"+argStr2;
		}
	}
	
	public double evaluate(){
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		return arg1.evaluate()/arg2.evaluate();
	}

	@Override
	public FormulaElement partialEval() {
		FormulaElement arg1 = getArguments().elementAt(0);
		FormulaElement arg2 = getArguments().elementAt(1);
		return new DivideFunctionElement(arg1.partialEval(), arg2.partialEval());
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
		if(degree<1)		//Stop recursion when degree is less than 1
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new DivideFunctionElement();
		FormulaElement first=elements.elementAt(0);
		FormulaElement second=elements.elementAt(1);
		FormulaElement divfirst=first.symbolicDiff(respect, degree);
		FormulaElement divsecond=second.symbolicDiff(respect, degree);
		MultipleFunctionElement mult1=new MultipleFunctionElement(divfirst,second);
		MultipleFunctionElement mult2=new MultipleFunctionElement(first,divsecond);
		FormulaElement out;
		
		if(!divfirst.equals(new ConstantElement(0))&&!divsecond.equals(new ConstantElement(0)))
			out=new MinusFunctionElement(mult1,mult2);
		else if(divfirst.equals(new ConstantElement(0)))
			out=mult2;
		else if(divsecond.equals(new ConstantElement(0)))
			out=mult1;
		else 
			out=new ConstantElement(0);
		
		elem.addArgument(out);
		PowerFunctionElement pow=new PowerFunctionElement(second,new ConstantElement(2));
		elem.addArgument(pow);
		return elem.symbolicDiff(respect, degree-1);	//Recursively call function with 1 less degree
	}
	
	@Override
	public FormulaElement getSimplifiedCopy()
	{
		Vector<FormulaElement> v=this.getArguments();
		FormulaElement div1=v.firstElement().getSimplifiedCopy();
		FormulaElement div2=v.lastElement().getSimplifiedCopy();
		
		if(div1.equals(div2))
			return new ConstantElement(1);
		if(div1 instanceof MultipleFunctionElement||div2 instanceof MultipleFunctionElement)
		{
			Vector<FormulaElement> var1=new Vector<FormulaElement>();
			Vector<FormulaElement> var2=new Vector<FormulaElement>();
			if(div1 instanceof MultipleFunctionElement)
				var1=((FunctionElement) div1).getArguments();
			else
				var1.add(div1);
			if(div2 instanceof MultipleFunctionElement)
				var2=((FunctionElement) div2).getArguments();
			else
				var2.add(div2);
			for(int i=0;i<var1.size();i++)
			{
				for(int j=0;j<var2.size();j++)
				{
					if(var1.elementAt(i).equals(var2.elementAt(j)))
					{
						var1.remove(i);
						var2.remove(j);
						i--;
						break;
					}
					else if(var1.elementAt(i) instanceof ConstantElement&&var2.elementAt(j) instanceof ConstantElement)
					{
						double nume=((ConstantElement)var1.remove(i)).getValue();
						double denom=((ConstantElement)var2.remove(j)).getValue();
						nume=nume/denom;
						var1.add(new ConstantElement(nume));
						i--;
						break;
					}
				}
			}
		}
		DivideFunctionElement div=new DivideFunctionElement();
		div.addArgument(div1.getSimplifiedCopy());
		div.addArgument(div2.getSimplifiedCopy());
		return div;
	}
}

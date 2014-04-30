package formulator;

import java.util.Vector;

public class MultipleFunctionElement extends FunctionElement {
	private boolean zeroFlag;
	
	//constructor that allows two arguments to be added immediately
	public MultipleFunctionElement(FormulaElement arg1, FormulaElement arg2){
		zeroFlag=false;
		if(arg1 instanceof ConstantElement && arg2 instanceof ConstantElement){
			Double d_arg1 = ((ConstantElement)arg1).getValue();
			Double d_arg2 = ((ConstantElement)arg2).getValue();
			addArgument(new ConstantElement(d_arg1*d_arg2));
		}
		else{
			addArgument(arg1);
			addArgument(arg2);
		}
	}
	
	public void addArgument(FormulaElement arg){
		if(arg instanceof MultipleFunctionElement){
			for(FormulaElement sub_arg: ((MultipleFunctionElement) arg).getArguments()){
				this.addArgument(sub_arg);
			}
		}
		else if(arg instanceof ConstantElement){
			if(((ConstantElement)arg).getValue()==0){
				arguments.add(arg);
				zeroFlag=true;
			}
			else{
				arguments.add(arg);
			}
		}
		else{
			arguments.add(arg);
		}
	}
	
	//empty constructor; arguments can be added manually
	public MultipleFunctionElement(){
		zeroFlag=false;
	}
	
	public String toString(){
		Vector<FormulaElement> arguments = getArguments();
		double retNum=0;
		String retString="";
		FormulaElement arg;
		for(int i=0; i<arguments.size(); i++){
			arg=arguments.elementAt(i);
			//allows for a result to be calculated if 1+ argument is a constant
			if(arg instanceof ConstantElement){
				retNum+=((ConstantElement) arg).getValue();
			}
			//if an argument is a PlusFuntionElement or MinusFunctionElement, add parentheses around it
			else if(arg instanceof PlusFunctionElement || arg instanceof MinusFunctionElement){
				retString+="("+arg.toString()+")";
			}
			else{
				retString+=arg.toString();
			}
		}
		System.out.println(zeroFlag);
		if(zeroFlag)
			return "";
		else if(retNum!=0 && retNum%1==0)
			return (int)retNum+retString;
		else if(retNum!=0)
			return retNum+retString;
		else
			return retString;
	}
	
	public double evaluate(){
		Vector<FormulaElement> arguments = getArguments();
		double retNum=1;
		FormulaElement arg;
		for(int i=0; i<arguments.size(); i++){
			arg=arguments.elementAt(i);
			retNum = retNum * arg.evaluate();
		}
		return retNum;
	}

	@Override
	public FormulaElement dEval() {
		Vector<FormulaElement> arguments = getArguments();
		MultipleFunctionElement newMult = new MultipleFunctionElement();
		FormulaElement arg;
		for(int i=0; i<arguments.size(); i++){
			arg=arguments.elementAt(i);
			newMult.addArgument(arg.dEval());
		}
		return newMult;
	}
	
	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree==0)
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new PlusFunctionElement();
		for(int i=0;i<elements.size();i++)
		{
			MultipleFunctionElement mult=new MultipleFunctionElement();
			mult.addArgument(elements.elementAt(i).symbolicDiff(respect, degree));
			for(int j=0;j<elements.size();j++)
				if(i!=j)
					mult.addArgument(elements.elementAt(j));
			elem.addArgument(mult);
		}
		return elem.symbolicDiff(respect, degree-1);
	}
}

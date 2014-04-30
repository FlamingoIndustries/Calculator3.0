package formulator;

import java.util.Vector;

public class MultipleFunctionElement extends FunctionElement {
	boolean zeroFlag;
	
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
		if(arg instanceof ConstantElement){
			if(((ConstantElement)arg).getValue()==0){
				zeroFlag=true;
			}
			else{
				zeroFlag=false;
				arguments.add(arg);
			}
		}
		else{
			zeroFlag=false;
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
}

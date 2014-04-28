package formulator;

import java.util.Vector;

public class MultipleFunctionElement extends FunctionElement {
	
	//constructor that allows two arguments to be added immediately
	public MultipleFunctionElement(FormulaElement arg1, FormulaElement arg2){
		addArgument(arg1);
		addArgument(arg2);
	}
	
	//empty constructor; arguments can be added manually
	public MultipleFunctionElement(){	
	}
	
	public String toString(){
		Vector<FormulaElement> arguments = getArguments();
		double retNum=0;
		String retString="";
		FormulaElement arg;
		for(int i=0; i<arguments.size(); i++){
			arg=arguments.elementAt(i);
			//allows for a result to be calculate if 1+ argument is a constant
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
		if(retNum!=0 && retNum%1==0)
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

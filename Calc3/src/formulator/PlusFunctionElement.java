package formulator;

import java.util.Vector;

public class PlusFunctionElement extends FunctionElement {
	
	//constructor that allows two arguments to be added immediately
	public PlusFunctionElement(FormulaElement arg1, FormulaElement arg2){
		addArgument(arg1);
		addArgument(arg2);
	}
	
	public void addArgument(FormulaElement arg){
		if(arg instanceof PlusFunctionElement){
			for(FormulaElement sub_arg: ((PlusFunctionElement) arg).getArguments()){
				arguments.add(sub_arg);
			}
		}
		else
			arguments.add(arg);
	}
	
	//empty constructor; arguments can be added manually
	public PlusFunctionElement(){
	}

	public String toString(){
		Vector<FormulaElement> arguments = getArguments();
		//for storing constants
		double retNum=0;
		//for storing variables
		String retString="";
		FormulaElement arg;
		//iterate through all elements in arguments vector
		for(int i=0; i<arguments.size(); i++){
			arg = arguments.elementAt(i);
			//add constants together
			if(arg instanceof ConstantElement){
				retNum+=((ConstantElement) arg).getValue();
			}
			//add variables together with plus symbol
			else{
				retString+=arg.toString()+" + ";
			}
		}
		//assemble return string, putting constants and variables together
		//return an int if the double constant is actually an int
		//if(retNum!=0 && retNum%1==0)
			//return retString+(int)retNum;
		if(retNum!=0)
			return retString+retNum;
		else
			return retString.substring(0, retString.length()-3);

	}
	
	public double evaluate(){
		Vector<FormulaElement> arguments = getArguments();
		//for storing constants
		double retNum=0;
		FormulaElement arg;
		//iterate through all elements in arguments vector
		for(int i=0; i<arguments.size(); i++){
			arg = arguments.elementAt(i);
			retNum+=arg.evaluate();
		}
		return retNum;
	}
	
	public FormulaElement dEval() {
		Vector<FormulaElement> arguments = getArguments();
		PlusFunctionElement newPlus = new PlusFunctionElement();
		FormulaElement arg;
		for(int i=0; i<arguments.size(); i++){
			arg=arguments.elementAt(i);
			newPlus.addArgument(arg.dEval());
		}
		return newPlus;
	}
}

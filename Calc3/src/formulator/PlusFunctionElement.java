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
				if(retString.isEmpty())
					retString+=arg.toString();
				else
					retString+="+"+arg.toString();
			}
		}
		//assemble return string, putting constants and variables together
		//return an int if the double constant is actually an int
		if(retNum!=0 && retNum%1==0 && !retString.isEmpty())
			return retString+"+"+(int)retNum;
		if(retNum!=0 && !retString.isEmpty())
			return retString+"+"+retNum;
		else if(retString.isEmpty())
			return ""+retNum;
		else
			return retString;

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
	
	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree==0)
			return this;
		Vector<FormulaElement> elements=this.getArguments();
		FunctionElement elem=new PlusFunctionElement();
		for(int i=0;i<elements.size();i++)
		{
			FormulaElement current=elements.elementAt(i).symbolicDiff(respect, degree);
			if(!current.equals(new ConstantElement(0)))
				elem.addArgument(current);
		}
		if(elem.getArguments().isEmpty())
			return new ConstantElement(0);
		return elem.symbolicDiff(respect, degree-1);
	}
}

/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;

import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

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
		else if(retString.isEmpty() && retNum%1==0)
			return ""+(int)retNum;
		else if (retString.isEmpty())
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
	
	public FormulaElement partialEval() {
		Vector<FormulaElement> arguments = getArguments();
		PlusFunctionElement newPlus = new PlusFunctionElement();
		FormulaElement arg;
		for(int i=0; i<arguments.size(); i++){
			arg=arguments.elementAt(i);
			newPlus.addArgument(arg.partialEval());
		}
		return newPlus;
	}
	
	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree<1)
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
	
	@Override
	public FormulaElement getSimplifiedCopy()
	{
		PlusFunctionElement out=new PlusFunctionElement();
		double consttotal=0;
		Vector<FormulaElement> pluselem=this.getArguments();
		HashMap<String, Vector<FormulaElement>> vars=new HashMap<String, Vector<FormulaElement>>();
		for(int x=0;x<pluselem.size();x++)
		{
			FormulaElement p=pluselem.remove(x);
			pluselem.add(x,p.getSimplifiedCopy());
		}
		for(int x=0;x<pluselem.size();)
		{
			if(pluselem.elementAt(x) instanceof ConstantElement)
				consttotal+=((ConstantElement) pluselem.remove(x)).getValue();
			else if(pluselem.elementAt(x) instanceof VariableElement)
				vars=this.addToHashMap(pluselem.remove(x), new ConstantElement(1), vars);
			else if(pluselem.elementAt(x) instanceof PlusFunctionElement)
				pluselem.addAll(((PlusFunctionElement)pluselem.remove(x)).getArguments());
			else if(pluselem.elementAt(x) instanceof MinusFunctionElement)
			{
				MinusFunctionElement minus=(MinusFunctionElement)pluselem.remove(x);
				Vector<FormulaElement> minelem=minus.getArguments();
				pluselem.add(minelem.elementAt(0));
				FormulaElement minuspart=minelem.lastElement();
				MultipleFunctionElement r=new MultipleFunctionElement(new ConstantElement(-1), minuspart);
				pluselem.add(r);
			}
			else if(pluselem.elementAt(x) instanceof MultipleFunctionElement)
				vars=this.simplifyMultipleFunctionElement(vars, (MultipleFunctionElement)pluselem.remove(x));
			else if(pluselem.elementAt(x) instanceof PowerFunctionElement)
				vars=this.addToHashMap((PowerFunctionElement) pluselem.remove(x), new ConstantElement(1), vars);
			else
				x++;
		}
		for(Entry<String,Vector<FormulaElement>> ent:vars.entrySet())
		{
			Vector<FormulaElement> var=ent.getValue();
			MultipleFunctionElement mult=new MultipleFunctionElement();
			FormulaElement varcount=var.lastElement();
			if (varcount instanceof ConstantElement&&((ConstantElement)varcount).getValue()==1)
				pluselem.add((FormulaElement)var.firstElement());
			else
			{
				mult.addArgument(varcount);
				mult.addArgument((FormulaElement)var.firstElement());
				pluselem.add(mult);
			}
		}
		if(consttotal!=0)
		{
			ConstantElement constelem=new ConstantElement(consttotal);
			if(pluselem.isEmpty())
				return constelem;
			pluselem.add(constelem);
		}
		if(pluselem.size()>1)
			for(FormulaElement e:pluselem)
				out.addArgument(e);
		else if(pluselem.size()==1)
			return pluselem.firstElement();
		else
			return new ConstantElement(0);
		return out;
	}
		
	private HashMap<String,Vector<FormulaElement>> simplifyMultipleFunctionElement(HashMap<String, Vector<FormulaElement>> vars, MultipleFunctionElement mult)
	{
		Vector<FormulaElement> v=mult.getArguments();
		double constprod=1;
		Vector<String> variables=new Vector<String>(); 
		FormulaElement check=mult;
		for(int i=0;i<v.size();i++)
		{
			if(v.elementAt(i) instanceof ConstantElement)
			{
				constprod*=((ConstantElement) v.remove(i)).getValue();
				i--;
			}
			else if(v.elementAt(i) instanceof VariableElement||v.elementAt(i) instanceof FunctionElement)
				variables.add(v.elementAt(i).toString());
		}
		if(v.size()>1)
		{
			check=new MultipleFunctionElement();
			for(FormulaElement elem:v)
				((MultipleFunctionElement) check).addArgument(elem);
		}
		else
			check=v.firstElement();
		vars=this.addToHashMap(check, new ConstantElement(constprod), vars);
		return vars;
	}
}

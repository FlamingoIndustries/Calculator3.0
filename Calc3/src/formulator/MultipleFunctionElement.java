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
import java.util.Map.Entry;
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
				arguments.add(arg);
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
				if(!arg.toString().equals("0"))
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
	public FormulaElement partialEval() {
		Vector<FormulaElement> arguments = getArguments();
		MultipleFunctionElement newMult = new MultipleFunctionElement();
		FormulaElement arg;
		for(int i=0; i<arguments.size(); i++){
			arg=arguments.elementAt(i);
			newMult.addArgument(arg.partialEval());
		}
		return newMult;
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
			MultipleFunctionElement mult=new MultipleFunctionElement();
			FormulaElement diff=elements.elementAt(i).symbolicDiff(respect, degree);
			if(!diff.equals(new ConstantElement(0)))
			{
				mult.addArgument(diff);
				for(int j=0;j<elements.size();j++)
					if(i!=j)
						mult.addArgument(elements.elementAt(j));
				elem.addArgument(mult);
			}
		}
		if(elem.getArguments().isEmpty())
			return new ConstantElement(0);
		return elem.symbolicDiff(respect, degree-1);
	}
	
	@Override
	public FormulaElement getSimplifiedCopy()
	{
		FormulaElement out=this;
		Vector<FormulaElement> multelem=this.getArguments();
		for(int i=0;i<multelem.size();i++)
		{
			FormulaElement m=multelem.remove(i);
			multelem.add(i,m.getSimplifiedCopy());
		}
		for(int i=0;i<multelem.size();i++)
		{
			if(multelem.elementAt(i) instanceof PlusFunctionElement)
			{
				out=new PlusFunctionElement();
				PlusFunctionElement f=(PlusFunctionElement)multelem.remove(i);
				Vector<FormulaElement> v=f.getArguments();
				for(int j=0;j<v.size();j++)
				{
					FormulaElement g=v.elementAt(j);
					MultipleFunctionElement m=new MultipleFunctionElement(); 
					m.addArgument(g);
					for(int p=0;p<multelem.size();p++)
						m.addArgument(multelem.elementAt(p));
					try
					{
						ConstantElement newcon=new ConstantElement(m.evaluate());
						((PlusFunctionElement) out).addArgument(newcon);
					}
					catch(Exception e)
					{
						((PlusFunctionElement) out).addArgument(m);
					}
				}
				return out.getSimplifiedCopy();
			}
		}
		double constprod=1;
		HashMap<String,Vector<FormulaElement>> vars=new HashMap<String,Vector<FormulaElement>>();
		for(int j=0;j<multelem.size();j++)
		{
			if(multelem.elementAt(j) instanceof ConstantElement)
			{
				constprod*=((ConstantElement)multelem.remove(j)).getValue();
				j--;
			}
			else if(multelem.elementAt(j) instanceof FormulaElement)
			{
				FormulaElement var=multelem.remove(j);
				FormulaElement count=new ConstantElement(1);
				if(var instanceof PowerFunctionElement)
				{
					Vector<FormulaElement> powargs=((PowerFunctionElement)var).getArguments();
					var=powargs.firstElement();
					count=powargs.lastElement();
				}
				vars=this.addToHashMap(var, count, vars);
				j--;
			}
		}
		for(Entry<String, Vector<FormulaElement>> ent:vars.entrySet())
		{
			Vector<FormulaElement> v=ent.getValue();
			if(v.elementAt(1) instanceof ConstantElement&&((ConstantElement)v.elementAt(1)).getValue()==1)
				multelem.add(v.firstElement());
			else
			{
				FormulaElement var=((FormulaElement) v.firstElement());
				PowerFunctionElement pow=new PowerFunctionElement();
				pow.addArgument(var);
				pow.addArgument(v.elementAt(1));
				multelem.add(pow);
			}
		}
		ConstantElement product=new ConstantElement(constprod);
		if(!multelem.isEmpty()&&constprod!=1)
			multelem.add(product);
		else if(multelem.isEmpty())
			return product;
		
		out=new MultipleFunctionElement();
		for(FormulaElement e:multelem)
			((MultipleFunctionElement)out).addArgument(e);
		return out;
	}
}

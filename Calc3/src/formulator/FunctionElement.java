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

abstract class FunctionElement extends FormulaElement {
	protected Vector<FormulaElement> arguments = new Vector<FormulaElement>();
	
	//empty default constructor
	public FunctionElement(){
	}
	
	public void addArgument(FormulaElement arg){
		arguments.add(arg);
	}
	
	public Vector<FormulaElement> getArguments(){
		return arguments;
	}
	
	@Override
	public String getXMLformat(String tabbing)
	{
		String newline="\n"+tabbing;
		String funcName=this.getClass().getSimpleName();
		String out="<"+funcName+">";
		for(FormulaElement e: arguments)
		{
			out+=newline+"\t"+e.getXMLformat(tabbing+"\t");
		}
		out+=newline+"</"+funcName+">";
		return out;
	}
	
	public boolean equals(FormulaElement comp)
	{
		if(this.getClass().getSimpleName().equals(comp.getClass().getSimpleName()))
		{
			FunctionElement comp1=(FunctionElement) comp;
			Vector<FormulaElement> thiselements=new Vector<FormulaElement>();
			Vector<FormulaElement> compelements=new Vector<FormulaElement>();
			for(FormulaElement e:arguments)
				thiselements.add(e);
			for(FormulaElement e:comp1.getArguments())
				compelements.add(e);
			for(int i=0;i<thiselements.size();i++)
			{
				for(int j=0;j<compelements.size();j++)
				{
					if(thiselements.elementAt(i).equals(compelements.elementAt(j)))
					{
						thiselements.remove(i);
						compelements.remove(j);
						i--;
						break;
					}
				}
			}
			if(thiselements.isEmpty()&&compelements.isEmpty())
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	@Override
	public VariableElement findVariable(String varName)
	{
		for(int i=0;i<arguments.size();i++)
		{
			FormulaElement e=arguments.elementAt(i);
			if(e instanceof VariableElement&& ((VariableElement)e).getName().equals(varName))
				return (VariableElement)e;
			else if(e instanceof FunctionElement)
			{
				VariableElement f=e.findVariable(varName);
				if(f!=null)
					return f;
			}
		}
		return null;
	}
}

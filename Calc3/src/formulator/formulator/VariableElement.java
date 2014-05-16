/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;

public class VariableElement extends FormulaElement {
	private String name;
	private double value;
	boolean valueAssigned;
	private FormulaElement partVal;

	public VariableElement(String input){
		name = input;
		valueAssigned=false;
		partVal=this;
	}
	
	public String getName(){
		return name;
	}
	
	public double getValue(){
		return value;
	}
	
	public void setValue(double x){
		value = x;
		valueAssigned=true;
		partVal = new ConstantElement(x);
	}
	
	public void unSetValue(){
		valueAssigned=false;
	}
	
	public String toString(){
		return name;
	}

	@Override
	public double evaluate() {
		return value;
	}

	public void setPartialValue(FormulaElement value){
		partVal = value;
	}
	
	@Override
	public FormulaElement partialEval() {
		return partVal;
	}
	
	@Override
	public String getXMLformat(String tabbing)
	{
		return "<"+this.getClass().getSimpleName()+" name=\""+name+"\"/>";
	}
	
	@Override
	public boolean equals(FormulaElement comp)
	{
		if(comp instanceof VariableElement&& name.equals(comp.toString()))
			return true;
		else
			return false;
	}

	@Override
	public FormulaElement symbolicDiff(String respect,int degree)
	{
		if(degree<1)		//Stop recursion when degree is less than 1
			return this;
		FormulaElement out;
		if(name.equals(respect))
			out=new ConstantElement(1).symbolicDiff(respect, degree-1);	//Recursively call function with 1 less degree
		else
			out=new ConstantElement(0);	//A variable which is not the respect, is treated like a constant
		return out;
	}
	
	@Override
	public VariableElement findVariable(String varName)
	{
		if(varName.equals(name))
			return this;
		return null;
	}
	
	@Override
	public FormulaElement getSimplifiedCopy()
	{
		return this;
	}
}

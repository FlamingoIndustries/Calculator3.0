package formulator;

public class ConstantElement extends FormulaElement {
	private double value;

	public ConstantElement(double val){
		value = val;
	}
	
	public double getValue(){
		return value;
	}
	
	public String toString(){
		if(value%1==0)
			return ""+((int)value);
		return ""+value;
	}

	public double evaluate() {
		return value;
	}

	@Override
	public FormulaElement dEval() {
		return this;
	}
	
	@Override
	public String getXMLformat(String tabbing)
	{
		String newline="\n"+tabbing;
		return "<"+this.getClass().getSimpleName()+">value="+value+"</"+this.getClass().getSimpleName()+">";
	}
	
	public boolean equals(FormulaElement comp)
	{
		if(comp instanceof ConstantElement&&value==((ConstantElement) comp).getValue())
			return true;
		else
			return false;
		
	}

	@Override
	public FormulaElement symbolicDiff(String respect, int degree)
	{
		if(degree==0)
			return this;
		return new ConstantElement(0);
	}
}

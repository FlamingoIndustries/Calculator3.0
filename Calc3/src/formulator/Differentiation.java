package formulator;

import java.util.Vector;

public class Differentiation
{

	
	final ConstantElement dx=new ConstantElement(0.0000001);
	
	public FormulaElement numericDiff(FormulaElement form, String respect, int degree)
	{
		FormulaElement replace=new PlusFunctionElement(dx, form.findVariable(respect));
		form.setDValue(respect, replace);//ASK BARBARA!
		System.out.println(form.dEval()+"\t\t"+form);
		MinusFunctionElement minus=new MinusFunctionElement(form.dEval(),form);
		DivideFunctionElement div=new DivideFunctionElement(minus,dx);
		if(degree==1)
			return div;
		else
			return numericDiff(div, respect, degree-1);
	}
	public FormulaElement symbolicDiff(FormulaElement form, String respect, int degree)
	{
		FormulaElement out=form;
		if(degree==0)
			out=form;
		else if(form instanceof ConstantElement)
			out=new ConstantElement(0);		
		else if(form instanceof VariableElement)
		{
			if(((VariableElement)form).getName().equals(respect))
				out=this.symbolicDiff(new ConstantElement(1), respect, degree-1);
			else
				out=new ConstantElement(0);
		}
		else if(form instanceof FunctionElement)
		{
			FunctionElement elem=(FunctionElement)form;
			Vector<FormulaElement> elements=elem.getArguments();
			if(form instanceof PlusFunctionElement||form instanceof MinusFunctionElement)
			{
				if(form instanceof PlusFunctionElement)
					elem=new PlusFunctionElement();
				else if(form instanceof MinusFunctionElement)
					elem=new MinusFunctionElement();
				for(int i=0;i<elements.size();i++)
					elem.addArgument(this.symbolicDiff(elements.elementAt(i), respect, degree));
				out=this.symbolicDiff(elem, respect, degree-1);
			}
			else if(form instanceof MultipleFunctionElement)
			{
				elem=new PlusFunctionElement();
				for(int i=0;i<elements.size();i++)
				{
					MultipleFunctionElement mult=new MultipleFunctionElement();
					mult.addArgument(this.symbolicDiff(elements.elementAt(i), respect, degree));
					for(int j=0;j<elements.size();j++)
						if(i!=j)
							mult.addArgument(elements.elementAt(j));
					elem.addArgument(mult);
				}
				out=this.symbolicDiff(elem, respect, degree-1);
			}
			else if(form instanceof DivideFunctionElement)
			{
				elem=new DivideFunctionElement();
				FormulaElement first=elements.elementAt(0);
				FormulaElement second=elements.elementAt(1);
				FormulaElement divfirst=this.symbolicDiff(first, respect, degree);
				FormulaElement divsecond=this.symbolicDiff(second, respect, degree);
				MultipleFunctionElement mult=new MultipleFunctionElement(divfirst,second);
				MultipleFunctionElement mult1=new MultipleFunctionElement(first,divsecond);
				MinusFunctionElement minus=new MinusFunctionElement(mult,mult1);
				System.out.println(first);
				elem.addArgument(minus);
				PowerFunctionElement pow=new PowerFunctionElement(second,new ConstantElement(2));
				elem.addArgument(pow);
				out=this.symbolicDiff(elem, respect, degree-1);
			}
			else if(form instanceof PowerFunctionElement)
			{
				elem=new MultipleFunctionElement();
				FormulaElement first=elements.elementAt(0);
				FormulaElement second=elements.elementAt(1);
				elem.addArgument(second);
				MinusFunctionElement minus=new MinusFunctionElement(second,new ConstantElement(1));
				
				PowerFunctionElement pow=new PowerFunctionElement(first,minus);
				elem.addArgument(pow);
				out=this.symbolicDiff(elem, respect, degree-1);
			}
			else if(form instanceof CosineFunctionElement)
			{
				elem=new MultipleFunctionElement();
				FormulaElement first=elements.elementAt(0);
				elem.addArgument(new ConstantElement(-1));
				elem.addArgument(new SineFunctionElement(first));
				elem.addArgument(this.symbolicDiff(first, respect, degree));
				System.out.println(first);//this.symbolicDiff(first, respect, degree));
				out=this.symbolicDiff(elem, respect, degree-1);
			}
			else if(form instanceof SineFunctionElement)
			{
				elem=new MultipleFunctionElement();
				FormulaElement first=elements.elementAt(0);
				elem.addArgument(new CosineFunctionElement(first));
				elem.addArgument(this.symbolicDiff(first, respect, degree));
				out=this.symbolicDiff(elem, respect, degree-1);
			}
		}
		return out;
	}
}

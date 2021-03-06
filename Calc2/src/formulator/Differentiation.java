package formulator;

import java.util.Vector;

public class Differentiation
{
/*
 * (f(x+dx)-f(x))/dx
 */
	final ConstantElement dx=new ConstantElement(0.0000001);
	public FormulaElement numericDiff(FormulaElement form, String respect, int degree) throws Exception
	{
		//Replace variable
		return null;
//		FormulaElement adjustedform= form.copyFormula();
//		FormulaElement replace=new PlusFunctionElement(dx, form.findVariable(respect));
//		adjustedform.replaceVariable("x", replace);
//		MinusFunctionElement minus=new MinusFunctionElement(adjustedform,form);
//		DivideFunctionElement div=new DivideFunctionElement(minus,dx);
//		if(degree==1)
//			return div;
//		else
//			return numericDiff(div, respect, degree-1);
	}
	public FormulaElement symbolicDiff(FormulaElement form, String respect, int degree) throws Exception
	{
		FormulaElement out=form;
		if(form instanceof ConstantElement)
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
				elem=new PlusFunctionElement();
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
				elem.addArgument(this.symbolicDiff(first, respect, degree));
				out=this.symbolicDiff(elem, respect, degree-1);
			}
			else if(form instanceof CosineFunctionElement)
			{
				elem=new MultipleFunctionElement();
				FormulaElement first=elements.elementAt(0);
				elem.addArgument(new ConstantElement(-1));
				elem.addArgument(new SineFunctionElement(first));
				elem.addArgument(this.symbolicDiff(first, respect, degree));
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
		else
			out=null;
		return out;
	}
}

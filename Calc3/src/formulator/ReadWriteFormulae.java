package formulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ReadWriteFormulae
{
	public static Boolean WriteFormulae()
	{
		Display display = new Display();
	    final Shell shell = new Shell(display);


	    FileDialog dlg = new FileDialog(shell, SWT.SAVE);
	    String[] extensions={"*.xml", "*.txt"};
	    dlg.setFilterExtensions(extensions);
	    String fileName = dlg.open();
	    if (fileName != null) {
	    	PrintWriter writer;
			try
			{
				writer = new PrintWriter(fileName, "UTF-8");
			} catch (FileNotFoundException e)
			{
				return false;
			} catch (UnsupportedEncodingException e)
			{
				return false;
			}
	    	HashMap<String, FormulaElement> formulae=new HashMap<String, FormulaElement>();
	    	formulae.put("form1", new PlusFunctionElement(new ConstantElement(3), new VariableElement("x")));
	    	for(Entry<String, FormulaElement> form:formulae.entrySet())
	    	{
	    		FormulaElement formula=form.getValue();
	    		String formXML="<"+form.getKey()+">"+"\n"+"\t";
	    		formXML+=formula.getXMLformat("\t")+"\n";
	    		formXML+="</"+form.getKey()+">";
	    		System.out.println(formXML);
	    		writer.print(formXML);
	    	}
	    	writer.close();
	    }
	    display.dispose();
	    return true;
	 }
	
	public static Boolean ReadFormulae()
	{
		HashMap<String, FormulaElement> out=new HashMap<String, FormulaElement>();
		Stack<String> xmlstatements=new Stack<String>();
		Stack<FormulaElement> formulae=new Stack<FormulaElement>();
		Display display = new Display();
	    final Shell shell = new Shell(display);
	    FileDialog dlg = new FileDialog(shell, SWT.NONE);
	    String[] extensions={"*.xml", "*.txt"};
	    dlg.setFilterExtensions(extensions);
	    String fileName = dlg.open();
	    display.dispose();
		Scanner reader;
		try
		{
			
			reader = new Scanner(new FileReader(fileName));
			String line;
			reader.useDelimiter("\n");
			Boolean newFormula=true;
			String currentFormula="";
			
			while (reader.hasNext()) 
			{
				line=reader.next();
				line=line.trim();
				if(line.matches("<[a-zA-Z]\\w*>"))
				{
					line=line.substring(1, line.length()-1);
					FormulaElement form=null;
					if(newFormula)
					{
						currentFormula=line;
						newFormula=false;
					}
					else
					{
						if(line.equals("CosineFunctionElement"))
							form=new CosineFunctionElement();
						else if(line.equals("SineFunctionElement"))
							form=new SineFunctionElement();
						else if(line.equals("DivideFunctionElement"))
							form=new DivideFunctionElement();
						else if(line.equals("MultipleFunctionElement"))
							form=new MultipleFunctionElement();
						else if(line.equals("PlusFunctionElement"))
							form=new PlusFunctionElement();
						else if(line.equals("MinusFunctionElement"))
							form=new MinusFunctionElement();
						else if(line.equals("PowerFunctionElement"))
							form=new PowerFunctionElement();
						formulae.push(form);
					}
						
					line="</"+line+">";
					xmlstatements.push(line);
					
				}
				else
				{
					FormulaElement elem=null;
					if(line.matches("</[a-zA-Z]\\w*>")&&xmlstatements.peek().equals(line))
					{
						xmlstatements.pop();
						if(!formulae.isEmpty())
							elem=formulae.pop();
					}
					else if(line.matches("<VariableElement>name=\\w+</VariableElement>"))
					{
						Pattern form= Pattern.compile("<VariableElement>name=(\\w+)</VariableElement>");
						Matcher m = form.matcher(line);
						m.find();
						String name=m.group(1);
						elem=new VariableElement(name);						
					}
					else if(line.matches("<ConstantElement>value=\\d+\\.\\d+</ConstantElement>"))
					{
						Pattern form= Pattern.compile("<ConstantElement>value=(\\d+\\.\\d+)</ConstantElement>");
						Matcher m = form.matcher(line);
						m.find();
						double value=Double.parseDouble(m.group(1));
						elem=new ConstantElement(value);
					}
					else
					{
						return false;
					}
					if(!formulae.isEmpty()&&formulae.peek() instanceof FunctionElement)
						((FunctionElement)formulae.peek()).addArgument(elem);
					else if(formulae.isEmpty()&&elem!=null)
					{
						out.put(currentFormula, elem);
						currentFormula="";
						newFormula=true;
					}
				}
			}
		} catch (IOException e)
		{
			return false;
		}
		System.out.println(out+"hi");
		return true;
	}
}

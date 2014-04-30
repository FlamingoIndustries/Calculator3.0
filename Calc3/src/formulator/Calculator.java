/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class Calculator {
	private  HashMap<String, FormulaElement> formulas;
	private boolean symbolic;
	
	public Calculator()
	{
		formulas=new HashMap<String, FormulaElement>();
		symbolic=false;
	}
	
	public String branch(String text)
	{
		text=text.trim();
		if(text.matches("^\\s*save\\s*$"))				//Branching to save all formulae in a file
		{
			if(!formulas.isEmpty())
				if(this.WriteFormulae())
					return "File Saved";
				else
					return "File not Saved";
			else
				return "Nothing to save!";
		}
		else if(text.matches("^\\s*load\\s*$"))		//Branching to load all formulae from a chosen file
		{
			if(this.ReadFormulae())
				return "Successfully read from file";
			else
				return "Unable to read from file";
		}
		else if(text.matches("^\\s*graph.*$"))		//Branching to graph code
		{
			return this.graphFormula(text);
		}
		else if(text.matches("^\\s*\\w+\\s*=.*"))	//Assigning rhs of = to formula name on lhs 
		{
			Pattern form= Pattern.compile("^\\s*(\\w+)\\s*=\\s*(.+)\\s*$");
			Matcher m = form.matcher(text);
			if(m.find()==false)
				return "Improper assignment form";
			FormulaElement newform;
			try
			{
				newform=FormulaElement.parseFormula(m.group(2), formulas, symbolic);
			}
			catch(Exception e)
			{
				return "Unable to save, invalid formula entered";
			}
			Boolean store=false;
			if(formulas.containsKey(m.group(1)))
			{
				int dialogResult = JOptionPane.showConfirmDialog (null, "The formula \""+m.group(1)+"\" already exists\nWould you like to overwrite?","Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult==0)
					store=true;
			}
			else
				store=true;
			if(store)
			{
				formulas.put(m.group(1), newform);
				return m.group(1)+" saved!";
			}
			else
				return m.group(1)+" not saved!";
			//Parse and store formula
		}
		else
		{
			//Parse as formula and attempt to solve
			try
			{
				FormulaElement e=FormulaElement.parseFormula(text, formulas, symbolic);
				if(e!=null)
					return "$"+e.toString();
				return "";
			}
			catch(Exception e)
			{
				return "Invalid input!";
			}
		}
	}
	
	/**
	 * 
	 * @param text input graph instruction
	 * @return String indicating success or failure to graph formula
	 */
	private String graphFormula(String text)
	{	
		if(!text.matches("^graph(\\s+\\w+'*\\(\\w+\\s*=\\s*\\-?\\d+(\\.\\d+)?\\s*,\\s*\\-?\\d+(\\.\\d+)?(\\s*,\\s*\\-?\\d+(\\.\\d+)?)?(\\s*,\\s*\\w+\\s*=\\s*\\-?\\d+(\\.\\d+)?)*\\))+"))
			return "Improper graph format";
		
		Vector<GraphFunction> graphs=new Vector<GraphFunction>();	//Using regular expression to separate out function parts
		Pattern form= Pattern.compile("\\w+'*\\(\\w+\\s*=\\s*\\-?\\d+(\\.\\d+)?\\s*,\\s*\\-?\\d+(\\.\\d+)?(\\s*,\\s*\\-?\\d+(\\.\\d+)?)?(\\s*,\\s*\\w+\\s*=\\s*\\-?\\d+(\\.\\d+)?)*\\)");
		Matcher m = form.matcher(text);
		Vector<String> formv=new Vector<String>();
		while(m.find()==true)
			formv.add(m.group(0));
		for(String s:formv)
		{
			Vector<String> results=new Vector<String>();
			form= Pattern.compile("(\\w+'*)\\((\\w+)\\s*=\\s*(\\-?\\d+(\\.\\d+)?)\\s*,\\s*(\\-?\\d+(\\.\\d+)?)(\\s*,\\s*(\\-?\\d+(\\.\\d+)?))?(.*)\\)");
			m = form.matcher(s);
			m.find();
			FormulaElement root=null;
			String var=null;
			String formName=m.group(1).replaceAll("(\\w+)('*)", "$1");
			int diffCount=(m.group(1).replaceAll("(\\w+)('*)", "$2")).length();
			double min=Double.parseDouble(m.group(3));
			double max=Double.parseDouble(m.group(5));
			double increment=1;
			for(int i=1;i<m.groupCount()+1;i++)
			{
				results.add("b"+m.group(i));
				if(i==1)
				{
					if(formulas.containsKey(formName))
					{
						if(symbolic)
							root=formulas.get(formName).symbolicDiff(m.group(2), diffCount);
						else
							root=formulas.get(formName).numericDiff(m.group(2), diffCount);
					}
					else
						return "Cannot graph "+formName+" as it does not exist";
				}
				else if(i==2)
					var=m.group(2);
				else if(i==8&&m.group(8)!=null&&m.group(8).matches("^-?\\d+(\\.\\d+)?$"))
					increment=Double.parseDouble(m.group(8));
				else if(i==10)
				{
					String assign=m.group(i);
					Pattern a= Pattern.compile("(\\w+)\\s*=\\s*(-?\\d+(\\.\\d+)?)");
					Matcher b = a.matcher(assign);
					while(b.find()==true)
					{
						String varName=b.group(1);
						double value=Double.parseDouble(b.group(2));
						root.setVariableValue(varName, value);
					}
				}
			}
			root.setVariableValue(var, 0);
			if(!root.isFullyGrounded())
				return "Cannot graph "+m.group(1)+" all other variables must be set";
			if(increment<1)
				return "Cannot graph "+m.group(1)+" increment must be positive!";
			if(min>max)
				return "Cannot graph "+m.group(1)+" min must be less than max";
			GraphFunction x= new GraphFunction(root, var, min, max, increment);
			graphs.add(x);
		}
		new GraphControl(graphs, formulas);
		return "Formulae successfully graphed";
	}
	
	/**
	 * 
	 * @return boolean indicating success or failure
	 */
	public boolean WriteFormulae()
	{
		Display display = Display.getCurrent();
	    final Shell shell = new Shell(display);
	    FileDialog dlg = new FileDialog(shell, SWT.SAVE);
	    String[] extensions={"*.xml"};
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
	    	for(Entry<String, FormulaElement> form:formulas.entrySet())
	    	{
	    		FormulaElement formula=form.getValue();
	    		String formXML="<"+form.getKey()+">"+"\n"+"\t";
	    		formXML+=formula.getXMLformat("\t")+"\n";
	    		formXML+="</"+form.getKey()+">";
	    		writer.print(formXML);
	    	}
	    	writer.close();
	    }
	    else
	    	return false;
	    return true;
	 }
	
	/**
	 * 
	 * @return boolean indicating success or failure to read from file
	 */
	public boolean ReadFormulae()
	{
		HashMap<String, FormulaElement> out=new HashMap<String, FormulaElement>();
		Stack<String> xmlstatements=new Stack<String>();
		Stack<FormulaElement> formulae=new Stack<FormulaElement>();
		Display display = Display.getCurrent();
	    final Shell shell = new Shell(display);
	    FileDialog dlg = new FileDialog(shell, SWT.NONE);
	    String[] extensions={"*.xml"};
	    dlg.setFilterExtensions(extensions);
	    String fileName = dlg.open();
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
						reader.close();
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
			reader.close();
		} catch (Exception e)
		{
			return false;
		}
		for(Entry<String, FormulaElement> e: out.entrySet())
		{
			if(formulas.containsKey(e.getKey()))
			{
				int dialogResult = JOptionPane.showConfirmDialog (null, "The formula \""+e.getKey()+"\" already exists\nWould you like to overwrite?","Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult==0)
					formulas.put(e.getKey(), e.getValue());
			}
			else
				formulas.put(e.getKey(), e.getValue());
		}
		return true;
	}
	
	/**
	 * 
	 * Switches differentiation type
	 */
	public boolean toggleDiff()
	{
		symbolic=!symbolic;
		return symbolic;
	}
}

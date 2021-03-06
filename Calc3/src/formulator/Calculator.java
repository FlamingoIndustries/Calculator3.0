/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class Calculator {
	private  HashMap<String, FormulaElement> formulas;
	private boolean symbolic;
	private String[] res={"graph", "abs", "save", "load"};
	private Vector<String> reserved=new Vector<String>(Arrays.asList(res));
	
	public Calculator()
	{
		formulas=new HashMap<String, FormulaElement>();
		symbolic=false;
	}
	
	/**
	 * 
	 * @param text indicating where to branch to 
	 * @return REsulting string to be shown to user
	 */
	public String branch(String text)
	{
		text=text.trim();
		if(text.equals("save"))				//Branching to save all formulae in a file
		{
			if(!formulas.isEmpty())
				if(this.WriteFormulae())
					return "File Saved";
				else
					return "File not Saved";
			else
				return "Nothing to save!";
		}
		else if(text.equals("What is love?"))
		{
			try 
			{
		        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("test.wav"));
		        Clip clip = AudioSystem.getClip();
		        clip.open(audioInputStream);
		        clip.start();
		        clip.loop(Clip.LOOP_CONTINUOUSLY);
		    } catch(Exception ex) {
		        System.out.println("Error with playing sound.");
		        ex.printStackTrace();
		    }
			return "Baby don't hurt me!";
		}
		else if(text.equals("load"))		//Branching to load all formulae from a chosen file
		{
			if(this.ReadFormulae())
				return "Successfully read from file";
			else
				return "Unable to read from file";
		}
		else if(text.matches("^\\s*graph.*$"))		//Branching to graph code
			return this.graphFormula(text);
		else if(text.matches("^\\s*\\w+\\s*=.*"))	//Assigning rhs of = to formula name on lhs 
		{
			Pattern form= Pattern.compile("^\\s*(\\w+)\\s*=\\s*(.+)\\s*$");
			Matcher m = form.matcher(text);
			if(m.find()==false)
				return "Improper assignment form";
			FormulaElement newform;
			try
			{
				newform=FormulaElement.parseFormula(m.group(2), formulas, symbolic).getSimplifiedCopy();
			}
			catch(Exception e)
			{
				return "Unable to store, invalid formula entered";
			}
			Boolean store=false;
			if(reserved.contains(m.group(1)))
				return "Unable to store, cannot store using reserved name";
			else if(formulas.containsKey(m.group(1)))
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
				return m.group(1)+" stored!";
			}
			else
				return m.group(1)+" not stored!";
		}
		else
		{
			//Parse as formula and attempt to solve
			try
			{
				FormulaElement e=FormulaElement.parseFormula(text, formulas, symbolic);
				if(e!=null)
					return "$"+e.getSimplifiedCopy().toString();
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
		String graphTitle=text.replaceAll("\\s*graph\\s*", "");
		String singlegraph="\\w+'*\\(\\w+\\s*=\\s*\\-?\\d+(\\.\\d+)?\\s*,\\s*\\-?\\d+(\\.\\d+)?(\\s*,\\s*\\-?\\d+(\\.\\d+)?)?(\\s*,\\s*\\w+\\s*=\\s*\\-?\\d+(\\.\\d+)?)*\\)";
		if(!text.matches("^graph(\\s+"+singlegraph+")+"))
			return "Improper graph format";
		
		Vector<GraphFunction> graphs=new Vector<GraphFunction>();	//Using regular expression to separate out function parts
		Pattern form= Pattern.compile(singlegraph);
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
			if(increment<=0)
				return "Cannot graph "+m.group(1)+" increment must be positive!";
			if(min>max)
				return "Cannot graph "+m.group(1)+" min must be less than max";
			GraphFunction x= new GraphFunction(root, var, min, max, increment);
			graphs.add(x);
		}
		new GraphControl(graphs, formulas, graphTitle);
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
	    dlg.setOverwrite(true);								//Creating file dialogue display
	    dlg.setFilterExtensions(extensions);
	    String fileName = dlg.open();
	    shell.close();
	    if (fileName != null) 
	    {
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
			boolean first=true;
			writer.print("<formulas>\n");
	    	for(Entry<String, FormulaElement> form:formulas.entrySet())
	    	{
	    		String formXML="";
	    		if(!first)
	    			formXML+="\n";
	    		first=false;
	    		FormulaElement formula=form.getValue();					//Writing XML form of formulas to file
	    		formXML+="\t<"+form.getKey()+">"+"\n"+"\t";
	    		formXML+=formula.getXMLformat("\t\t")+"\n";
	    		formXML+="\t</"+form.getKey()+">";
	    		writer.print(formXML);
	    	}
	    	writer.print("\n</formulas>");
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
		final Shell shell = new Shell(display);						//Creating file dialogue display
		FileDialog dlg = new FileDialog(shell, SWT.NONE);
		String[] extensions={"*.xml"};
		dlg.setFilterExtensions(extensions);
		String fileName = dlg.open();
		shell.close();
		
		Scanner reader;
		try
		{
			reader = new Scanner(new FileReader(fileName));
			String line;
			reader.useDelimiter("\n");
			boolean newFormula=true;
			boolean newFile=true;
			String currentFormula="";
			
			while (reader.hasNext()) 
			{
				line=reader.next();
				line=line.trim();
				
				if(line.matches("<[a-zA-Z]\\w*>"))
				{
					line=line.substring(1, line.length()-1);
					FormulaElement form=null;
					
					if(newFile&&line.equals("formulas"))			//The file must begin with formulas as the root
						newFile=false;
					else if(newFormula)								//The root of each formula is its filename
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
						xmlstatements.pop();				//When function element is closed, it is popped from the stack
						if(!formulae.isEmpty())				//If formula stack still has elements on it, the current formula is the popped top of it
							elem=formulae.pop();
					}
					else if(line.matches("<VariableElement name=\"\\w+\"/>"))
					{
						Pattern form= Pattern.compile("<VariableElement name=\"(\\w+)\"/>");
						Matcher m = form.matcher(line);
						m.find();
						String name=m.group(1);
						elem=new VariableElement(name);						
					}
					else if(line.matches("<ConstantElement value=\"\\d+\\.\\d+\"/>"))
					{
						Pattern form= Pattern.compile("<ConstantElement value=\"(\\d+\\.\\d+)\"/>");
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
						((FunctionElement)formulae.peek()).addArgument(elem);			//The current element is added to the function on top of the stack
					else if(formulae.isEmpty()&&elem!=null)
					{
						out.put(currentFormula, elem);			//The final element is added to the map of read formulae
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
		//Adding read formulas to calculator formula list and confirming with user if formula name already exists 
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
	 * @return Whether the differentiation type is symbolic or not.
	 */
	public boolean toggleDiff()
	{
		symbolic=!symbolic;
		return symbolic;
	}
}

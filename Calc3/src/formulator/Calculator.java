package formulator;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;


public class Calculator {
	public  HashMap<String, FormulaElement> formulas;
	
	public Calculator()
	{
		formulas=new HashMap<String, FormulaElement>();
		formulas.put("f", new VariableElement("x"));
	}
	
	public String branch(String text)
	{
		if(text.equals("save"))
		{
			if(ReadWriteFormulae.WriteFormulae())
				return "Successfully written to file";
			else
				return "Unable to write to file";
		}
		else if(text.equals("load"))
		{
			if(ReadWriteFormulae.ReadFormulae())
				return "Successfully read from file";
			else
				return "Unable to read from file";
		}
		else if(text.matches("^graph(\\s+\\w+\\(\\w+=\\d+(\\.\\d+)?,\\s?\\d+(\\.\\d+)?(,\\s?\\d+(\\.\\d+)?)?(,\\s?\\w+=\\d+(\\.\\d+)?)*\\))+"))
		{
			Vector<GraphFunction> graphs=new Vector<GraphFunction>();
			
			Pattern form= Pattern.compile("\\w+\\(\\w+=\\d+(\\.\\d+)?,\\s?\\d+(\\.\\d+)?(,\\s?\\d+(\\.\\d+)?)?(,\\s?\\w+=\\d+(\\.\\d+)?)*\\)");
			Matcher m = form.matcher(text);
			Vector<String> formv=new Vector<String>();
			while(m.find()==true)
			{
				formv.add(m.group(0));
			}
			for(String s:formv)
			{
				Vector<String> results=new Vector<String>();
				form= Pattern.compile("(\\w+)\\((\\w+)=(\\d+(\\.\\d+)?),\\s?(\\d+(\\.\\d+)?)(,\\s?(\\d+(\\.\\d+)?))?(.*)\\)");
				m = form.matcher(s);
				m.find();
				FormulaElement root=null;
				String var=null;
				double min=Double.parseDouble(m.group(3));
				double max=Double.parseDouble(m.group(5));
				double increment=1;
				for(int i=1;i<m.groupCount()+1;i++)
				{
					results.add("b"+m.group(i));
					if(i==1)
					{
						if(formulas.containsKey(m.group(1)))
							root=formulas.get(m.group(1));
						else
							return "Cannot graph "+m.group(1)+" as it does not exist";
					}
					else if(i==2)
						var=m.group(2);
					else if(i==8&&m.group(8)!=null&&m.group(8).matches("^\\d+(\\.\\d+)?$"))
						increment=Double.parseDouble(m.group(8));
					else if(i==10)
					{
						String assign=m.group(i);
						Pattern a= Pattern.compile("(\\w+)=(\\d+(\\.\\d+)?)");
						Matcher b = a.matcher(assign);
						while(b.find()==true)
						{
							String varName=b.group(1);
							double value=Double.parseDouble(b.group(2));
							root.setVariableValue(varName, value);
						}
						
					}
				}
				GraphFunction x= new GraphFunction(root, var, min, max, increment);
				graphs.add(x);
			}
			GraphControl y= new GraphControl(graphs);
			return "Formulae successfully graphed";
		}
		else if(text.equals("save graph"))
		{
			return "save graph";
		}
		else if(text.equals("load graph"))
		{
			return "load graph";
		}
		else if(text.matches("^\\w+=.+"))
		{
			Pattern form= Pattern.compile("^(\\w+)=(.+)");
			Matcher m = form.matcher(text);
			Vector<String> formv=new Vector<String>();
			m.find();
			FormulaElement newform=FormulaElement.parseFormula(m.group(2));
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
			return "Solve";
		}
	}
}

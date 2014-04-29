package formulator;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Branch extends Calculator
{
	public void branch(String text)
	{
		if(text.equals("save"))
		{
			ReadWriteFormulae.WriteFormulae();
		}
		else if(text.equals("load"))
		{
			ReadWriteFormulae.ReadFormulae();
		}
		else if(text.matches("^graph(\\s+\\w+\\(\\w+=\\d+,\\s?\\d+(,\\s?\\d+)?(,\\s?\\w+=\\d+)*\\))+"))
		{
			Vector<GraphFunction> graphs=new Vector<GraphFunction>();
			
			Pattern form= Pattern.compile("\\w+\\(\\w+=\\d+,\\s?\\d+(,\\s?\\d+)?(,\\s?\\w+=\\d+)*\\)");
			Matcher m = form.matcher(text);
			Vector<String> formv=new Vector<String>();
			while(m.find()==true)
			{
				formv.add(m.group(0));
			}
			for(String s:formv)
			{
				Vector<String> results=new Vector<String>();
				form= Pattern.compile("(\\w+)\\((\\w+)=(\\d+),\\s?(\\d+)(,\\s?(\\d+))?(,\\s?(\\w+)=(\\d+))*\\)");
				m = form.matcher(s);
				m.find();
				for(int i=1;i<m.groupCount();i++)
				{
					results.add(m.group(i));
				}
				System.out.println(results);
			}
		}
		else if(text.equals("save graph"))
		{
			
		}
		else if(text.equals("load graph"))
		{
			
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
				//MessageBox msg=new MessageBox(getShell(),SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
				int dialogResult = JOptionPane.showConfirmDialog (null, "The formula \""+m.group(1)+"\" already exists\nWould you like to overwrite?","Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult==0)
					store=true;
			}
			else
				store=true;
			if(store)
				formulas.put(m.group(1), newform);
			//Parse and store formula
		}
		else
		{
			//Parse as formula and attempt to solve
		}
	}
}

package formulator;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Branch
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
		else if(text.matches("^graph\\s+\\w+\\(\\w+(,\\w+)*\\)"))
		{
			System.out.println("graph!");
		}
		else if(text.equals("save graph"))
		{
			
		}
		else if(text.equals("load graph"))
		{
			
		}
		else if(text.matches("^\\w+\\(\\w+(,\\w+)*\\)=.+"))
		{
			Pattern form= Pattern.compile("(^\\w+\\(\\w+(,\\w+)*\\))=(.+)");
			Matcher m = form.matcher(text);
			Vector<String> formv=new Vector<String>();
			m.find();
			formv.add(m.group(1));
			formv.add(m.group(3));
			//Parse and store formula
		}
		else if(text.matches(".*\\w+'+\\(.*\\).*"))
		{
			Pattern diff= Pattern.compile("(\\w+('+)\\([^()]+\\))");
			Matcher m = diff.matcher(text);
			Vector<String> diffv=new Vector<String>();
			while (m.find())
			{
				diffv.add(""+m.group(2).length());
			    diffv.add(m.group(1));
			}
			System.out.println(diffv);
			//check formula map, diff formula and replace f'(x) with it in formula
		}
		else
		{
			//Parse as formula and attempt to solve
		}
	}
}

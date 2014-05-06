/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AnotherShell
{


    private Shell shlHelp;
    boolean active = true;
    private Text text;

    public AnotherShell()
    {
        shlHelp = new Shell(Display.getCurrent(),SWT.DIALOG_TRIM | SWT.MIN | SWT.ON_TOP);
        shlHelp.setSize(527, 409);
        shlHelp.setText("Help");
        
        text = new Text(shlHelp, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        text.setBounds(10, 10, 501, 361);
        text.setText("HELP TEXT\n");
        text.append("I. Interface Notes\n");
        text.append("\tPress enter or \"evaluate\" after each command to evaluate\n");
        text.append("\tToggle Differentiating button swaps the mode from numeric to symbolic\n\n");
        text.append("II. Formula Operations\n");
        text.append("\tFormula Declaration\n");
        text.append("\t\tf= x+4\n\n");
        text.append("\tFormula Differentiation\n");
        text.append("\t\tf'(x)\n\n");
        text.append("III. Evaluating Formulas\n");
        text.append("\tMake sure you define a formula, before attempting to evaluate in the following form:\n");
        text.append("\t\tf(3)\n\n");
        text.append("IV. Saving and Loading Formulas\n");
        text.append("\tSave any currently stored formulae to file by inputting:\n");
        text.append("\t\tsave\n\n");
        text.append("\tLoad formulae from a .xml file by inputting:\n");
        text.append("\t\tload\n\n");
        text.append("V. Graphing Formulas\n");
        text.append("\tThe following format allows you to plot a single function:\n");
        text.append("\t\tgraph f(x=0,20,1)\n");
        text.append("\t\tgraph FUNCTION_NAME(VARIABLE=MIN,MAX,INCREMENT)\n\n");
        text.append("\tMultiple variables may be involved with the function, but all but one requires static values, i.e.:\n");
        text.append("\t\tgraph f(x=0,20,1, y=3, z=8)\n\n");
        text.append("\tMultiple graphs can be plotted simultaneously by using the following format:\n");
        text.append("\t\tgraph f(x=0,20,1) g(x=-5,10,2) h(x=5,10,0.5)\n\n");
        text.append("\tPlease ensure ranges are declared from lowest and highest, to ensure that the increment value is always positive.\n\n");
        text.append("VI. Graphing Interface\n");
        text.append("\tHotkey Commands\n");
        text.append("\t1 - Toggle Labels\n");
        text.append("\t2 - Toggle Points\n");
        text.append("\t3 - Toggle Lines\n");
        text.append("\t4 - Toggle Axes");
    }


    public void open()
    {
        shlHelp.open();
        shlHelp.forceActive();
        shlHelp.setFocus();
        shlHelp.forceFocus();
    }
   

    public  void close()
    {
    	active = false;
        shlHelp.setVisible(false);
    }
}
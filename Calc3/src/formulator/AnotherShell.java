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
        text.setText("Hello");
    }


    public void open()
    {
        shlHelp.open();
        shlHelp.forceActive();
    }

    public  void close()
    {
    	active = false;
        shlHelp.setVisible(false);
    }
}
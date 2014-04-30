/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AnotherShell
{


    private Shell shell;

    public AnotherShell()
    {
        shell = new Shell(Display.getCurrent());
    }


    public void open()
    {
        shell.open();
    }

    public void close()
    {
        shell.setVisible(false);
    }
}
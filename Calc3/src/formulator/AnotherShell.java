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
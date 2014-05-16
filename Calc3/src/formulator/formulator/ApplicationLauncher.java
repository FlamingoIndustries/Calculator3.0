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
 
public class ApplicationLauncher
{
    //Creating static so that can access from splash window code
    //In production code, use event handling
    public static CalculatorUI calculator;
    public static Display display;
     
    @SuppressWarnings("unused")
    public ApplicationLauncher()
    {
		display = Display.getDefault();
		calculator = new CalculatorUI(display);
        Splash splashWindow = new Splash(display);
        
        while((Display.getCurrent().getShells().length != 0)
                 && !Display.getCurrent().getShells()[0].isDisposed())
        {
             if(!display.readAndDispatch())
             {
                 display.sleep();
             }
        }      
    }
     
    public static void main(String[] args)
    {
        new ApplicationLauncher();
    }
}
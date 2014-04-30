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
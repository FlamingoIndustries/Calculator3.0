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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
 
public class Splash {
     
    private int splashPos = 0;
 
    private final int SPLASH_MAX = 10;
 
    public Splash(Display display)
    {
        final Image image = new Image(display, "bats260.png");
 
        final Shell splash = new Shell(SWT.ON_TOP);
        final ProgressBar bar = new ProgressBar(splash, SWT.NONE);
        bar.setMaximum(SPLASH_MAX);
 
        Label label = new Label(splash, SWT.NONE);
        label.setImage(image);
        Device device = Display.getCurrent();
        Color red = new Color(device, 205, 201, 201);
        label.setBackground(red);
 
        FormLayout layout = new FormLayout();
        splash.setLayout(layout);
 
        FormData labelData = new FormData();
        labelData.right = new FormAttachment(100, 0);
        labelData.bottom = new FormAttachment(100, 0);
        label.setLayoutData(labelData);
 
        FormData progressData = new FormData();
        progressData.left = new FormAttachment(0, -5);
        progressData.right = new FormAttachment(100, 0);
        progressData.bottom = new FormAttachment(100, 0);
        bar.setLayoutData(progressData);
        splash.pack();
 
        Rectangle splashRect = splash.getBounds();
        Rectangle displayRect = display.getBounds();
        int x = (displayRect.width - splashRect.width) / 2;
        int y = (displayRect.height - splashRect.height) / 2;
        splash.setLocation(x, y);
        splash.open();
 
        display.asyncExec(new Runnable()
        {
            public void run()
            {
 
                for(splashPos = 0; splashPos < SPLASH_MAX; splashPos++)
                {
                    try {
 
                        Thread.sleep(100);
                    }
                    catch(InterruptedException e) {
 
                        e.printStackTrace();
                    }
                    bar.setSelection(splashPos);
                }
                
        		ApplicationLauncher.calculator.open();
        		ApplicationLauncher.calculator.layout();
                splash.close();
                image.dispose();
            }
       });
 
        while(splashPos != SPLASH_MAX)
        {
            if(!display.readAndDispatch())
            {
                display.sleep();
            }
        }
    }
}
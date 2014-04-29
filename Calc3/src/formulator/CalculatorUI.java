package formulator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



public class CalculatorUI extends Shell {
		private Text text;
		// The three calculator registers.
		private String displayString = "0.";
		// A variable to store the pending calculation
		private char calcChar = ' ';
		// Error strings
		private final String ERROR_STRING = "Error: ";
		private final String LONG_STRING = "Number too long";
		private final String INFINITY_STRING = "Infinity";
		// A flag to check if display should be cleared on the next keystroke
		private boolean clearDisplay = true;
		
	/**
	 * Create the shell.
	 * @param display
	 */
	public CalculatorUI(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(null);
		
		text = new Text(this, SWT.BORDER | SWT.H_SCROLL | SWT.CANCEL);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					updateDisplay('R');
				}
			}
		});
		text.setBounds(23, 10, 260, 44);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
		text.setEditable (true);
		text.setDoubleClickEnabled(false);
		text.setText(displayString);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('1');
			}
		});
		btnNewButton.setCapture(true);
		btnNewButton.setToolTipText("Numeric Pad");
		btnNewButton.setBounds(23, 69, 47, 37);
		btnNewButton.setText("1");
		
		Button button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('2');
			}
		});
		button.setText("2");
		button.setBounds(77, 69, 47, 37);
		
		Button button_1 = new Button(this, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('3');
			}
		});
		button_1.setText("3");
		button_1.setBounds(130, 69, 47, 37);
		
		Button button_2 = new Button(this, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('4');
			}
		});
		button_2.setText("4");
		button_2.setBounds(23, 112, 47, 37);
		
		Button button_3 = new Button(this, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('5');
			}
		});
		button_3.setText("5");
		button_3.setBounds(77, 112, 47, 37);
		
		Button button_4 = new Button(this, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('6');
			}
		});
		button_4.setText("6");
		button_4.setBounds(130, 112, 47, 37);
		
		Button button_5 = new Button(this, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('7');
			}
		});
		button_5.setText("7");
		button_5.setBounds(23, 155, 47, 37);
		
		Button button_6 = new Button(this, SWT.NONE);
		button_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('8');
			}
		});
		button_6.setText("8");
		button_6.setBounds(77, 155, 47, 37);
		
		Button button_7 = new Button(this, SWT.NONE);
		button_7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('9');
			}
		});
		button_7.setText("9");
		button_7.setBounds(130, 155, 47, 37);
		
		Button button_8 = new Button(this, SWT.NONE);
		button_8.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('.');
			}
		});
		button_8.setText(".");
		button_8.setBounds(23, 198, 47, 37);
		
		Button button_9 = new Button(this, SWT.NONE);
		button_9.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('0');
			}
		});
		button_9.setText("0");
		button_9.setBounds(77, 198, 47, 37);
		
		Button button_11 = new Button(this, SWT.NONE);
		button_11.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('+');
			}
		});
		button_11.setText("+");
		button_11.setBounds(183, 112, 47, 37);
		
		Button button_12 = new Button(this, SWT.NONE);
		button_12.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('-');
			}
		});
		button_12.setText("-");
		button_12.setBounds(236, 112, 47, 37);
		
		Button button_13 = new Button(this, SWT.NONE);
		button_13.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('*');
			}
		});
		button_13.setText("*");
		button_13.setBounds(236, 155, 47, 37);
		
		Button button_14 = new Button(this, SWT.NONE);
		button_14.setText("=");
		button_14.setBounds(183, 198, 100, 37);
		
		Button button_10 = new Button(this, SWT.NONE);
		button_10.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('/');
			}
		});
		button_10.setText("/");
		button_10.setBounds(183, 155, 47, 37);
		
		Button button_15 = new Button(this, SWT.NONE);
		button_15.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('^');
			}
		});
		button_15.setText("^");
		button_15.setBounds(130, 198, 47, 37);
		
		Button btnBsp = new Button(this, SWT.NONE);
		btnBsp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateDisplay('B');
			}
		});
		btnBsp.setText("DEL");
		btnBsp.setBounds(183, 69, 47, 37);
		
		Button btnClr = new Button(this, SWT.NONE);
		btnClr.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			updateDisplay('C');
		}
		});
		btnClr.setToolTipText("Clear Screen");
		btnClr.setText("CLR");
		btnClr.setBounds(236, 69, 47, 37);
		
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.setText("Open");
		
		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.setText("Save");
		
		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");
		
		Menu menu_2 = new Menu(mntmView);
		mntmView.setMenu(menu_2);
		
		MenuItem mntmStandard = new MenuItem(menu_2, SWT.CHECK);
		mntmStandard.setText("Standard");
		
		MenuItem mntmScientific = new MenuItem(menu_2, SWT.CHECK);
		mntmScientific.setText("Scientific");
		createContents();
	}

		private void updateDisplay(final char keyPressed) {
		    char keyVal = keyPressed;
		    String tempString = new String();
		    boolean doClear = false;
		    
		    if (!clearDisplay) {
		        tempString = displayString;
		      }
		    
		    switch (keyVal) {
		    case 'B': // Backspace
		      if (tempString.length() < 2) {
		        tempString = "";
		      } else {
		        tempString = tempString.substring(0, tempString.length() - 1);
		      }
		      break;
		      
		    case 'R':
		    	
		    	
		      
		    case 'C': // Clear
		      tempString = "0.";
		      calcChar = ' ';
		      doClear = true;
		      break;
		      
		    default:  // Default case is for the digits 1 through 9.
		          tempString = tempString + keyVal;
		        break;
		      }
		    
		    clearDisplay = doClear;
		    if (!displayString.equals(tempString)) {
		      displayString = tempString;
		      text.setText(displayString);
		    }
		
	}
		
		

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Formulator");
		setSize(323, 312);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

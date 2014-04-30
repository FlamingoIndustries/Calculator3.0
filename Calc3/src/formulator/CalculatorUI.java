package formulator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;



public class CalculatorUI extends Shell {
		private Text text;
		// The three calculator registers.
		private String displayString = "";
		String answer = new String();
		// A flag to check if display should be cleared on the next keystroke
		private boolean clearDisplay = true;
		private Text text_1;
		private Calculator calc;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			CalculatorUI shell = new CalculatorUI(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public CalculatorUI(Display display) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
		setLayout(null);
		calc=new Calculator();
		text = new Text(this, SWT.BORDER | SWT.H_SCROLL | SWT.CANCEL);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						text.setFocus();
						text.setSelection(text.getText().length());
					}
				});
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					updateDisplay('R');
				}
			}
		});
		text.setBounds(23, 88, 260, 44);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		text.setEditable (true);
		text.setDoubleClickEnabled(false);
		text.setText(displayString);
		
		text_1 = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text_1.setBounds(23, 10, 279, 72);
		text_1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		Button button1 = new Button(this, SWT.NONE);
		button1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('1');
			}
		});
		button1.setToolTipText("Numeric Pad");
		button1.setBounds(23, 138, 47, 37);
		button1.setText("1");
		
		Button button2 = new Button(this, SWT.NONE);
		button2.setToolTipText("Numeric Pad");
		button2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('2');
			}
		});
		button2.setText("2");
		button2.setBounds(77, 138, 47, 37);
		
		Button button3 = new Button(this, SWT.NONE);
		button3.setToolTipText("Numeric Pad");
		button3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('3');
			}
		});

		button3.setText("3");
		button3.setBounds(130, 138, 47, 37);
		
		Button button4 = new Button(this, SWT.NONE);
		button4.setToolTipText("Numeric Pad");
		button4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('4');
			}
		});
		button4.setText("4");
		button4.setBounds(23, 181, 47, 37);
		
		Button button5 = new Button(this, SWT.NONE);
		button5.setToolTipText("Numeric Pad");
		button5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('5');
			}
		});
		button5.setText("5");
		button5.setBounds(77, 181, 47, 37);
		
		Button button6 = new Button(this, SWT.NONE);
		button6.setToolTipText("Numeric Pad");
		button6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('6');
			}
		});
		button6.setText("6");
		button6.setBounds(130, 181, 47, 37);
		
		Button button7 = new Button(this, SWT.NONE);
		button7.setToolTipText("Numeric Pad");
		button7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('7');
			}
		});

		button7.setText("7");
		button7.setBounds(23, 224, 47, 37);
		
		Button button8 = new Button(this, SWT.NONE);
		button8.setToolTipText("Numeric Pad");
		button8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('8');
			}
		});
		button8.setText("8");
		button8.setBounds(77, 224, 47, 37);
		
		Button button9 = new Button(this, SWT.NONE);
		button9.setToolTipText("Numeric Pad");
		button9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('9');
			}
		});
		button9.setText("9");
		button9.setBounds(130, 224, 47, 37);
		
		Button button0 = new Button(this, SWT.NONE);
		button0.setToolTipText("Numeric Pad");
		button0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('0');
			}
		});
		
				button0.setText("0");
				button0.setBounds(77, 267, 47, 37);
		
		Button buttonDot = new Button(this, SWT.NONE);
		buttonDot.setToolTipText("Numeric Pad");
		buttonDot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('.');
			}
		});
		buttonDot.setText(".");
		buttonDot.setBounds(23, 267, 47, 37);
		
		Button buttonPlus = new Button(this, SWT.NONE);
		buttonPlus.setToolTipText("Plus");
		buttonPlus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('+');
			}
		});
		buttonPlus.setText("+");
		buttonPlus.setBounds(183, 181, 47, 37);
		
		Button buttonMinus = new Button(this, SWT.NONE);
		buttonMinus.setToolTipText("Minus");
		buttonMinus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('-');
			}
		});

		buttonMinus.setText("-");
		buttonMinus.setBounds(236, 181, 47, 37);
		
		Button buttonMultiply = new Button(this, SWT.NONE);
		buttonMultiply.setToolTipText("Multiply");
		buttonMultiply.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('*');
			}
		});

		buttonMultiply.setText("*");
		buttonMultiply.setBounds(236, 224, 47, 37);
		
		Button buttonDivide = new Button(this, SWT.NONE);
		buttonDivide.setToolTipText("Divide");
		buttonDivide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('/');
			}
		});
		buttonDivide.setText("/");
		buttonDivide.setBounds(183, 224, 47, 37);
		
		Button buttonPower = new Button(this, SWT.NONE);
		buttonPower.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('^');
			}
		});
		buttonPower.setToolTipText("Power");
		buttonPower.setText("^");
		buttonPower.setBounds(130, 267, 47, 37);
		
		Button btnBsp = new Button(this, SWT.NONE);
		btnBsp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('B');
			}
		});

		btnBsp.setText("DEL");
		btnBsp.setToolTipText("Backspace");
		btnBsp.setBounds(183, 138, 47, 37);
		
		Button btnClr = new Button(this, SWT.NONE);
		btnClr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('C');
			}
		});

		btnClr.setToolTipText("Clear Screen");
		btnClr.setText("CLR");
		btnClr.setBounds(236, 138, 47, 37);
		
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
		menu_2.setEnabled(false);
		mntmView.setMenu(menu_2);
		
		MenuItem mntmStandard = new MenuItem(menu_2, SWT.CHECK);
		mntmStandard.setSelection(true);
		mntmStandard.setText("Standard");
		
		MenuItem mntmScientific = new MenuItem(menu_2, SWT.CHECK);
		mntmScientific.setText("Scientific");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");
		
		Button button_1 = new Button(this, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay(')');
			}
		});
		button_1.setToolTipText("Right Bracket");
		button_1.setText(")");
		button_1.setBounds(77, 310, 47, 37);
		
		Button button = new Button(this, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('(');
			}
		});
		button.setToolTipText("Left Bracket");
		button.setText("(");
		button.setBounds(23, 310, 47, 37);
		
		Button btnAns = new Button(this, SWT.NONE);
		btnAns.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('A');
			}
		});
		btnAns.setToolTipText("Return Previous Result");
		btnAns.setText("Ans");
		btnAns.setBounds(130, 310, 47, 37);
		
		Button buttonEquals = new Button(this, SWT.NONE);
		buttonEquals.setToolTipText("Equals");
		buttonEquals.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('E');
			}
		});
		buttonEquals.setText("=");
		buttonEquals.setBounds(183, 267, 100, 80);
		createContents();
	}

		private void updateDisplay(final char keyPressed) {
		    char keyVal = keyPressed;
		    String tempString = new String();
		    
		    if (!clearDisplay) {
		        tempString = displayString;
		      }
		    
		    switch (keyVal) {
		    
		    case 'A': 
		    	if (answer.charAt(0) == '$'){
		    		text.append(answer.substring(1));
		    	}
		    	break;
		    	
		    	
		    case 'B': // Backspace
		    	tempString = text.getText();
		      if (tempString.length() < 2) {
		        tempString = "";
		      } else {
		        tempString = tempString.substring(0, tempString.length() - 1);
		      }
		      displayString =  tempString;
		      text.setText(displayString);
		      break;
		      
		    case 'E':
		    	String current = text.getText();
		    	
		    	String result=calc.branch(current);
		    	answer = result;
		    	text_1.append(current+"\n");
		    	text_1.append("Result:"+ result.substring(1) +"\n");
		    	text_1.setTopIndex(text_1.getLineCount()-1);
		    	tempString = "";
		    	text.setText(tempString);
		    	break;
		    	
		      
		    case 'R':
		    	String current2 = text.getText();
		    	text_1.append(current2+"\n");
		    	String result2=calc.branch(current2);
		    	answer = result2;
		    	text_1.append("Result:" +result2.substring(1));
		    	text_1.setTopIndex(text_1.getLineCount()-1);
		    	tempString = "";
		    	text.setText(tempString);
		    	break;
		     
		    	
		      
		    case 'C': // Clear
		      tempString = "";
		      text.setText("");
		      break;
		      
		    default: 
		    	tempString = ""+keyVal; 
			     displayString =  text.getText() + tempString;
			     text.setText(displayString);

		        break;
		      }
		    
		    //clearDisplay = doClear;

		
	}
		
		

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Formulator");
		setSize(314, 410);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

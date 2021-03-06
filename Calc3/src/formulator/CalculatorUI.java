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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.TraverseEvent;



public class CalculatorUI extends Shell {
		private Text text;
		String answer = new String();
		private Text text_1;
		private Calculator calc;
		private AnotherShell anothershell;

	/**
	 * Create the shell.
	 * @param display
	 */
	public CalculatorUI(Display display) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
		setLayout(null);
		calc=new Calculator();
		text = new Text(this, SWT.BORDER | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.CR) {
					updateDisplay('E');
				}
			}
		});
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
		text.setBounds(23, 88, 556, 44);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		text.setEditable (true);
		text.setDoubleClickEnabled(false);
		text.setText("");
		
		text_1 = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text_1.setBounds(23, 10, 556, 72);
		text_1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		Button button1 = new Button(this, SWT.NONE);
		button1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('1');
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
			}
		});
		buttonDivide.setText("/");
		buttonDivide.setBounds(183, 224, 47, 37);
		
		Button buttonPower = new Button(this, SWT.NONE);
		buttonPower.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('^');
				text.setSelection(text.getText().length());
			}
		});
		buttonPower.setToolTipText("Power");
		buttonPower.setText("^");
		buttonPower.setBounds(368, 181, 47, 37);
		
		Button btnBsp = new Button(this, SWT.NONE);
		btnBsp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('B');
				text.setSelection(text.getText().length());
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
				text.setSelection(text.getText().length());
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
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.append("\n >" + calc.branch("load") + "\n");
			}
		});
		mntmOpen.setText("Load Formula");
		
		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.append("\n >" + calc.branch("save") + "\n");
			}
		});
		
		mntmSave.setText("Save Formula");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				anothershell= new AnotherShell();
				anothershell.open();
				
				}
		});
		mntmHelp.setText("Help");
		
		Button buttonLPar = new Button(this, SWT.NONE);
		buttonLPar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('(');
				text.setSelection(text.getText().length());
			}
		});
		buttonLPar.setToolTipText("Left Bracket");
		buttonLPar.setText("(");
		buttonLPar.setBounds(315, 138, 47, 37);
		
		Button buttonRPar = new Button(this, SWT.NONE);
		buttonRPar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay(')');
				text.setSelection(text.getText().length());
			}
		});
		buttonRPar.setToolTipText("Right Bracket");
		buttonRPar.setText(")");
		buttonRPar.setBounds(368, 138, 47, 37);
		
		Button btnAns = new Button(this, SWT.NONE);
		btnAns.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('A');
				text.setSelection(text.getText().length());
			}
		});
		btnAns.setToolTipText("Return Previous Result");
		btnAns.setText("Ans");
		btnAns.setBounds(130, 267, 47, 37);
		
		Button buttonEquals = new Button(this, SWT.NONE);
		buttonEquals.setToolTipText("Equals");
		buttonEquals.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('=');
				text.setSelection(text.getText().length());
			}
		});
		buttonEquals.setText("=");
		buttonEquals.setBounds(183, 267, 100, 37);
		
		Button btnX = new Button(this, SWT.NONE);
		btnX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('x');
				text.setSelection(text.getText().length());
			}
		});
		btnX.setToolTipText("Variable Name");
		btnX.setText("x");
		btnX.setBounds(421, 138, 47, 37);
		
		Button btnY = new Button(this, SWT.NONE);
		btnY.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('y');
				text.setSelection(text.getText().length());
			}
		});
		btnY.setToolTipText("Variable Name");
		btnY.setText("y");
		btnY.setBounds(421, 181, 47, 37);
		
		Button btnSin = new Button(this, SWT.NONE);
		btnSin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('S');
				text.setSelection(text.getText().length());
			}
		});
		btnSin.setToolTipText("Sin");
		btnSin.setText("sin");
		btnSin.setBounds(315, 181, 47, 37);
		
		Button btnCos = new Button(this, SWT.NONE);
		btnCos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('I');
				text.setSelection(text.getText().length());
			}
		});
		btnCos.setToolTipText("Cos");
		btnCos.setText("cos");
		btnCos.setBounds(315, 224, 47, 37);
		
		Button btnAbs = new Button(this, SWT.NONE);
		btnAbs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('O');
				text.setSelection(text.getText().length());
			}
		});
		btnAbs.setToolTipText("Abs");
		btnAbs.setText("abs");
		btnAbs.setBounds(315, 267, 47, 37);
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		label.setBounds(289, 138, 23, 166);
		
		Button btnGraph = new Button(this, SWT.NONE);
		btnGraph.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('G');
			}
		});
		btnGraph.setToolTipText("Graph the function");
		btnGraph.setText("Graph Func.");
		btnGraph.setBounds(368, 224, 100, 37);
		
		Button btnSpace = new Button(this, SWT.NONE);
		btnSpace.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay(' ');
				text.setSelection(text.getText().length());
			}
		});
		btnSpace.setToolTipText("Space");
		btnSpace.setText("Space");
		btnSpace.setBounds(368, 267, 100, 37);
		
		Button btnEvaluate = new Button(this, SWT.NONE);
		btnEvaluate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				updateDisplay('E');
			}
		});
		btnEvaluate.setToolTipText("Evaluate the function");
		btnEvaluate.setText("Evaluate");
		btnEvaluate.setBounds(474, 181, 105, 123);
		
		Button btnNumDiff = new Button(this, SWT.NONE);
		btnNumDiff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				text_1.append("Symbolic Differentiation:" + calc.toggleDiff()+ "\n" );
			
			}
		});
		btnNumDiff.setToolTipText("Toggle Between Symbolic and Numeric Differentiation");
		btnNumDiff.setText("Toggle Diff.");
		btnNumDiff.setBounds(474, 138, 105, 37);
		createContents();
	}

		private void updateDisplay(final char keyPressed) {
		    char keyVal = keyPressed;
		    String tempString = new String();
		    
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
		      text.setText(tempString);
		      break;
		      
		      
		    case 'E':
		    	String current = text.getText();
		    	String result=calc.branch(current);
		    	answer = result;
		    	text_1.append(current+"\n");
		    	if (!result.equals("")&&result.charAt(0) == '$'){
		    		text_1.append(">"+ result.substring(1) +"\n");
		    	}
		    	else {
		    		text_1.append(">"+ result+"\n");
		    	}
		    	text_1.setTopIndex(text_1.getLineCount()-1);
		    	text.setText("");
		    	Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						text.setFocus();
						text.setSelection(text.getText().length());
						updateDisplay('C');
					}
				});
		    	break;
		    	
		    case 'G':
		    	String current3 = ("graph " + text.getText());
		    	text_1.append(current3+"\n");
		    	String result3 =calc.branch(current3);
		    	text_1.append(">"+result3+"\n");
		    	text.setText("");
		    	break;
		    
		    case 'S':
		    	text.append("sin(");
		    	break;
		    	
		    case 'I':
		    	text.append("cos(");
		    	break;
		    	
		    case 'O':
		    	text.append("abs(");
		    	break;
		      
		    case 'C': // Clear
		      text.setText("");
		      break;
		      
		    default: 
		    	tempString = ""+keyVal;
		    	tempString =  text.getText() + tempString;
			    text.setText(tempString);
		        break;
		      }

		
	}
		
		

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Formulator");
		setSize(598, 363);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
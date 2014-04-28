package formulator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Cartesian {

}

@SuppressWarnings("serial")
class CartesianFrame extends JFrame {
	CartesianPanel panel;
	boolean labels;
	boolean dots;
	boolean lines;
	FormulaElement input;
	double min;
	double max;
	double increment;
	
	public CartesianFrame(FormulaElement input2, double min2, double max2, double increment2, boolean label_tog, boolean dots_tog, boolean lines_tog) {
		labels = label_tog;
		dots = dots_tog;
		lines = lines_tog;
		min = min2;
		max = max2;
		input = input2;
		increment = increment2;
		panel = new CartesianPanel(input, min, max, increment, labels, dots, lines);
		add(panel);
		//KEY BINDINGS 
        
        //Actions taken by KeyBindings
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "one");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "two");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "three");

        panel.getActionMap().put("one", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("one");
                labels = !labels;
        		panel = new CartesianPanel(input, min, max, increment, labels, dots, lines);
        		add(panel);
				showUI();
            }
        });
        panel.getActionMap().put("two", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("two");
                dots = !dots;
        		panel = new CartesianPanel(input, min, max, increment, labels, dots, lines);
        		add(panel);
				showUI();
            }
        });
        panel.getActionMap().put("three", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("three");
                lines = !lines;
        		panel = new CartesianPanel(input, min, max, increment, labels, dots, lines);
        		add(panel);
				showUI();
            }
        });
        
	}
	
	
	public void showUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cartesian");
		setSize(700, 700);
		setVisible(true);
	}
}

@SuppressWarnings("serial")
class CartesianPanel extends JPanel {
	FormulaElement node;
	double range_min;
	double range_max;
	double range_total;
	boolean labels;
	boolean dots;
	boolean lines;
	double increment;

	public CartesianPanel(FormulaElement input, double min, double max, double incre, boolean label_tog, boolean dot_tog, boolean lines_tog) {
		node = input;
		range_min = min;
		range_max = max;
		increment = incre;
		
		if (min < 0) {
			range_total = max - min;
		} else {
			range_total = max;
		}
		labels = label_tog;
		dots = dot_tog;
		lines = lines_tog;
	}

	// x-axis coord constants
	public static final int X_AXIS_FIRST_X_COORD = 50;
	public static final int X_AXIS_SECOND_X_COORD = 600;
	public static int X_AXIS_Y_COORD = 600;

	// y-axis coord constants
	public static final int Y_AXIS_FIRST_Y_COORD = 50;
	public static final int Y_AXIS_SECOND_Y_COORD = 600;
	public static int Y_AXIS_X_COORD = 50;

	// arrows of axis are represented with "hypotenuse" of
	// triangle
	// now we are define length of cathetas of that triangle
	public static final int FIRST_LENGHT = 10;
	public static final int SECOND_LENGHT = 5;

	// size of start coordinate lenght
	public static final int ORIGIN_COORDINATE_LENGHT = 6;

	// distance of coordinate strings from axis
	public static final int AXIS_STRING_DISTANCE = 20;

	public void paintComponent(Graphics g) {

		Vector<Point> points = new Vector<Point>();
		double x;
		double y;
		double max_x = 0;
		double min_x = 0;
		double max_y = 0, min_y = 0;
		System.out.println(increment);
		// Find points
		// Static value range currently, will sub in user input later.
		for (x = range_min; x <= range_max; x+= increment) {
			((FormulaElement) node).setVariableValue("x", x);
			y = node.evaluate();
			Point current = new Point(x, y);
			if (max_x < x) {
				max_x = x;
			}
			if (min_x > x) {
				min_x = x;
			}
			if (max_y < y) {
				max_y = y;
			}
			if (min_y > y) {
				min_y = y;
			}
			points.add(current);
			//String t = "(" + px + ", " + py + ")";

		}

		// finding ranges
		double xt = (max_x + Math.abs(min_x));
		double yt =  (max_y + Math.abs(min_y));
		double largest = Math.max(xt, yt);
		double xCoordNumbers = largest;
		double yCoordNumbers = largest;
		System.out.println("largest:" + largest);

		double xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
				/ xCoordNumbers;
		double yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
				/ yCoordNumbers;

		int x_meets_y = 50;
		int y_meets_x = 600;
		if (min_x < 0) {
			double r = X_AXIS_FIRST_X_COORD + (yLength * -(min_x));
			Y_AXIS_X_COORD = (int) r;
			x_meets_y = (int) r;
		}

		if (min_y < 0) {
			double r = 0;
			System.out.println((min_y));
			r = Y_AXIS_SECOND_Y_COORD - (yLength * -(min_y));
			System.out.println(Y_AXIS_SECOND_Y_COORD);
			System.out.println(yLength);
			X_AXIS_Y_COORD = (int) r;
			y_meets_x = (int) r;
		}

		System.out.println("Min_x: " + min_x + "Min_y" + min_y);
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// x-axis
		g2.drawLine(X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD,
				X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);
		// y-axis
		g2.drawLine(Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD, Y_AXIS_X_COORD,
				Y_AXIS_SECOND_Y_COORD);

		// draw text "X" and draw text "Y"
		g2.drawString("X", X_AXIS_SECOND_X_COORD + 20, X_AXIS_Y_COORD - 15
				+ AXIS_STRING_DISTANCE);
		g2.drawString("Y", Y_AXIS_X_COORD + 15 - AXIS_STRING_DISTANCE,
				Y_AXIS_FIRST_Y_COORD - 20 + AXIS_STRING_DISTANCE / 2);
		

		// if range extends over 30, draw every 5th.
		// if over 70, draw every tenth.
		int j;
		int div_factor = 1;
		if(largest>=30){
			div_factor =5;
		}
		if(largest>=70){
			div_factor =10;
		}
		if (range_min < 0) {
			j = (int) range_min;
		} else {
			j = 0;
		}
		
		// draw x-axis numbers
		for (int i = 0; i <= largest; i++) {
				if (i % div_factor == 0) {
					g2.drawString(Integer.toString(j), (int) (X_AXIS_FIRST_X_COORD
							+ (i * xLength) - 3), X_AXIS_Y_COORD
							+ AXIS_STRING_DISTANCE);
				}
				Shape l = new Line2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength),
						X_AXIS_Y_COORD - SECOND_LENGHT, X_AXIS_FIRST_X_COORD
						+ (i * xLength), X_AXIS_Y_COORD + SECOND_LENGHT);
                g2.draw(l);
				j++;
			} 

		// draw y-axis numbers
		System.out.println("RANGE_MIN: " + min_y);

		if (min_y < 0) {
			j = (int) min_y;
			System.out.println("RANGE_MIN: " + min_y);
		} else {
			j = 0;
		}
		for (int i = 0; i <= largest; i++) {
			
				if (i % div_factor == 0) {
					g2.drawString(Integer.toString(j), Y_AXIS_X_COORD
							- AXIS_STRING_DISTANCE, (int) (Y_AXIS_SECOND_Y_COORD
							- (i * yLength)));
				}
				Shape l = new Line2D.Double(Y_AXIS_X_COORD - SECOND_LENGHT,
						Y_AXIS_SECOND_Y_COORD - (i * yLength), Y_AXIS_X_COORD
						+ SECOND_LENGHT, Y_AXIS_SECOND_Y_COORD
						- (i * yLength));
                g2.draw(l);
				j++;
			} 
		
		//Draw Origin point *uses doubles
		g2.fill(new Ellipse2D.Double(
				x_meets_y - (ORIGIN_COORDINATE_LENGHT / 2), y_meets_x
						- (ORIGIN_COORDINATE_LENGHT / 2),
				ORIGIN_COORDINATE_LENGHT, ORIGIN_COORDINATE_LENGHT));
		
		double old_dotx=0;
		double old_doty=0;
		
		// Save it to the vector, then draw...
		for (int i = 0; i < points.size(); i++) {
			// Draw points
			double px = points.get(i).getx();
			double py = points.get(i).gety();
			double dotx = (50 + (px * xLength));
			double doty =  (600 - (py * yLength));

			if (x_meets_y != 0) {
				dotx = (x_meets_y + (px * xLength));
			}
			if (y_meets_x != 0) {
				doty =  (y_meets_x - (py * yLength));
			}

			System.out.println("Pixel values for x,y: " + dotx + ", " + doty);
			System.out.println("meets: " + x_meets_y + ", " + y_meets_x);
			if(dots == true){
				g2.fill(new Ellipse2D.Double((dotx - (ORIGIN_COORDINATE_LENGHT/2)), doty - (ORIGIN_COORDINATE_LENGHT/2), ORIGIN_COORDINATE_LENGHT,
					ORIGIN_COORDINATE_LENGHT));
			}
			
			// Draw labels
			if(labels  == true){
				
			String t = "(" + Math.round(px) + ", " + Math.round(py) + ")";
			System.out.println(t);
			g2.drawString(t, (int) dotx + 12, (int) doty);
			}
			
			//Draw lines
			if(i>0 && lines == true){
				Shape l = new Line2D.Double(old_dotx, old_doty, dotx ,doty);
                g2.draw(l);
			}
			old_dotx = dotx;
			old_doty = doty;
		}
		
		
		 
	}
}
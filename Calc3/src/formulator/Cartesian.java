package formulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class Cartesian {

}

@SuppressWarnings("serial")
class CartesianFrame extends JFrame {
	CartesianPanel panel;
	boolean labels;
	boolean dots;
	boolean lines;
	Vector<GraphFunction> graphs = new Vector<GraphFunction>();

	public CartesianFrame(Vector<GraphFunction> graphs_in, boolean label_tog,
			boolean dots_tog, boolean lines_tog) {
		labels = label_tog;
		dots = dots_tog;
		lines = lines_tog;
		graphs = graphs_in;
		panel = new CartesianPanel(graphs, labels, dots, lines);
		add(panel);
		// KEY BINDINGS

		// Actions taken by KeyBindings
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "one");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "two");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "three");

		panel.getActionMap().put("one", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				labels = !labels;
				panel = new CartesianPanel(graphs, labels, dots, lines);
				add(panel);
				showUI();
			}
		});
		panel.getActionMap().put("two", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dots = !dots;
				panel = new CartesianPanel(graphs, labels, dots, lines);
				add(panel);
				showUI();
			}
		});
		panel.getActionMap().put("three", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lines = !lines;
				panel = new CartesianPanel(graphs, labels, dots, lines);
				add(panel);
				showUI();
			}
		});
		
		ActionListener actionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		        System.out.println("I was selected.");
		      }
		    };

		    MouseListener mouseListener = new MouseAdapter() {
		      public void mousePressed(MouseEvent mouseEvent) {
		        int modifiers = mouseEvent.getModifiers();
		        if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
		          // Mask may not be set properly prior to Java 2
		          // See SwingUtilities.isLeftMouseButton() for workaround
		          System.out.println("Left button pressed.");
		        }
		        if ((modifiers & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK) {
		          System.out.println("Middle button pressed.");
		        }
		        if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
		          System.out.println("Right button pressed.");
		        }
		      }
		    };
			JButton button = new JButton("Save Graph");
			button.setLayout(null);
		    button.addActionListener(actionListener);
		    button.addMouseListener(mouseListener);
		    button.setSize(80,20);
		    button.setBounds(60, 300, 220, 30); 
		    button.setVerticalAlignment(SwingConstants.BOTTOM);
		    button.setHorizontalAlignment(SwingConstants.RIGHT);
		    panel.add(button);
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
	Vector<GraphFunction> graphs;

	// x-axis coord constants
	public static final int X_AXIS_FIRST_X_COORD = 50;
	public static final int X_AXIS_SECOND_X_COORD = 600;
	public static int X_AXIS_Y_COORD = 600;

	// y-axis coord constants
	public static final int Y_AXIS_FIRST_Y_COORD = 50;
	public static final int Y_AXIS_SECOND_Y_COORD = 600;
	public static int Y_AXIS_X_COORD = 50;

	// size of axis indents
	public static final int FIRST_LENGHT = 10;
	public static final int SECOND_LENGHT = 5;

	// size of start coordinate lenght
	public static final int ORIGIN_COORDINATE_LENGHT = 6;

	// distance of coordinate strings from axis
	public static final int AXIS_STRING_DISTANCE = 20;

	public CartesianPanel(Vector<GraphFunction> graphs_in, boolean label_tog,
			boolean dot_tog, boolean lines_tog) {
		labels = label_tog;
		dots = dot_tog;
		lines = lines_tog;
		graphs = graphs_in;
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		double x;
		double y;
		double max_y = 0, min_y = 0, max_x = 0, min_x = 0;
		String variable = "";
		int xCoordNumbers = 0;
		int yCoordNumbers = 0;

		// Cycle through the GraphFunction objects, find all the points and then
		// work out scales and ranges.
		for (int e = 0; e < graphs.size(); e++) {
			GraphFunction graph = graphs.get(e);
			node = graph.root;
			range_min = graph.min;
			range_max = graph.max;
			increment = graph.increment;
			variable = graph.var_name;

			if (range_min < 0) {
				range_total = range_max - range_min;
			} else {
				range_total = range_max;
			}

			// Find points
			graph.points.clear();
			for (x = range_min; x <= range_max; x += increment) {
				((FormulaElement) node).setVariableValue(variable, x);

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
				graph.points.add(current);
				// String t = "(" + x + ", " + y + ")";
			}
		}

		// finding ranges
		double xRange = Math.ceil(max_x);
		double yRange = Math.ceil(max_y);
		if (min_x < 0) {
			xRange = Math.ceil(max_x) + Math.abs(Math.floor(min_x));
		}
		if (min_y < 0) {
			yRange = Math.ceil(max_y) + Math.abs(Math.floor(min_y));
		}

		xCoordNumbers = (int) xRange;
		yCoordNumbers = (int) yRange;

		int xLength = (X_AXIS_SECOND_X_COORD - X_AXIS_FIRST_X_COORD)
				/ xCoordNumbers;
		int yLength = (Y_AXIS_SECOND_Y_COORD - Y_AXIS_FIRST_Y_COORD)
				/ yCoordNumbers;

		double x_meets_y = 50;
		double y_meets_x = 600;

		// Find where axes meet
		if (min_x < 0) {
			double r = X_AXIS_FIRST_X_COORD + (xLength * -(min_x));
			Y_AXIS_X_COORD = (int) r;
			x_meets_y = r;
		}
		if (min_y < 0) {
			double r = 0;
			r = Y_AXIS_SECOND_Y_COORD - (yLength * -(Math.floor(min_y)));
			X_AXIS_Y_COORD = (int) r;
			y_meets_x = r;
		}

		// draw x-axis
		g2.drawLine(X_AXIS_FIRST_X_COORD, X_AXIS_Y_COORD,
				X_AXIS_SECOND_X_COORD, X_AXIS_Y_COORD);
		// draw y-axis
		g2.drawLine(Y_AXIS_X_COORD, Y_AXIS_FIRST_Y_COORD, Y_AXIS_X_COORD,
				Y_AXIS_SECOND_Y_COORD);

		// draw text "X" & text "Y"
		g2.drawString("X", X_AXIS_SECOND_X_COORD + 20, X_AXIS_Y_COORD - 15
				+ AXIS_STRING_DISTANCE);
		g2.drawString("Y", Y_AXIS_X_COORD + 15 - AXIS_STRING_DISTANCE,
				Y_AXIS_FIRST_Y_COORD - 20 + AXIS_STRING_DISTANCE / 2);

		// if range extends over 30, draw every 5th.
		// if over 70, draw every tenth.
		int j;
		int div_factor = 1;
		if (xRange >= 30) {
			div_factor = 5;
		}
		if (xRange >= 70) {
			div_factor = 10;
		}
		if (range_min < 0) {
			j = (int) range_min;
		} else {
			j = 0;
		}

		// draw x-axis numbers
		for (int i = 0; i <= xRange; i++) {
			if (i % div_factor == 0) {
				g2.drawString(Integer.toString(j), (int) (X_AXIS_FIRST_X_COORD
						+ (i * xLength) - 3), X_AXIS_Y_COORD
						+ AXIS_STRING_DISTANCE);
			}
			Shape l = new Line2D.Double(X_AXIS_FIRST_X_COORD + (i * xLength),
					(int) y_meets_x - SECOND_LENGHT, X_AXIS_FIRST_X_COORD
							+ (i * xLength), (int) y_meets_x + SECOND_LENGHT);
			g2.draw(l);
			j++;
		}

		// draw y-axis numbers
		div_factor = 1;
		if (xRange >= 30) {
			div_factor = 5;
		}
		if (xRange >= 70) {
			div_factor = 10;
		}
		j = (int) Math.floor(min_y);
		for (int i = 0; i <= yRange; i++) {

			if (i % div_factor == 0) {
				g2.drawString(Integer.toString(j), Y_AXIS_X_COORD
						- AXIS_STRING_DISTANCE,
						(int) (Y_AXIS_SECOND_Y_COORD - (i * yLength)));
			}
			Shape l = new Line2D.Double(Y_AXIS_X_COORD - SECOND_LENGHT,
					Y_AXIS_SECOND_Y_COORD - (i * yLength), Y_AXIS_X_COORD
							+ SECOND_LENGHT, Y_AXIS_SECOND_Y_COORD
							- (i * yLength));
			g2.draw(l);
			j++;
		}

		// Draw Origin point where axes meet
		g2.fill(new Ellipse2D.Double(
				x_meets_y - (ORIGIN_COORDINATE_LENGHT / 2), y_meets_x
						- (ORIGIN_COORDINATE_LENGHT / 2),
				ORIGIN_COORDINATE_LENGHT, ORIGIN_COORDINATE_LENGHT));

		double old_dotx = 0;
		double old_doty = 0;

		// For each graph, draw lines between it's points
		for (int e = 0; e < graphs.size(); e++) {
			Vector<Point> current_points = graphs.get(e).points;

			for (int i = 0; i < current_points.size(); i++) {
				// Draw points
				double px = current_points.get(i).getx();
				double py = current_points.get(i).gety();
				double dotx = (50 + (px * xLength));
				double doty = (600 - (py * yLength));

				if (x_meets_y != 0) {
					dotx = (x_meets_y + (px * xLength));
				}
				if (y_meets_x != 0) {
					doty = (y_meets_x - (py * yLength));
				}

				// System.out.println("Pixel values for x,y: " + dotx + ", " +
				// doty);
				// System.out.println("meets: " + x_meets_y + ", " + y_meets_x);

				if (e % 3 == 0) {
					g2.setPaint(Color.BLACK);
				}
				if (e % 3 == 1) {
					g2.setPaint(Color.RED);
				}
				if (e % 3 == 2) {
					g2.setPaint(Color.BLUE);
				}
				if (e % 3 == 4) {
					g2.setPaint(Color.GREEN);
				}
				if (e % 3 == 5) {
					g2.setPaint(Color.CYAN);
				}
				// Draw points
				if (dots == true) {
					g2.fill(new Ellipse2D.Double(
							(dotx - (ORIGIN_COORDINATE_LENGHT / 2)), doty
									- (ORIGIN_COORDINATE_LENGHT / 2),
							ORIGIN_COORDINATE_LENGHT, ORIGIN_COORDINATE_LENGHT));
				}

				// Draw labels
				if (labels == true) {
					String t = "(" + Math.round(px * 100.0) / 100.0 + ", "
							+ Math.round(py * 100.0) / 100.0 + ")";
					g2.drawString(t, (int) dotx + 12, (int) doty);
				}

				// Draw lines
				if (i > 0 && lines == true) {
					Shape l = new Line2D.Double(old_dotx, old_doty, dotx, doty);
					g2.draw(l);
				}
				old_dotx = dotx;
				old_doty = doty;
			}
		}
	}
}
package formulator;

import java.util.Vector;

import javax.swing.SwingUtilities;

public class GraphControl {
	/*Current bug list:
	 Rounding doubles is leaving the origin point on axes a little off.
	 Sin and Cos are broken
	 
	 SHORTCUTS:
	 1 - Toggle labels
	 2 - Toggle dots
	 3 - Toggle lines
	*/
	Vector<GraphFunction> graphlist;
	public GraphControl(Vector<GraphFunction> graphs){
		graphlist = graphs;	
	}
	public static void main
	(String[] args) {
		  SwingUtilities.invokeLater(new Runnable() {
		   
		   @Override
		   public void run() {
			   	//String str = "cos(x)";
			   	String str = "x(4/3)";
				//String str = "sin(x)";
				//String str = "(x+2)(x-(y^7)+cos(2^x))";
				FormulaElement result = (FormulaElement.parseFormula(str));
				//Pass in the root node of the formula to the CartesianFrame constructor
				double min =-5;
				double max =5;
				double incre = 1;
				Vector<GraphFunction> graphs = new Vector<GraphFunction>();
				GraphFunction test = new GraphFunction(result, "x", min, max, incre);
				graphs.add(test);
			   	String str2 = "cos(x)";

				FormulaElement result2 = (FormulaElement.parseFormula(str2));
				GraphFunction test2 = new GraphFunction(result2, "x", min, max, incre);
				graphs.add(test2);

				CartesianFrame frame = new CartesianFrame(graphs, true, true, true);
				frame.showUI();
		   }
		  });
		 }
}

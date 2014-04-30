package formulator;

import java.util.Vector;

import javax.swing.SwingUtilities;

public class GraphControl {
	
	public Vector<GraphFunction> graphlist = new Vector<GraphFunction>();
	public GraphControl(Vector<GraphFunction> graphs){
		graphlist = graphs;	
		//doGraph(graphlist);
	}
	
	//public static void doGraph(final Vector<GraphFunction> graphlist2) {
	public static void main(String args[]) {

		  SwingUtilities.invokeLater(new Runnable() {
		 
		//public Vector<GraphFunction> graphlist = graphlist2;
	  

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
			   	String str3 = "x(2/3)";

				FormulaElement result2 = (FormulaElement.parseFormula(str2));
				FormulaElement result3 = (FormulaElement.parseFormula(str3));

				GraphFunction test2 = new GraphFunction(result2, "x", min, max, incre);
				GraphFunction test3 = new GraphFunction(result3, "x", min, max, incre);
				graphs.add(test2);
				graphs.add(test3);

				CartesianFrame frame = new CartesianFrame(graphs, true, true, true);
				frame.showUI();
		   }
		  });
		 }
}

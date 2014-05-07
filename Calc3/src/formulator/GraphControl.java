package formulator;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class GraphControl {
	
	public Vector<GraphFunction> graphlist = new Vector<GraphFunction>();
	public HashMap<String, FormulaElement> formulas;
	public String text;
	public GraphControl(Vector<GraphFunction> graphs, HashMap<String, FormulaElement> map, String formulatext){
		graphlist = graphs;
		formulas = map;
		text = formulatext;
		doGraph();
	}
	
	public void doGraph() {

		  SwingUtilities.invokeLater(new Runnable() {
		 	  
		@Override
		   public void run() {
				CartesianFrame frame = new CartesianFrame(graphlist, false, true, true, true);
				frame.showUI();
				 frame.toFront();
			        frame.repaint();
		   }
		  });
		 }
}

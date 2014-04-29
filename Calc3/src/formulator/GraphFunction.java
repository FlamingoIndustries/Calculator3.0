package formulator;

import java.util.Vector;

public class GraphFunction {

public FormulaElement root;
public double min;
public double max;
public double increment;
public String var_name;
Vector<Point> points;

	public GraphFunction(FormulaElement input_root, String var, double min_in, double max_in, double incre){
		root = input_root;
		min = min_in;
		max = max_in;		
		increment = incre;
		var_name = var;
	}
	
}

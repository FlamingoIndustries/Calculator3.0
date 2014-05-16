/*
 * Group name: All Caps Bats
 * Team Members: 
 * Alan Mulhall 10335911
 * Barbara DeKegel 11702369
 * Stephen Read 11312696
 * Thomas Higgins 11322981 
 */

package formulator;

import java.util.Vector;

public class GraphFunction {

public FormulaElement root;
public double min;
public double max;
public double increment;
public String var_name;
public Vector<Point> points = new Vector<Point>();

	public GraphFunction(FormulaElement input_root, String var, double min_in, double max_in, double incre){
		root = input_root;
		min = min_in;
		max = max_in;		
		increment = incre;
		var_name = var;
	}
	
}

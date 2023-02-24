package input.components;

import java.util.Set;

import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNodeDatabase;
import utilities.io.StringUtilities;

/**
 * A basic figure consists of points, segments, and an optional description
 * 
 * Each figure has distinct points and segments (thus unique database objects).
 * 
 */
public class FigureNode implements ComponentNode
{
	protected String              _description;
	protected PointNodeDatabase   _points;
	protected SegmentNodeDatabase _segments;

	public String              getDescription()    { return _description; }
	public PointNodeDatabase   getPointsDatabase() { return _points; }
	public SegmentNodeDatabase getSegments()       { return _segments; }
	
	public FigureNode(String description, PointNodeDatabase points, SegmentNodeDatabase segments)
	{
		_description = description;
		_points = points;
		_segments = segments;
	}

	@Override
	public void unparse(StringBuilder sb, int level)
	{
		// Sample output: 
		// Figure
		// {
		// Description: _____________
		// Points
				// {
					// method call
				// }
		// Segments:
				//{
					// method call
				// }
		// }

		sb.append("Figure" + "\n" + StringUtilities.indent(level) + "{");
		
		//increment level for proper indentation when methods are called
		level++;
		
		sb.append("\n" + StringUtilities.indent(level) + "Description : " + getDescription());
		sb.append("\n" + StringUtilities.indent(level) + "Points: \n" + StringUtilities.indent(level) + "{");
		
		//method call to add points to the sb
		_points.unparse(sb, level + 1);
		
		sb.append("\n" + StringUtilities.indent(level) + "}");
		sb.append("\n" + StringUtilities.indent(level) + "Segments: \n" + StringUtilities.indent(level) + "{");
		
		//method call to add segments to the sb
		_segments.unparse(sb, level + 1);
		
		sb.append("\n" + StringUtilities.indent(level) + "}");
		
		//decrement level for proper indentation when methods are called
		level--;
		
		sb.append("\n" + StringUtilities.indent(level) + "}");
	}
	
	
	
}
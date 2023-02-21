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
		sb.append("Figure" + "/n" + StringUtilities.indent(level) + "{");
		
		level++;
		
		sb.append("/n" + StringUtilities.indent(level) + "Description : " + getDescription());
		
		sb.append("/n" + StringUtilities.indent(level) + "Points:" + "/n" + StringUtilities.indent(level) + "{");
		
		_points.unparse(sb, level + 1);
		
		sb.append("/n" + StringUtilities.indent(level) + "}");
		
		sb.append("/n" + StringUtilities.indent(level) + "Segments: " + "/n" + StringUtilities.indent(level) + "{");
		
		_segments.unparse(sb, level + 1);
		
		sb.append("/n" + StringUtilities.indent(level) + "}");
		
		level--;
		
		sb.append("/n" + StringUtilities.indent(level) + "}");
	}
	
	
	
}
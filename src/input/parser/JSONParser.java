//TODO list
//copy and paste test, do it with other files
//test exceptions when does not contain description, points, segments
//look for other places to throw exceptions
//add comments
//reformat parse into more methods

package input.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import input.components.*;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNodeDatabase;
import input.exception.ParseException;

public class JSONParser
{
	protected ComponentNode  _astRoot;

	public JSONParser()
	{
		_astRoot = null;
	}

	private void error(String message)
	{
		throw new ParseException("Parse error: " + message);
	}

	public ComponentNode parse(String str) throws ParseException
	{
		// Parsing is accomplished via the JSONTokenizer class. 
		JSONTokener tokenizer = new JSONTokener(str);
		JSONObject  JSONroot = (JSONObject)tokenizer.nextValue();
		
		JSONObject figure = new JSONObject();
		
		String description = "";
		JSONArray pointsAsJSONArray = new JSONArray();
		JSONArray segmentsAsJSONArray = new JSONArray();
		
		try {
		figure = JSONroot.getJSONObject("Figure");
		
		description = figure.getString("Description");
		pointsAsJSONArray = figure.getJSONArray("Points");
		segmentsAsJSONArray = figure.getJSONArray("Segments");
		}
		catch(JSONException e)
		{
			error("Does not contain necessary components");
		}
		
		PointNodeDatabase points = new PointNodeDatabase();
		SegmentNodeDatabase segments = new SegmentNodeDatabase();
		
		addPoints(points, pointsAsJSONArray);
		addSegments(segments,points, segmentsAsJSONArray);
		
		return new FigureNode(description, points, segments);
	}
	
	private void addPoints(PointNodeDatabase points, JSONArray pointsAsJSONArray)
	{
		for(Object point : pointsAsJSONArray)
		{
			//I had to wrap point to make it work
			points.put(convertToJavaPoint((JSONObject)point));
		}
	}
	
	private void addSegments(SegmentNodeDatabase segments, PointNodeDatabase points, JSONArray segmentsAsJSONArray)
	{
		for(int i = 0; i < segmentsAsJSONArray.length(); i++)
		{
			String keyName = segmentsAsJSONArray.getJSONObject(i).keys().next();
			
			segments.addAdjacencyList(points.getPoint(keyName), connectedNodes(segmentsAsJSONArray.getJSONObject(i), points, keyName));
			
			connectedNodes(segmentsAsJSONArray.getJSONObject(i), points, keyName);
		}
	}

	private PointNode convertToJavaPoint(JSONObject point) 
	{
		return new PointNode(point.getString("name"), point.getDouble("x"), point.getDouble("y"));
	}
	
	private List<PointNode> connectedNodes(JSONObject nodes, PointNodeDatabase points, String key)
	{
		List<PointNode> allNodes = new ArrayList<PointNode>();
		
		JSONArray connectedNodes = nodes.getJSONArray(key);
		
		for(int i = 0; i < connectedNodes.length(); i++)
		{
			allNodes.add(points.getPoint(connectedNodes.getString(i)));
		}
		
		return allNodes;
	}

}
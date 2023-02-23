//TODO list
//look for other places to throw exceptions
//add comments

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
		
		//initializes vars that will store data from JSON file
		JSONObject figure = new JSONObject();
		String description = "";
		JSONArray pointsAsJSONArray = new JSONArray();
		JSONArray segmentsAsJSONArray = new JSONArray();
		
		//stores information from the JSON file
		try {
		figure = JSONroot.getJSONObject("Figure");
		
		description = figure.getString("Description");
		pointsAsJSONArray = figure.getJSONArray("Points");
		segmentsAsJSONArray = figure.getJSONArray("Segments");
		}
		//if the file doesn't contain a necessary element, an error is thrown
		catch(JSONException e)
		{
			error("Does not contain necessary components");
		}
		
		//initializes structures that will store the converted data from the JSON file
		PointNodeDatabase points = new PointNodeDatabase();
		SegmentNodeDatabase segments = new SegmentNodeDatabase();
		
		//converts the JSON file into useable data
		addPoints(points, pointsAsJSONArray);
		addSegments(segments,points, segmentsAsJSONArray);
		
		return new FigureNode(description, points, segments);
	}
	
	private void addPoints(PointNodeDatabase points, JSONArray pointsAsJSONArray)
	{
		//populates a PointNodeDatabase with points taken from the given JSONArray
		for(Object point : pointsAsJSONArray)
			points.put(convertToJavaPoint((JSONObject)point));
	}
	
	private void addSegments(SegmentNodeDatabase segments, PointNodeDatabase points, JSONArray segmentsAsJSONArray)
	{
		for(int i = 0; i < segmentsAsJSONArray.length(); i++)
		{
			//gets the key from each object in the array
			String keyName = segmentsAsJSONArray.getJSONObject(i).keys().next();
			
			//adds segments going out from the key
			segments.addAdjacencyList(points.getPoint(keyName), connectedNodes(segmentsAsJSONArray.getJSONObject(i), points, keyName));
		}
	}

	private PointNode convertToJavaPoint(JSONObject point) 
	{
		//converts JSON data to a PointNode
		return new PointNode(point.getString("name"), point.getDouble("x"), point.getDouble("y"));
	}
	
	private List<PointNode> connectedNodes(JSONObject nodes, PointNodeDatabase points, String key)
	{
		List<PointNode> allNodes = new ArrayList<PointNode>();
		
		JSONArray connectedNodes = nodes.getJSONArray(key);
		
		//converts JSONArray 
		for(int i = 0; i < connectedNodes.length(); i++)
			allNodes.add(points.getPoint(connectedNodes.getString(i)));
		
		return allNodes;
	}

}
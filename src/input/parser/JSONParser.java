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

/**
* The JSONParser class provides functionality for parsing JSON 
* representations of geometry figures and constructing the representations with
* String descriptions, PointNodeDatabases and SegmentaNodeDatabases
*
* <p>Bugs: None
*
* @author Sean Rowland, Caden Parry, Hayes Brown
* @date 02/23/2023
*/

public class JSONParser
{
	protected ComponentNode  _astRoot;

	public JSONParser()
	{
		_astRoot = null;
	}

	/**
	 * Throw a ParseException with a given error message.
	*/
	private void error(String message)
	{
		throw new ParseException("Parse error: " + message);
	}

	/**
	 * Parse a String containing a JSON representation of a geometry figures and 
	 * construct those figures using PointNodeDatabases and SegmentNodeDatabases
	*/
	public ComponentNode parse(String str) throws ParseException
	{
		// Parsing is accomplished via the JSONTokenizer class. 
		JSONTokener tokenizer = new JSONTokener(str);
		JSONObject  JSONroot = (JSONObject)tokenizer.nextValue();
		
		// Initializes variables that will store data from JSON file
		String description = "";
		PointNodeDatabase points = new PointNodeDatabase();
		SegmentNodeDatabase segments = new SegmentNodeDatabase();
		
		// Stores information from the JSON file
		try 
		{
			JSONObject figure = JSONroot.getJSONObject("Figure");
			
			description = figure.getString("Description");
			points = convertToPoints(figure.getJSONArray("Points"));
			segments = convertToSegments(points, figure.getJSONArray("Segments"));
		}
		// If the file doesn't contain a necessary component then throw an exception
		catch(JSONException e)
		{
			error("Does not contain necessary components");
		}
		
		return new FigureNode(description, points, segments);
	}
	
	/**
	 * Take in a JSONArray representing points and return a PointNodeDatabase containing those points.
	*/
	private PointNodeDatabase convertToPoints(JSONArray pointsAsJSONArray)
	{
		PointNodeDatabase points = new PointNodeDatabase();
		
		// Populates a PointNodeDatabase with points taken from the given JSONArray
		for(Object point : pointsAsJSONArray)
			points.put(convertToPoint((JSONObject)point));
		
		return points;
	}
	
	private SegmentNodeDatabase convertToSegments(PointNodeDatabase points, JSONArray segmentsAsJSONArray)
	{
		SegmentNodeDatabase segments = new SegmentNodeDatabase();
		for(int i = 0; i < segmentsAsJSONArray.length(); i++)
		{
			//gets the key from each object in the array
			String keyName = segmentsAsJSONArray.getJSONObject(i).keys().next();
			
			//adds segments going out from the key
			segments.addAdjacencyList(points.getPoint(keyName), connectedNodes(segmentsAsJSONArray.getJSONObject(i), points, keyName));
		}
		
		return segments;
	}

	private PointNode convertToPoint(JSONObject point) 
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
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

        // TODO: Build the whole AST, check for return class object, and return the root
		
		String description = JSONroot.getString("Description");
		JSONArray pointsAsJSONArray = JSONroot.getJSONArray("Points");
		JSONArray segmentsAsJSONArray = JSONroot.getJSONArray("Segments");
		
		PointNodeDatabase points = new PointNodeDatabase();
		SegmentNodeDatabase segments = new SegmentNodeDatabase();
		
		for(Object point : pointsAsJSONArray)
		{
			//I had to wrap point to make it work
			points.put(convertToJavaPoint((JSONObject)point));
		}
				
		// Repeat for segment nodes
		//I've been working on this for like an hour. I can't figure out how to find the right starting point. maybe use .keySet?
		
		// return new FigureNode(description, points, segments);
		
	}

	private PointNode convertToJavaPoint(JSONObject point) 
	{
		return new PointNode(point.getString("name"), point.getDouble("x"), point.getDouble("y"));
	}

}
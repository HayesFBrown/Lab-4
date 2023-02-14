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
		
		PointNodeDatabase points = new PointNodeDatabase();
		
		for(Object point : pointsAsJSONArray)
		{
			//points.put(convertToJavaPoint(point));
		}
		
		// Repeat for segment nodes
		
		// return new FigureNode(description, points, segments);
		
	}

	
	// TODO: convert JSON points into actual points
	private PointNode convertToJavaPoint() 
	{
		// get JSON Object "name"
		// get JSON Object "y"
		// ....
	}

}
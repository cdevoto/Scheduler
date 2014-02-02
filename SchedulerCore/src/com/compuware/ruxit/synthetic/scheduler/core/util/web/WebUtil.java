package com.compuware.ruxit.synthetic.scheduler.core.util.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.ui.model.JsonSource;
import com.compuware.ruxit.synthetic.scheduler.core.util.JsonDataGenerator;

public class WebUtil {
	
	public static Integer getParameterAsInt(HttpServletRequest request, String paramName, boolean required)
			throws JSONException {
		String stringValue = getParameter(request, paramName, required);
		if (stringValue == null) {
			return null;
		}
		Integer intValue = intValue(request, paramName, stringValue, required);
		if (intValue == null) {
			return null;
		}
		return intValue;
	}

	public static Long getParameterAsLong(HttpServletRequest request, String paramName, boolean required)
			throws JSONException {
		String stringValue = getParameter(request, paramName, required);
		if (stringValue == null) {
			return null;
		}
		Long longValue = longValue(request, paramName, stringValue, required);
		if (longValue == null) {
			return null;
		}
		return longValue;
	}

	public static Boolean getParameterAsBoolean(HttpServletRequest request, String paramName, boolean required)
			throws JSONException {
		String stringValue = getParameter(request, paramName, required);
		if (stringValue == null) {
			return null;
		}
		return Boolean.parseBoolean(stringValue);
	}

	public static String getParameter(HttpServletRequest request, String name, boolean required)
			throws JSONException {
		String value = request.getParameter(name);
		if (value == null && required) {
			String json = generateErrorResponse(500, String.format("Expected a '%s' parameter.", name));
			request.setAttribute("json", json);
		}
		return value;
	}

	public static Integer intValue(HttpServletRequest request, String paramName,
			String stringValue, boolean required) throws JSONException {
		Integer value = null;
		try {
			value = Integer.parseInt(stringValue);
		} catch (NumberFormatException ex) {
			if (required) {
			    String json = generateErrorResponse(500, String.format("Expected the '%s' parameter to contain a positive integer value.", paramName));
			    request.setAttribute("json", json);
			}
		}
		return value;
	}

	public static Long longValue(HttpServletRequest request, String paramName,
			String stringValue, boolean required) throws JSONException {
		Long value = null;
		try {
			value = Long.parseLong(stringValue);
		} catch (NumberFormatException ex) {
			if (required) {
			    String json = generateErrorResponse(500, String.format("Expected the '%s' parameter to contain a positive integer value.", paramName));
			    request.setAttribute("json", json);
			}
		}
		return value;
	}

	public static String generateErrorResponse(int status, String message) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", status).put("message", message);
		json.put("data", new JSONObject());
		return json.toString();
	}
	
	public static <T> String generateNormalResponse (JsonDataGenerator<T> generator) throws JSONException {
		return generateNormalResponse(generator, "Operation successful.");
		
	}

	public static <T> String generateNormalResponse (JsonDataGenerator<T> generator, String message) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", 200);
		T data = generator.generate();
		json.put("data", data);
		return json.toString();
		
	}

	public static String generateNormalResponse () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", 200);
		json.put("message", "Operation successful.");
		json.put("data", new JSONObject());
		return json.toString();
	}

	public static String generateNormalResponse (String message) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", 200);
		json.put("message", message);
		json.put("data", new JSONObject());
		return json.toString();
	}
	
    public static <T extends JsonSource> String generateGetAllResponse(final List<T> jsonSources)
			throws JSONException {
		String json = generateNormalResponse(new JsonDataGenerator<JSONArray> () {

			@Override
			public JSONArray generate() throws JSONException {
				JSONArray data = new JSONArray();
				for (T script : jsonSources) {
					data.put(script.toJsonObject());
				}
				return data;
			}
		});
		return json.toString();
	}
    
    public static <T extends JsonSource> String generateGetResponse(final T script)
			throws JSONException {
		String json = generateNormalResponse(new JsonDataGenerator<JSONObject> () {

			@Override
			public JSONObject generate() throws JSONException {
				JSONObject data = script.toJsonObject();
				return data;
			}
		});
		return json.toString();
	}
    
	
}

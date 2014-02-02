package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public interface JsonSource {
	
	public JSONObject toJsonObject () throws JSONException;

}

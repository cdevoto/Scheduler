package com.compuware.ruxit.synthetic.scheduler.core.util;

import org.codehaus.jettison.json.JSONException;

public interface JsonDataGenerator <T> {
	
	public T generate () throws JSONException;

}

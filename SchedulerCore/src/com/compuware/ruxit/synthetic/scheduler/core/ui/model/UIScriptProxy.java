package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptProxy;

public class UIScriptProxy implements JsonSource {
	private ScriptProxy scriptProxy;
	
	public static Builder create () {
		return new Builder ();
	}
	
	private UIScriptProxy (Builder builder) {
		this.scriptProxy = builder.scriptProxy;
	}

	public long getId() {
		return scriptProxy.getId();
	}

	public String getName() {
		return scriptProxy.getName();
	}

	public long getLastModified() {
		return scriptProxy.getLastModified();
	}
	
	@Override
	public JSONObject toJsonObject () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("name", getName());
		json.put("lastModified", getLastModified());
		return json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((scriptProxy == null) ? 0 : scriptProxy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UIScriptProxy other = (UIScriptProxy) obj;
		if (scriptProxy == null) {
			if (other.scriptProxy != null)
				return false;
		} else if (!scriptProxy.equals(other.scriptProxy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIScriptProxy [scriptProxy=" + scriptProxy + "]";
	}
	
	public static class Builder {
		private ScriptProxy scriptProxy;
		
		private Builder () {}
		
		public Builder withScriptProxy (ScriptProxy scriptProxy) {
			this.scriptProxy = scriptProxy;
			return this;
		}
		
		public UIScriptProxy build () {
			validateNotNull("scriptProxy", scriptProxy);
			return new UIScriptProxy(this);
		}
	}
	

}

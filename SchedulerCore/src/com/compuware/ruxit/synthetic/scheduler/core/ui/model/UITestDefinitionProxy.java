package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinitionProxy;

public class UITestDefinitionProxy implements JsonSource {
	private TestDefinitionProxy testDefinitionProxy;
	
	public static Builder create () {
		return new Builder ();
	}
	
	private UITestDefinitionProxy (Builder builder) {
		this.testDefinitionProxy = builder.testDefinitionProxy;
	}

	public long getId() {
		return testDefinitionProxy.getId();
	}

	public String getName() {
		return testDefinitionProxy.getName();
	}

	public boolean isSuspended () {
		return testDefinitionProxy.isSuspended();
	}
	public long getLastModified() {
		return testDefinitionProxy.getLastModified();
	}
	
	@Override
	public JSONObject toJsonObject () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("name", getName());
		json.put("suspended", isSuspended());
		json.put("lastModified", getLastModified());
		return json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((testDefinitionProxy == null) ? 0 : testDefinitionProxy.hashCode());
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
		UITestDefinitionProxy other = (UITestDefinitionProxy) obj;
		if (testDefinitionProxy == null) {
			if (other.testDefinitionProxy != null)
				return false;
		} else if (!testDefinitionProxy.equals(other.testDefinitionProxy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UITestDefinitionProxy [testDefinitionProxy=" + testDefinitionProxy + "]";
	}
	
	public static class Builder {
		private TestDefinitionProxy testDefinitionProxy;
		
		private Builder () {}
		
		public Builder withTestDefinitionProxy (TestDefinitionProxy testDefinitionProxy) {
			this.testDefinitionProxy = testDefinitionProxy;
			return this;
		}
		
		public UITestDefinitionProxy build () {
			validateNotNull("testDefinitionProxy", testDefinitionProxy);
			return new UITestDefinitionProxy(this);
		}
	}
	

}

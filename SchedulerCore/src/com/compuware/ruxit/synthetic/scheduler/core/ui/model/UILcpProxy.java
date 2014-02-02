package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;

public class UILcpProxy implements JsonSource {
	private LcpProxy lcpProxy;
	
	public static Builder create () {
		return new Builder ();
	}
	
	private UILcpProxy (Builder builder) {
		this.lcpProxy = builder.lcpProxy;
	}

	public long getId() {
		return lcpProxy.getId();
	}

	public String getName() {
		return lcpProxy.getName();
	}
	
	@Override
	public JSONObject toJsonObject () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("name", getName());
		return json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lcpProxy == null) ? 0 : lcpProxy.hashCode());
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
		UILcpProxy other = (UILcpProxy) obj;
		if (lcpProxy == null) {
			if (other.lcpProxy != null)
				return false;
		} else if (!lcpProxy.equals(other.lcpProxy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UILcpProxy [lcpProxy=" + lcpProxy + "]";
	}

	public static class Builder {
		private LcpProxy lcpProxy;
		
		private Builder () {}
		
		public Builder withLcpProxy (LcpProxy scriptProxy) {
			this.lcpProxy = scriptProxy;
			return this;
		}
		
		public UILcpProxy build () {
			validateNotNull("lcpProxy", lcpProxy);
			return new UILcpProxy(this);
		}
	}
	

}

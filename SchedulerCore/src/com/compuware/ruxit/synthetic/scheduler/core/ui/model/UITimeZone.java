package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TimeZone;

public class UITimeZone implements JsonSource {
	private TimeZone timeZone;
	
	public static Builder create () {
		return new Builder ();
	}
	
	private UITimeZone (Builder builder) {
		this.timeZone = builder.timeZone;
	}

	public long getId() {
		return timeZone.getId();
	}

	public String getName() {
		return timeZone.getName();
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
				+ ((timeZone == null) ? 0 : timeZone.hashCode());
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
		UITimeZone other = (UITimeZone) obj;
		if (timeZone == null) {
			if (other.timeZone != null)
				return false;
		} else if (!timeZone.equals(other.timeZone))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UITimeZone [timeZone=" + timeZone + "]";
	}

	public static class Builder {
		private TimeZone timeZone;
		
		private Builder () {}
		
		public Builder withTimeZone (TimeZone scriptProxy) {
			this.timeZone = scriptProxy;
			return this;
		}
		
		public UITimeZone build () {
			validateNotNull("timeZone", timeZone);
			return new UITimeZone(this);
		}
	}
	

}

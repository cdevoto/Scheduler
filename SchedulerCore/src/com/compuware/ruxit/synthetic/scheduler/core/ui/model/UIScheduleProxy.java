package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScheduleProxy;

public class UIScheduleProxy implements JsonSource {
	private ScheduleProxy scheduleProxy;
	
	public static Builder create () {
		return new Builder ();
	}
	
	private UIScheduleProxy (Builder builder) {
		this.scheduleProxy = builder.scheduleProxy;
	}

	public long getId() {
		return scheduleProxy.getId();
	}

	public String getName() {
		return scheduleProxy.getName();
	}

	public long getLastModified() {
		return scheduleProxy.getLastModified();
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
				+ ((scheduleProxy == null) ? 0 : scheduleProxy.hashCode());
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
		UIScheduleProxy other = (UIScheduleProxy) obj;
		if (scheduleProxy == null) {
			if (other.scheduleProxy != null)
				return false;
		} else if (!scheduleProxy.equals(other.scheduleProxy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIScheduleProxy [scheduleProxy=" + scheduleProxy + "]";
	}
	
	public static class Builder {
		private ScheduleProxy scheduleProxy;
		
		private Builder () {}
		
		public Builder withScheduleProxy (ScheduleProxy scheduleProxy) {
			this.scheduleProxy = scheduleProxy;
			return this;
		}
		
		public UIScheduleProxy build () {
			validateNotNull("scheduleProxy", scheduleProxy);
			return new UIScheduleProxy(this);
		}
	}
	

}

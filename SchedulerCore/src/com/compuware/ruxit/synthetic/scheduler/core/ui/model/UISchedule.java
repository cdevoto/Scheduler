package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Schedule;

public class UISchedule implements JsonSource {
	private Schedule schedule;
	private UITimeZone timeZone;
	private List<UITestDefinitionProxy> testDefs = new LinkedList<>();
	
	public static Builder create () {
		return new Builder();
	}
	
	private UISchedule (Builder builder) {
		this.schedule = builder.schedule;
		this.timeZone = builder.timeZone;
		this.testDefs.addAll(builder.testDefs);
	}

	public Long getId() {
		return schedule.getScheduleId();
	}

	public long getTenantId() {
		return schedule.getTenantId();
	}

	public String getName() {
		return schedule.getName();
	}

	public String getRecurrenceRule() {
		return schedule.getRecurrenceRule();
	}

	public Integer getDuration() {
		return schedule.getDuration();
	}

	public boolean isMaintenance() {
		return schedule.isMaintenance();
	}

	public Long getLastModified() {
		return schedule.getLastModified();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((schedule == null) ? 0 : schedule.hashCode());
		result = prime * result
				+ ((testDefs == null) ? 0 : testDefs.hashCode());
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
		UISchedule other = (UISchedule) obj;
		if (schedule == null) {
			if (other.schedule != null)
				return false;
		} else if (!schedule.equals(other.schedule))
			return false;
		if (testDefs == null) {
			if (other.testDefs != null)
				return false;
		} else if (!testDefs.equals(other.testDefs))
			return false;
		if (timeZone == null) {
			if (other.timeZone != null)
				return false;
		} else if (!timeZone.equals(other.timeZone))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UISchedule [schedule=" + schedule + ", timeZone=" + timeZone
				+ ", testDefs=" + testDefs + "]";
	}

	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("tenantId", getTenantId());
		if (timeZone != null) {
		    json.put("timeZone", timeZone.toJsonObject());
		}
		String name = getName();
		if (name != null) {
		    json.put("name", getName());
		}
		json.put("rrule", getRecurrenceRule());
		Integer duration  = getDuration();
		if (duration != null) {
			json.put("duration", duration);
		}
		json.put("isMaintenance", isMaintenance());
		if (!this.testDefs.isEmpty()) {
			JSONArray testDefinitions = new JSONArray();
			for (UITestDefinitionProxy testDef : testDefs) {
				testDefinitions.put(testDef.toJsonObject());
			}
			json.put("testDefinitions", testDefinitions);
		}
		return json;
	}
	
	public static class Builder {
		private Schedule schedule;
		private UITimeZone timeZone;
		private List<UITestDefinitionProxy> testDefs = new LinkedList<>();
		
		private Builder () {}

		public Builder withSchedule(Schedule schedule) {
			this.schedule = schedule;
			return this;
		}

		public Builder withTimeZone(UITimeZone timeZone) {
			this.timeZone = timeZone;
			return this;
		}
		
		public Builder withTestDefintion(UITestDefinitionProxy testDef) {
			this.testDefs.add(testDef);
			return this;
		}
		
		public Builder withTestDefintions(List<UITestDefinitionProxy> testDefs) {
			this.testDefs.addAll(testDefs);
			return this;
		}

		public UISchedule build () {
			validateNotNull("schedule", schedule);
			return new UISchedule(this);
		}
		
	}

}

package com.compuware.ruxit.synthetic.scheduler.core.ui.model;


import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

public class UIScheduleForm {
	private Long scheduleId;
	private long tenantId;
	private long timezoneId;
	private String name;
	private String rrule;
	private Integer duration;
	private boolean isMaintenance;
	private List<Long> testDefinitions = new LinkedList<Long>();
	
	
	public static Builder create () {
		return new Builder();
	}
	
	private UIScheduleForm (Builder builder) {
		this.scheduleId = builder.scheduleId;
		this.tenantId = builder.tenantId;
		this.timezoneId = builder.timezoneId;
		this.name = builder.name;
		this.rrule = builder.rrule;
		this.duration = builder.duration;
		this.isMaintenance = builder.isMaintenance;
		this.testDefinitions.addAll(builder.testDefinitions);
	}
	
	public Long getScheduleId() {
		return scheduleId;
	}

	public long getTenantId() {
		return tenantId;
	}

	public long getTimezoneId() {
		return timezoneId;
	}

	public String getName() {
		return name;
	}

	public String getRecurrenceRule() {
		return rrule;
	}

	public Integer getDuration() {
		return duration;
	}

	public boolean isMaintenance() {
		return isMaintenance;
	}

	public List<Long> getTestDefinitions() {
		return new LinkedList<>(testDefinitions);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + (isMaintenance ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rrule == null) ? 0 : rrule.hashCode());
		result = prime * result
				+ ((scheduleId == null) ? 0 : scheduleId.hashCode());
		result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
		result = prime * result
				+ ((testDefinitions == null) ? 0 : testDefinitions.hashCode());
		result = prime * result + (int) (timezoneId ^ (timezoneId >>> 32));
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
		UIScheduleForm other = (UIScheduleForm) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (isMaintenance != other.isMaintenance)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rrule == null) {
			if (other.rrule != null)
				return false;
		} else if (!rrule.equals(other.rrule))
			return false;
		if (scheduleId == null) {
			if (other.scheduleId != null)
				return false;
		} else if (!scheduleId.equals(other.scheduleId))
			return false;
		if (tenantId != other.tenantId)
			return false;
		if (testDefinitions == null) {
			if (other.testDefinitions != null)
				return false;
		} else if (!testDefinitions.equals(other.testDefinitions))
			return false;
		if (timezoneId != other.timezoneId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIScheduleForm [scheduleId=" + scheduleId + ", tenantId="
				+ tenantId + ", timezoneId=" + timezoneId + ", name=" + name
				+ ", rrule=" + rrule + ", duration=" + duration
				+ ", isMaintenance=" + isMaintenance + ", testDefinitions="
				+ testDefinitions + "]";
	}

	public static class Builder {
		private Long scheduleId;
		private Long tenantId;
		private Long timezoneId;
		private String name;
		private String rrule;
		private Integer duration;
		private Boolean isMaintenance;
		private List<Long> testDefinitions = new LinkedList<Long>();
		
		private Builder () {}
		
		public Builder withScheduleId(Long scheduleId) {
            this.scheduleId = scheduleId;
            return this;
		}

		public Builder withTenantId(long tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public Builder withTimezoneId(long timezoneId) {
			this.timezoneId = timezoneId;
			return this;
		}

		public Builder withName (String name) {
			this.name = name;
			return this;
		}
		
		public Builder withRecurrenceRule(String rrule) {
			this.rrule = rrule;
			return this;
			
		}

		public Builder withDuration (Integer duration) {
			this.duration = duration;
			return this;
		}
		
		public Builder withMaintenance (boolean isMaintenance) {
			this.isMaintenance = isMaintenance;
			return this;
		}

		public Builder withTestDefinitionString (String testDefinition) {
			try {
				Long testDefinitionId = Long.parseLong(testDefinition);
			    this.testDefinitions.add(testDefinitionId);
			    return this;
			} catch (NumberFormatException ex) {
				throw new NumberFormatException("Expected a positive integer value for the 'testDefinition' parameter.");
			}
		}

		public Builder withTestDefinition (Long testDefinitionId) {
		    this.testDefinitions.add(testDefinitionId);
		    return this;
		}

		public Builder withTestDefinitions (List<Long> testDefinitionIds) {
		    this.testDefinitions.addAll(testDefinitionIds);
		    return this;
		}

		public Builder withTestDefinitionStrings (String ... testDefinitionIds) {
		    for (String testDefId : testDefinitionIds) {
		    	withTestDefinitionString(testDefId);
		    }
		    return this;
		}

		public UIScheduleForm build () {
			validateNotNull("tenantId", tenantId);
			validateNotNull("timezoneId", tenantId);
			validateNotNull("name", name);
			validateNotNull("rrule", rrule);
        	validateNotNull("isMaintenance", isMaintenance);
			return new UIScheduleForm(this);
		}


		
	}

}

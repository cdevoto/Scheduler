package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class Schedule {
	private Long scheduleId;
	private long tenantId;
	private TimeZone timeZone;
	private String name;
	private String recurrenceRule;
	private Integer duration;
	private boolean isMaintenance;
	private Long lastModified;
	
    public static Builder create () {
    	return new Builder();
    }
	
	private Schedule(Builder builder) {
		this.scheduleId = builder.scheduleId;
		this.tenantId = builder.tenantId;
		this.timeZone = builder.timeZone;
		this.name = builder.name;
		this.recurrenceRule = builder.recurrenceRule;
		this.duration = builder.duration;
		this.isMaintenance = builder.isMaintenance;
		this.lastModified = builder.lastModified;
	}
	
	public Long getScheduleId() {
		return scheduleId;
	}

	public long getTenantId() {
		return tenantId;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public String getName() {
		return name;
	}

	public String getRecurrenceRule() {
		return recurrenceRule;
	}

	public Integer getDuration() {
		return duration;
	}

	public boolean isMaintenance() {
		return isMaintenance;
	}

	public Long getLastModified() {
		return lastModified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + (isMaintenance ? 1231 : 1237);
		result = prime * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((recurrenceRule == null) ? 0 : recurrenceRule.hashCode());
		result = prime * result
				+ ((scheduleId == null) ? 0 : scheduleId.hashCode());
		result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
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
		Schedule other = (Schedule) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (isMaintenance != other.isMaintenance)
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (recurrenceRule == null) {
			if (other.recurrenceRule != null)
				return false;
		} else if (!recurrenceRule.equals(other.recurrenceRule))
			return false;
		if (scheduleId == null) {
			if (other.scheduleId != null)
				return false;
		} else if (!scheduleId.equals(other.scheduleId))
			return false;
		if (tenantId != other.tenantId)
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
		return "Schedule [scheduleId=" + scheduleId + ", tenantId=" + tenantId
				+ ", timeZone=" + timeZone + ", name=" + name
				+ ", recurrenceRule=" + recurrenceRule + ", duration="
				+ duration + ", isMaintenance=" + isMaintenance
				+ ", lastModified=" + lastModified + "]";
	}

	public static class Builder {
		private Long scheduleId;
		private long tenantId;
		private TimeZone timeZone;
		private String name;
		private String recurrenceRule;
		private Integer duration;
		private Boolean isMaintenance;
		public Long lastModified;
		
		private Builder () {}

		public Builder withScheduleId(Long scheduleId) {
			this.scheduleId = scheduleId;
			return this;
		}

		public Builder withTenantId(long tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public Builder withTimeZone(TimeZone timeZone) {
			this.timeZone = timeZone;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withRecurrenceRule(String recurrenceRule) {
			this.recurrenceRule = recurrenceRule;
			return this;
		}

		public Builder withDuration(Integer duration) {
			this.duration = duration;
			return this;
		}

		public Builder withMaintenance(Boolean isMaintenance) {
			this.isMaintenance = isMaintenance;
			return this;
		}
		
		public Builder withLastModified(Long lastModified) {
			this.lastModified = lastModified;
			return this;
		}

		public Schedule build () {
			validateNotNull("tenantId", tenantId);
			validateNotNull("recurrenceRule", recurrenceRule);
			validateNotNull("isMaintenance", isMaintenance);
			return new Schedule(this);
		}
		
		
	}

}

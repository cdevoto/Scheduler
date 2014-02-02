package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotEmpty;
import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

public class MaintScheduleView {
	
	private long scheduleId;
	private long tenantId;
	private TimeZone timeZone;
	private String name;
	private String recurrenceRule;
	private int duration;
	private long lastModified;
	private boolean deleted;
	private boolean active;
	private List<Long> testDefinitionIds = new LinkedList<>();
	
	
    public static Builder create () {
    	return new Builder();
    }
	
    public static Builder create (MaintScheduleView schedule) {
    	return new Builder(schedule);
    }

    private MaintScheduleView(Builder builder) {
		this.scheduleId = builder.scheduleId;
		this.tenantId = builder.tenantId;
		this.timeZone = builder.timeZone;
		this.name = builder.name;
		this.recurrenceRule = builder.recurrenceRule;
		this.duration = builder.duration;
		this.lastModified = builder.lastModified;
		this.deleted = builder.deleted;
		this.active = builder.active;
		this.testDefinitionIds.addAll(builder.testDefinitionIds);
	}
	
	public long getScheduleId() {
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

	public int getDuration() {
		return duration;
	}

	public long getLastModified() {
		return lastModified;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isActive() {
		return active;
	}

	public synchronized List<Long> getTestDefinitionIds() {
		return new LinkedList<>(testDefinitionIds);
	}
	
	public synchronized int numTestDefinitionIds() {
		return testDefinitionIds.size();
	}
	
	public synchronized void addTestDefinitionId (long id) {
		this.testDefinitionIds.add(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + duration;
		result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((recurrenceRule == null) ? 0 : recurrenceRule.hashCode());
		result = prime * result + (int) (scheduleId ^ (scheduleId >>> 32));
		result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
		result = prime
				* result
				+ ((testDefinitionIds == null) ? 0 : testDefinitionIds
						.hashCode());
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
		MaintScheduleView other = (MaintScheduleView) obj;
		if (active != other.active)
			return false;
		if (deleted != other.deleted)
			return false;
		if (duration != other.duration)
			return false;
		if (lastModified != other.lastModified)
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
		if (scheduleId != other.scheduleId)
			return false;
		if (tenantId != other.tenantId)
			return false;
		if (testDefinitionIds == null) {
			if (other.testDefinitionIds != null)
				return false;
		} else if (!testDefinitionIds.equals(other.testDefinitionIds))
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
		return "MaintScheduleView [scheduleId=" + scheduleId + ", tenantId="
				+ tenantId + ", timeZone=" + timeZone + ", name=" + name
				+ ", recurrenceRule=" + recurrenceRule + ", duration="
				+ duration + ", lastModified=" + lastModified + ", deleted="
				+ deleted + ", active=" + active + ", testDefinitionIds="
				+ testDefinitionIds + "]";
	}

	public static class Builder {
		private Long scheduleId;
		private Long tenantId;
		private TimeZone timeZone;
		private String name;
		private String recurrenceRule;
		private Integer duration;
		private Long lastModified;
		private Boolean deleted;
		private Boolean active;
		private List<Long> testDefinitionIds = new LinkedList<>();
		
		private Builder () {}

	    private Builder(MaintScheduleView schedule) {
			this.scheduleId = schedule.scheduleId;
			this.tenantId = schedule.tenantId;
			this.timeZone = schedule.timeZone;
			this.name = schedule.name;
			this.recurrenceRule = schedule.recurrenceRule;
			this.duration = schedule.duration;
			this.lastModified = schedule.lastModified;
			this.deleted = schedule.deleted;
			this.active = schedule.active;
			this.testDefinitionIds.addAll(schedule.testDefinitionIds);
		}

		public Builder withScheduleId(long scheduleId) {
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

		public Builder withDuration(int duration) {
			this.duration = duration;
			return this;
		}
		
		public Builder withTestDefinition(long testDefId) {
			this.testDefinitionIds.add(testDefId);
			return this;
		}

		
		public Builder withLastModified(long lastModified) {
			this.lastModified = lastModified;
			return this;
		}

		public Builder withDeleted(boolean deleted) {
			this.deleted = deleted;
			return this;
		}
		public Builder withActive(boolean active) {
			this.active = active;
			return this;
		}
		
		public MaintScheduleView build () {

			validateNotNull("scheduleId", scheduleId);
			validateNotNull("tenantId", tenantId);
			validateNotNull("timeZone", timeZone);
			validateNotNull("recurrenceRule", recurrenceRule);
			validateNotNull("duration", duration);
			validateNotNull("lastModified", lastModified);
			validateNotNull("deleted", deleted);
			validateNotNull("active", active);
			validateNotEmpty("testDefinitionIds", testDefinitionIds);

			return new MaintScheduleView(this);
		}
		
		
	}

}

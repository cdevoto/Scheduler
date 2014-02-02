package com.compuware.ruxit.synthetic.scheduler.core.ui.model;


import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotEmpty;
import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

public class UITestDefinitionForm {
	private Long testDefinitionId;
	private long scriptId;
	private long tenantId;
	private String name;
	private Long executionScheduleId;
	private String executionSchedule;
	private List<String> requiresFlags = new LinkedList<String>();
	private List<Long> lcps = new LinkedList<Long>();
	
	
	public static Builder create () {
		return new Builder();
	}
	
	private UITestDefinitionForm (Builder builder) {
		this.testDefinitionId = builder.testDefinitionId;
		this.scriptId = builder.scriptId;
		this.tenantId = builder.tenantId;
		this.name = builder.name;
		this.executionScheduleId = builder.executionScheduleId;
		this.executionSchedule = builder.executionSchedule;
		this.requiresFlags.addAll(builder.requiresFlags);
		this.lcps.addAll(builder.lcps);
	}
	
	public Long getTestDefinitionId () {
	    return testDefinitionId;	
	}

	public long getTenantId () {
	    return tenantId;	
	}
	
	public String getName() {
		return name;
	}

	public List<String> getRequiresFlags() {
		return new LinkedList<String>(requiresFlags);
	}
	
	public long getScriptId() {
		return scriptId;
	}

	public Long getExecutionScheduleId() {
		return executionScheduleId;
	}

	public String getExecutionSchedule() {
		return executionSchedule;
	}

	public List<Long> getLcps() {
		return new LinkedList<Long>(lcps);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((executionSchedule == null) ? 0 : executionSchedule
						.hashCode());
		result = prime
				* result
				+ ((executionScheduleId == null) ? 0 : executionScheduleId
						.hashCode());
		result = prime * result + ((lcps == null) ? 0 : lcps.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((requiresFlags == null) ? 0 : requiresFlags.hashCode());
		result = prime * result + (int) (scriptId ^ (scriptId >>> 32));
		result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
		result = prime
				* result
				+ ((testDefinitionId == null) ? 0 : testDefinitionId.hashCode());
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
		UITestDefinitionForm other = (UITestDefinitionForm) obj;
		if (executionSchedule == null) {
			if (other.executionSchedule != null)
				return false;
		} else if (!executionSchedule.equals(other.executionSchedule))
			return false;
		if (executionScheduleId == null) {
			if (other.executionScheduleId != null)
				return false;
		} else if (!executionScheduleId.equals(other.executionScheduleId))
			return false;
		if (lcps == null) {
			if (other.lcps != null)
				return false;
		} else if (!lcps.equals(other.lcps))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (requiresFlags == null) {
			if (other.requiresFlags != null)
				return false;
		} else if (!requiresFlags.equals(other.requiresFlags))
			return false;
		if (scriptId != other.scriptId)
			return false;
		if (tenantId != other.tenantId)
			return false;
		if (testDefinitionId == null) {
			if (other.testDefinitionId != null)
				return false;
		} else if (!testDefinitionId.equals(other.testDefinitionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UITestDefinitionForm [testDefinitionId=" + testDefinitionId
				+ ", scriptId=" + scriptId + ", tenantId=" + tenantId
				+ ", name=" + name
				+ ", executionScheduleId=" + executionScheduleId
				+ ", executionSchedule=" + executionSchedule
				+ ", requiresFlags=" + requiresFlags + ", lcps=" + lcps + "]";
	}



	public static class Builder {
		private Long testDefinitionId;
		private Long scriptId;
		private Long tenantId;
		private String name;
		private Long executionScheduleId;
		private String executionSchedule;
		private List<String> requiresFlags = new LinkedList<String>();
		private List<Long> lcps = new LinkedList<Long>();
		
		private Builder () {}
		
		public Builder withTestDefinitionId(Long testDefinitionId) {
            this.testDefinitionId = testDefinitionId;
            return this;
		}

		public Builder withScriptId(long scriptId) {
			this.scriptId = scriptId;
			return this;
		}

		public Builder withTenantId(long tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public Builder withName (String name) {
			this.name = name;
			return this;
		}
		
		public Builder withExecutionScheduleId(Long execScheduleId) {
			this.executionScheduleId = execScheduleId;
			return this;
			
		}

		public Builder withExecutionSchedule (String executionSchedule) {
			this.executionSchedule = executionSchedule;
			return this;
		}
		
		public Builder withRequiresFlag (String requiresFlag) {
			this.requiresFlags.add(requiresFlag);
			return this;
		}
		
		public Builder withLcp (String lcp) {
			try {
				Long lcpId = Long.parseLong(lcp);
			    this.lcps.add(lcpId);
			    return this;
			} catch (NumberFormatException ex) {
				throw new NumberFormatException("Expected a positive integer value for the 'lcp' parameter.");
			}
		}
		
		public Builder withLcp(long lcp) {
			this.lcps.add(lcp);
			return this;
		}

		public UITestDefinitionForm build () {
			validateNotNull("scriptId", scriptId);
			validateNotNull("tenantId", tenantId);
			validateNotNull("name", name);
			validateNotNull("executionSchedule", executionSchedule);
        	validateNotEmpty("lcps", lcps);
			return new UITestDefinitionForm(this);
		}


		
	}

}

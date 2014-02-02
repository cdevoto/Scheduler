package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotEmpty;
import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

public class TestDefinition {

	private Long testDefinitionId;
	private long scriptId;
	private long tenantId;
	private String name;
	private long requiresF;
	private Boolean suspended;
	private Long lastModified;
	private Schedule executionSchedule;
	private List<LcpProxy> lcps = new LinkedList<LcpProxy>();

	public static Builder create () {
		return new Builder();
	}
	
	private TestDefinition(Builder builder) {
		this.testDefinitionId = builder.testDefinitionId;
		this.scriptId = builder.scriptId;
		this.tenantId = builder.tenantId;
		this.name = builder.name;
		this.requiresF = builder.requiresF;
		this.suspended = builder.suspended;
		this.lastModified = builder.lastModified;
		this.executionSchedule = builder.executionSchedule;
		this.lcps.addAll(builder.lcps);
	}
	
	public Long getTestDefinitionId() {
		return testDefinitionId;
	}

	public long getScriptId() {
		return scriptId;
	}

	public long getTenantId() {
		return tenantId;
	}

	public String getName() {
		return name;
	}

	public long getRequiresF() {
		return requiresF;
	}

	public Schedule getExecutionSchedule() {
		return executionSchedule;
	}

	public List<LcpProxy> getLcps() {
		return lcps;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((executionSchedule == null) ? 0 : executionSchedule
						.hashCode());
		result = prime * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((lcps == null) ? 0 : lcps.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (requiresF ^ (requiresF >>> 32));
		result = prime * result + (int) (scriptId ^ (scriptId >>> 32));
		result = prime * result + (suspended ? 1231 : 1237);
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
		TestDefinition other = (TestDefinition) obj;
		if (executionSchedule == null) {
			if (other.executionSchedule != null)
				return false;
		} else if (!executionSchedule.equals(other.executionSchedule))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
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
		if (requiresF != other.requiresF)
			return false;
		if (scriptId != other.scriptId)
			return false;
		if (suspended != other.suspended)
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
		return "TestDefinition [testDefinitionId=" + testDefinitionId
				+ ", scriptId=" + scriptId + ", tenantId=" + tenantId
				+ ", name=" + name + ", requiresF="
				+ requiresF + ", suspended=" + suspended + ", lastModified="
				+ lastModified + ", executionSchedule=" + executionSchedule
				+ ", lcps="
				+ lcps + "]";
	}



	public static class Builder {
		private Long testDefinitionId;
		private Long scriptId;
		private Long tenantId;
		private String name;
		private Long requiresF;
		public Boolean suspended;
		public Long lastModified;
		private Schedule executionSchedule;
		private List<LcpProxy> lcps = new LinkedList<LcpProxy>();
		
		private Builder () {}
		
		private Builder (TestDefinition testDef) {
			this.testDefinitionId = testDef.testDefinitionId;
			this.scriptId = testDef.scriptId;
			this.tenantId = testDef.tenantId;
			this.name = testDef.name;
			this.requiresF = testDef.requiresF;
			this.executionSchedule = testDef.executionSchedule;
			this.lcps.addAll(testDef.lcps);
		}

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

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withRequiresF(Long requiresF) {
			this.requiresF = requiresF;
			return this;
		}

		public Builder withSuspended(Boolean suspended) {
			this.suspended = suspended;
			return this;
		}

		public Builder withLastModified(Long lastModified) {
			this.lastModified = lastModified;
			return this;
		}
		
		public Builder withExecutionSchedule(Schedule executionSchedule) {
			this.executionSchedule = executionSchedule;
			return this;
		}

		public Builder withLcp(LcpProxy lcp) {
			this.lcps.add(lcp);
			return this;
		}

		public TestDefinition build () {
        	validateNotNull("scriptId", scriptId);
        	validateNotNull("tenantId", tenantId);
        	validateNotNull("name", name);
        	validateNotNull("requiresF", requiresF);
        	validateNotNull("executionSchedule", requiresF);
        	validateNotEmpty("lcps", lcps);
        	return new TestDefinition(this);
        }
		
	}

}

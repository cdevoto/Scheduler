package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class TestPlanView {
	private long id;
	private long testDefinitionId;
	private long lcpId;
	private long scriptId;
	private long tenantId;
	private long requiresF;
	private String recurrenceRule;
	private boolean active;
	private boolean deleted;
	private long lastModified;
	
	public static Builder create () {
		return new Builder();
	}
	
	private TestPlanView (Builder builder) {
		this.id = builder.id;
		this.testDefinitionId = builder.testDefinitionId;
		this.lcpId = builder.lcpId;
		this.scriptId = builder.scriptId;
		this.tenantId = builder.tenantId;
		this.requiresF = builder.requiresF;
		this.recurrenceRule = builder.recurrenceRule;
		this.active = builder.active;
		this.deleted = builder.deleted;
		this.lastModified = builder.lastModified;
	}
	
	public long getId() {
		return id;
	}

	public long getTestDefinitionId() {
		return testDefinitionId;
	}

	public long getLcpId() {
		return lcpId;
	}

	public long getScriptId() {
		return scriptId;
	}

	public long getTenantId() {
		return tenantId;
	}

	public long getRequiresF() {
		return requiresF;
	}

	public String getRecurrenceRule() {
		return recurrenceRule;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public long getLastModified() {
		return lastModified;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
		result = prime * result + (int) (lcpId ^ (lcpId >>> 32));
		result = prime * result
				+ ((recurrenceRule == null) ? 0 : recurrenceRule.hashCode());
		result = prime * result + (int) (requiresF ^ (requiresF >>> 32));
		result = prime * result + (int) (scriptId ^ (scriptId >>> 32));
		result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
		result = prime * result
				+ (int) (testDefinitionId ^ (testDefinitionId >>> 32));
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
		TestPlanView other = (TestPlanView) obj;
		if (active != other.active)
			return false;
		if (deleted != other.deleted)
			return false;
		if (id != other.id)
			return false;
		if (lastModified != other.lastModified)
			return false;
		if (lcpId != other.lcpId)
			return false;
		if (recurrenceRule == null) {
			if (other.recurrenceRule != null)
				return false;
		} else if (!recurrenceRule.equals(other.recurrenceRule))
			return false;
		if (requiresF != other.requiresF)
			return false;
		if (scriptId != other.scriptId)
			return false;
		if (tenantId != other.tenantId)
			return false;
		if (testDefinitionId != other.testDefinitionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestPlanView [id=" + id + ", testDefinitionId="
				+ testDefinitionId + ", lcpId=" + lcpId + ", scriptId="
				+ scriptId + ", tenantId=" + tenantId + ", requiresF="
				+ requiresF + ", recurrenceRule=" + recurrenceRule
				+ ", active=" + active + ", deleted=" + deleted
				+ ", lastModified=" + lastModified + "]";
	}




	public static class Builder {
		private Long id;
		private Long testDefinitionId;
		private Long lcpId;
		private Long scriptId;
		private Long tenantId;
		private Long requiresF;
		private String recurrenceRule;
		private Boolean active;
		private Boolean deleted;
		private Long lastModified;
		
		private Builder () {}

		public Builder withId(long id) {
			this.id = id;
			return this;
		}

		public Builder withTestDefinitionId(long testDefinitionId) {
			this.testDefinitionId = testDefinitionId;
			return this;
		}

		public Builder withLcpId(long lcpId) {
			this.lcpId = lcpId;
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
		
		public Builder withRequiresF(long requiresF) {
			this.requiresF = requiresF;
			return this;
		}

		public Builder withRecurrenceRule(String recurrenceRule) {
			this.recurrenceRule = recurrenceRule;
			return this;
		}
		
		public Builder withActive(boolean active) {
			this.active = active;
			return this;
		}
		public Builder withDeleted(boolean deleted) {
			this.deleted = deleted;
			return this;
		}
		
		public Builder withLastModified(long lastModified) {
			this.lastModified = lastModified;
			return this;
		}
		public TestPlanView build () {
			validateNotNull("id", id);
			validateNotNull("testDefinitionId", testDefinitionId);
			validateNotNull("lcpId", lcpId);
			validateNotNull("scriptId", scriptId);
			validateNotNull("tenantId", tenantId);
			validateNotNull("requiresF", requiresF);
			validateNotNull("recurrenceRule", recurrenceRule);
			validateNotNull("active", active);
			validateNotNull("deleted", deleted);
			validateNotNull("lastModified", lastModified);
			
			return new TestPlanView(this);
		}
		
		
	}

}

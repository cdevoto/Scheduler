package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.CancelledStatus;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.CompletedStatus;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.Priority;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.Status;

public class TestView {

	private Long id;
	private long testDefinitionId;
	private long scriptId;
	private long tenantId;
	private long lcpId;
	private long requiresF;
	private Priority priority;
	private Status status;
	private long enqueuedAt;
	private Long dispatchedAt;
	private Long dispatchedVucId;
	private Long cancelledAt;
	private CancelledStatus cancelledStatus;
	private Long completedAt;
	private CompletedStatus completedStatus;
	private long testDefLastModified;
	private long scriptLastModified;
	private boolean suspended;
	private boolean maintSuspended;
	private boolean active;
	private boolean testDefDeleted;
	private boolean scriptDeleted;
	
	public static Builder create () {
		return new Builder();
	}
	
	public static Builder create (TestView test) {
		return new Builder(test);
	}

	private TestView (Builder builder) {
		this.id = builder.id;
		this.testDefinitionId = builder.testDefinitionId;
		this.scriptId = builder.scriptId;
		this.tenantId = builder.tenantId;
		this.lcpId = builder.lcpId;
		this.requiresF = builder.requiresF;
		this.priority = builder.priority;
		this.status = builder.status;
		this.enqueuedAt = builder.enqueuedAt;
		this.dispatchedAt = builder.dispatchedAt;
		this.dispatchedVucId = builder.dispatchedVucId;
		this.cancelledAt = builder.cancelledAt;
		this.cancelledStatus = builder.cancelledStatus;
		this.completedAt = builder.completedAt;
		this.completedStatus = builder.completedStatus;
		this.testDefLastModified = builder.testDefLastModified;
		this.scriptLastModified = builder.scriptLastModified;
		this.suspended = builder.suspended;
		this.maintSuspended = builder.maintSuspended;
		this.active = builder.active;
		this.testDefDeleted = builder.testDefDeleted;
		this.scriptDeleted = builder.scriptDeleted;

	}
	
	public Long getId() {
		return id;
	}

	public long getTestDefinitionId() {
		return testDefinitionId;
	}

	public long getScriptId() {
		return scriptId;
	}

	public long getTenantId() {
		return tenantId;
	}

	public long getLcpId() {
		return lcpId;
	}

	public long getRequiresF() {
		return requiresF;
	}

	public Priority getPriority() {
		return priority;
	}

	public Status getStatus() {
		return status;
	}

	public long getEnqueuedAt() {
		return enqueuedAt;
	}

	public Long getDispatchedAt() {
		return dispatchedAt;
	}

	public Long getDispatchedVucId() {
		return dispatchedVucId;
	}

	public Long getCancelledAt() {
		return cancelledAt;
	}

	public CancelledStatus getCancelledStatus() {
		return cancelledStatus;
	}

	public Long getCompletedAt() {
		return completedAt;
	}

	public CompletedStatus getCompletedStatus() {
		return completedStatus;
	}

	public long getTestDefLastModified() {
		return testDefLastModified;
	}

	public long getScriptLastModified() {
		return scriptLastModified;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public boolean isMaintSuspended() {
		return maintSuspended;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isTestDefDeleted() {
		return testDefDeleted;
	}

	public boolean isScriptDeleted() {
		return scriptDeleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((cancelledAt == null) ? 0 : cancelledAt.hashCode());
		result = prime * result
				+ ((cancelledStatus == null) ? 0 : cancelledStatus.hashCode());
		result = prime * result
				+ ((completedAt == null) ? 0 : completedAt.hashCode());
		result = prime * result
				+ ((completedStatus == null) ? 0 : completedStatus.hashCode());
		result = prime * result
				+ ((dispatchedAt == null) ? 0 : dispatchedAt.hashCode());
		result = prime * result
				+ ((dispatchedVucId == null) ? 0 : dispatchedVucId.hashCode());
		result = prime * result + (int) (enqueuedAt ^ (enqueuedAt >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (lcpId ^ (lcpId >>> 32));
		result = prime * result + (maintSuspended ? 1231 : 1237);
		result = prime * result
				+ ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + (int) (requiresF ^ (requiresF >>> 32));
		result = prime * result + (scriptDeleted ? 1231 : 1237);
		result = prime * result + (int) (scriptId ^ (scriptId >>> 32));
		result = prime * result
				+ (int) (scriptLastModified ^ (scriptLastModified >>> 32));
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + (suspended ? 1231 : 1237);
		result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
		result = prime * result + (testDefDeleted ? 1231 : 1237);
		result = prime * result
				+ (int) (testDefLastModified ^ (testDefLastModified >>> 32));
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
		TestView other = (TestView) obj;
		if (active != other.active)
			return false;
		if (cancelledAt == null) {
			if (other.cancelledAt != null)
				return false;
		} else if (!cancelledAt.equals(other.cancelledAt))
			return false;
		if (cancelledStatus != other.cancelledStatus)
			return false;
		if (completedAt == null) {
			if (other.completedAt != null)
				return false;
		} else if (!completedAt.equals(other.completedAt))
			return false;
		if (completedStatus != other.completedStatus)
			return false;
		if (dispatchedAt == null) {
			if (other.dispatchedAt != null)
				return false;
		} else if (!dispatchedAt.equals(other.dispatchedAt))
			return false;
		if (dispatchedVucId == null) {
			if (other.dispatchedVucId != null)
				return false;
		} else if (!dispatchedVucId.equals(other.dispatchedVucId))
			return false;
		if (enqueuedAt != other.enqueuedAt)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lcpId != other.lcpId)
			return false;
		if (maintSuspended != other.maintSuspended)
			return false;
		if (priority != other.priority)
			return false;
		if (requiresF != other.requiresF)
			return false;
		if (scriptDeleted != other.scriptDeleted)
			return false;
		if (scriptId != other.scriptId)
			return false;
		if (scriptLastModified != other.scriptLastModified)
			return false;
		if (status != other.status)
			return false;
		if (suspended != other.suspended)
			return false;
		if (tenantId != other.tenantId)
			return false;
		if (testDefDeleted != other.testDefDeleted)
			return false;
		if (testDefLastModified != other.testDefLastModified)
			return false;
		if (testDefinitionId != other.testDefinitionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestView [id=" + id + ", testDefinitionId=" + testDefinitionId
				+ ", scriptId=" + scriptId + ", tenantId=" + tenantId
				+ ", lcpId=" + lcpId + ", requiresF=" + requiresF
				+ ", priority=" + priority + ", status=" + status
				+ ", enqueuedAt=" + enqueuedAt + ", dispatchedAt="
				+ dispatchedAt + ", dispatchedVucId=" + dispatchedVucId
				+ ", cancelledAt=" + cancelledAt + ", cancelledStatus="
				+ cancelledStatus + ", completedAt=" + completedAt
				+ ", completedStatus=" + completedStatus
				+ ", testDefLastModified=" + testDefLastModified
				+ ", scriptLastModified=" + scriptLastModified + ", suspended="
				+ suspended + ", maintSuspended=" + maintSuspended
				+ ", active=" + active + ", testDefDeleted=" + testDefDeleted
				+ ", scriptDeleted=" + scriptDeleted + "]";
	}

	public static class Builder {
		private Long id;
		private Long testDefinitionId;
		private Long scriptId;
		private Long tenantId;
		private Long lcpId;
		private Long requiresF;
		private Priority priority;
		private Status status;
		private Long enqueuedAt;
		private Long dispatchedAt;
		private Long dispatchedVucId;
		private Long cancelledAt;
		private CancelledStatus cancelledStatus;
		private Long completedAt;
		private CompletedStatus completedStatus;
		private Long testDefLastModified;
		private Long scriptLastModified;
		private Boolean suspended;
		private Boolean maintSuspended;
		private Boolean active;
		private Boolean testDefDeleted;
		private Boolean scriptDeleted;
		
		private Builder () {}
		
		private Builder (TestView test) {
			this.id = test.id;
			this.testDefinitionId = test.testDefinitionId;
			this.scriptId = test.scriptId;
			this.tenantId = test.tenantId;
			this.lcpId = test.lcpId;
			this.requiresF = test.requiresF;
			this.priority = test.priority;
			this.status = test.status;
			this.enqueuedAt = test.enqueuedAt;
			this.dispatchedAt = test.dispatchedAt;
			this.dispatchedVucId = test.dispatchedVucId;
			this.cancelledAt = test.cancelledAt;
			this.cancelledStatus = test.cancelledStatus;
			this.completedAt = test.completedAt;
			this.completedStatus = test.completedStatus;
			this.testDefLastModified = test.testDefLastModified;
			this.scriptLastModified = test.scriptLastModified;
			this.suspended = test.suspended;
			this.maintSuspended = test.maintSuspended;
			this.active = test.active;
			this.testDefDeleted = test.testDefDeleted;
			this.scriptDeleted = test.scriptDeleted;
		}
		
		public Builder withId(Long id) {
			this.id = id;
		    return this;
		}

		public Builder withTestDefinitionId(Long testDefinitionId) {
			this.testDefinitionId = testDefinitionId;
		    return this;
		}

		public Builder withScriptId(Long scriptId) {
			this.scriptId = scriptId;
		    return this;
		}

		public Builder withTenantId(Long tenantId) {
			this.tenantId = tenantId;
		    return this;
		}

		public Builder withLcpId(Long lcpId) {
			this.lcpId = lcpId;
		    return this;
		}

		public Builder withRequiresF(Long requiresF) {
			this.requiresF = requiresF;
		    return this;
		}

		public Builder withPriority(Priority priority) {
			this.priority = priority;
		    return this;
		}

		public Builder withStatus(Status status) {
			this.status = status;
		    return this;
		}

		public Builder withEnqueuedAt(Long enqueuedAt) {
			this.enqueuedAt = enqueuedAt;
		    return this;
		}

		public Builder withDispatchedAt(Long dispatchedAt) {
			this.dispatchedAt = dispatchedAt;
		    return this;
		}

		public Builder withDispatchedVucId(Long dispatchedVucId) {
			this.dispatchedVucId = dispatchedVucId;
		    return this;
		}

		public Builder withCancelledAt(Long cancelledAt) {
			this.cancelledAt = cancelledAt;
		    return this;
		}

		public Builder withCancelledStatus(CancelledStatus cancelledStatus) {
			this.cancelledStatus = cancelledStatus;
		    return this;
		}

		public Builder withCompletedAt(Long completedAt) {
			this.completedAt = completedAt;
		    return this;
		}

		public Builder withCompletedStatus(CompletedStatus completedStatus) {
			this.completedStatus = completedStatus;
		    return this;
		}

		public Builder withTestDefLastModified(Long testDefLastModified) {
			this.testDefLastModified = testDefLastModified;
		    return this;
		}

		public Builder withScriptLastModified(Long scriptLastModified) {
			this.scriptLastModified = scriptLastModified;
		    return this;
		}

		public Builder withSuspended(Boolean suspended) {
			this.suspended = suspended;
		    return this;
		}

		public Builder withMaintSuspended(Boolean maintSuspended) {
			this.maintSuspended = maintSuspended;
		    return this;
		}

		public Builder withActive(Boolean active) {
			this.active = active;
		    return this;
		}

		public Builder withTestDefDeleted(Boolean testDefDeleted) {
			this.testDefDeleted = testDefDeleted;
		    return this;
		}

		public Builder withScriptDeleted(Boolean scriptDeleted) {
			this.scriptDeleted = scriptDeleted;
		    return this;
		}

		public TestView build () {
			
			validateNotNull("id", id);
			validateNotNull("testDefinitionId", testDefinitionId);
			validateNotNull("scriptId", scriptId);
			validateNotNull("tenantId", tenantId);
			validateNotNull("lcpId", lcpId);
			validateNotNull("requiresF", requiresF);
			validateNotNull("priority", priority);
			validateNotNull("status", status);
			validateNotNull("enqueuedAt", enqueuedAt);
			validateNotNull("testDefLastModified", testDefLastModified);
			validateNotNull("scriptLastModified", scriptLastModified);
			validateNotNull("testDefDeleted", testDefDeleted);
			validateNotNull("scriptDelete", scriptDeleted);
			validateNotNull("active", active);
			validateNotNull("suspended", suspended);
			validateNotNull("maintSuspended", suspended);
			return new TestView(this);
		}
	}

}

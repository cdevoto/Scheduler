package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.HashMap;
import java.util.Map;

public class Test {

	private Long id;
	private long testDefinitionId;
	private long scriptId;
	private long tenantId;
	private long lcpId;
	private long requiresF;
	private Priority priority;
	private Status status;
	private Long enqueuedAt;
	private Long dispatchedAt;
	private Long dispatchedVucId;
	private Long cancelledAt;
	private CancelledStatus cancelledStatus;
	private Long completedAt;
	private CompletedStatus completedStatus;
	
	public static Builder create () {
		return new Builder();
	}
	
	private Test (Builder builder) {
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

	public Long getEnqueuedAt() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		result = prime * result
				+ ((enqueuedAt == null) ? 0 : enqueuedAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (lcpId ^ (lcpId >>> 32));
		result = prime * result
				+ ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + (int) (requiresF ^ (requiresF >>> 32));
		result = prime * result + (int) (scriptId ^ (scriptId >>> 32));
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Test other = (Test) obj;
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
		if (enqueuedAt == null) {
			if (other.enqueuedAt != null)
				return false;
		} else if (!enqueuedAt.equals(other.enqueuedAt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lcpId != other.lcpId)
			return false;
		if (priority != other.priority)
			return false;
		if (requiresF != other.requiresF)
			return false;
		if (scriptId != other.scriptId)
			return false;
		if (status != other.status)
			return false;
		if (tenantId != other.tenantId)
			return false;
		if (testDefinitionId != other.testDefinitionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Test [id=" + id + ", testDefinitionId=" + testDefinitionId
				+ ", scriptId=" + scriptId + ", tenantId=" + tenantId
				+ ", lcpId=" + lcpId + ", requiresF=" + requiresF
				+ ", priority=" + priority + ", status=" + status
				+ ", enqueuedAt=" + enqueuedAt + ", dispatchedAt="
				+ dispatchedAt + ", dispatchedVucId=" + dispatchedVucId
				+ ", cancelledAt=" + cancelledAt + ", cancelledStatus="
				+ cancelledStatus + ", completedAt=" + completedAt
				+ ", completedStatus=" + completedStatus + "]";
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
		
		private Builder () {}
		
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

		public Test build () {
			validateNotNull("testDefinitionId", testDefinitionId);
			validateNotNull("scriptId", scriptId);
			validateNotNull("tenantId", tenantId);
			validateNotNull("lcpId", lcpId);
			validateNotNull("requiresF", requiresF);
			validateNotNull("priority", priority);
			validateNotNull("status", status);
			
			return new Test(this);
		}
	}

	public static enum Priority {
		HIGH(1),
		MEDIUM(5),
		LOW(10);
		
		private static final Priority [] all = {HIGH, MEDIUM, LOW};
		private static final Map<Integer, Priority> map = new HashMap<>();
		
		static {
			for (Priority obj : all) {
				map.put(obj.getValue(), obj);
			}
		}
		
		public static Priority get (int value) {
			return map.get(value);
		}
		
		private int value;
		
		private Priority(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}

	public static enum Status {
		ENQUEUED(1),
		DISPATCHED(2),
		CANCELLED(3),
		COMPLETED(4);
	
		private static final Status [] all = {ENQUEUED, DISPATCHED, CANCELLED, COMPLETED};
		private static final Map<Integer, Status> map = new HashMap<>();
		
		static {
			for (Status obj : all) {
				map.put(obj.getValue(), obj);
			}
		}
		
		public static Status get (int value) {
			return map.get(value);
		}
	private int value;
		
		private Status(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}

	public static enum CompletedStatus {
		SUCCESS(1),
		FAILURE(2);
	
		private static final CompletedStatus [] all = {SUCCESS, FAILURE};
		private static final Map<Integer, CompletedStatus> map = new HashMap<>();
		
		static {
			for (CompletedStatus obj : all) {
				map.put(obj.getValue(), obj);
			}
		}
		
		public static CompletedStatus get (int value) {
			return map.get(value);
		}
		private int value;
		
		private CompletedStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	public static enum CancelledStatus {
		MAINT_WINDOW(1),
		SUSPENDED(2),
		SCRIPT_DELETED(3),
		TEST_DEF_DELETED(4),
		INACTIVE(5),
		ENQUEUE_EXPIRE(6),
		DISPATCH_EXPIRE(7);
	
		private static final CancelledStatus [] all = {MAINT_WINDOW, SUSPENDED, SCRIPT_DELETED, TEST_DEF_DELETED, INACTIVE, ENQUEUE_EXPIRE, DISPATCH_EXPIRE};
		private static final Map<Integer, CancelledStatus> map = new HashMap<>();
		
		static {
			for (CancelledStatus obj : all) {
				map.put(obj.getValue(), obj);
			}
		}
		
		public static CancelledStatus get (int value) {
			return map.get(value);
		}
		private int value;
		
		private CancelledStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	

}

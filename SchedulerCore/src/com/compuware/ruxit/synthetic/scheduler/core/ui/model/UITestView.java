package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.CancelledStatus;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.CompletedStatus;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.Priority;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.Status;

public class UITestView implements JsonSource {
	private TestView test;
	private List<UIAbilityFlag> requiresFlags = new LinkedList<UIAbilityFlag>();
	
	public static Builder create () {
		return new Builder ();
	}

	private UITestView (Builder builder) {
		this.test = builder.test;
		this.requiresFlags.addAll(builder.requiresFlags);
	}

	public Long getId() {
		return test.getId();
	}

	public long getTestDefinitionId() {
		return test.getTestDefinitionId();
	}

	public long getScriptId() {
		return test.getScriptId();
	}

	public long getTenantId() {
		return test.getTenantId();
	}

	public long getLcpId() {
		return test.getLcpId();
	}

	public List<UIAbilityFlag> getRequiresFlags() {
		return new LinkedList<UIAbilityFlag>(requiresFlags);
	}

	public Priority getPriority() {
		return test.getPriority();
	}

	public Status getStatus() {
		return test.getStatus();
	}

	public long getEnqueuedAt() {
		return test.getEnqueuedAt();
	}

	public Long getDispatchedAt() {
		return test.getDispatchedAt();
	}

	public Long getDispatchedVucId() {
		return test.getDispatchedVucId();
	}

	public Long getCancelledAt() {
		return test.getCancelledAt();
	}

	public CancelledStatus getCancelledStatus() {
		return test.getCancelledStatus();
	}

	public Long getCompletedAt() {
		return test.getCompletedAt();
	}

	public CompletedStatus getCompletedStatus() {
		return test.getCompletedStatus();
	}

	public long getTestDefLastModified() {
		return test.getTestDefLastModified();
	}

	public long getScriptLastModified() {
		return test.getScriptLastModified();
	}

	public boolean isSuspended() {
		return test.isSuspended();
	}

	public boolean isMaintSuspended() {
		return test.isMaintSuspended();
	}

	public boolean isActive() {
		return test.isActive();
	}

	public boolean isTestDefDeleted() {
		return test.isTestDefDeleted();
	}

	public boolean isScriptDeleted() {
		return test.isScriptDeleted();
	}

	public JSONObject toJsonObject () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("testId", getId());
		json.put("tenantId", getTenantId());
		json.put("scriptId", getScriptId());
		json.put("scriptLastModified", getScriptLastModified());
		json.put("testDefinitionId", getTestDefinitionId());
		json.put("testDefinitionLastModified", getTestDefLastModified());
		json.put("priority", getPriority());
		json.put("enqueuedAt", getEnqueuedAt());
		JSONArray requires = new JSONArray();
		for (UIAbilityFlag requiresFlag : this.requiresFlags) {
			requires.put(requiresFlag.toJsonObject());
		}
		json.put("requires", requires);
		
		return json;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requiresFlags == null) ? 0 : requiresFlags.hashCode());
		result = prime * result + ((test == null) ? 0 : test.hashCode());
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
		UITestView other = (UITestView) obj;
		if (requiresFlags == null) {
			if (other.requiresFlags != null)
				return false;
		} else if (!requiresFlags.equals(other.requiresFlags))
			return false;
		if (test == null) {
			if (other.test != null)
				return false;
		} else if (!test.equals(other.test))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UITestView [test=" + test + ", requiresFlags=" + requiresFlags
				+ "]";
	}



	public static class Builder {
		private TestView test;
		private List<UIAbilityFlag> requiresFlags = new LinkedList<UIAbilityFlag>();
		
		private Builder () {}
		
		public Builder withTest (TestView test) {
			this.test = test;
			return this;
		}
		
		public Builder withRequiresFlag (UIAbilityFlag requiresFlag) {
			this.requiresFlags.add(requiresFlag);
			return this;
		}
		
		public Builder withRequiresFlags (List<UIAbilityFlag> requiresFlag) {
			this.requiresFlags.addAll(requiresFlag);
			return this;
		}
		
		
		public UITestView build () {
			validateNotNull("test", test);
			return new UITestView(this);
		}
	}
	

}

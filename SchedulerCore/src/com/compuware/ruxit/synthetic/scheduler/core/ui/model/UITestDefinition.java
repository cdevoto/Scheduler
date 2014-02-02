package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotEmpty;
import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinition;

public class UITestDefinition implements JsonSource {
	private TestDefinition testDefinition;
	private UISchedule execSchedule;
	private List<UIAbilityFlag> requiresFlags = new LinkedList<>();
	private List<UILcpProxy> lcps = new LinkedList<>();
	
	public static Builder create () {
		return new Builder();
	}
	
	private UITestDefinition (Builder builder) {
		this.testDefinition = builder.testDefinition;
		this.execSchedule = builder.execSchedule;
		this.requiresFlags.addAll(builder.requiresFlags);
		this.lcps.addAll(builder.lcps);
	}
	
	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("scriptId", getScriptId());
		json.put("tenantId", getTenantId());
		json.put("name", getName());
		
		JSONArray requiresFlags = new JSONArray();
		for (UIAbilityFlag requiresFlag : this.requiresFlags) {
			requiresFlags.put(requiresFlag.toJsonObject());
		}
		json.put("requiresFlags", requiresFlags);
		
		json.put("execSchedule", execSchedule.toJsonObject());
		
		JSONArray lcps = new JSONArray();
		for (UILcpProxy lcp : this.lcps) {
			lcps.put(lcp.toJsonObject());
		}
		json.put("lcps", lcps);
		
		return json;
	}

	public Long getId() {
		return testDefinition.getTestDefinitionId();
	}

	public long getScriptId() {
		return testDefinition.getScriptId();
	}

	public long getTenantId() {
		return testDefinition.getTenantId();
	}

	public String getName() {
		return testDefinition.getName();
	}

	public List<UIAbilityFlag> getRequiresFlags() {
		return new LinkedList<>(requiresFlags);
	}

	public UISchedule getExecutionSchedule() {
		return execSchedule;
	}

	public List<UILcpProxy> getLcps() {
		return new LinkedList<>(lcps);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((execSchedule == null) ? 0 : execSchedule.hashCode());
		result = prime * result + ((lcps == null) ? 0 : lcps.hashCode());
		result = prime * result
				+ ((requiresFlags == null) ? 0 : requiresFlags.hashCode());
		result = prime * result
				+ ((testDefinition == null) ? 0 : testDefinition.hashCode());
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
		UITestDefinition other = (UITestDefinition) obj;
		if (execSchedule == null) {
			if (other.execSchedule != null)
				return false;
		} else if (!execSchedule.equals(other.execSchedule))
			return false;
		if (lcps == null) {
			if (other.lcps != null)
				return false;
		} else if (!lcps.equals(other.lcps))
			return false;
		if (requiresFlags == null) {
			if (other.requiresFlags != null)
				return false;
		} else if (!requiresFlags.equals(other.requiresFlags))
			return false;
		if (testDefinition == null) {
			if (other.testDefinition != null)
				return false;
		} else if (!testDefinition.equals(other.testDefinition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UITestDefinition [testDefinition=" + testDefinition
				+ ", execSchedule=" + execSchedule
				+ ", requiresFlags="
				+ requiresFlags + ", lcps=" + lcps + "]";
	}

	public static class Builder {
		private TestDefinition testDefinition;
		private UISchedule execSchedule;
		private List<UIAbilityFlag> requiresFlags = new LinkedList<>();
		private List<UILcpProxy> lcps = new LinkedList<>();
		
		private Builder () {}

		public Builder withTestDefinition(TestDefinition testDefinition) {
			this.testDefinition = testDefinition;
			return this;
		}

		public Builder withExecSchedule(UISchedule execSchedule) {
			this.execSchedule = execSchedule;
			return this;
		}

		public Builder withRequiresFlag(UIAbilityFlag requiresFlag) {
			this.requiresFlags.add(requiresFlag);
			return this;
		}

		public Builder withLcp(UILcpProxy lcp) {
			this.lcps.add(lcp);
			return this;
		}
		
		public UITestDefinition build () {
			validateNotNull("testDefinition", testDefinition);
			validateNotNull("execSchedule", execSchedule);
			validateNotEmpty("lcps", lcps);
			return new UITestDefinition(this);
		}
 		
	}

}

package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Script;

public class UIScript implements JsonSource {
	private Script script;
	private UIScriptType scriptType;
	private List<UIAbilityFlag> requiresFlags = new LinkedList<UIAbilityFlag>();
	
	public static Builder create () {
		return new Builder();
	}
	
	private UIScript (Builder builder) {
		this.script = builder.script;
		this.scriptType = builder.scriptType;
		this.requiresFlags.addAll(builder.requiresFlags);
	}

	public Long getId() {
		return script.getId();
	}

	public UIScriptType getScriptType() {
		return scriptType;
	}

	public long getTenantId() {
		return script.getTenantId();
	}

	public String getName() {
		return script.getName();
	}

	public List<UIAbilityFlag> getRequiresFlags() {
		return new LinkedList<UIAbilityFlag>(requiresFlags);
	}

	public Long getLastModified() {
		return script.getLastModified();
	}
	
	public JSONObject toJsonObject () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("tenantId", getTenantId());
		json.put("scriptType", scriptType.toJsonObject());
		json.put("name", getName());
		JSONArray jsonRequiresFlags = new JSONArray();
		for (UIAbilityFlag requiresFlag : requiresFlags) {
			jsonRequiresFlags.put(requiresFlag.toJsonObject());
		}
		json.put("lastModified", getLastModified());
		return json;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((requiresFlags == null) ? 0 : requiresFlags.hashCode());
		result = prime * result + ((script == null) ? 0 : script.hashCode());
		result = prime * result
				+ ((scriptType == null) ? 0 : scriptType.hashCode());
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
		UIScript other = (UIScript) obj;
		if (requiresFlags == null) {
			if (other.requiresFlags != null)
				return false;
		} else if (!requiresFlags.equals(other.requiresFlags))
			return false;
		if (script == null) {
			if (other.script != null)
				return false;
		} else if (!script.equals(other.script))
			return false;
		if (scriptType == null) {
			if (other.scriptType != null)
				return false;
		} else if (!scriptType.equals(other.scriptType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIScript [script=" + script + ", scriptType=" + scriptType
				+ ", requiresFlags=" + requiresFlags + "]";
	}

	public static class Builder {
		private Script script;
		private UIScriptType scriptType;
		private List<UIAbilityFlag> requiresFlags = new LinkedList<UIAbilityFlag>();
		
		private Builder () {}
		
		public Builder withScript (Script script) {
			this.script = script;
			return this;
		}
		
		public Builder withScriptType (UIScriptType scriptType) {
			this.scriptType = scriptType;
			return this;
		}

		public Builder withRequiresFlags (UIAbilityFlag requiresFlag) {
			this.requiresFlags.add(requiresFlag);
			return this;
		}
		
		public Builder withRequiresFlags (List<UIAbilityFlag> requiresFlag) {
			this.requiresFlags.addAll(requiresFlag);
			return this;
		}

		public UIScript build () {
			validateNotNull("script", script);
			validateNotNull("scriptType", scriptType);
			return new UIScript(this);
		}
	}

}

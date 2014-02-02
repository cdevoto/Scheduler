package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptType;

public class UIScriptType implements JsonSource {
	private ScriptType scriptType;
	private List<UIAbilityFlag> abilityFlags = new LinkedList<>();
	
	public static Builder create () {
		return new Builder ();
	}
	
	private UIScriptType (Builder builder) {
		this.scriptType = builder.scriptType;
		this.abilityFlags.addAll(builder.abilityFlags);
	}

	public long getId() {
		return scriptType.getId();
	}

	public String getName() {
		return scriptType.getName();
	}
	
	public List<UIAbilityFlag> getAbilityFlags () {
		return new LinkedList<UIAbilityFlag>(this.abilityFlags);
	}
	
	@Override
	public JSONObject toJsonObject () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("name", getName());
		JSONArray jsonAbilityFlags = new JSONArray();
		for (UIAbilityFlag abilityFlag : abilityFlags) {
			JSONObject jsonAbilityFlag = abilityFlag.toJsonObject();
			jsonAbilityFlags.put(jsonAbilityFlag);
		}
		json.put("abilityFlags", jsonAbilityFlags);
		return json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abilityFlags == null) ? 0 : abilityFlags.hashCode());
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
		UIScriptType other = (UIScriptType) obj;
		if (abilityFlags == null) {
			if (other.abilityFlags != null)
				return false;
		} else if (!abilityFlags.equals(other.abilityFlags))
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
		return "UIScriptType [scriptType=" + scriptType + ", abilityFlags="
				+ abilityFlags + "]";
	}

	public static class Builder {
		private ScriptType scriptType;
		private List<UIAbilityFlag> abilityFlags = new LinkedList<>();
		
		private Builder () {}
		
		public Builder withScriptType (ScriptType scriptProxy) {
			this.scriptType = scriptProxy;
			return this;
		}
		
		public Builder withAbilityFlag(UIAbilityFlag abilityFlag) {
			this.abilityFlags.add(abilityFlag);
			return this;
		}
		
		public UIScriptType build () {
			validateNotNull("scriptType", scriptType);
			return new UIScriptType(this);
		}
	}
	

}

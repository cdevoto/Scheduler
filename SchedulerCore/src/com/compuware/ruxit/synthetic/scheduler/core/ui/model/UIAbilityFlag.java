package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;

public class UIAbilityFlag implements JsonSource {
	private AbilityFlag abilityFlag;
	
	public static Builder create () {
		return new Builder ();
	}
	
	private UIAbilityFlag (Builder builder) {
		this.abilityFlag = builder.abilityFlag;
	}

	public long getId() {
		return abilityFlag.getId();
	}

	public String getDescription() {
		return abilityFlag.getDescription();
	}
	
	public JSONObject toJsonObject () throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("description", getDescription());
		return json;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abilityFlag == null) ? 0 : abilityFlag.hashCode());
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
		UIAbilityFlag other = (UIAbilityFlag) obj;
		if (abilityFlag == null) {
			if (other.abilityFlag != null)
				return false;
		} else if (!abilityFlag.equals(other.abilityFlag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIAbilityFlag [abilityFlag=" + abilityFlag + "]";
	}

	public static class Builder {
		private AbilityFlag abilityFlag;
		
		private Builder () {}
		
		public Builder withAbilityFlag (AbilityFlag abilityFlag) {
			this.abilityFlag = abilityFlag;
			return this;
		}
		
		public UIAbilityFlag build () {
			validateNotNull("abilityFlag", abilityFlag);
			return new UIAbilityFlag(this);
		}
	}
	

}

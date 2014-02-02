package com.compuware.ruxit.synthetic.scheduler.core.ui.model;


import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

import java.util.LinkedList;
import java.util.List;

public class UIScriptForm {
	private long tenantId;
	private String name;
	private long scriptTypeId;
	private List<String> requiresFlags = new LinkedList<String>();
	
	public static Builder create () {
		return new Builder();
	}
	
	private UIScriptForm (Builder builder) {
		this.tenantId = builder.tenantId;
		this.name = builder.name;
		this.scriptTypeId = builder.scriptTypeId;
		this.requiresFlags.addAll(builder.requiresFlags);
	}
	
	public long getTenantId () {
	    return tenantId;	
	}
	
	public String getName() {
		return name;
	}

	public long getScriptTypeId() {
		return scriptTypeId;
	}

	public List<String> getRequiresFlags() {
		return new LinkedList<String>(requiresFlags);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((requiresFlags == null) ? 0 : requiresFlags.hashCode());
		result = prime * result + (int) (scriptTypeId ^ (scriptTypeId >>> 32));
		result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
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
		UIScriptForm other = (UIScriptForm) obj;
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
		if (scriptTypeId != other.scriptTypeId)
			return false;
		if (tenantId != other.tenantId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIScriptForm [tenantId=" + tenantId + ", name=" + name
				+ ", scriptTypeId=" + scriptTypeId + ", requiresFlags="
				+ requiresFlags + "]";
	}

	public static class Builder {
		private Long tenantId;
		private String name;
		private Long scriptTypeId;
		private List<String> requiresFlags = new LinkedList<String>();
		
		private Builder () {}
		
		public Builder withTenantId(long tenantId) {
			this.tenantId = tenantId;
			return this;
		}

		public Builder withName (String name) {
			this.name = name;
			return this;
		}
		
		public Builder withScriptTypeId (long scriptTypeId) {
			this.scriptTypeId = scriptTypeId;
			return this;
		}
		
		public Builder withRequiresFlag (String requiresFlag) {
			this.requiresFlags.add(requiresFlag);
			return this;
		}
		
		public UIScriptForm build () {
			validateNotNull("tenantId", tenantId);
			validateNotNull("name", name);
			validateNotNull("scriptTypeId", scriptTypeId);
			return new UIScriptForm(this);
		}

		
	}

}

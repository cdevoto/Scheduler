package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class Script {
    private Long id;
    private ScriptType scriptType;
    private long tenantId;
    private String name;
    private long requiresF;
    private Boolean active;
	private Boolean deleted;
    private Long lastModified;
    
    public static Builder create () {
    	return new Builder();
    }
    
    public static Builder create (Script script) {
    	return new Builder(script);
    }
    
	private Script (Builder builder) {
    	this.id = builder.id;
    	this.scriptType = builder.scriptType;
    	this.tenantId = builder.tenantId;
    	this.name = builder.name;
    	this.requiresF = builder.requiresF;
    	this.active = builder.active;
    	this.deleted = builder.deleted;
    	this.lastModified = builder.lastModified;
    }
    
    public Long getId() {
		return id;
	}

	public ScriptType getScriptType() {
		return scriptType;
	}

	public long getTenantId() {
		return tenantId;
	}

	public String getName() {
		return name;
	}

	public long getRequiresFlags() {
		return requiresF;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Long getLastModified() {
		return lastModified;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (requiresF ^ (requiresF >>> 32));
		result = prime * result
				+ ((scriptType == null) ? 0 : scriptType.hashCode());
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
		Script other = (Script) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (requiresF != other.requiresF)
			return false;
		if (scriptType == null) {
			if (other.scriptType != null)
				return false;
		} else if (!scriptType.equals(other.scriptType))
			return false;
		if (tenantId != other.tenantId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Script [id=" + id + ", scriptType=" + scriptType
				+ ", tenantId=" + tenantId + ", name=" + name + ", requiresF="
				+ requiresF + ", active=" + active + ", deleted=" + deleted
				+ ", lastModified=" + lastModified + "]";
	}

    public static class Builder {
        private Long id;
        private ScriptType scriptType;
        private Long tenantId;
        private String name;
        private Long requiresF;
        private Boolean active;
		private Boolean deleted;
        private Long lastModified;
        
        private Builder () {}
        
        private Builder (Script script) {
        	this.id = script.id;
        	this.scriptType = script.scriptType;
        	this.tenantId = script.tenantId;
        	this.name = script.name;
        	this.requiresF = script.requiresF;
        	this.active = script.active;
        	this.deleted = script.deleted;
        	this.lastModified = script.lastModified;
        }
        
        public Builder withId (long id) {
        	this.id = id;
        	return this;
        }
        
        public Builder withScriptType (ScriptType scriptType) {
        	this.scriptType = scriptType;
        	return this;
        }
        
        public Builder withTenantId (long tenantId) {
        	this.tenantId = tenantId;
        	return this;
        }

        public Builder withName (String name) {
        	this.name = name;
        	return this;
        }

        public Builder withRequiresFlags (long requiresF) {
        	this.requiresF = requiresF;
        	return this;
        }
        
        public Builder withActive (boolean active) {
        	this.active = active;
        	return this;
        }

        public Builder withDeleted(boolean deleted) {
			this.deleted = deleted;
			return this;
		}

		public Builder withLastModified (long lastModified) {
        	this.lastModified = lastModified;
        	return this;
        }
        
        public Script build () {
        	validateNotNull("scriptType", scriptType);
           	validateNotNull("tenantId", tenantId);
           	validateNotNull("name", name);
           	validateNotNull("requiresF", requiresF);
           	return new Script(this);
        }
    }
}

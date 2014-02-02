package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public final class AbilityFlag {
    private long id;
    private AbilityFlagLevel level;
    private long mask;
    private String description;
    
    public static Builder create () {
    	return new Builder();
    }
    
    public static Builder create (AbilityFlag abilityFlag) {
    	return new Builder(abilityFlag);
    }

    private AbilityFlag (Builder builder) {
    	this.id = builder.id;
    	this.level = builder.level;
    	this.mask = builder.mask;
    	this.description = builder.description;
    }
    
    public long setFlag (long bitMask) {
    	return bitMask | this.mask;
    }
    
    public long unsetFlag (long bitMask) {
    	return (bitMask | this.mask) ^ this.mask;
    }
    
    public long getId() {
		return id;
	}

	public AbilityFlagLevel getLevel() {
		return level;
	}

	public long getMask() {
		return mask;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + (int) (mask ^ (mask >>> 32));
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
		AbilityFlag other = (AbilityFlag) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (level != other.level)
			return false;
		if (mask != other.mask)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbilityFlag [id=" + id + ", level=" + level + ", mask=" + mask
				+ ", description=" + description + "]";
	}

	public static class Builder {
        private Long id;
        private AbilityFlagLevel level;
        private Long mask;
        private String description;
        
        private Builder () {}
        
        private Builder (AbilityFlag abilityFlag) {
        	this.id = abilityFlag.id;
        	this.level = abilityFlag.level;
        	this.mask = abilityFlag.mask;
        	this.description = abilityFlag.description;
        }
        
        public Builder withId (long id) {
        	this.id = id;
        	return this;
        }
        
        public Builder withLevel (long abilityFlagLevelId) {
        	AbilityFlagLevel level = AbilityFlagLevel.get(abilityFlagLevelId);
        	if (level == null) {
        		throw new IllegalArgumentException("Invalid value specified for the 'abilityFlagLevelId' field.");
        	}
        	this.level = level;
        	return this;
        }
     	
        public Builder withMask (long mask) {
        	this.mask = mask;
        	return this;
        }

        public Builder withDescription (String description) {
        	this.description = description;
        	return this;
        }
        
        public AbilityFlag build () {
        	validateNotNull("id", id);
        	validateNotNull("level", level);
        	validateNotNull("mask", mask);
        	validateNotNull("description", description);
        	return new AbilityFlag(this);
        }
    
    }
}

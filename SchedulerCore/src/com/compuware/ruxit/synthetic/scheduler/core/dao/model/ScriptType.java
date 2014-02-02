package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class ScriptType {
	private long id;
	private String name;
	
	public static Builder create () {
		return new Builder();
	}
	
	public static Builder create (ScriptType type) {
		return new Builder(type);
	}
	
	private ScriptType (Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ScriptType other = (ScriptType) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScriptType [id=" + id + ", name=" + name + "]";
	}



	public static class Builder {
		private Long id;
		private String name;
		
		private Builder () {}
		
		private Builder (ScriptType type) {
			this.id = type.id;
			this.name = type.name;
		}
		
		public Builder withId (long id) {
			this.id = id;
			return this;
		}
		
		public Builder withName (String name) {
			this.name = name;
			return this;
		}
		
		public ScriptType build () {
			validateNotNull("id", id);
			validateNotNull("name", name);
			return new ScriptType(this);
		}
	}
}

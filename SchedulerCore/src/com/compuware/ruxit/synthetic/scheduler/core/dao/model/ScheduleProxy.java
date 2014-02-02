package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class ScheduleProxy {
	private long id;
	private String name;
	private long lastModified;
	
	public static Builder create () {
		return new Builder();
	}
	
	public static Builder create (ScheduleProxy type) {
		return new Builder(type);
	}
	
	private ScheduleProxy (Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.lastModified = builder.lastModified;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public long getLastModified() {
		return lastModified;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
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
		ScheduleProxy other = (ScheduleProxy) obj;
		if (id != other.id)
			return false;
		if (lastModified != other.lastModified)
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
		return "ScriptProxy [id=" + id + ", name=" + name + ", lastModified="
				+ lastModified + "]";
	}

	public static class Builder {
		private Long id;
		private String name;
		private Long lastModified;
		
		private Builder () {}
		
		private Builder (ScheduleProxy script) {
			this.id = script.id;
			this.name = script.name;
			this.lastModified = script.lastModified;
		}
		
		public Builder withId (long id) {
			this.id = id;
			return this;
		}
		
		public Builder withName (String name) {
			this.name = name;
			return this;
		}
		
		public Builder withLastModified (long lastModified) {
			this.lastModified = lastModified;
			return this;
		}
		
		public ScheduleProxy build () {
			validateNotNull("id", id);
			validateNotNull("name", name);
			validateNotNull("lastModified", lastModified);
			return new ScheduleProxy(this);
		}
	}
}

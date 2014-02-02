package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class LcpProxy {
	private long id;
	private String name;
	
	public static Builder create () {
		return new Builder();
	}
	
	public static Builder create (LcpProxy type) {
		return new Builder(type);
	}
	
	private LcpProxy (Builder builder) {
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
		LcpProxy other = (LcpProxy) obj;
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
		return "LcpProxy [id=" + id + ", name=" + name + "]";
	}

	public static class Builder {
		private Long id;
		private String name;
		
		private Builder () {}
		
		private Builder (LcpProxy script) {
			this.id = script.id;
			this.name = script.name;
		}
		
		public Builder withId (long id) {
			this.id = id;
			return this;
		}
		
		public Builder withName (String name) {
			this.name = name;
			return this;
		}
		
		public LcpProxy build () {
			validateNotNull("id", id);
			validateNotNull("name", name);
			return new LcpProxy(this);
		}
	}
}

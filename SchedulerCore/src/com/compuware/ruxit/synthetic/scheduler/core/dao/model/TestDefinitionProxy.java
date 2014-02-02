package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class TestDefinitionProxy {
	private long id;
	private String name;
	private boolean suspended;
	private long lastModified;
	
	public static Builder create () {
		return new Builder();
	}
	
	private TestDefinitionProxy(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.suspended = builder.suspended;
		this.lastModified = builder.lastModified;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isSuspended() {
		return suspended;
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
		result = prime * result + (suspended ? 1231 : 1237);
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
		TestDefinitionProxy other = (TestDefinitionProxy) obj;
		if (id != other.id)
			return false;
		if (lastModified != other.lastModified)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (suspended != other.suspended)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestDefinitionProxy [id=" + id + ", name=" + name
				+ ", suspended=" + suspended + ", lastModified=" + lastModified
				+ "]";
	}

	public static class Builder {
		private Long id;
		private String name;
		public Boolean suspended;
		private Long lastModified;
		
		private Builder () {}
		
		public Builder withId(Long id) {
			this.id = id;
			return this;
		}
		
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder withSuspended(Boolean suspended) {
			this.suspended = suspended;
			return this;
		}

		public Builder withLastModified(Long lastModified) {
			this.lastModified = lastModified;
			return this;
		}
		
		public TestDefinitionProxy build () {
			validateNotNull("id", id);
			validateNotNull("name", name);
			validateNotNull("suspended", suspended);
			validateNotNull("lastModified", lastModified);
			return new TestDefinitionProxy(this);
		}
	}

}

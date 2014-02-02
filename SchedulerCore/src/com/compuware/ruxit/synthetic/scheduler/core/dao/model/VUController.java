package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.*;

public class VUController {
	private Long id;
	private long supportsF;
	private long locationId;
	
	public static Builder create () {
		return new Builder();
	}
	
	private VUController (Builder builder) {
		this.id = builder.id;
		this.supportsF = builder.supportsF;
		this.locationId = builder.locationId;
	}
	
	public Long getId() {
		return id;
	}

	public long getSupportsF() {
		return supportsF;
	}

	public long getLocationId() {
		return locationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (locationId ^ (locationId >>> 32));
		result = prime * result + (int) (supportsF ^ (supportsF >>> 32));
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
		VUController other = (VUController) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (locationId != other.locationId)
			return false;
		if (supportsF != other.supportsF)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VUController [id=" + id + ", supportsF=" + supportsF
				+ ", locationId=" + locationId + "]";
	}

	public static class Builder {
		private Long id;
		private Long supportsF;
		private Long locationId;
		
		private Builder () {}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withSupportsF(Long supportsF) {
			this.supportsF = supportsF;
			return this;
		}

		public Builder withLocationId(Long locationId) {
			this.locationId = locationId;
			return this;
		}

		public VUController build () {
			validateNotNull("supportsF", supportsF);
			validateNotNull("locationId", locationId);
			return new VUController(this);
		}
		
	}
	

}



public class LcpGroup  {
	private Node location;
	private Node carrier;
	private Node agentType;
	
	public static Builder create () {
		return new Builder();
	}
	
	private LcpGroup (Builder builder) {
		this.location = builder.location;
		this.carrier = builder.carrier;
		this.agentType = builder.agentType;
	}
	
	public Node getLocation() {
		return location;
	}

	public Node getCarrier() {
		return carrier;
	}

	public Node getAgentType() {
		return agentType;
	}

    public String toString () {
    	return location.getName() + ":" + carrier.getName() + ":" + agentType.getName();
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carrier == null) ? 0 : carrier.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((agentType == null) ? 0 : agentType.hashCode());
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
		LcpGroup other = (LcpGroup) obj;
		if (carrier == null) {
			if (other.carrier != null)
				return false;
		} else if (!carrier.equals(other.carrier))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (agentType == null) {
			if (other.agentType != null)
				return false;
		} else if (!agentType.equals(other.agentType))
			return false;
		return true;
	}


	public static class Builder {
		private Node location;
		private Node carrier;
		private Node agentType;
		
		private Builder () {}
		
		
		public Builder withLocation (Node location) {
            this.location = location;
            return this;
		}
		
		public Builder withCarrier (Node carrier) {
            this.carrier = carrier;
            return this;
		}

		public Builder withAgentType (Node agentType) {
            this.agentType = agentType;
            return this;
		}

		public LcpGroup build () {
			return new LcpGroup(this);
		}
	}
}

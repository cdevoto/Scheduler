

public class Lcp  {
    private Integer id;
	private Node location;
	private Node carrier;
	private Node player;
	
	public static Builder create () {
		return new Builder();
	}
	
	public static Builder create (Lcp lcp) {
		return new Builder(lcp);
	}
	
	private Lcp (Builder builder) {
		this.id = builder.id;
		this.location = builder.location;
		this.carrier = builder.carrier;
		this.player = builder.player;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Node getLocation() {
		return location;
	}

	public Node getCarrier() {
		return carrier;
	}

	public Node getPlayer() {
		return player;
	}

    public String toString () {
    	return location.getName() + ":" + carrier.getName() + ":" + player.getName();
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carrier == null) ? 0 : carrier.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
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
		Lcp other = (Lcp) obj;
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
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}


	public static class Builder {
		private Integer id;
		private Node location;
		private Node carrier;
		private Node player;
		
		private Builder () {}
		
		private Builder (Lcp lcp) {
			this.id = lcp.id;
			this.location = lcp.location;
			this.carrier = lcp.carrier;
			this.player = lcp.player;
		}
		
		public Builder withId (int id) {
			this.id = id;
			return this;
		}
		
		public Builder withLocation (Node location) {
            this.location = location;
            return this;
		}
		
		public Builder withCarrier (Node carrier) {
            this.carrier = carrier;
            return this;
		}

		public Builder withPlayer (Node player) {
            this.player = player;
            return this;
		}

		public Lcp build () {
			return new Lcp(this);
		}
	}
}

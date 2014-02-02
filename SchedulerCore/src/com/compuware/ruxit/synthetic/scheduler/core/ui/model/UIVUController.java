package com.compuware.ruxit.synthetic.scheduler.core.ui.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.*;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.VUController;

public class UIVUController implements JsonSource {
	private VUController vuController;
	private List<UIAbilityFlag> supportsFlags = new LinkedList<>();
	private List<UILcpProxy> lcps = new LinkedList<>();
	
	public static Builder create () {
		return new Builder();
	}
	
	private UIVUController (Builder builder) {
		this.vuController = builder.vuController;
		this.supportsFlags.addAll(builder.supportsFlags);
		this.lcps.addAll(builder.lcps);
	}
	
	public Long getId() {
		return vuController.getId();
	}

	public long getSupportsF() {
		return vuController.getSupportsF();
	}

	public long getLocationId() {
		return vuController.getLocationId();
	}
	
	public List<UIAbilityFlag> getSupportsFlags () {
		return new LinkedList<>(supportsFlags);
	}

	public List<UILcpProxy> getLcps () {
		return new LinkedList<>(lcps);
	}

	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("locationId", getLocationId());
		
		JSONArray supportsFlags = new JSONArray();
		for (UIAbilityFlag requiresFlag : this.supportsFlags) {
			supportsFlags.put(requiresFlag.toJsonObject());
		}
		json.put("supportsFlags", supportsFlags);
		
		JSONArray lcps = new JSONArray();
		for (UILcpProxy lcp : this.lcps) {
			lcps.put(lcp.toJsonObject());
		}
		json.put("lcps", lcps);
		
		return json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lcps == null) ? 0 : lcps.hashCode());
		result = prime * result
				+ ((supportsFlags == null) ? 0 : supportsFlags.hashCode());
		result = prime * result
				+ ((vuController == null) ? 0 : vuController.hashCode());
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
		UIVUController other = (UIVUController) obj;
		if (lcps == null) {
			if (other.lcps != null)
				return false;
		} else if (!lcps.equals(other.lcps))
			return false;
		if (supportsFlags == null) {
			if (other.supportsFlags != null)
				return false;
		} else if (!supportsFlags.equals(other.supportsFlags))
			return false;
		if (vuController == null) {
			if (other.vuController != null)
				return false;
		} else if (!vuController.equals(other.vuController))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIVUController [vuController=" + vuController
				+ ", supportsFlags=" + supportsFlags + ", lcps=" + lcps + "]";
	}

	public static class Builder {
		private VUController vuController;
		private List<UIAbilityFlag> supportsFlags = new LinkedList<>();
		private List<UILcpProxy> lcps = new LinkedList<>();
		
		private Builder () {}
		
		public Builder withVuController (VUController vuc) {
			this.vuController = vuc;
			return this;
		}
		
		public Builder withSupportsFlag (UIAbilityFlag supportsFlag) {
			this.supportsFlags.add(supportsFlag);
			return this;
		}
		
		public Builder withSupportsFlags (List<UIAbilityFlag> supportsFlags) {
			this.supportsFlags.addAll(supportsFlags);
			return this;
		}
		
		public Builder withLcp (UILcpProxy lcp) {
			this.lcps.add(lcp);
			return this;
		}
		
		public Builder withLcps (List<UILcpProxy> lcps) {
			this.lcps.addAll(lcps);
			return this;
		}
		
		public UIVUController build () {
			validateNotNull("vuController", vuController);
			validateNotEmpty("lcps", lcps);
			return new UIVUController(this);
		}
	}

}

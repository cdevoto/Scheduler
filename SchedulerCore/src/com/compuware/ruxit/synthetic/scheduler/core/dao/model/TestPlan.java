package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.*;

public class TestPlan {
	private Long id;
	private Long testDefinitionId;
	private LcpProxy lcp;
	private String recurrenceRule;
	
	public static Builder create () {
		return new Builder();
	}
	
	private TestPlan (Builder builder) {
		this.id = builder.id;
		this.testDefinitionId = builder.testDefinitionId;
		this.lcp = builder.lcp;
		this.recurrenceRule = builder.recurrenceRule;
	}
	
	public Long getId() {
		return id;
	}

	public Long getTestDefinitionId() {
		return testDefinitionId;
	}

	public LcpProxy getLcp() {
		return lcp;
	}

	public String getRecurrenceRule() {
		return recurrenceRule;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lcp == null) ? 0 : lcp.hashCode());
		result = prime * result
				+ ((recurrenceRule == null) ? 0 : recurrenceRule.hashCode());
		result = prime * result
				+ (int) (testDefinitionId ^ (testDefinitionId >>> 32));
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
		TestPlan other = (TestPlan) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lcp == null) {
			if (other.lcp != null)
				return false;
		} else if (!lcp.equals(other.lcp))
			return false;
		if (recurrenceRule == null) {
			if (other.recurrenceRule != null)
				return false;
		} else if (!recurrenceRule.equals(other.recurrenceRule))
			return false;
		if (testDefinitionId != other.testDefinitionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestPlan [id=" + id + ", testDefinitionId=" + testDefinitionId
				+ ", lcp=" + lcp 
				+ ", recurrenceRule=" + recurrenceRule + "]";
	}

	public static class Builder {
		private Long id;
		private Long testDefinitionId;
		private LcpProxy lcp;
		private String recurrenceRule;
		
		private Builder () {}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withTestDefinitionId(long testDefinitionId) {
			this.testDefinitionId = testDefinitionId;
			return this;
		}

		public Builder withLcp(LcpProxy lcp) {
			this.lcp = lcp;
			return this;
		}

		public Builder withRecurrenceRule(String recurrenceRule) {
			this.recurrenceRule = recurrenceRule;
			return this;
		}
		
		public TestPlan build () {
			validateNotNull("lcp", lcp);
			validateNotNull("recurrenceRule", recurrenceRule);
			
			return new TestPlan(this);
		}
		
		
	}

}

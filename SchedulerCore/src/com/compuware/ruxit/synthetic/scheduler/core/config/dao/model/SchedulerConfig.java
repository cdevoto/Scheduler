package com.compuware.ruxit.synthetic.scheduler.core.config.dao.model;

import static com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil.validateNotNull;

public class SchedulerConfig {
	private long id;
	private int workerNumber;
	private int totalWorkers;
	
	public static Builder create () {
		return new Builder();
	}
	
	private SchedulerConfig (Builder builder) {
		this.id = builder.id;
		this.workerNumber = builder.workerNumber;
		this.totalWorkers = builder.totalWorkers;
	}
	
	public long getId() {
		return id;
	}
	
	public int getWorkerNumber() {
		return workerNumber;
	}

	public int getTotalWorkers() {
		return totalWorkers;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + totalWorkers;
		result = prime * result + workerNumber;
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
		SchedulerConfig other = (SchedulerConfig) obj;
		if (id != other.id)
			return false;
		if (totalWorkers != other.totalWorkers)
			return false;
		if (workerNumber != other.workerNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Scheduler [id=" + id + ", workerNumber=" + workerNumber
				+ ", totalWorkers=" + totalWorkers + "]";
	}



	public static class Builder {
		private Long id;
		private int workerNumber;
		private int totalWorkers;
		
		private Builder () {}
		
		public Builder withId (long id) {
			this.id = id;
			return this;
		}
		
		public Builder withWorkerNumber (int workerNumber) {
			this.workerNumber = workerNumber;
			return this;
		}
		
		public Builder withTotalWorkers (int totalWorkers) {
			this.totalWorkers = totalWorkers;
			return this;
		}

		public SchedulerConfig build () {
			validateNotNull("id", id);
			validateNotNull("workerNumber", workerNumber);
			validateNotNull("totalWorkers", totalWorkers);
			return new SchedulerConfig(this);
		}
	}
	
	public static class Id {
		private long schedulerId;
		
		public Id(long schedulerId) {
			this.schedulerId = schedulerId;
		}
		
		public long get () {
			return this.schedulerId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (schedulerId ^ (schedulerId >>> 32));
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
			Id other = (Id) obj;
			if (schedulerId != other.schedulerId)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "SchedulerConfig.Id [schedulerId=" + schedulerId + "]";
		}

	}

}

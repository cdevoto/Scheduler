package com.compuware.ruxit.synthetic.dispatcher;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.util.ValidationUtil;

public class TestDispatchServiceConfig {
	public static final long DEFAULT_WORKER_TIMEOUT = 90 * 1000;
	public static final long DEFAULT_WORKER_POLL_INTERVAL = 500;
	
	private long workerTimeout;
	private long workerPollInterval;
	private TestDefinitionService service;
	

	public static Builder create () {
		return new Builder();
	}
	
	public TestDispatchServiceConfig(Builder builder) {
		this.workerTimeout = builder.workerTimeout;
		this.workerPollInterval = builder.workerPollInterval;
		this.service = builder.service;
	}

	public long getWorkerTimeout() {
		return workerTimeout;
	}

	public long getWorkerPollInterval() {
		return workerPollInterval;
	}
	
	public TestDefinitionService getService() {
		return service;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result
				+ (int) (workerPollInterval ^ (workerPollInterval >>> 32));
		result = prime * result
				+ (int) (workerTimeout ^ (workerTimeout >>> 32));
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
		TestDispatchServiceConfig other = (TestDispatchServiceConfig) obj;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (workerPollInterval != other.workerPollInterval)
			return false;
		if (workerTimeout != other.workerTimeout)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestDispatchServiceConfig [workerTimeout=" + workerTimeout
				+ ", workerPollInterval=" + workerPollInterval + ", service="
				+ service + "]";
	}


	public static class Builder {
		private Long workerTimeout = DEFAULT_WORKER_TIMEOUT;
		private Long workerPollInterval = DEFAULT_WORKER_POLL_INTERVAL;
		private TestDefinitionService service;

		private Builder () {}

		public Builder withWorkerTimeout(String workerTimeout) {
		    Integer seconds = valueOf(workerTimeout);
		    if (seconds != null) {
		    	this.workerTimeout = seconds * 1000L;
		    }
			return this;
		}

		public Builder withWorkerPollInterval(String workerPollInterval) {
		    Integer seconds = valueOf(workerPollInterval);
		    if (seconds != null) {
		    	this.workerPollInterval = seconds * 1000L;
		    }
			return this;
		}
		
		public Builder withService (TestDefinitionService service) {
			this.service = service;
			return this;
		}
		
		public TestDispatchServiceConfig build () {
			ValidationUtil.validateNotNull("service", service);
            return new TestDispatchServiceConfig(this);			
		}
		
		private Integer valueOf (String s) {
			if (s == null) {
				return null;
			}
			try {
				int result = Integer.parseInt(s);
				if (result < 1) {
					return null;
				}
				return result;
			} catch (NumberFormatException ex) {
				return null;
			}
		}
	}
	

}

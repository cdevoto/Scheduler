package com.compuware.ruxit.synthetic.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;


public class TestDispatchServiceConfig {
	public static final long DEFAULT_WORKER_TIMEOUT = 90 * 1000;
	public static final long DEFAULT_WORKER_POLL_INTERVAL = 5 * 1000;
	public static final int DEFAULT_DISPATCH_WORKERS = 10;
	
	private long workerTimeout = DEFAULT_WORKER_TIMEOUT;
	private long workerPollInterval = DEFAULT_WORKER_POLL_INTERVAL;
	private int dispatchWorkers = DEFAULT_DISPATCH_WORKERS;
	@Autowired
	private TestDefinitionService service;
	

	public TestDispatchServiceConfig() {}
	
	public void setWorkerTimeout(long workerTimeout) {
		if (workerTimeout > 0) {
			this.workerTimeout = workerTimeout;
		}
	}
	
	public void setWorkerPollInterval(long workerPollInterval) {
		if (workerPollInterval > 0) {
			this.workerPollInterval = workerPollInterval;
		}
	}

	public void setDispatchWorkers(int dispatchWorkers) {
		if (dispatchWorkers > 0) {
			this.dispatchWorkers = dispatchWorkers;
		}
	}

	public long getWorkerTimeout() {
		return workerTimeout;
	}

	public long getWorkerPollInterval() {
		return workerPollInterval;
	}
	
	public int getDispatchWorkers() {
		return dispatchWorkers;
	}
	
	public TestDefinitionService getService() {
		return service;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dispatchWorkers;
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
		if (dispatchWorkers != other.dispatchWorkers)
			return false;
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
				+ ", workerPollInterval=" + workerPollInterval
				+ ", dispatchWorkers=" + dispatchWorkers + ", service="
				+ service + "]";
	}

}

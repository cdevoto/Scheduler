package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.event;

public class LastPollTime {
	private static class InstanceHolder {
		private static final LastPollTime INSTANCE = new LastPollTime();
	}

	private volatile long prevLastPollTime;
	private volatile long lastPollTime;

	public static final long snapshot () {
		return InstanceHolder.INSTANCE.doSnapshot();
	}
	
	public static final long get () {
		return InstanceHolder.INSTANCE.doGet();
	}
	
	public static final void reset () {
		InstanceHolder.INSTANCE.doReset();
	}
	
	private LastPollTime () {
		doSnapshot();
	}
	
	private long doSnapshot() {
		this.prevLastPollTime = this.lastPollTime;
		return this.lastPollTime = System.currentTimeMillis();
	}
	
	private void doReset() {
		this.lastPollTime = this.prevLastPollTime;
	}

	private long doGet() {
		return this.lastPollTime;
	}

}

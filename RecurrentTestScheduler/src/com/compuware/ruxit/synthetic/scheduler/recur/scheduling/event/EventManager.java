package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.event;


public interface EventManager extends Runnable {
	public void addListener (EventListener listener);
	public void removeListener (EventListener listener);
}

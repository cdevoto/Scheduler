package com.compuware.ruxit.synthetic.engine;


public abstract class LifecycleAdapter implements Lifecycle {

	@Override
	public void init() throws Exception {
	}

	@Override
	public void start() throws Exception {
	}

	@Override
	public void stop() throws Exception {
	}

	@Override
	public int getPriority() {
		return Lifecycle.MEDIUM_PRIORITY;
	}
	
	

}

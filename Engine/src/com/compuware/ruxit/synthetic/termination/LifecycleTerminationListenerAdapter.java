package com.compuware.ruxit.synthetic.termination;

import com.compuware.ruxit.synthetic.engine.Lifecycle;
import com.compuware.ruxit.synthetic.engine.LifecycleAdapter;

public abstract class LifecycleTerminationListenerAdapter extends LifecycleAdapter implements TerminationListener {
	private int priority = Lifecycle.MEDIUM_PRIORITY;
	
	public void setPriority (int priority) {
		this.priority = priority;
	}
	
	@Override
	public int getPriority () {
		return this.priority;
	}

}

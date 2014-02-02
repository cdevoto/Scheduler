package com.compuware.ruxit.synthetic.engine;

public interface Lifecycle {
	public static final int HIGH_PRIORITY = 10;
	public static final int MEDIUM_PRIORITY = 20;
	public static final int LOW_PRIORITY = 30;
	

	public String getName ();
	public void init () throws Exception;
	public void start () throws Exception;
	public void stop () throws Exception;
	public int getPriority ();

}

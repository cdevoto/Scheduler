package com.compuware.ruxit.synthetic.scheduler.core.util;

public final class ExceptionUtil {

	public static RuntimeException launderThrowable(Throwable t) {
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else if (t instanceof Error) {
			throw (Error) t;
		} else {
			throw new RuntimeException(t);
		}
	}
	
	private ExceptionUtil () {}
	
}

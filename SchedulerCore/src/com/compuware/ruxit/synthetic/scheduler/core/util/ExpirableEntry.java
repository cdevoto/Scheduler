package com.compuware.ruxit.synthetic.scheduler.core.util;

public class ExpirableEntry <T> {
	private long expiresAt;
	private T value;
	
	public ExpirableEntry(long expiresAt, T value) {
		this.expiresAt = expiresAt;
		this.value = value;
	}
	
	public boolean isExpired (long now) {
		return now > expiresAt; 
	}
	
	public T value () {
		return value;
	}

}

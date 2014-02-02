package com.compuware.ruxit.synthetic.scheduler.core.dao;

public interface TenantDao {
	
	public long authenticate (String userName, char [] password);

}

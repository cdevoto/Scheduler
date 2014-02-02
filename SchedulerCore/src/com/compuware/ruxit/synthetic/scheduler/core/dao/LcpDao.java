package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;

public interface LcpDao {

	public List<LcpProxy> getLcps(long scriptId, long requiresF);
	public LcpProxy getById (long lcpId);
	public List<LcpProxy> getByTestDefId(long testDefinitionId);
	public List<LcpProxy> getByVucId(long vucId);
}

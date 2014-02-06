package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;

public interface TestPlanDao {
	
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified);
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified, long minTestDefId, long minTestPlanId);
	
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified, long minLastModified);
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified, long minTestDefId, long minTestPlanId, long minLastModified);

	public List<TestPlanView> getByTestDefId(long testDefId);
}

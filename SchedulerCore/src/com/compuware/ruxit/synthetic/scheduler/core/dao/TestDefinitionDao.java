package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinitionProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlan;

public interface TestDefinitionDao {

    public TestDefinition getById (long id);
    public List<TestDefinitionProxy> getByScheduleId (long scheduleId);
    public List<TestDefinitionProxy> getByTenantId (long tenantId);
    public List<TestDefinitionProxy> getAll (long scriptId);
    public long insert(TestDefinition testDefinition, List<TestPlan> testPlans);
    public void deleteById (long id);
	public void suspend(long testDefinitionId, boolean suspended);
	public void suspendForMaintenance(long testDefinitionId, boolean suspended);
	public void update(TestDefinition testDef, List<TestPlan> testPlans);

}

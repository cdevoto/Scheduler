package com.compuware.ruxit.synthetic.scheduler.core.service.planner;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlan;

public interface TestPlanner {

	public List<TestPlan> generateTestPlans (TestDefinition testDef);
}

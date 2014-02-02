package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.event;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;

public interface EventListener {
	
	public void onTestPlanModified (TestPlanView testPlan) throws Exception;
	public void onMaintScheduleModified (MaintScheduleView schedule) throws Exception;

}

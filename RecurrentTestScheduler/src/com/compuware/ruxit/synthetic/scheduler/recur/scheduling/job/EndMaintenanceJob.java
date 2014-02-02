package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestDefinitionDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.MaintScheduleCache;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.TestDefinition;

public class EndMaintenanceJob implements Job {
	private static Logger log = LoggerFactory.getLogger(EndMaintenanceJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		MaintScheduleView schedule = (MaintScheduleView) data.get("data");
		TestDefinitionDao dao = (TestDefinitionDao) data.get("dao");
		MaintScheduleCache cache = (MaintScheduleCache) data.get("cache");
		for (long testDefId : schedule.getTestDefinitionIds()) {
			TestDefinition testDef = cache.getTestDefinitionById(testDefId);
			try {
				synchronized (testDef) {
					testDef.removeActiveMaintSchedule(schedule.getScheduleId());
					if (!testDef.hasActiveMaintSchedules()) {
					    dao.suspendForMaintenance(testDefId, false);	
					}
				}
			    log.info(String.format("Ended a maintenance window for test definition %d  with schedule %s", testDefId, schedule.toString()));
			} catch (Throwable t) {
				log.error(String.format("A problem occurred while attempting to end a maintenance window for test definition %d  with schedule %s", testDefId, schedule.toString()), t);
			}
		}
		
	}
	

}

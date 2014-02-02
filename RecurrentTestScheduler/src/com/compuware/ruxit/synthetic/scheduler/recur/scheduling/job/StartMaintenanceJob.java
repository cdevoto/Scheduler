package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job;

import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.SchedulingUtil.scheduleEndMaintWindow;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestDefinitionDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.TimeBasedScheduler;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.MaintScheduleCache;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.TestDefinition;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.CronUtil;

public class StartMaintenanceJob implements Job {
	private static Logger log = LoggerFactory.getLogger(StartMaintenanceJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		MaintScheduleView schedule = (MaintScheduleView) data.get("data");
		TestDefinitionDao dao = (TestDefinitionDao) data.get("dao");
		MaintScheduleCache cache = (MaintScheduleCache) data.get("cache");
		TimeBasedScheduler scheduler = (TimeBasedScheduler) data.get("scheduler");
		for (long testDefId : schedule.getTestDefinitionIds()) {
			TestDefinition testDef = cache.getTestDefinitionById(testDefId);
			try {
				synchronized (testDef) {
					boolean isSuspended = testDef.hasActiveMaintSchedules();
					testDef.addActiveMaintSchedule(schedule);
					if (!isSuspended) {
					    dao.suspendForMaintenance(testDefId, true);	
					}
				}
			    log.info(String.format("Started a maintenance window for test definition %d  with schedule %s", testDefId, schedule.toString()));
			} catch (Throwable t) {
				log.error(String.format("A problem occurred while attempting to start a maintenance window for test definition %d  with schedule %s", testDefId, schedule.toString()), t);
			}
		}
		long prevRecurrenceEnd = CronUtil.getPrevRecurrenceEnd(schedule);
		try {
			scheduleEndMaintWindow(scheduler, schedule, prevRecurrenceEnd, dao, cache);
		} catch (SchedulerException e) {
			throw new JobExecutionException(e);
		}
	}
	

}

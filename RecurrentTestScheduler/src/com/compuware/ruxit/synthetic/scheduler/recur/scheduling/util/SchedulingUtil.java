package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import java.util.Date;
import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestDefinitionDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestQueueDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.TimeBasedScheduler;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job.EndMaintenanceJob;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job.EnqueueTestJob;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job.StartMaintenanceJob;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.MaintScheduleCache;

public class SchedulingUtil {
	private static Logger log = LoggerFactory.getLogger(SchedulingUtil.class);

	public static void scheduleStartMaintWindow(TimeBasedScheduler scheduler, MaintScheduleView schedule, TestDefinitionDao dao, MaintScheduleCache cache)
			throws SchedulerException {
 		long id = schedule.getScheduleId();
		String rrule = schedule.getRecurrenceRule();
		rrule = CronUtil.translate(rrule, 0);
	    log.info(String.format("Scheduling start job for maintenance schedule with id: %d, rrule: %s, duration: %d.", id, rrule, schedule.getDuration()));
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("data", schedule);
		dataMap.put("dao", dao);
		dataMap.put("cache", cache);
		dataMap.put("scheduler", scheduler);
		CronScheduleBuilder cronBuilder = cronSchedule(rrule)
				.inTimeZone(TimeZone.getTimeZone(schedule.getTimeZone().getName()));
		Trigger trigger = newTrigger()
			    .withIdentity("start-trigger-" + id, "maintSchedules")
			    .withSchedule(cronBuilder)
			    .build();
		JobDetail job = newJob(StartMaintenanceJob.class)
				.withIdentity("start-job-" + id, "maintSchedules")
				.usingJobData(dataMap)
				.build();
		try {
		    scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException ex) {
			log.error(String.format("A problem occurred while attempting to load the specified maintenance start schedule into the scheduler (%s): %s", ex.getMessage(), schedule));
		}
	}

	public static void scheduleEndMaintWindow(TimeBasedScheduler scheduler, MaintScheduleView schedule,
			long prevRecurrenceEnd, TestDefinitionDao dao, MaintScheduleCache cache) throws SchedulerException {
		long id = schedule.getScheduleId();
	    log.info(String.format("Scheduling end job for maintenance schedule with id: %d, end time: %s.", id, DateFormatUtil.format(prevRecurrenceEnd)));
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("data", schedule);
		dataMap.put("dao", dao);
		dataMap.put("cache", cache);
		dataMap.put("scheduler", scheduler);
		Trigger trigger = newTrigger()
			    .withIdentity("end-trigger-" + id, "maintSchedules")
			    .startAt(new Date(prevRecurrenceEnd))
			    .build();
		JobDetail job = newJob(EndMaintenanceJob.class)
				.withIdentity("end-job-" + id, "maintSchedules")
				.usingJobData(dataMap)
				.build();
		try {
		    scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException ex) {
			log.error(String.format("A problem occurred while attempting to load the specified maintenance end schedule into the scheduler (%s): %s", ex.getMessage(), schedule));
		}
	}

	public static void scheduleTestPlan(TimeBasedScheduler scheduler, TestPlanView testPlan, TestQueueDao dao)
			throws SchedulerException {
		long id = testPlan.getId();
		String rrule = testPlan.getRecurrenceRule();
		rrule = CronUtil.translate(rrule);
	    log.info(String.format("Scheduling job for test plan with id: %d, rrule: %s.", id, rrule));
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("data", testPlan);
		dataMap.put("dao", dao);
		Trigger trigger = newTrigger()
			    .withIdentity("trigger-" + id, "testPlans")
			    .withSchedule(cronSchedule(rrule))
			    .build();
		JobDetail job = newJob(EnqueueTestJob.class)
				.withIdentity("job-" + id, "testPlans")
				.usingJobData(dataMap)
				.build();
		try {
		    scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException ex) {
			log.error(String.format("A problem occurred while attempting to load the specified test plan into the scheduler (%s): %s", ex.getMessage(), testPlan));
		}
	}

	public static void unscheduleTestPlan(TimeBasedScheduler scheduler, TestPlanView oldTestPlan)
			throws SchedulerException {
		long id = oldTestPlan.getId();
	    log.info(String.format("Unscheduling job for test plan with id: %d.", id));
		scheduler.unscheduleJob(triggerKey("trigger-" + id, "testPlans"));
		scheduler.deleteJob(jobKey("job-" + id, "group1"));
	}
	
	public static void unscheduleStartMaintWindow(TimeBasedScheduler scheduler, MaintScheduleView oldSchedule)
			throws SchedulerException {
		long id = oldSchedule.getScheduleId();
	    log.info(String.format("Unscheduling start job for maintenance schedule with id: %d.", id));
		scheduler.unscheduleJob(triggerKey("start-trigger-" + id, "maintSchedules"));
		scheduler.deleteJob(jobKey("start-job-" + id, "maintSchedules"));
	}

	public static void unscheduleEndMaintWindow(TimeBasedScheduler scheduler, MaintScheduleView oldSchedule)
			throws SchedulerException {
		long id = oldSchedule.getScheduleId();
	    log.info(String.format("Unscheduling end job for maintenance schedule with id: %d.", id));
		scheduler.unscheduleJob(triggerKey("end-trigger-" + id, "maintSchedules"));
		scheduler.deleteJob(jobKey("end-job-" + id, "maintSchedules"));
	}
	
	private SchedulingUtil () {}
	
}

package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TimeZone;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job.ClockJob;

public class TestCronUtil {

	@Test
	public void testGetPrevEndNonExact() {
		MaintScheduleView schedule = MaintScheduleView.create()
				.withScheduleId(1L)
				.withName("Test 1")
				.withRecurrenceRule("32 10 * * *")
				.withDuration(60)
				.withTenantId(1L)
				.withTimeZone(TimeZone.create()
						.withId(7L)
						.withName("US/Eastern")
						.build())
				.withLastModified(System.currentTimeMillis())
				.withDeleted(false)
				.withActive(true)
				.withTestDefinition(1L)
				.build();	
		
		long prevEnd = CronUtil.getPrevRecurrenceEnd(schedule);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		df.setTimeZone(java.util.TimeZone.getTimeZone(schedule.getTimeZone().getName()));
		System.out.println(df.format(new Date(prevEnd)));
	}

	@Test
	public void testGetPrevEndExact() {
		MaintScheduleView schedule = MaintScheduleView.create()
				.withScheduleId(1L)
				.withName("Test 1")
				.withRecurrenceRule("32 10 31 1 * 2014")
				.withDuration(60)
				.withTenantId(1L)
				.withTimeZone(TimeZone.create()
						.withId(7L)
						.withName("US/Eastern")
						.build())
				.withLastModified(System.currentTimeMillis())
				.withDeleted(false)
				.withActive(true)
				.withTestDefinition(1L)
				.build();	
		
		long prevEnd = CronUtil.getPrevRecurrenceEnd(schedule);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		df.setTimeZone(java.util.TimeZone.getTimeZone(schedule.getTimeZone().getName()));
		System.out.println(df.format(new Date(prevEnd)));
	}
	
	@Test
	public void testTranslate() {
		String translated = CronUtil.translate("32 10 * * *", 0);
		assertThat(translated, is("0 32 10 ? * *"));

		translated = CronUtil.translate("32 10 1 * *", 0);
		assertThat(translated, is("0 32 10 1 * ?"));

		translated = CronUtil.translate("32 10 31 1 * 2014", 0);
		assertThat(translated, is("0 32 10 31 1 ? 2014"));
	}
	
	@Test
	public void testQuartzExact() throws SchedulerException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("data", "Hello world!");
		CronScheduleBuilder cronBuilder = cronSchedule("0 9 11 31 1 ? 2014")
				.inTimeZone(java.util.TimeZone.getTimeZone("US/Eastern"));
		
		Trigger trigger = newTrigger()
			    .withIdentity("trigger1", "group1")
			    .withSchedule(cronBuilder)
			    .build();
		JobDetail job = newJob(ClockJob.class)
				.withIdentity("job1", "group1")
				.usingJobData(dataMap)
				.build();
		scheduler.scheduleJob(job, trigger);
		scheduler.start();
		
		try {
			Thread.sleep(2 * 60 * 1000);
			scheduler.shutdown();
		} catch (Throwable t) {
			scheduler.shutdown();
		}
		
	}
	
}

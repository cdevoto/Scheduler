package com.compuware.ruxit.synthetic.scheduler.recur.scheduling;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.springframework.stereotype.Component;

import com.compuware.ruxit.synthetic.termination.LifecycleTerminationListenerAdapter;

@Component
public class TimeBasedScheduler extends LifecycleTerminationListenerAdapter {
	private static final String NAME = "Time-based Scheduler";
	private Scheduler scheduler;

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public void init() throws Exception {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
	}

	@Override
	public void start() throws Exception {
		scheduler.start();
	}

	@Override
	public void stop() throws Exception {
		scheduler.shutdown();
	}

	public void addCalendar(String arg0, Calendar arg1, boolean arg2,
			boolean arg3) throws SchedulerException {
		scheduler.addCalendar(arg0, arg1, arg2, arg3);
	}

	public void addJob(JobDetail arg0, boolean arg1, boolean arg2)
			throws SchedulerException {
		scheduler.addJob(arg0, arg1, arg2);
	}

	public void addJob(JobDetail arg0, boolean arg1) throws SchedulerException {
		scheduler.addJob(arg0, arg1);
	}

	public boolean checkExists(JobKey arg0) throws SchedulerException {
		return scheduler.checkExists(arg0);
	}

	public boolean checkExists(TriggerKey arg0) throws SchedulerException {
		return scheduler.checkExists(arg0);
	}

	public void clear() throws SchedulerException {
		scheduler.clear();
	}

	public boolean deleteCalendar(String arg0) throws SchedulerException {
		return scheduler.deleteCalendar(arg0);
	}

	public boolean deleteJob(JobKey arg0) throws SchedulerException {
		return scheduler.deleteJob(arg0);
	}

	public boolean deleteJobs(List<JobKey> arg0) throws SchedulerException {
		return scheduler.deleteJobs(arg0);
	}

	public Calendar getCalendar(String arg0) throws SchedulerException {
		return scheduler.getCalendar(arg0);
	}

	public List<String> getCalendarNames() throws SchedulerException {
		return scheduler.getCalendarNames();
	}

	public SchedulerContext getContext() throws SchedulerException {
		return scheduler.getContext();
	}

	public List<JobExecutionContext> getCurrentlyExecutingJobs()
			throws SchedulerException {
		return scheduler.getCurrentlyExecutingJobs();
	}

	public JobDetail getJobDetail(JobKey arg0) throws SchedulerException {
		return scheduler.getJobDetail(arg0);
	}

	public List<String> getJobGroupNames() throws SchedulerException {
		return scheduler.getJobGroupNames();
	}

	public Set<JobKey> getJobKeys(GroupMatcher<JobKey> arg0)
			throws SchedulerException {
		return scheduler.getJobKeys(arg0);
	}

	public ListenerManager getListenerManager() throws SchedulerException {
		return scheduler.getListenerManager();
	}

	public SchedulerMetaData getMetaData() throws SchedulerException {
		return scheduler.getMetaData();
	}

	public Set<String> getPausedTriggerGroups() throws SchedulerException {
		return scheduler.getPausedTriggerGroups();
	}

	public String getSchedulerInstanceId() throws SchedulerException {
		return scheduler.getSchedulerInstanceId();
	}

	public String getSchedulerName() throws SchedulerException {
		return scheduler.getSchedulerName();
	}

	public Trigger getTrigger(TriggerKey arg0) throws SchedulerException {
		return scheduler.getTrigger(arg0);
	}

	public List<String> getTriggerGroupNames() throws SchedulerException {
		return scheduler.getTriggerGroupNames();
	}

	public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> arg0)
			throws SchedulerException {
		return scheduler.getTriggerKeys(arg0);
	}

	public TriggerState getTriggerState(TriggerKey arg0)
			throws SchedulerException {
		return scheduler.getTriggerState(arg0);
	}

	public List<? extends Trigger> getTriggersOfJob(JobKey arg0)
			throws SchedulerException {
		return scheduler.getTriggersOfJob(arg0);
	}

	public boolean interrupt(JobKey arg0) throws UnableToInterruptJobException {
		return scheduler.interrupt(arg0);
	}

	public boolean interrupt(String arg0) throws UnableToInterruptJobException {
		return scheduler.interrupt(arg0);
	}

	public boolean isInStandbyMode() throws SchedulerException {
		return scheduler.isInStandbyMode();
	}

	public boolean isShutdown() throws SchedulerException {
		return scheduler.isShutdown();
	}

	public boolean isStarted() throws SchedulerException {
		return scheduler.isStarted();
	}

	public void pauseAll() throws SchedulerException {
		scheduler.pauseAll();
	}

	public void pauseJob(JobKey arg0) throws SchedulerException {
		scheduler.pauseJob(arg0);
	}

	public void pauseJobs(GroupMatcher<JobKey> arg0) throws SchedulerException {
		scheduler.pauseJobs(arg0);
	}

	public void pauseTrigger(TriggerKey arg0) throws SchedulerException {
		scheduler.pauseTrigger(arg0);
	}

	public void pauseTriggers(GroupMatcher<TriggerKey> arg0)
			throws SchedulerException {
		scheduler.pauseTriggers(arg0);
	}

	public Date rescheduleJob(TriggerKey arg0, Trigger arg1)
			throws SchedulerException {
		return scheduler.rescheduleJob(arg0, arg1);
	}

	public void resumeAll() throws SchedulerException {
		scheduler.resumeAll();
	}

	public void resumeJob(JobKey arg0) throws SchedulerException {
		scheduler.resumeJob(arg0);
	}

	public void resumeJobs(GroupMatcher<JobKey> arg0) throws SchedulerException {
		scheduler.resumeJobs(arg0);
	}

	public void resumeTrigger(TriggerKey arg0) throws SchedulerException {
		scheduler.resumeTrigger(arg0);
	}

	public void resumeTriggers(GroupMatcher<TriggerKey> arg0)
			throws SchedulerException {
		scheduler.resumeTriggers(arg0);
	}

	public void scheduleJob(JobDetail arg0, Set<? extends Trigger> arg1,
			boolean arg2) throws SchedulerException {
		scheduler.scheduleJob(arg0, arg1, arg2);
	}

	public Date scheduleJob(JobDetail arg0, Trigger arg1)
			throws SchedulerException {
		return scheduler.scheduleJob(arg0, arg1);
	}

	public Date scheduleJob(Trigger arg0) throws SchedulerException {
		return scheduler.scheduleJob(arg0);
	}

	public void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> arg0,
			boolean arg1) throws SchedulerException {
		scheduler.scheduleJobs(arg0, arg1);
	}

	public void setJobFactory(JobFactory arg0) throws SchedulerException {
		scheduler.setJobFactory(arg0);
	}

	public void shutdown() throws SchedulerException {
		scheduler.shutdown();
	}

	public void shutdown(boolean arg0) throws SchedulerException {
		scheduler.shutdown(arg0);
	}

	public void standby() throws SchedulerException {
		scheduler.standby();
	}

	public void triggerJob(JobKey arg0, JobDataMap arg1)
			throws SchedulerException {
		scheduler.triggerJob(arg0, arg1);
	}

	public void triggerJob(JobKey arg0) throws SchedulerException {
		scheduler.triggerJob(arg0);
	}

	public boolean unscheduleJob(TriggerKey arg0) throws SchedulerException {
		return scheduler.unscheduleJob(arg0);
	}

	public boolean unscheduleJobs(List<TriggerKey> arg0)
			throws SchedulerException {
		return scheduler.unscheduleJobs(arg0);
	}
	
	
	
}

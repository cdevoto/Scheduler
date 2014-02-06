package com.compuware.ruxit.synthetic.scheduler.recur.scheduling;

import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.SchedulingUtil.scheduleEndMaintWindow;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.SchedulingUtil.scheduleStartMaintWindow;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.SchedulingUtil.scheduleTestPlan;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.SchedulingUtil.unscheduleEndMaintWindow;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.SchedulingUtil.unscheduleStartMaintWindow;
import static com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.SchedulingUtil.unscheduleTestPlan;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.compuware.ruxit.synthetic.scheduler.core.config.dao.SchedulerConfigDao;
import com.compuware.ruxit.synthetic.scheduler.core.config.dao.model.SchedulerConfig;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScheduleDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestDefinitionDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestPlanDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestQueueDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;
import com.compuware.ruxit.synthetic.scheduler.core.util.DateFormatUtil;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.event.EventListener;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.event.EventManager;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.event.LastPollTime;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.MaintScheduleCache;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.TestDefinition;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.TestPlanCache;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.util.CronUtil;
import com.compuware.ruxit.synthetic.termination.LifecycleTerminationListenerAdapter;

public class TestScheduleManager extends LifecycleTerminationListenerAdapter implements EventListener {
	private static final String NAME = "Test Schedule Manager";
	private static final Logger log = LoggerFactory.getLogger(TestScheduleManager.class);
	
	private int maxRows = 10;
	private int pollFrequency = 60; // seconds
	private SchedulerConfig schedulerConfig;
	@Autowired
	private TimeBasedScheduler scheduler;
	@Autowired
	private TestPlanCache testPlanCache;
	@Autowired
	private MaintScheduleCache maintScheduleCache;
	@Autowired
	private TestPlanDao testPlanDao;
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private TestDefinitionDao testDefDao;
	@Autowired
	private TestQueueDao testQueueDao;

	private EventManager eventManager;
	private ScheduledExecutorService scheduledThreadPool;

	public TestScheduleManager () {}
	
	@Override
	public String getName() {
		return NAME + " " + schedulerConfig.getId();
	}
	
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	
	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	@Autowired
	public void setSchedulerConfigDao(SchedulerConfigDao schedulerConfigDao) {
		this.schedulerConfig = schedulerConfigDao.get();
	}
	

	@Autowired
	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
		eventManager.addListener(this);
	}

	@Override
	public void init() throws Exception {
		this.scheduledThreadPool = Executors.newScheduledThreadPool(1);
		LastPollTime.snapshot();
		loadTestPlans();
		loadMaintenanceSchedules();
		if (log.isDebugEnabled()) {
		    log.info(maintScheduleCache.toString());
		    log.info(testPlanCache.toString());
		}
	}

	@Override
	public void start() throws Exception {
		scheduledThreadPool.scheduleAtFixedRate(eventManager, pollFrequency, pollFrequency, TimeUnit.SECONDS);
	}

	@Override
	public void stop() throws Exception {
		scheduledThreadPool.shutdown();
	}

	@Override
	public void onTestPlanModified(TestPlanView testPlan) throws SchedulerException {
		List<TestPlanView> oldTestPlans = testPlanCache.getByTestDefId(testPlan.getTestDefinitionId());
		for (TestPlanView oldTestPlan : oldTestPlans) {
			if (oldTestPlan.getLastModified() < testPlan.getLastModified()) {
			    testPlanCache.remove(oldTestPlan);
			    unscheduleTestPlan(scheduler, oldTestPlan);
			}
		}
		if (testPlan.isActive() && !testPlan.isDeleted()) {
			testPlanCache.add(testPlan);
			scheduleTestPlan(scheduler, testPlan, testQueueDao, maintScheduleCache);
		}
	}

	@Override
	public void onMaintScheduleModified(MaintScheduleView schedule) throws SchedulerException {
		Map<Long, TestDefinition> impactedTestDefs = new HashMap<>();
		MaintScheduleView oldSchedule = maintScheduleCache.get(schedule.getScheduleId());
		if (oldSchedule != null) {
			for (long testDefId : oldSchedule.getTestDefinitionIds()) {
				TestDefinition testDef = maintScheduleCache.getTestDefinitionById(testDefId);
				if (testDef != null) {
					impactedTestDefs.put(testDefId, testDef);
					synchronized (testDef) {
						testDef.removeActiveMaintSchedule(oldSchedule.getScheduleId());
					}
				}
			}
			unscheduleStartMaintWindow(scheduler, oldSchedule);
			unscheduleEndMaintWindow(scheduler, oldSchedule);
			maintScheduleCache.remove(oldSchedule);
		}
		if (schedule.isActive() && !schedule.isDeleted()) {
			maintScheduleCache.add(schedule);
			scheduleStartMaintWindow(scheduler, schedule, testDefDao, maintScheduleCache);
			checkActive(schedule);
			for (Long testDefId : schedule.getTestDefinitionIds()) {
				TestDefinition testDef = maintScheduleCache.getTestDefinitionById(testDefId);
				impactedTestDefs.put(testDef.getId(), testDef);
			}
		}
		updateMaintSuspend(new HashSet<>(impactedTestDefs.values()));
		if (log.isDebugEnabled()) {
		    log.debug(maintScheduleCache.toString());
		}
	}

	private void loadTestPlans() throws SchedulerException {
		long maxLastModified = LastPollTime.get();
		List<TestPlanView> testPlans = testPlanDao.get(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified);
		while (true) {
			for (TestPlanView testPlan : testPlans) {
				testPlanCache.add(testPlan);
				scheduleTestPlan(scheduler, testPlan, testQueueDao, maintScheduleCache);
			}
			if (testPlans.size() == maxRows) {
				TestPlanView last = testPlans.get(maxRows - 1);
				long minTestDefId = last.getTestDefinitionId();
				long minTestPlanId = last.getId();
				testPlans = testPlanDao.get(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified, minTestDefId, minTestPlanId);
			} else {
				break;
			}
		}
	}

	private void loadMaintenanceSchedules() throws SchedulerException {
		long maxLastModified = LastPollTime.get();
        Map<Long, MaintScheduleView> allSchedules = new HashMap<>(); 
		List<MaintScheduleView> schedules = scheduleDao.getMaintenanceSchedules(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified);
		while (true) {
			for (MaintScheduleView schedule : schedules) {
				MaintScheduleView existing = allSchedules.get(schedule.getScheduleId());
				if (existing != null) {
					for (long testDefId : schedule.getTestDefinitionIds()) {
					    existing.addTestDefinitionId(testDefId);
					}
				} else {
				    allSchedules.put(schedule.getScheduleId(), MaintScheduleView.create(schedule).build());
				}
			}
			if (schedules.size() == maxRows) {
				MaintScheduleView last = schedules.get(maxRows - 1);
				long minScheduleId = last.getScheduleId();
				long minTestDefId = last.getTestDefinitionIds().get(0);
				schedules = scheduleDao.getMaintenanceSchedules(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified, minScheduleId, minTestDefId);
			} else {
				break;
			}
		}
		for (MaintScheduleView schedule : allSchedules.values()) {
			maintScheduleCache.add(schedule);
			scheduleStartMaintWindow(scheduler, schedule, testDefDao, maintScheduleCache);
			
			// Check to see if the maintenance schedule is currently active
			checkActive(schedule);
		}
		// Update the maint_suspend attribute for all test definitions
        updateMaintSuspend(maintScheduleCache.getTestDefinitions());


	}

	private void updateMaintSuspend(Collection<TestDefinition> testDefs) {
		for (TestDefinition testDef : testDefs) {
        	synchronized (testDef) {
        	    boolean suspended = testDef.hasActiveMaintSchedules();
        	    testDefDao.suspendForMaintenance(testDef.getId(), suspended);
        	}
        }
	}

	private void checkActive(MaintScheduleView schedule)
			throws SchedulerException {
		long prevRecurrenceEnd = isActive(schedule);
		if (prevRecurrenceEnd != -1) {
			scheduleEndMaintWindow(scheduler, schedule, prevRecurrenceEnd, testDefDao, maintScheduleCache);
			for (long testDefId : schedule.getTestDefinitionIds()) {
				TestDefinition testDef = maintScheduleCache.getTestDefinitionById(testDefId);
				synchronized (testDef) {
				    testDef.addActiveMaintSchedule(schedule);
				}
			}
		}
	}
	
	private long isActive(MaintScheduleView schedule) {
		long prevRecurrenceEnd = CronUtil.getPrevRecurrenceEnd(schedule);
		if (prevRecurrenceEnd > System.currentTimeMillis() + (30 * 1000)) {
			log.info(String.format("Maintenance schedule with id: %d, rrule: %s, duration: %d is active.  The current time is %s, and the current maintenance window ends at %s.", schedule.getScheduleId(), schedule.getRecurrenceRule(), schedule.getDuration(), DateFormatUtil.formatNow(), DateFormatUtil.format(prevRecurrenceEnd)));
			return prevRecurrenceEnd;
		}
		log.info(String.format("Maintenance schedule with id: %d, rrule: %s, duration: %d is not active.  The current time is %s, and the previous maintenance window ended at %s.", schedule.getScheduleId(), schedule.getRecurrenceRule(), schedule.getDuration(), DateFormatUtil.formatNow(), DateFormatUtil.format(prevRecurrenceEnd)));
		return -1L;
	}

}

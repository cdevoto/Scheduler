package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.event;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.compuware.ruxit.synthetic.scheduler.core.config.dao.SchedulerConfigDao;
import com.compuware.ruxit.synthetic.scheduler.core.config.dao.model.SchedulerConfig;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScheduleDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestPlanDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;
import com.compuware.ruxit.synthetic.scheduler.core.util.DateFormatUtil;

public class PollingEventManager implements Runnable, EventManager {
	private static Logger log = LoggerFactory.getLogger(PollingEventManager.class);
	
	// No real need for thread safety on the listeners object since it will only be updated at initialization time.
	private List<EventListener> listeners = new LinkedList<>();

    private SchedulerConfig schedulerConfig;
	private int maxRows;
	private TestPlanDao testPlanDao;
	private ScheduleDao scheduleDao;
	
	public PollingEventManager () {}
	
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	@Autowired
	public void setSchedulerConfigDao(SchedulerConfigDao schedulerConfigDao) {
		this.schedulerConfig = schedulerConfigDao.get();
	}
	
	@Autowired
	public void setTestPlanDao(TestPlanDao testPlanDao) {
		this.testPlanDao = testPlanDao;
	}

	@Autowired
	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}

	@Override
	public void addListener(EventListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(EventListener listener) {
		this.listeners.remove(listener);
	}
	
	@Override
	public void run() {
		long minLastModified = LastPollTime.get();
		log.info(String.format("Polling the scheduling repository for any changes made since %s.", DateFormatUtil.format(new Date(minLastModified))));
		long maxLastModified = LastPollTime.snapshot();
		pollTestPlans(minLastModified, maxLastModified);
		pollMaintSchedules(minLastModified, maxLastModified);
	}

	private void pollMaintSchedules(long minLastModified, long maxLastModified) {
		Map<Long, MaintScheduleView> maintSchedules = new HashMap<>();
		int totalChanges = 0;
		List<MaintScheduleView> schedules = scheduleDao.getMaintenanceSchedules(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified, minLastModified);
		while (true) {
			totalChanges += schedules.size();
			for (MaintScheduleView schedule : schedules) {
				MaintScheduleView existing = maintSchedules.get(schedule.getScheduleId());
				if (existing != null) {
					for (long testDefId : schedule.getTestDefinitionIds()) {
					    existing.addTestDefinitionId(testDefId);
					}
				} else {
				    maintSchedules.put(schedule.getScheduleId(), MaintScheduleView.create(schedule).build());
				}
			}
			if (schedules.size() == maxRows) {
				MaintScheduleView last = schedules.get(maxRows - 1);
				long minScheduleId = last.getScheduleId();
				long minTestDefId = last.getTestDefinitionIds().get(0);
				schedules = scheduleDao.getMaintenanceSchedules(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified, minScheduleId, minTestDefId, minLastModified);
			} else {
				break;
			}
		}
		if (totalChanges > 0) {
		    log.info(String.format("Found %d relevant maintenance schedule changes.", totalChanges));
		}
		Set<Long> affectedTestDefs = new HashSet<>();
		for (MaintScheduleView schedule : maintSchedules.values()) {
			affectedTestDefs.addAll(schedule.getTestDefinitionIds());
			notify(schedule);
		}
		if (!affectedTestDefs.isEmpty()) {
		    log.info(String.format("%d test definitions affected.", affectedTestDefs.size()));
		}
	}

	private void pollTestPlans(long minLastModified, long maxLastModified) {
		int totalChanges = 0;
		List<TestPlanView> testPlans = testPlanDao.get(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified, minLastModified);
		while (true) {
			totalChanges += testPlans.size();
			for (TestPlanView testPlan : testPlans) {
				notify(testPlan);
			}
			if (testPlans.size() == maxRows) {
				TestPlanView last = testPlans.get(maxRows - 1);
				long minTestDefId = last.getTestDefinitionId();
				long minTestPlanId = last.getId();
				testPlans = testPlanDao.get(schedulerConfig.getTotalWorkers(), schedulerConfig.getWorkerNumber(), maxRows, maxLastModified, minTestDefId, minTestPlanId, minLastModified);
			} else {
				break;
			}
		}
		if (totalChanges > 0) {
		    log.info(String.format("Found %d relevant test plan changes.", totalChanges));
		}
	}

	private void notify(MaintScheduleView subject) {
		for (EventListener listener : listeners) {
			try {
			    listener.onMaintScheduleModified(subject);
			} catch (Throwable t) {
				log.error(String.format("An error occurred while processing a change to maintenance schedule %s.", subject), t);
			}
		}
	}

	private void notify(TestPlanView subject) {
		for (EventListener listener : listeners) {
			try {
			    listener.onTestPlanModified(subject);
			} catch (Throwable t) {
				log.error(String.format("An error occurred while processing a change to test plan %s.", subject), t);
			}
		}
	}

}

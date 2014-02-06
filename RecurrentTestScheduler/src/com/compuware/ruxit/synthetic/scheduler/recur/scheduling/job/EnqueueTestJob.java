package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestQueueDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.MaintScheduleCache;
import com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model.TestDefinition;

public class EnqueueTestJob implements Job {
	private static Logger log = LoggerFactory.getLogger(EnqueueTestJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		TestPlanView testPlan = (TestPlanView) data.get("data");
		MaintScheduleCache cache = (MaintScheduleCache) data.get("cache");
		TestDefinition testDef = cache.getTestDefinitionById(testPlan.getTestDefinitionId());
		if (testDef != null && testDef.hasActiveMaintSchedules()) {
			log.info("Test cancelled due to active maintenance schedule for test definition - " + testPlan);
			return;
		}
		TestQueueDao dao = (TestQueueDao) data.get("dao");
		Test test = Test.create()
				.withTestDefinitionId(testPlan.getTestDefinitionId())
				.withScriptId(testPlan.getScriptId())
				.withTenantId(testPlan.getTenantId())
				.withLcpId(testPlan.getLcpId())
				.withPriority(Test.Priority.MEDIUM)
				.withRequiresF(testPlan.getRequiresF())
				.withStatus(Test.Status.ENQUEUED)
				.build();
		try {
		    dao.push(test);
		    log.info("Test enqueued - " + test.toString());
		} catch (Throwable t) {
			log.error("A problem occurred while attempting to enqueue test - " + test, t);
		}
	}
	

}

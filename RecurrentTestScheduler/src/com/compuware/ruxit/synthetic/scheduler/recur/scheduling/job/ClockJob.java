package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ClockJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.zzz");
		JobDataMap data = context.getJobDetail().getJobDataMap();
		System.out.printf("[%s] Executing Test: %s%n", df.format(now), data.get("data").toString());
	}
	

}

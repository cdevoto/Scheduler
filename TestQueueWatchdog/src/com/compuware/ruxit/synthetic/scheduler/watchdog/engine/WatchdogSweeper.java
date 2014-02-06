package com.compuware.ruxit.synthetic.scheduler.watchdog.engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestQueueDao;
import com.compuware.ruxit.synthetic.termination.LifecycleTerminationListenerAdapter;

public class WatchdogSweeper extends LifecycleTerminationListenerAdapter {
	private static Logger log = LoggerFactory.getLogger(WatchdogSweeper.class);

	private ScheduledExecutorService scheduledThreadPool;
	private int pollFrequency = 60; // seconds
	private int testExpiration = 5 * 60; // seconds
	@Autowired
	private TestQueueDao dao;
	
	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	public void setTestExpiration(int testExpiration) {
		this.testExpiration = testExpiration;
	}

	@Override
	public String getName() {
		return "Watchdog Sweeper";
	}
	
	@Override
	public void init() throws Exception {
		this.scheduledThreadPool = Executors.newScheduledThreadPool(1);
	}
	
	@Override
	public void start() throws Exception {
		scheduledThreadPool.scheduleAtFixedRate(new WatchdogSweeperTask(), pollFrequency, pollFrequency, TimeUnit.SECONDS);
	}

	@Override
	public void stop() throws Exception {
		scheduledThreadPool.shutdown();
	}
	
	private class WatchdogSweeperTask implements Runnable {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");

		@Override
		public void run() {
			log.info("Watchdog Sweeper Task started...");
			try {
				Date cutoff = new Date(System.currentTimeMillis() - (testExpiration * 1000));
				log.info(String.format("Sweeping for tests that were enqueued or dispatched before %s", df.format(cutoff)));
				dao.cancelTestsEnqueuedBefore(cutoff.getTime());
				dao.cancelTestsDispatchedBefore(cutoff.getTime());
				log.info("Watchdog Sweeper Task completed successfully.");
			} catch (Throwable t) {
				log.error("A problem occurred while attempting to clean up the test queue", t);
			}
		}
		
	}

}

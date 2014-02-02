package com.compuware.ruxit.synthetic.dispatcher;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;

@WebListener
public class TestDispatchManager implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(TestDispatchManager.class);
	public static final int DEFAULT_DISPATCH_WORKERS = 10;

	private ExecutorService jobExecutor;
	private volatile boolean terminated;
	private TestDispatchServiceConfig serviceConfig;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
	    WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        TestDefinitionService service = springContext.getBean(TestDefinitionService.class);
	    
		ServletContext context = event.getServletContext();
		int dispatchWorkers = getDispatchWorkers(context);
		String workerTimeout = context.getInitParameter("workerTimeout");
		String workerPollInterval = context.getInitParameter("workerPollInterval");
		
		this.serviceConfig = TestDispatchServiceConfig.create()
				.withWorkerTimeout(workerTimeout)
				.withWorkerPollInterval(workerPollInterval)
				.withService(service)
				.build();
		
		
	    final Queue<AsyncContext> jobQueue = new ConcurrentLinkedQueue<AsyncContext>();
	    event.getServletContext().setAttribute("dispatchJobQueue", jobQueue);
	    // pool size matching Web services capacity
	    this.jobExecutor = Executors.newFixedThreadPool(dispatchWorkers);
	    Thread dispatchManagerThread = new Thread(new Runnable () {
	    	
	    	public void run () {
			    while(!terminated) {
			        if(!jobQueue.isEmpty()) {
			            final AsyncContext asyncContext = jobQueue.poll();
			            jobExecutor.execute(new TestDispatchService(serviceConfig, asyncContext));             
			        }
			    }
	    	}
	    }, "Test-Dispatch-Manager");
	    dispatchManagerThread.setDaemon(true);
	    dispatchManagerThread.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		this.terminated = true;
		jobExecutor.shutdown();
	}

	private int getDispatchWorkers(ServletContext context) {
		String dispatchWorkersParam = context.getInitParameter("dispatchWorkers");
		int dispatchWorkers = DEFAULT_DISPATCH_WORKERS;
		if (dispatchWorkersParam == null) {
			log.warn(String.format("Expected a 'dispatchWorkers' context parameter. Defaulting to %d.", DEFAULT_DISPATCH_WORKERS));
		} else {
			try {
				dispatchWorkers = Integer.parseInt(dispatchWorkersParam);
				if (dispatchWorkers < 1) {
					log.warn(String.format("Expected a positive integer value for the 'dispatchWorkers' context parameter; defaulting to %d.", DEFAULT_DISPATCH_WORKERS));
                    dispatchWorkers = DEFAULT_DISPATCH_WORKERS;
				}
			} catch (NumberFormatException ex) {
				log.warn(String.format("Expected a positive integer value for the 'dispatchWorkers' context parameter; defaulting to %d.", DEFAULT_DISPATCH_WORKERS));
			}
		}
		return dispatchWorkers;
	}



}

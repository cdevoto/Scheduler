package com.compuware.ruxit.synthetic.dispatcher;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class TestDispatchManager implements ServletContextListener {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(TestDispatchManager.class);
	public static final int DEFAULT_DISPATCH_WORKERS = 10;

	private ExecutorService jobExecutor;
	private volatile boolean terminated;
	private TestDispatchServiceFactory factory;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
	    WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
	    this.factory = springContext.getBean(TestDispatchServiceFactory.class);
	    int dispatchWorkers = factory.getConfig().getDispatchWorkers();
	    
	    final Queue<AsyncContext> jobQueue = new ConcurrentLinkedQueue<AsyncContext>();
	    event.getServletContext().setAttribute("dispatchJobQueue", jobQueue);
	    // pool size matching Web services capacity
	    this.jobExecutor = Executors.newFixedThreadPool(dispatchWorkers);
	    Thread dispatchManagerThread = new Thread(new Runnable () {
	    	
	    	public void run () {
			    while(!terminated) {
			        if(!jobQueue.isEmpty()) {
			            final AsyncContext asyncContext = jobQueue.poll();
			            jobExecutor.execute(factory.create(asyncContext));             
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

}

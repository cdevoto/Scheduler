package com.compuware.ruxit.synthetic.engine;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.di.AppContextService;
import com.compuware.ruxit.synthetic.di.AppContextServices;
import com.compuware.ruxit.synthetic.termination.TerminationListener;
import com.compuware.ruxit.synthetic.termination.TerminationSensor;
import com.compuware.ruxit.synthetic.termination.Terminator;

public abstract class Engine extends LifecycleAdapter {
	private static final Logger log = LoggerFactory.getLogger(Engine.class);
	
	
	public Engine () {}
	

	@Override
	public final void init() throws Exception {
		UncaughtExceptionHandler handler = new UncaughtExceptionHandler () {
 
			@Override
			public void uncaughtException(Thread thread, Throwable t) {
				doGracefulShutdown();
			}
			
		};
		// For some reason, the uncaught exception handler is never invoked on my Mac OSx PC!
		Thread.setDefaultUncaughtExceptionHandler(handler);
		Thread.currentThread().setUncaughtExceptionHandler(handler);
		onInit();
	}

	@Override
	public final void start() throws Exception {
		log.info(String.format("Attempting to start the %s Engine...", getName()));
		AppContextService appContext = AppContextServices.getInstance();
		TerminationSensor terminationSensor = appContext.getBean(TerminationSensor.class);
		try {
			KeepAliveDaemon keepAliveDaemon = new KeepAliveDaemon();
			terminationSensor.addTerminationListener(keepAliveDaemon);
			Thread keepAliveThread = new Thread(keepAliveDaemon, "Keep-Alive-Thread");
			keepAliveThread.start();
			terminationSensor.start();
			
			Collection<TerminationListener> terminationListeners = appContext.getBeansOfType(TerminationListener.class);
			PriorityQueue<Lifecycle> lifecycles = new PriorityQueue<Lifecycle>(5, new Comparator<Lifecycle> () {
	
				@Override
				public int compare(Lifecycle lc1, Lifecycle lc2) {
					return lc1.getPriority() - lc2.getPriority();
				}
				
			});
			for (TerminationListener listener : terminationListeners) {
				terminationSensor.addTerminationListener(listener);
				if (listener instanceof Lifecycle) {
					Lifecycle lc = (Lifecycle) listener;
					lifecycles.add(lc);
				}
			}
			
			for (Lifecycle lc : lifecycles) {
				log.info(String.format("Initializing %s...", lc.getName()));
				lc.init();
				log.info(String.format("%s initialized.", lc.getName()));
			}
			for (Lifecycle lc : lifecycles) {
				log.info(String.format("Starting %s...", lc.getName()));
				lc.start();
				log.info(String.format("%s started.", lc.getName()));
			}
			
			onStart();
			log.info(String.format("The %s Engine has been started", getName()));
		} catch (Throwable t) {
            log.error(String.format("A problem occurred while attempting to start the %s Engine", getName()), t);
            terminationSensor.stop();
		}
	}

	@Override
	public final void stop() throws Exception {
		log.info(String.format("Attempting to stop the %s Engine...", getName()));
		onStop();
		AppContextService appContext = AppContextServices.getInstance();
        Terminator terminator = appContext.getBean(Terminator.class);
        try {
            terminator.terminate();
        } catch (Exception ex) {
            log.error(String.format("A problem occurred while attempting to stop the %s Engine", getName()), ex);
        }
	}
	
    private void doGracefulShutdown () {
		log.info(String.format("Attempting stop the %s Engine....", getName()));
		AppContextService appContext = AppContextServices.getInstance();
		if (appContext != null) {
		    TerminationSensor terminationSensor = appContext.getBean(TerminationSensor.class);
		    if (terminationSensor != null) {
		    	try {
					terminationSensor.stop();
				} catch (Exception e) {
					log.error(String.format("A problem occurred while attempting to stop the %s", terminationSensor.getName()), e);
				}
		    }
		}
    }
	
	protected void onInit () throws Exception {}
	protected void onStart () throws Exception {}
	protected void onStop () throws Exception {}

	
	private class KeepAliveDaemon implements TerminationListener, Runnable {
		private volatile boolean terminated;
		private volatile Thread contextThread;

		@Override
		public void run() {
			this.contextThread = Thread.currentThread();
			while (!terminated) {
				try {
					Thread.sleep(60000);
				} catch (InterruptedException ex) {}
			}
			log.info(String.format("Stopping the %s Engine...", getName()));
			// Grant a 1 second period before exiting so that other threads can terminate themselves gracefully.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {}
			log.info(String.format("The %s Engine has been stopped.", getName()));
		}

		@Override
		public void stop() {
			terminated = true;
			if (contextThread != null) {
				contextThread.interrupt();
			}
			
		}
		
	}
	
}

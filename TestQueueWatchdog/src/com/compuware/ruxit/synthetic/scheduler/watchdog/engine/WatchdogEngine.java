package com.compuware.ruxit.synthetic.scheduler.watchdog.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.di.AppContextProvider;
import com.compuware.ruxit.synthetic.engine.Engine;

public class WatchdogEngine extends Engine {

	private static final String APP_NAME = "Test Queue Watchdog";
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(WatchdogEngine.class);

	@Override
	public String getName() {
		return APP_NAME;
	}

	@Override
	protected void onInit() throws Exception {
		System.setProperty(AppContextProvider.PROPERTY_NAME, "com.compuware.ruxit.synthetic.scheduler.watchdog.di.SpringAppContextProvider");
		
	}
}

package com.compuware.ruxit.synthetic.dispatcher;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil;

public class TestDispatchEventListener implements AsyncListener {
	Logger log = LoggerFactory.getLogger(TestDispatchEventListener.class);
	
	
	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		log.info("Async complete called");

	}

	@Override
	public void onError(AsyncEvent event) throws IOException {
		log.info("Async error called");

	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
		log.info("Async start called");

	}

	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		log.info("Async timeout called");
		try {
			WebUtil.generateNormalResponse();
			event.getAsyncContext().dispatch("/WEB-INF/views/json.jsp");
		} catch (JSONException e) {
			// Should never happen!
		}
        

	}

}

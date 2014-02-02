package com.compuware.ruxit.synthetic.dispatcher;

import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateNormalResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsInt;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsLong;

import java.util.Collections;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestView;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIVUController;
import com.compuware.ruxit.synthetic.scheduler.core.util.JsonDataGenerator;

public class TestDispatchService implements Runnable {
	Logger log = LoggerFactory.getLogger(TestDispatchService.class);
	
	private AsyncContext asyncContext;
	private TestDispatchServiceConfig config;
	
	public TestDispatchService(TestDispatchServiceConfig config,
			AsyncContext asyncContext) {
		this.config = config;
		this.asyncContext = asyncContext;
	}

	@Override
	public void run() {
		long estimatedTimeout = System.currentTimeMillis() + config.getWorkerTimeout();
		
		HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
	    HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
		try {
	    	
		    //CORS header
		    response.setHeader("Access-Control-Allow-Origin", "*");
				
			Long vucId = getParameterAsLong(request, "vucId", true);
			if (vucId == null) {
				generateOutput();
				return;
			}
	
			String json = null;
			Long supportsF = getParameterAsLong(request, "supportsF", false);
			if (supportsF == null) {
				try {
					UIVUController vuc = config.getService().getVUController(vucId);
					supportsF = vuc.getSupportsF();
				} catch (Exception ex) {
					ex.printStackTrace();
					json = generateErrorResponse(500, ex.getMessage());
					generateOutput();
				}
			}

			Integer maxTests = getParameterAsInt(request, "maxTests", false);
			if (maxTests == null) {
				maxTests = 1;
			}

			try {
				List<UITestView> tests = Collections.emptyList();
				while (tests.isEmpty() && System.currentTimeMillis() < estimatedTimeout) {
					tests = config.getService().poll(vucId, supportsF, maxTests);
					if (tests.isEmpty()) {
						try {
							Thread.sleep(config.getWorkerPollInterval());
						} catch (InterruptedException ex) {}
					}
				}
				json = generateGetResponse(tests);
			} catch (Exception ex) {
				ex.printStackTrace();
				json = generateErrorResponse(500, ex.getMessage());
			}
			request.setAttribute("json", json);
			generateOutput();
		} catch (IllegalStateException ex) {
			log.info("The async context associated with the test dispatcher appears to have timed out.");
		} catch (Throwable t) {
			log.error("A problem occurred while attempting to process a test dispatch request.", t);
			try {
			    request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, t);
			    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			    asyncContext.complete();
			} catch (Throwable t2) { /* eat it! */}
		}

	}

	private String generateGetResponse(final List<UITestView> tests)
			throws JSONException {
		String json = generateNormalResponse(new JsonDataGenerator<JSONArray> () {

			@Override
			public JSONArray generate() throws JSONException {
				JSONArray data = new JSONArray();
				for (UITestView test : tests) {
					data.put(test.toJsonObject());
				}
				return data;
			}
		});
		return json.toString();
	}
	
	private void generateOutput() {
		asyncContext.dispatch("/WEB-INF/views/json.jsp");
	}


}

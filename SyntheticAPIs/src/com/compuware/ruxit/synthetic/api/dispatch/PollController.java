package com.compuware.ruxit.synthetic.api.dispatch;

import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateNormalResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsInt;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsLong;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestView;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIVUController;
import com.compuware.ruxit.synthetic.scheduler.core.util.JsonDataGenerator;

@Controller
@RequestMapping("/poll")
public class PollController {
	private static final String DEFAULT_VIEW = "json";
	
	@Autowired
	private TestDefinitionService service;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long vucId = getParameterAsLong(request, "vucId", true);
		if (vucId == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}

		String json = null;
		Long supportsF = getParameterAsLong(request, "supportsF", false);
		if (supportsF == null) {
			try {
				UIVUController vuc = service.getVUController(vucId);
				supportsF = vuc.getSupportsF();
			} catch (Exception ex) {
				ex.printStackTrace();
				json = generateErrorResponse(500, ex.getMessage());
				return new ModelAndView(DEFAULT_VIEW);
			}
		}

		Integer maxTests = getParameterAsInt(request, "maxTests", false);
		if (maxTests == null) {
			maxTests = 1;
		}

		try {
			List<UITestView> tests = service.poll(vucId, supportsF, maxTests);
			json = generateGetResponse(tests);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);

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


}

package com.compuware.ruxit.synthetic.api.crud;

import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateNormalResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;

@Controller
@RequestMapping("/instant-test")
public class InstantTestController {
	private static final String DEFAULT_VIEW = "json";
	
	@Autowired
	private TestDefinitionService service;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long testDefId = getParameterAsLong(request, "testDefinitionId", false);
		if (testDefId == null) {
		   return new ModelAndView(DEFAULT_VIEW);
		} 
		
 		String json = null;
 		try {
 			service.createInstantTests(testDefId);
 			json = generateNormalResponse();
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			json = generateErrorResponse(500, ex.getMessage());
 		}
 		request.setAttribute("json", json);
 		return new ModelAndView(DEFAULT_VIEW);
	}


}

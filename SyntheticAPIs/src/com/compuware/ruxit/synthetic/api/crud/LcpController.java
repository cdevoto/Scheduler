package com.compuware.ruxit.synthetic.api.crud;


import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetAllResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsLong;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UILcpProxy;

@Controller
@RequestMapping("/lcp")
public class LcpController {
	private static final String DEFAULT_VIEW = "json";
	
	@Autowired
	private TestDefinitionService service;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long scriptId = getParameterAsLong(request, "scriptId", true);
		if (scriptId == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}

    	
		String [] requires = request.getParameterValues("requires");
		List<String> requiresFlags = null;
		if (requires != null) {
            requiresFlags = Arrays.asList(requires);
		} else {
			requiresFlags = Collections.emptyList();
		}
    	
		String json = null;
		try {
			List<UILcpProxy> lcps = service.getLcps(scriptId, requiresFlags);
			json = generateGetAllResponse(lcps);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);

	}

}

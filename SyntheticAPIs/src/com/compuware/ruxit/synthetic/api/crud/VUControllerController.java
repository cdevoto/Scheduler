package com.compuware.ruxit.synthetic.api.crud;

import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetAllResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsLong;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.compuware.ruxit.synthetic.scheduler.core.exception.ValidationException;
import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIVUController;

@Controller
@RequestMapping("/vuc")
public class VUControllerController {
	private static final String DEFAULT_VIEW = "json";
	
	@Autowired
	private TestDefinitionService service;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long vucId = getParameterAsLong(request, "id", false);
		if (vucId == null) {
			return handleGetVUControllers(request, response);
		} 
		return handleGetVUController(request, response, vucId);
	}

	private ModelAndView handleGetVUController(HttpServletRequest request,
			HttpServletResponse response, Long vucId) throws JSONException {
		String json = null;
		try {
			UIVUController vuc = service.getVUController(vucId);
			if (vuc == null) {
				throw new ValidationException("The specified VU Controller could not be found.");
			}
			json = generateGetResponse(vuc);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);
	}

	private ModelAndView handleGetVUControllers(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String json = null;
		try {
			List<UIVUController> vucs = service.getVUControllers();
			json = generateGetAllResponse(vucs);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);
	}
}

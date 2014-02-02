package com.compuware.ruxit.synthetic.api.crud;

import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetAllResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateNormalResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameter;
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
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScript;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScriptForm;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScriptProxy;

@Controller
@RequestMapping("/script")
public class ScriptController {
	private static final String DEFAULT_VIEW = "json";
	
	@Autowired
	private TestDefinitionService service;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long tenantId = getParameterAsLong(request, "tenantId", false);
		if (tenantId == null) {
			Long scriptId = getParameterAsLong(request, "scriptId", false);
			if (scriptId == null) {
				String json = generateErrorResponse(500, "Expected either a 'tenantId' parameter or a 'scriptId' parameter.");
				request.setAttribute("json", json);
				return new ModelAndView(DEFAULT_VIEW);
			}
			return handleGetScript(request, scriptId);
		} 
		return handleGetScripts(request, tenantId);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView handlePost(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long tenantId = getParameterAsLong(request, "tenantId", true);
		if (tenantId == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}

		String name = getParameter(request, "name", true);
		if (name == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
		Long scriptTypeId = getParameterAsLong(request, "scriptType", true);
		if (scriptTypeId == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
		String [] requires = request.getParameterValues("requires");
		
		UIScriptForm.Builder builder = UIScriptForm.create()
				.withTenantId(tenantId)
				.withName(name)
				.withScriptTypeId(scriptTypeId);
		if (requires != null) {
			for (String requiresFlag : requires) {
				builder.withRequiresFlag(requiresFlag);
			}
		}
		UIScriptForm script = builder.build();

		String json = null;
		try {
			service.createScript(script);
			json = generateNormalResponse();
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);

	}

    @RequestMapping(method = RequestMethod.DELETE)
 	public ModelAndView handleDelete(HttpServletRequest request,
 			HttpServletResponse response) throws JSONException {
     	//CORS header
     	response.setHeader("Access-Control-Allow-Origin", "*");
 		
 		Long scriptId = getParameterAsLong(request, "scriptId", true);
 		if (scriptId == null) {
 			return new ModelAndView(DEFAULT_VIEW);
 		}

 		String json = null;
 		try {
 			service.deleteScript(scriptId);
 			json = generateNormalResponse();
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			json = generateErrorResponse(500, ex.getMessage());
 		}
 		request.setAttribute("json", json);
 		return new ModelAndView(DEFAULT_VIEW);
 	}
    
    private ModelAndView handleGetScript(HttpServletRequest request,
			long scriptId) throws JSONException {
		String json = null;
		try {
			UIScript script = service.getScript(scriptId);
			if (script == null) {
				throw new ValidationException("The specified script could not be found.");
			}
			json = generateGetResponse(script);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);
	}

	private ModelAndView handleGetScripts(HttpServletRequest request,
			long tenantId) throws JSONException {
		String json = null;
		try {
			List<UIScriptProxy> scripts = service.getScripts(tenantId);
			json = generateGetAllResponse(scripts);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);
	}

}

package com.compuware.ruxit.synthetic.api.crud;

import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetAllResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateNormalResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameter;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsBoolean;
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
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinitionForm;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinitionProxy;

@Controller
@RequestMapping("/test-definition")
public class TestDefinitionController {
	private static final String DEFAULT_VIEW = "json";
	
	@Autowired
	private TestDefinitionService service;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		Long scriptId = getParameterAsLong(request, "scriptId", false);
		if (scriptId == null) {
			Long testDefinitionId = getParameterAsLong(request, "testDefinitionId", false);
			if (testDefinitionId == null) {
				Long tenantId = getParameterAsLong(request, "tenantId", false);
				if (tenantId == null) {
					String json = generateErrorResponse(500, "Expected either a 'scriptId' parameter, a 'tenantId' parameter,  or a 'testDefinitionId' parameter.");
					request.setAttribute("json", json);
					return new ModelAndView(DEFAULT_VIEW);
				}
				return handleGetTestDefinitionsByTenantId(request, tenantId);
			}
			Boolean suspended = getParameterAsBoolean(request, "suspended", false);
			if (suspended != null) {
				return handleSuspendTestDefinition(request, testDefinitionId, suspended);
			}

			return handleGetTestDefinition(request, testDefinitionId);
		} 
		return handleGetTestDefinitions(request, scriptId);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView handlePost(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		Long scriptId = getParameterAsLong(request, "scriptId", true);
		if (scriptId == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
		Long tenantId = getParameterAsLong(request, "tenantId", true);
		if (tenantId == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}

		String name = getParameter(request, "name", true);
		if (name == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
		String executionSchedule = getParameter(request, "executionSchedule", true);
		if (executionSchedule == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
        Long execScheduleId = null;
        Long testDefinitionId = getParameterAsLong(request, "testDefinitionId", false);
        if (testDefinitionId != null) {
        	execScheduleId = getParameterAsLong(request, "execScheduleId", true);
    		if (execScheduleId == null) {
    			return new ModelAndView(DEFAULT_VIEW);
    		}
        }

		String [] requiresFlags = request.getParameterValues("requires");
		
		String [] lcps = request.getParameterValues("lcpId");
        if (lcps == null) {
        	String json = generateErrorResponse(500, "Expected at least one value for the 'lcp' parameter.");
        	request.setAttribute("json", json);
        	return new ModelAndView(DEFAULT_VIEW);
        }
		
		UITestDefinitionForm.Builder builder = UITestDefinitionForm.create()
				.withScriptId(scriptId)
				.withTenantId(tenantId)
				.withName(name)
				.withExecutionSchedule(executionSchedule);
		
		if (testDefinitionId != null) {
			builder.withTestDefinitionId(testDefinitionId);
		}
		
		if (execScheduleId != null) {
			builder.withExecutionScheduleId(execScheduleId);
		}
		
		if (requiresFlags != null) {
			for (String requiresFlag : requiresFlags) {
				builder.withRequiresFlag(requiresFlag);
			}
		}

		if (lcps != null) {
			for (String lcp : lcps) {
				builder.withLcp(lcp);
			}
		}
		
		UITestDefinitionForm testDefinition = builder.build();

		String json = null;
		try {
			service.createTestDefinition(testDefinition);
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
 		
 		Long testDefinitionId = getParameterAsLong(request, "testDefinitionId", true);
 		if (testDefinitionId == null) {
 			return new ModelAndView(DEFAULT_VIEW);
 		}

 		String json = null;
 		try {
 			service.deleteTestDefinition(testDefinitionId);
 			json = generateNormalResponse();
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			json = generateErrorResponse(500, ex.getMessage());
 		}
 		request.setAttribute("json", json);
 		return new ModelAndView(DEFAULT_VIEW);
 	}
    
    private ModelAndView handleGetTestDefinition(HttpServletRequest request,
			long testDefinitionId) throws JSONException {
    	
		String json = null;
		try {
			UITestDefinition testDef = service.getTestDefinition(testDefinitionId);
			if (testDef == null) {
				throw new ValidationException("The specified test definition could not be found.");
			}
			json = generateGetResponse(testDef);
		} catch (Exception ex) {
			ex.printStackTrace();
			
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		
		return new ModelAndView(DEFAULT_VIEW);
	}

    private ModelAndView handleSuspendTestDefinition(HttpServletRequest request,
			long testDefinitionId, boolean suspended) throws JSONException {
 		String json = null;
		try {
			service.suspendTestDefinition(testDefinitionId, suspended);
			json = generateNormalResponse();
		} catch (Exception ex) {
			ex.printStackTrace();
			
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);
	}
    
	private ModelAndView handleGetTestDefinitions(HttpServletRequest request,
			long scriptId) throws JSONException {
		String json = null;
		try {
			List<UITestDefinitionProxy> scripts = service.getTestDefinitions(scriptId);
			json = generateGetAllResponse(scripts);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);
	}

	private ModelAndView handleGetTestDefinitionsByTenantId(HttpServletRequest request,
			long tenantId) throws JSONException {
		String json = null;
		try {
			List<UITestDefinitionProxy> scripts = service.getTestDefinitionsByTenantId(tenantId);
			json = generateGetAllResponse(scripts);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);
	}
}

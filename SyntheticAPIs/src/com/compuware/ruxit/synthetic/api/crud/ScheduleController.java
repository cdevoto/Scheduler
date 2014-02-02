package com.compuware.ruxit.synthetic.api.crud;


import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetAllResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateGetResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateNormalResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameter;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsBoolean;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.getParameterAsInt;
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
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UISchedule;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScheduleForm;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScheduleProxy;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
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
			Long scheduleId = getParameterAsLong(request, "scheduleId", false);
			if (scheduleId == null) {
				String json = generateErrorResponse(500, "Expected either a  'tenantId' parameter, or a 'scheduleId' parameter.");
				request.setAttribute("json", json);
				return new ModelAndView(DEFAULT_VIEW);
				
			} 
			return handleGetSchedule(request, scheduleId);
		}
		
		Boolean isMaintenance = getParameterAsBoolean(request, "isMaintenance", false);
		if (isMaintenance == null) {
			isMaintenance = false;
		}
		
		return handleGetSchedules(request, tenantId, isMaintenance);

	}

    @RequestMapping(method = RequestMethod.DELETE)
  	public ModelAndView handleDelete(HttpServletRequest request,
  			HttpServletResponse response) throws JSONException {
      	//CORS header
      	response.setHeader("Access-Control-Allow-Origin", "*");
  		
  		Long scheduleId = getParameterAsLong(request, "scheduleId", true);
  		if (scheduleId == null) {
  			return new ModelAndView(DEFAULT_VIEW);
  		}

  		String json = null;
  		try {
  			service.deleteSchedule(scheduleId);
  			json = generateNormalResponse();
  		} catch (Exception ex) {
  			ex.printStackTrace();
  			json = generateErrorResponse(500, ex.getMessage());
  		}
  		request.setAttribute("json", json);
  		return new ModelAndView(DEFAULT_VIEW);
  	}
     
     private ModelAndView handleGetSchedule(HttpServletRequest request,
 			long scheduleId) throws JSONException {
     	
 		String json = null;
 		try {
 			UISchedule schedule = service.getSchedule(scheduleId);
 			if (schedule == null) {
 				throw new ValidationException("The specified schedule could not be found.");
 			}
 			json = generateGetResponse(schedule);
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			
 			json = generateErrorResponse(500, ex.getMessage());
 		}
 		request.setAttribute("json", json);
 		
 		return new ModelAndView(DEFAULT_VIEW);
 	}

 	private ModelAndView handleGetSchedules(HttpServletRequest request,
 			long tenantId, boolean isMaintenance) throws JSONException {
 		String json = null;
 		try {
 			List<UIScheduleProxy> schedules = service.getSchedules(tenantId, isMaintenance);
 			json = generateGetAllResponse(schedules);
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			json = generateErrorResponse(500, ex.getMessage());
 		}
 		request.setAttribute("json", json);
 		return new ModelAndView(DEFAULT_VIEW);
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
		
		String rrule = getParameter(request, "rrule", true);
		if (rrule == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
		Long timezone = getParameterAsLong(request, "timezone", true);
		if (timezone == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
		Integer duration = getParameterAsInt(request, "duration", true);
		if (duration == null) {
			return new ModelAndView(DEFAULT_VIEW);
		}
		
		Long scheduleId = getParameterAsLong(request, "scheduleId", false);


        String [] testDefs = request.getParameterValues("testDef");
		
		UIScheduleForm.Builder builder = UIScheduleForm.create()
				.withTenantId(tenantId)
				.withName(name)
				.withTimezoneId(timezone)
				.withRecurrenceRule(rrule)
				.withDuration(duration)
				.withMaintenance(true);
		
		if (scheduleId != null) {
			builder.withScheduleId(scheduleId);
		}
		
		if (testDefs != null) {
		    builder.withTestDefinitionStrings(testDefs);
		}		
		
		UIScheduleForm schedule = builder.build();

		String json = null;
		try {
			if (scheduleId == null) {
			    service.createSchedule(schedule);
			} else {
			    service.updateSchedule(schedule);
			}
			json = generateNormalResponse();
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		
		return new ModelAndView(DEFAULT_VIEW);

	}



}

package com.compuware.ruxit.synthetic.api.crud;


import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateErrorResponse;
import static com.compuware.ruxit.synthetic.scheduler.core.util.web.WebUtil.generateNormalResponse;

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
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScriptType;
import com.compuware.ruxit.synthetic.scheduler.core.util.JsonDataGenerator;

@Controller
@RequestMapping("/script-type")
public class ScriptTypeController {
	private static final String DEFAULT_VIEW = "json";
	
	@Autowired
	private TestDefinitionService service;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
    	//CORS header
    	response.setHeader("Access-Control-Allow-Origin", "*");
		
		String json = null;
		try {
			List<UIScriptType> scriptTypes = service.getScriptTypes();
			json = generateGetResponse(scriptTypes);
		} catch (Exception ex) {
			ex.printStackTrace();
			json = generateErrorResponse(500, ex.getMessage());
		}
		request.setAttribute("json", json);
		return new ModelAndView(DEFAULT_VIEW);

	}

	private String generateGetResponse(final List<UIScriptType> scriptTypes)
			throws JSONException {
		String json = generateNormalResponse(new JsonDataGenerator<JSONArray> () {

			@Override
			public JSONArray generate() throws JSONException {
				JSONArray data = new JSONArray();
				for (UIScriptType scriptType : scriptTypes) {
					data.put(scriptType.toJsonObject());
				}
				return data;
			}
		});
		return json.toString();
	}


}

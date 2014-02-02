package com.compuware.ruxit.synthetic.test.service;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScriptProxy;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIVUController;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class TestTestDefinitionService {
	private static ApplicationContext context;
	private TestDefinitionService service;

	@BeforeClass
	public static void beforeClass() {
		context = AppContextService.getApplicationContext();
	}
	
	@Before
	public void before() {
		service = context.getBean(TestDefinitionService.class);
	}

	@Test
	public void testGetScripts() {
		List<UIScriptProxy> uiScripts = service.getScripts(1L);
		assertThat(uiScripts, notNullValue());
		assertThat(uiScripts.size(), greaterThan(1));
		for (UIScriptProxy uiScript : uiScripts) {
			assertThat(uiScript.getId(), greaterThan(0L));
			assertThat(uiScript.getName(), notNullValue());
			assertThat(uiScript.getLastModified(), greaterThan(1389912336000L));
		}
	}

	@Test
	public void testGetTestDefinition() throws JSONException {
		UITestDefinition uiTestDef = service.getTestDefinition(1L);
		assertThat(uiTestDef, notNullValue());
		System.out.println(uiTestDef);
		System.out.println(uiTestDef.toJsonObject().toString());
	}
	
	@Test
	public void testGetVUController() throws JSONException {
		UIVUController vuc = service.getVUController(49);
		System.out.println(vuc);
		System.out.println(vuc.toJsonObject().toString());
	}
}

package com.compuware.ruxit.synthetic.test.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinitionForm;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class GenerateTestDefinitions {
	
	public static void main(String[] args) {
		ApplicationContext context = AppContextService.getApplicationContext();
		TestDefinitionService service = context.getBean(TestDefinitionService.class);
		int numTestDefs = 1000;
		
		List<Long> ids = new ArrayList<>();
		for (int i = 0; i < numTestDefs; i++) {
			UITestDefinitionForm.Builder builder = UITestDefinitionForm.create();
			builder.withName("Test Definition " + (i + 1))
			       .withScriptId(2)  
			       .withTenantId(1)
			       .withRequiresFlag("IPv4")
	               .withExecutionSchedule("*/5 * * * *")
	               .withLcp(11) // New York City:AT&T:Chrome
	               .withLcp(31) // Los Angeles:AT&T:Chrome
	               .withLcp(46) // Chicago:Verizon:Chrome
	               .withLcp(51) // Chicago:AT&T:Chrome
	               .withLcp(67);// Detroit:Level 3:Chrome
			UITestDefinitionForm testDef = builder.build();
			long id = service.createTestDefinition(testDef);
			ids.add(id);
		}
		
		/*
		  VUC's that can consume these test definitions are as follows:
		  
		  LCP 11: 43           [41, 42 do not support IPv4]
		  LCP 31: 59, 61, 62   [60 does not support IPv4]
		  LCP 46: 67           [68, 69 do not support IPv4]
		  LCP 51: 47, 48       [49 does not support IPv4] 
		  LCP 67: 32
		 */
	}

}

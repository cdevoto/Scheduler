package com.compuware.ruxit.synthetic.test.data.generator;

import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinitionForm;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class UpdateTestDef {
	
	public static void main(String[] args) {
		ApplicationContext context = AppContextService.getApplicationContext();
		TestDefinitionService service = context.getBean(TestDefinitionService.class);
		
		UITestDefinitionForm.Builder builder = UITestDefinitionForm.create();
		builder.withName("Test Definition X")
		       .withTestDefinitionId(2L)
		       .withScriptId(1)  // Check on what sorts of limitations the script has and confirm that the Lcps
		       .withTenantId(1)
		       .withRequiresFlag("IPv4")
		       .withRequiresFlag("IPv6")
               .withExecutionSchedule("*/10 * * * *")
               .withLcp(11)
               .withLcp(46)
               .withLcp(51);
		UITestDefinitionForm testDef = builder.build();
		service.createTestDefinition(testDef);

	}

}

package com.compuware.ruxit.synthetic.test.data.generator;

import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class DeleteTestDef {
	
	public static void main(String[] args) {
		ApplicationContext context = AppContextService.getApplicationContext();
		TestDefinitionService service = context.getBean(TestDefinitionService.class);
		
		service.deleteTestDefinition(2L);

	}

}

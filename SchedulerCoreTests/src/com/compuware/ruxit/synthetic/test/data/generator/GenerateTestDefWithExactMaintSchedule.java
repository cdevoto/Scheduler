package com.compuware.ruxit.synthetic.test.data.generator;

import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScheduleForm;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinitionForm;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class GenerateTestDefWithExactMaintSchedule {
	
	public static void main(String[] args) {
		ApplicationContext context = AppContextService.getApplicationContext();
		TestDefinitionService service = context.getBean(TestDefinitionService.class);
		
		UITestDefinitionForm.Builder builder = UITestDefinitionForm.create();
		builder.withName("Test Definition X")
		       .withScriptId(1)  // Check on what sorts of limitations the script has and confirm that the Lcps
		       .withTenantId(1)
		       .withRequiresFlag("IPv4")
               .withExecutionSchedule("*/5 * * * *")
               .withLcp(11)
               .withLcp(31)
               .withLcp(46)
               .withLcp(51);
		UITestDefinitionForm testDef = builder.build();
		long id1 = service.createTestDefinition(testDef);

		System.out.printf("Generated a test definition with id %d.%n", id1);

		
		UIScheduleForm schedule = UIScheduleForm.create()
				.withTenantId(1)
				.withTimezoneId(7)
				.withName("Custom Maintenance Schedule 1")
				.withRecurrenceRule("15 11 31 1 * 2014")
				.withDuration(60)
				.withTestDefinition(id1)
				.withMaintenance(true)
				.build();
		long id3 = service.createSchedule(schedule);

		System.out.printf("Generated a schedule with id %d.%n", id3);
		
		
		
	}

}

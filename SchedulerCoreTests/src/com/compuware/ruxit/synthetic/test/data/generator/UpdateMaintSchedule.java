package com.compuware.ruxit.synthetic.test.data.generator;

import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScheduleForm;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class UpdateMaintSchedule {
	
	public static void main(String[] args) {
		ApplicationContext context = AppContextService.getApplicationContext();
		TestDefinitionService service = context.getBean(TestDefinitionService.class);
		
		UIScheduleForm schedule = UIScheduleForm.create()
				.withScheduleId(6L)
				.withTenantId(1)
				.withTimezoneId(7)
				.withName("Custom Maintenance Schedule 1")
				.withRecurrenceRule("0 21 * * *")
				.withDuration(50)
				.withTestDefinition(2L)
				.withMaintenance(true)
				.build();
		service.updateSchedule(schedule);

	}

}

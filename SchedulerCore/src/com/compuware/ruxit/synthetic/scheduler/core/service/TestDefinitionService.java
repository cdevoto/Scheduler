package com.compuware.ruxit.synthetic.scheduler.core.service;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIAbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UILcpProxy;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UISchedule;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScheduleForm;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScheduleProxy;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScript;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScriptForm;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScriptProxy;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIScriptType;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinitionForm;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestDefinitionProxy;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITestView;
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UITimeZone;

public interface TestDefinitionService extends VUControllerService {
	
    public UIScript getScript(long scriptId); 
	public List<UIScriptProxy> getScripts (long tenantId);
	public void createScript(UIScriptForm script);
	public void deleteScript(long scriptId);
	
	public UITestDefinition getTestDefinition(long testDefinitionId);
	public List<UITestDefinitionProxy> getTestDefinitionsByTenantId (long tenantId);
	public List<UITestDefinitionProxy> getTestDefinitions (long scriptId);
	public void deleteTestDefinition(long testDefinitionId);
	public long createTestDefinition(UITestDefinitionForm testDefinition);
	public void suspendTestDefinition(long testDefinitionId, boolean suspended);

	public List<UIScriptType> getScriptTypes ();
	public List<UIAbilityFlag> getAbilityFlags(long level);
	public List<UITimeZone> getTimeZones ();
	
	public List<UILcpProxy> getLcps(long scriptId, List<String> requiresFlags);

	public long createSchedule(UIScheduleForm schedule);
	public void updateSchedule(UIScheduleForm schedule);
	public void deleteSchedule(long scheduleId);
	
	public List<UITestView> poll(long vucId, long supportsF, int maxRows);	
	
	public void createInstantTests(long testDefId);
	
	public UISchedule getSchedule (long scheduleId);
	public List<UIScheduleProxy> getSchedules(long tenantId);
	public List<UIScheduleProxy> getSchedules(long tenantId, boolean isMaintenance);
	
	
	
}

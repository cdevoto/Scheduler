package com.compuware.ruxit.synthetic.scheduler.core.service.local;

import static com.compuware.ruxit.synthetic.scheduler.core.service.util.AbilityFlagUtil.*;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compuware.ruxit.synthetic.scheduler.core.dao.AbilityFlagDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.LcpDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScheduleDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptTypeDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestDefinitionDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestPlanDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestQueueDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TimeZoneDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlagLevel;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Schedule;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScheduleProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Script;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptType;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinitionProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlan;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TimeZone;
import com.compuware.ruxit.synthetic.scheduler.core.service.TestDefinitionService;
import com.compuware.ruxit.synthetic.scheduler.core.service.VUControllerService;
import com.compuware.ruxit.synthetic.scheduler.core.service.planner.TestPlanner;
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
import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIVUController;

@Component
public class LocalTestDefinitionService implements TestDefinitionService {
	@Autowired
	private ScriptDao scriptDao;
	@Autowired
	private ScriptTypeDao scriptTypeDao;
	@Autowired
	private AbilityFlagDao abilityFlagDao;
	@Autowired
	private TestDefinitionDao testDefinitionDao;
	@Autowired
	private TimeZoneDao timeZoneDao;
	@Autowired
	private LcpDao lcpDao;
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private TestQueueDao testQueueDao;
	@Autowired
	private TestPlanDao testPlanDao;
	@Autowired
	private TestPlanner planner;
	@Autowired
	private VUControllerService vucService;
	
	@Override
	public UIScript getScript(long scriptId) {
		Script daoScript = scriptDao.getById(scriptId);
		if (daoScript == null) {
			return null;
		}
		UIScriptType uiScriptType = getUIScriptType(daoScript.getScriptType());
		List<UIAbilityFlag> uiRequiresFlags = decodeAbilityFlags(abilityFlagDao, daoScript.getRequiresFlags());
		UIScript uiScript = UIScript.create()
				.withScript(daoScript)
				.withScriptType(uiScriptType)
				.withRequiresFlags(uiRequiresFlags)
				.build();
		return uiScript;
	}

	@Override
	public List<UIScriptProxy> getScripts(long tenantId) {
	    List<UIScriptProxy> uiScripts = new LinkedList<>();
	    List<ScriptProxy> daoScripts = scriptDao.getAll(tenantId);
	    for (ScriptProxy daoScript : daoScripts) {
	    	UIScriptProxy uiScript = UIScriptProxy.create()
	    			.withScriptProxy(daoScript)
	    			.build();
	    	uiScripts.add(uiScript);
	    }
	    return uiScripts;
	}

	@Override
	public void deleteScript(long scriptId) {
		scriptDao.deleteById(scriptId);
	}

	@Override
	public List<UIScriptType> getScriptTypes() {
	    List<UIScriptType> uiScriptTypes = new LinkedList<>();
	    List<ScriptType> daoScriptTypes = scriptTypeDao.getAll();
	    for (ScriptType daoScriptType : daoScriptTypes) {
	    	UIScriptType uiScriptType = getUIScriptType(daoScriptType);
	    	uiScriptTypes.add(uiScriptType);
	    }
	    return uiScriptTypes;
	}

	@Override
	public List<UIAbilityFlag> getAbilityFlags(long level) {
	    List<UIAbilityFlag> uiAbilityFlags = new LinkedList<>();
	    AbilityFlagLevel flagLevel = AbilityFlagLevel.get(level);
	    List<AbilityFlag> daoAbilityFlags = abilityFlagDao.getByLevel(flagLevel);
	    for (AbilityFlag daoAbilityFlag : daoAbilityFlags) {
	    	UIAbilityFlag uiAbilityFlag = UIAbilityFlag.create()
	    			.withAbilityFlag(daoAbilityFlag)
	    			.build();
	    	uiAbilityFlags.add(uiAbilityFlag);
	    }
	    return uiAbilityFlags;
	}

	@Override
	public void createScript(UIScriptForm uiScript) {
		Script.Builder builder = Script.create()
				.withTenantId(uiScript.getTenantId())
				.withName(uiScript.getName());
		ScriptType scriptType = scriptTypeDao.getById(uiScript.getScriptTypeId());
		if (scriptType == null) {
			throw new IllegalArgumentException(String.format("The specified script type (%d) does not exist", uiScript.getScriptTypeId()));
		}
		builder.withScriptType(scriptType);
		List<String> requiresFlags = uiScript.getRequiresFlags();
		long requiresF = encodeAbilityFlags(abilityFlagDao, requiresFlags);
		builder.withRequiresFlags(requiresF);
		Script daoScript = builder.build();
		scriptDao.insert(daoScript);
	}

	@Override
	public List<UITestDefinitionProxy> getTestDefinitions(long scriptId) {
	    List<UITestDefinitionProxy> uiTestDefinitions = new LinkedList<>();
	    List<TestDefinitionProxy> daoTestDefinitions = testDefinitionDao.getAll(scriptId);
	    for (TestDefinitionProxy daoTestDefinition : daoTestDefinitions) {
	    	UITestDefinitionProxy uiTestDefinition = UITestDefinitionProxy.create()
	    			.withTestDefinitionProxy(daoTestDefinition)
	    			.build();
	    	uiTestDefinitions.add(uiTestDefinition);
	    }
	    return uiTestDefinitions;
	}

	@Override
	public void deleteTestDefinition(long testDefinitionId) {
		testDefinitionDao.deleteById(testDefinitionId);
	}

	@Override
	public List<UITimeZone> getTimeZones() {
	    List<UITimeZone> uiTimeZones = new LinkedList<>();
	    List<TimeZone> daoTimeZones = timeZoneDao.getAll();
	    for (TimeZone daoTimeZone : daoTimeZones) {
	    	UITimeZone uiTimeZone = UITimeZone.create()
	    			.withTimeZone(daoTimeZone)
	    			.build();
	    	uiTimeZones.add(uiTimeZone);
	    }
	    return uiTimeZones;
	}

	@Override
	public List<UILcpProxy> getLcps(long scriptId, List<String> requiresFlags) {
		long requiresF = encodeAbilityFlags(abilityFlagDao, requiresFlags);
		List<LcpProxy> daoProxies = lcpDao.getLcps(scriptId, requiresF);
		List<UILcpProxy> uiProxies = new LinkedList<>(); 
		for (LcpProxy daoProxy : daoProxies) {
			UILcpProxy uiProxy = UILcpProxy.create()
					.withLcpProxy(daoProxy)
					.build();
			uiProxies.add(uiProxy);
		}
		return uiProxies;
	}


	private UIScriptType getUIScriptType(ScriptType daoScriptType) {
		List<AbilityFlag> abilityFlags = abilityFlagDao.getByScriptType(daoScriptType.getId());
		UIScriptType.Builder builder = UIScriptType.create();
		builder.withScriptType(daoScriptType);
		for (AbilityFlag abilityFlag : abilityFlags) {
			builder.withAbilityFlag(UIAbilityFlag.create()
					   .withAbilityFlag(abilityFlag)
					   .build());
		}
		UIScriptType uiScriptType = builder.build();
		return uiScriptType;
	}

	@Override
	public long createTestDefinition(UITestDefinitionForm uiTestDefinition) {
		boolean isInsert = uiTestDefinition.getTestDefinitionId() == null;
		TestDefinition.Builder builder = TestDefinition.create()
				.withScriptId(uiTestDefinition.getScriptId())
				.withTenantId(uiTestDefinition.getTenantId())
				.withName(uiTestDefinition.getName());
		
		Long testDefinitionId = uiTestDefinition.getTestDefinitionId();
		if (testDefinitionId != null) {
			builder.withTestDefinitionId(testDefinitionId);
		}
		
		long requiresF = encodeAbilityFlags(abilityFlagDao, uiTestDefinition.getRequiresFlags());
		builder.withRequiresF(requiresF);
		
		for (Long lcpId : uiTestDefinition.getLcps()) {
			LcpProxy lcpProxy = lcpDao.getById(lcpId);
			if (lcpProxy == null) {
				throw new IllegalArgumentException(String.format("The specified lcp '%d' does not exist", lcpId));
			}
			builder.withLcp(lcpProxy);
		}
		
		Schedule.Builder scheduleBuilder = Schedule.create();
		if (!isInsert) {
			scheduleBuilder.withScheduleId(uiTestDefinition.getExecutionScheduleId());
		}
		
		Schedule execSchedule = scheduleBuilder
				.withTenantId(uiTestDefinition.getTenantId())
				.withName(uiTestDefinition.getName() + " - Exec")
				.withRecurrenceRule(uiTestDefinition.getExecutionSchedule())
				.withMaintenance(false)
				.build();
		builder.withExecutionSchedule(execSchedule);
		
		TestDefinition testDef = builder.build();
		
		List<TestPlan> testPlans = planner.generateTestPlans(testDef);
		
		Long id = null;
		if (isInsert) {
		    id = testDefinitionDao.insert(testDef, testPlans);
		} else {
			id = testDef.getTestDefinitionId();
		    testDefinitionDao.update(testDef, testPlans);
		}
		return id;
	}

	@Override
	public void suspendTestDefinition(long testDefinitionId, boolean suspended) {
		testDefinitionDao.suspend(testDefinitionId, suspended);
	}

	@Override
	public UITestDefinition getTestDefinition(long testDefinitionId) {
		TestDefinition testDef = testDefinitionDao.getById(testDefinitionId);
		UITestDefinition.Builder builder = UITestDefinition.create()
				.withTestDefinition(testDef);
		
		Schedule execSchedule = testDef.getExecutionSchedule();
		UISchedule uiExecSchedule = UISchedule.create()
				.withSchedule(execSchedule)
				.build();
		builder.withExecSchedule(uiExecSchedule);
		
		List<AbilityFlag> requiresFlags = abilityFlagDao.getByBitMapField(testDef.getRequiresF());
		for (AbilityFlag requiresFlag : requiresFlags) {
			UIAbilityFlag uiRequiresFlag = UIAbilityFlag.create()
					.withAbilityFlag(requiresFlag)
					.build();
			builder.withRequiresFlag(uiRequiresFlag);
		}
		
		List<LcpProxy> lcps = lcpDao.getByTestDefId(testDefinitionId);
		for (LcpProxy lcp : lcps) {
			UILcpProxy uiLcp = UILcpProxy.create()
					.withLcpProxy(lcp)
					.build();
			builder.withLcp(uiLcp);
		}
		UITestDefinition uiTestDef = builder.build();
		return uiTestDef;
	}

	@Override
	public long createSchedule(UIScheduleForm uiSchedule) {
		List<Long> testDefIds = uiSchedule.getTestDefinitions();
		TimeZone timeZone = timeZoneDao.getById(uiSchedule.getTimezoneId());
		Schedule schedule = Schedule.create()
				.withTenantId(uiSchedule.getTenantId())
				.withTimeZone(timeZone)
				.withName(uiSchedule.getName())
				.withRecurrenceRule(uiSchedule.getRecurrenceRule())
				.withDuration(uiSchedule.getDuration())
				.withMaintenance(uiSchedule.isMaintenance())
				.build();
		long id = scheduleDao.insert(schedule, testDefIds);
		return id;
	}

	@Override
	public void updateSchedule(UIScheduleForm uiSchedule) {
		List<Long> testDefIds = uiSchedule.getTestDefinitions();
		TimeZone timeZone = timeZoneDao.getById(uiSchedule.getTimezoneId());
		Schedule schedule = Schedule.create()
				.withScheduleId(uiSchedule.getScheduleId())
				.withTenantId(uiSchedule.getTenantId())
				.withTimeZone(timeZone)
				.withName(uiSchedule.getName())
				.withRecurrenceRule(uiSchedule.getRecurrenceRule())
				.withDuration(uiSchedule.getDuration())
				.withMaintenance(uiSchedule.isMaintenance())
				.build();
		scheduleDao.update(schedule, testDefIds);
	}

	@Override
	public void deleteSchedule(long scheduleId) {
		scheduleDao.deleteById(scheduleId);
	}

	@Override
	public List<UITestView> poll(long vucId, long supportsF, int maxRows) {
		List<TestView> daoTests = testQueueDao.poll(vucId, supportsF, maxRows);
		List<UITestView> uiTests = new LinkedList<>();
		for (TestView daoTest : daoTests) {
			List<UIAbilityFlag> uiRequiresFlags = decodeAbilityFlags(abilityFlagDao, daoTest.getRequiresF());
			UITestView uiTest = UITestView.create()
					.withTest(daoTest)
					.withRequiresFlags(uiRequiresFlags)
					.build();
			uiTests.add(uiTest);
		}
		return uiTests;
	}

	@Override
	public UIVUController getVUController(long vucId) {
		return vucService.getVUController(vucId);
	}

	@Override
	public void createInstantTests(long testDefId) {
		List<TestPlanView> testPlans = testPlanDao.getByTestDefId(testDefId);
		if (testPlans.isEmpty()) {
			throw new IllegalArgumentException(String.format("A test definition with id %d could not be found.", testDefId));
		}
		for (TestPlanView testPlan : testPlans) {
			Test test = Test.create()
					.withTestDefinitionId(testPlan.getTestDefinitionId())
					.withScriptId(testPlan.getScriptId())
					.withTenantId(testPlan.getTenantId())
					.withLcpId(testPlan.getLcpId())
					.withPriority(Test.Priority.HIGH)
					.withRequiresF(testPlan.getRequiresF())
					.withStatus(Test.Status.ENQUEUED)
					.build();
            testQueueDao.push(test);
		}
	}

	@Override
	public UISchedule getSchedule(long scheduleId) {
		Schedule daoSchedule = scheduleDao.getById(scheduleId);
		UITimeZone uiTimeZone = UITimeZone.create()
				.withTimeZone(daoSchedule.getTimeZone())
				.build();
		List<TestDefinitionProxy> daoTestDefs = testDefinitionDao.getByScheduleId(scheduleId);
		List<UITestDefinitionProxy> uiTestDefs = new LinkedList<>();
		for (TestDefinitionProxy daoTestDef : daoTestDefs) {
			UITestDefinitionProxy uiTestDef = UITestDefinitionProxy.create()
					.withTestDefinitionProxy(daoTestDef)
					.build();
			uiTestDefs.add(uiTestDef);
		}
		UISchedule uiSchedule = UISchedule.create()
				.withTimeZone(uiTimeZone)
				.withTestDefintions(uiTestDefs)
				.withSchedule(daoSchedule)
				.build();
		return uiSchedule;
	}

	@Override
	public List<UIScheduleProxy> getSchedules(long tenantId) {
		// TODO Auto-generated method stub
		return doGetSchedules(tenantId, null);
	}

	@Override
	public List<UIScheduleProxy> getSchedules(long tenantId,
			boolean isMaintenance) {
		return doGetSchedules(tenantId, isMaintenance);
	}

	private List<UIScheduleProxy> doGetSchedules(long tenantId, Boolean isMaintenance) {
	    List<UIScheduleProxy> uiScripts = new LinkedList<>();
	    List<ScheduleProxy> daoScripts = null;
	    if (isMaintenance == null) {
	        daoScripts = scheduleDao.getAll(tenantId);
	    } else {
	    	daoScripts = scheduleDao.getAll(tenantId, isMaintenance);
	    }
	    for (ScheduleProxy daoScript : daoScripts) {
	    	UIScheduleProxy uiScript = UIScheduleProxy.create()
	    			.withScheduleProxy(daoScript)
	    			.build();
	    	uiScripts.add(uiScript);
	    }
	    return uiScripts;
	}

	@Override
	public List<UITestDefinitionProxy> getTestDefinitionsByTenantId(
			long tenantId) {
	    List<UITestDefinitionProxy> uiTestDefinitions = new LinkedList<>();
	    List<TestDefinitionProxy> daoTestDefinitions = testDefinitionDao.getByTenantId(tenantId);
	    for (TestDefinitionProxy daoTestDefinition : daoTestDefinitions) {
	    	UITestDefinitionProxy uiTestDefinition = UITestDefinitionProxy.create()
	    			.withTestDefinitionProxy(daoTestDefinition)
	    			.build();
	    	uiTestDefinitions.add(uiTestDefinition);
	    }
	    return uiTestDefinitions;
	}

	@Override
	public List<UIVUController> getVUControllers() {
		return vucService.getVUControllers();
	}

}

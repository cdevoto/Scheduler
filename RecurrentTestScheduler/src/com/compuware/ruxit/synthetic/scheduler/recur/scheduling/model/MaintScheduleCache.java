package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;

@Component
public class MaintScheduleCache {
	private Map<Long, MaintScheduleView> maintSchedulesById = new ConcurrentHashMap<>();
	private Map<Long, Map<Long, MaintScheduleView>> maintSchedulesByTestDefId = new ConcurrentHashMap<>();
	private Map<Long, TestDefinition> testDefsById = new ConcurrentHashMap<>();
	
	
	public synchronized void add (MaintScheduleView schedule) {
		this.maintSchedulesById.put(schedule.getScheduleId(), schedule);
		for (long testDefId : schedule.getTestDefinitionIds()) {
			Map<Long, MaintScheduleView> map = maintSchedulesByTestDefId.get(testDefId);
			if (map == null) {
				map = new ConcurrentHashMap<>();
				maintSchedulesByTestDefId.put(testDefId, map);
			}
			map.put(schedule.getScheduleId(), schedule);
			TestDefinition testDef = testDefsById.get(testDefId);
			if (testDef == null) {
				testDef = new TestDefinition(testDefId);
				testDefsById.put(testDefId, testDef);
			}
		}
	}
	
	public synchronized void remove(MaintScheduleView schedule) {
		this.maintSchedulesById.remove(schedule.getScheduleId());
		for (long testDefId : schedule.getTestDefinitionIds()) {
			Map<Long, MaintScheduleView> map = maintSchedulesByTestDefId.get(testDefId);
			if (map != null) {
				map.remove(schedule.getScheduleId());
				if (map.isEmpty()) {
					maintSchedulesByTestDefId.remove(testDefId);
					testDefsById.remove(testDefId);
				}
			}
		}
	}
	
	public synchronized MaintScheduleView get (long id) {
		return maintSchedulesById.get(id);
	}
	
	public synchronized Set<MaintScheduleView> getByTestDefId (long id) {
		Collection<MaintScheduleView> result = maintSchedulesByTestDefId.get(id).values();
		if (result == null) {
			result = Collections.emptySet();
		}
		return new HashSet<>(result);
	}

	public synchronized TestDefinition getTestDefinitionById (long id) {
		TestDefinition result = testDefsById.get(id);
		return result;
	}

	public synchronized List<MaintScheduleView> getAll () {
		List<MaintScheduleView> result = new ArrayList<>();
		result.addAll(maintSchedulesById.values());
		return result;
	}

	public List<TestDefinition> getTestDefinitions () {
		List<TestDefinition> result = new ArrayList<>();
		result.addAll(testDefsById.values());
		return result;
	}

	@Override
	public String toString() {
		return "\nMaintScheduleCache [\n\tmaintSchedulesById=" + maintSchedulesById
				+ ",\n\tmaintSchedulesByTestDefId=" + maintSchedulesByTestDefId
				+ ",\n\ttestDefsById=" + testDefsById + "\n]";
	}
	
}

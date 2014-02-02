package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;

public class TestDefinition {
	private long id;
	private Map<Long, MaintScheduleView> activeMaintSchedules = new LinkedHashMap<>();
	
	public TestDefinition (long id) {
        this.id = id;		
	}

	public long getId() {
		return id;
	}

	public Set<MaintScheduleView> getActiveMaintSchedules() {
		Collection<MaintScheduleView> values = activeMaintSchedules.values();
		Set<MaintScheduleView> result;
		if (values == null) {
			result = Collections.emptySet();
		} else {
			result = new LinkedHashSet<>(values);
		}
		return result;
	}
	
	public void addActiveMaintSchedule(MaintScheduleView schedule) {
		activeMaintSchedules.put(schedule.getScheduleId(), schedule);
	}
	
	public void removeActiveMaintSchedule(long scheduleId) {
		activeMaintSchedules.remove(scheduleId);
	}
	
	public boolean hasActiveMaintSchedules () {
		return !activeMaintSchedules.isEmpty();
	}

	@Override
	public String toString() {
		return "TestDefinition [id=" + id + ", activeMaintSchedules="
				+ activeMaintSchedules + "]";
	}
}

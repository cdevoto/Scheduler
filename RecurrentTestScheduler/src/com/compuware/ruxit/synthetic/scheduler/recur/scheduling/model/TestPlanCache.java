package com.compuware.ruxit.synthetic.scheduler.recur.scheduling.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;

@Component
public class TestPlanCache {
	private Map<Long, TestPlanView> testPlansById = new ConcurrentHashMap<>();
	private Map<Long, Set<TestPlanView>> testPlansByTestDefId = new ConcurrentHashMap<>();
	
	
	public synchronized void add (TestPlanView testPlan) {
		this.testPlansById.put(testPlan.getId(), testPlan);
		Set<TestPlanView> testPlans = testPlansByTestDefId.get(testPlan.getTestDefinitionId());
		if (testPlans == null) {
			testPlans = new HashSet<>();
			testPlansByTestDefId.put(testPlan.getTestDefinitionId(), testPlans);
		}
		testPlans.add(testPlan);		
	}
	
	public synchronized void remove (TestPlanView testPlan) {
		this.testPlansById.remove(testPlan.getId());
		Set<TestPlanView> testPlans = testPlansByTestDefId.get(testPlan.getTestDefinitionId());
		if (testPlans != null) {
			testPlans.remove(testPlan);
			if (testPlans.isEmpty()) {
				testPlansByTestDefId.remove(testPlan.getTestDefinitionId());
			}
		}
	}

	public synchronized TestPlanView get (long id) {
		return testPlansById.get(id);
	}
	
	public synchronized List<TestPlanView> getByTestDefId (long id) {
		Set<TestPlanView> testPlans = testPlansByTestDefId.get(id);
		List<TestPlanView> result;
		if (testPlans == null) {
			result = Collections.emptyList();
		} else {
			result = new LinkedList<>(testPlans);
		}
		return result;
	}

	public synchronized List<TestPlanView> getAll () {
		List<TestPlanView> result = new ArrayList<>();
		result.addAll(testPlansById.values());
		return result;
	}

	@Override
	public String toString() {
		return "\nTestPlanCache [\n\ttestPlansById=" + testPlansById
				+ ",\n\ttestPlansByTestDefId=" + testPlansByTestDefId + "\n]";
	}

	
}

package com.compuware.ruxit.synthetic.scheduler.core.service.planner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlan;
import com.compuware.ruxit.synthetic.scheduler.core.service.planner.TestPlanner;

@Component
public class StaggeredIntervalTestPlanner implements TestPlanner {
	private static final Rules EVERY_5_MINUTES;
	private static final Rules EVERY_10_MINUTES;
	private static final Rules EVERY_15_MINUTES;
	private static final Rules EVERY_30_MINUTES;
	private static final Rules EVERY_HOUR;
	
	private static final Rules [] ruleList;
	private static final Map<String, Rules> ruleMap = new HashMap<>();

	static {
		EVERY_5_MINUTES = Rules.create("*/5 * * * *")
				.withRules(1, "*/5 * * * *")
				.withRules(2, "*/5 * * * *", "3-59/5 * * * *")
				.withRules(3, "*/5 * * * *", "2-59/5 * * * *", "4-59/5 * * * *")
				.withRules(4, "*/5 * * * *", "2-59/5 * * * *", "3-59/5 * * * *", "4-59/5 * * * *")
				.withRules(5, "*/5 * * * *", "1-59/5 * * * *", "2-59/5 * * * *", "3-59/5 * * * *", "4-59/5 * * * *")
				.build();
				
		EVERY_10_MINUTES = Rules.create("*/10 * * * *")
				.withRules(1, "*/10 * * * *")
				.withRules(2, "*/10 * * * *", "5-59/10 * * * *")
				.withRules(3, "*/10 * * * *", "3-59/10 * * * *", "6-59/10 * * * *")
				.withRules(4, "*/10 * * * *", "2-59/10 * * * *", "5-59/10 * * * *", "8-59/10 * * * *")
				.withRules(5, "*/10 * * * *", "2-59/10 * * * *", "4-59/10 * * * *", "6-59/10 * * * *", "8-59/10 * * * *")
				.build();
				
		EVERY_15_MINUTES = Rules.create("*/15 * * * *")
				.withRules(1, "*/15 * * * *")
				.withRules(2, "*/15 * * * *", "8-59/15 * * * *")
				.withRules(3, "*/15 * * * *", "5-59/15 * * * *", "10-59/15 * * * *")
				.withRules(4, "*/15 * * * *", "4-59/15 * * * *", "8-59/15 * * * *", "12-59/15 * * * *")
				.withRules(5, "*/15 * * * *", "3-59/15 * * * *", "6-59/15 * * * *", "9-59/15 * * * *", "12-59/15 * * * *")
				.build();
		
		EVERY_30_MINUTES = Rules.create("*/30 * * * *")
				.withRules(1, "*/30 * * * *")
				.withRules(2, "*/30 * * * *", "15-59/30 * * * *")
				.withRules(3, "*/30 * * * *", "10-59/30 * * * *", "20-59/30 * * * *")
				.withRules(4, "*/30 * * * *", "7-59/30 * * * *", "15-59/30 * * * *", "22-59/30 * * * *")
				.withRules(5, "*/30 * * * *", "6-59/30 * * * *", "12-59/30 * * * *", "18-59/30 * * * *", "24-59/30 * * * *")
				.withRules(5, "*/30 * * * *", "5-59/30 * * * *", "10-59/30 * * * *", "15-59/30 * * * *", "20-59/30 * * * *", "25-59/30 * * * *")
				.build();

		EVERY_HOUR = Rules.create("0 * * * *")
				.withRules(1, "0 * * * *")
				.withRules(2, "0 * * * *", "30 * * * *")
				.withRules(3, "0 * * * *", "20 * * * *", "40 * * * *")
				.withRules(4, "0 * * * *", "15 * * * *", "30 * * * *", "45 * * * *")
				.withRules(5, "0 * * * *", "12 * * * *", "24 * * * *", "36 * * * *", "48 * * * *")
				.withRules(6, "0 * * * *", "10 * * * *", "20 * * * *", "30 * * * *", "40 * * * *", "50 * * * *")
				.build();

		ruleList = new Rules [] { EVERY_5_MINUTES, EVERY_10_MINUTES, EVERY_15_MINUTES, EVERY_30_MINUTES, EVERY_HOUR };
		 for (Rules rules : ruleList) {
			 ruleMap.put(rules.getParentRule(), rules);
		 }
	}
	
	public StaggeredIntervalTestPlanner () {}
	
	@Override
	public List<TestPlan> generateTestPlans(TestDefinition testDef) {
		List<LcpProxy> lcps = testDef.getLcps();
		String parentRule = testDef.getExecutionSchedule().getRecurrenceRule();
		Rules.RuleList ruleList = getRuleList(parentRule, lcps.size());
		Map<Integer, LcpProxy> randomizedLcps = new TreeMap<>();
		for (LcpProxy lcp : lcps) {
			int random;
			do {
			    random = (int) (Math.random() * 1000000);	
			}while (randomizedLcps.containsKey(random));
			randomizedLcps.put(random, lcp);
		}
		List<TestPlan> testPlans = new ArrayList<>(lcps.size());
		for (LcpProxy lcp : randomizedLcps.values()) {
			TestPlan.Builder builder = TestPlan.create()
					.withLcp(lcp)
					.withRecurrenceRule(ruleList.next());
			if (testDef.getTestDefinitionId() != null) {
				builder.withTestDefinitionId(testDef.getTestDefinitionId());
			}
			TestPlan testPlan = builder.build();
			testPlans.add(testPlan);
		}
		return testPlans;
	}
	
	private Rules.RuleList getRuleList (String parentRule, int numTests) {
		Rules rules = ruleMap.get(parentRule);
		if (rules == null) {
			rules = Rules.create(parentRule).build();
		}
		Rules.RuleList result = rules.getRules(numTests);
		return result;
	}
	
}

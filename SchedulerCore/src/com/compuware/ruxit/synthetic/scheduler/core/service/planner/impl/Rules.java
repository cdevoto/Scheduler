package com.compuware.ruxit.synthetic.scheduler.core.service.planner.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Rules {
    private int maxTests = Integer.MIN_VALUE;
    private String parentRule;
    private Map<Integer, List<String>> rules = new TreeMap<>();

    public static Builder create (String parentRule) {
	    return new Builder(parentRule);
    }
   
    private Rules (Builder builder) {
 	   this.maxTests = builder.maxTests;
 	   this.parentRule = builder.parentRule;
 	   this.rules.putAll(builder.rules);
    }
   
    public String getParentRule () {
	   return parentRule;
    }
   
    public RuleList getRules(int numTests) {
	   int match = -1;
	   for (int candidate : this.rules.keySet()) {
		   if (numTests <= candidate) {
			   match = candidate;
			   break;
		   }
	   }
	   if (match == -1) {
		   match = maxTests;
	   }
	   final List<String> rules = this.rules.get(match);
	   return new RuleList () {
			int index = 0;

			@Override
			public String next() {
				String result = rules.get(index++);
				if (index >= rules.size()) {
					index = 0;
				}
				return result;
			}
	   };
    }
   
	@Override
	public String toString() {
		return "Rules [maxTests=" + maxTests + ", parentRule=" + parentRule
				+ ", rules=" + rules + "]";
	}



    public static class Builder {
	   private int maxTests = Integer.MIN_VALUE;
	   private String parentRule;
	   private Map<Integer, List<String>> rules = new TreeMap<>();
	   
	   private Builder (String parentRule) {
		   this.parentRule = parentRule;
	   }
	   
	   public Builder withRules(int numTests, String rule, String ... rules) {
		   if (numTests > maxTests) {
			   maxTests = numTests;
		   }
		   List<String> ruleList = new LinkedList<>();
		   ruleList.add(rule);
		   for (String r : rules) {
				ruleList.add(r);
		   }
		   this.rules.put(numTests, ruleList);
		   return this;
	   }
	   
	   public Rules build () {
		   if (rules.isEmpty()) {
			   withRules(1, parentRule);
		   }
		   return new Rules(this);
	   }
	   
   }

   public static interface RuleList {
	   public String next ();
   }
}

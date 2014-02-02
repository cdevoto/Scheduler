package com.compuware.ruxit.synthetic.scheduler.core.dao.model;

import java.util.LinkedHashMap;
import java.util.Map;

public enum AbilityFlagLevel {
	SCRIPT(1, "SCRIPT"),
	TEST_DEFINITION(2, "TEST_DEFINITION");
	
	
	private static final Map<Long, AbilityFlagLevel> levelMap = new LinkedHashMap<>();
	
	private long id;
	private String level;
	
	static {
	    AbilityFlagLevel [] levels = { SCRIPT, TEST_DEFINITION };
	    
	    for (AbilityFlagLevel level : levels) {
	    	levelMap.put(level.id, level);
	    }
	}
	
	public static AbilityFlagLevel get (long id) {
		return levelMap.get(id);
	}

	private AbilityFlagLevel(long id, String level) {
		this.id = id;
		this.level = level;
	}

	public long getId() {
		return id;
	}

	public String getLevel() {
		return level;
	}
	
}

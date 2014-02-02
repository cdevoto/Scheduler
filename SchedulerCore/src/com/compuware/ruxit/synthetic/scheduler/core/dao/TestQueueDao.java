package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestView;

public interface TestQueueDao {
	
	public long push(Test test);
	public List<TestView> poll(long vucId, long supportsF, int maxRows);	
	public Test getById(long id);

}

package com.compuware.ruxit.synthetic.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestDefinitionDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinition;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class TestTestDefinitionDao {
	private static ApplicationContext context;
	
	private TestDefinitionDao dao;
	
	@BeforeClass
	public static void beforeClass () {
		context = AppContextService.getApplicationContext();
	}

	@Before
	public void before () {
		this.dao = context.getBean(TestDefinitionDao.class);
	}

	@Test
	public void testFilterLcps() {
		TestDefinition testDef = dao.getById(1L);
		assertNotNull(testDef);
		System.out.println(testDef);
	}

}

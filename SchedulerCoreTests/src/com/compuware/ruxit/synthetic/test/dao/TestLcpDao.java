package com.compuware.ruxit.synthetic.test.dao;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.dao.LcpDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

@SuppressWarnings("unused")
public class TestLcpDao {
	private static ApplicationContext context;
	
	private LcpDao dao;
	
	@BeforeClass
	public static void beforeClass () {
		context = AppContextService.getApplicationContext();
	}

	@Before
	public void before () {
		this.dao = context.getBean(LcpDao.class);
	}

	@Test
	public void testFilterLcps() {
		List<LcpProxy> lcps = dao.getLcps(6L, 2L);
		assertNotNull(lcps);
		System.out.println(lcps);
	}

}

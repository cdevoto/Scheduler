package com.compuware.ruxit.synthetic.test.dao;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TimeZoneDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TimeZone;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

@SuppressWarnings("unused")
public class TestTimeZoneDao {
	private static ApplicationContext context;
	
	private TimeZoneDao dao;
	
	@BeforeClass
	public static void beforeClass () {
		context = AppContextService.getApplicationContext();
	}

	@Before
	public void before () {
		this.dao = context.getBean(TimeZoneDao.class);
	}

	@Test
	public void testGetById() {
		TimeZone timeZone = dao.getById(1L);
		assertNotNull(timeZone);
		System.out.println(timeZone);
	}

	@Test
	public void testGetAll() {
		List<TimeZone> timeZones = dao.getAll();
		assertNotNull(timeZones);
		System.out.println(timeZones);
		
	}
	
}

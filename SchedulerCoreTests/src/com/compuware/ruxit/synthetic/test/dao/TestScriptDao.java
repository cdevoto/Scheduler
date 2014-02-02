package com.compuware.ruxit.synthetic.test.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptTypeDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Script;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptType;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class TestScriptDao {
	private static ApplicationContext context;
	private ScriptDao dao;
	private ScriptTypeDao dao2;
	
	@BeforeClass
	public static void beforeClass () {
		context = AppContextService.getApplicationContext();
	}
	
	@Before
	public void before () {
		this.dao = context.getBean(ScriptDao.class);
		this.dao2 = context.getBean(ScriptTypeDao.class);
	}

	@Test
	public void testGetById() {
		Script script = dao.getById(1L);
        assertThat(script.getId(), equalTo(1L));
        assertThat(script.getScriptType(), equalTo(ScriptType.create()
        		.withId(1L)
        		.withName("All Browsers")
        		.build()));
        assertThat(script.getTenantId(), equalTo(1L));
        assertThat(script.getName(), equalTo("Browser Script"));
        assertThat(script.getRequiresFlags(), equalTo(0L));
        assertThat(script.getActive(), equalTo(true));
        assertThat(script.getDeleted(), equalTo(false));
        assertThat(script.getLastModified(), greaterThan(1389912336000L));
        
	}

	@Test
	public void testGetAll() {
		List<ScriptProxy> scripts = dao.getAll(1L);
		assertNotNull(scripts);
		
		Set<ScriptProxy> scriptSet = new LinkedHashSet<>(scripts);
		assertThat(scriptSet.size(), greaterThan(1));
		for (ScriptProxy proxy : scriptSet) {
			assertThat(proxy.getId(), greaterThan(0L));
			assertThat(proxy.getName(), notNullValue());
			assertThat(proxy.getLastModified(), greaterThan(1389912336000L));

			Script script = dao.getById(proxy.getId());
		    assertThat(script.getActive(), equalTo(true));
			assertThat(script.getDeleted(), equalTo(false));
		}
	}
	
	@Test
	public void testInsertAndDelete () throws SQLException, InterruptedException {
		ScriptType type = dao2.getById(4L);
		Script script = Script.create()
				.withName("Test Script")
				.withScriptType(type)
				.withRequiresFlags(0L)
				.withTenantId(3L)
				.build();
		
		script = dao.insert(script);
		try {
	        assertThat(script.getId(), greaterThan(0L));
	        assertThat(script.getTenantId(), equalTo(3L));
	        assertThat(script.getName(), equalTo("Test Script"));
	        assertThat(script.getScriptType(), equalTo(type));
	        assertThat(script.getRequiresFlags(), equalTo(0L));
	        assertThat(script.getActive(), equalTo(true));
	        assertThat(script.getDeleted(), equalTo(false));
	        assertThat(script.getLastModified(), greaterThan(1389912336000L));

	        Thread.sleep(1000);
	        
	        dao.deleteById(script.getId());
	        Script deleted = dao.getById(script.getId());
	    	assertThat(deleted, notNullValue());
	        assertThat(deleted.getId(), equalTo(script.getId()));
	        assertThat(deleted.getTenantId(), equalTo(script.getTenantId()));
	        assertThat(deleted.getName(), equalTo(script.getName()));
	        assertThat(deleted.getScriptType(), equalTo(script.getScriptType()));
	        assertThat(deleted.getRequiresFlags(), equalTo(script.getRequiresFlags()));
	        assertThat(deleted.getActive(), equalTo(true));
	        assertThat(deleted.getDeleted(), equalTo(false));
	        assertThat(deleted.getLastModified(), greaterThan(script.getLastModified()));
		} finally {     
	        DataSource dataSource = context.getBean(DataSource.class);
	        try (Connection conn = dataSource.getConnection()) {
	            PreparedStatement ps = conn.prepareStatement("delete from script where script_id = ?");
	            ps.setLong(1, script.getId());
	            ps.execute();
	        	script = dao.getById(script.getId());
	        	assertThat(script, nullValue());
	        } catch (SQLException ex) {
	        	throw ex;
	        }
		}
	}
	
}

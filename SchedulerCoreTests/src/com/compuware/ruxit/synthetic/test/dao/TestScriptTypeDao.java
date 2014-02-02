package com.compuware.ruxit.synthetic.test.dao;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptTypeDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptType;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class TestScriptTypeDao {
	private static ApplicationContext context;
	private static ScriptType ALL_BROWSERS;
	private static ScriptType ANDROID;
	private static ScriptType IOS;
	private static ScriptType CHROME;
	private static ScriptType FIREFOX;
	private static ScriptType IE;
	
	@BeforeClass
	public static void beforeClass () {
		context = AppContextService.getApplicationContext();

		ALL_BROWSERS = ScriptType.create()
				.withId(1L)
				.withName("All Browsers")
				.build();
		ANDROID = ScriptType.create()
				.withId(2L)
				.withName("Android Native")
				.build();
		IOS = ScriptType.create()
				.withId(3L)
				.withName("iOS Native")
				.build();
		CHROME = ScriptType.create()
				.withId(4L)
				.withName("Chrome")
				.build();
		FIREFOX = ScriptType.create()
				.withId(5L)
				.withName("Firefox")
				.build();
		IE = ScriptType.create()
				.withId(6L)
				.withName("Internet Explorer")
				.build();
	}

	@Test
	public void testGetById() {
		ScriptTypeDao dao = context.getBean(ScriptTypeDao.class);
		ScriptType scriptType = dao.getById(1L);
		assertThat(scriptType.getId(), is(1L));
		assertThat(scriptType.getName(), is("All Browsers"));
		assertThat(scriptType, is(ALL_BROWSERS));
		assertThat(scriptType, not(ANDROID));
		assertThat(scriptType, not(IOS));
		assertThat(scriptType, not(CHROME));
		assertThat(scriptType, not(FIREFOX));
		assertThat(scriptType, not(IE));
		
		scriptType = dao.getById(2L);
		assertThat(scriptType, not(ALL_BROWSERS));
		assertThat(scriptType, is(ANDROID));
		assertThat(scriptType, not(IOS));
		assertThat(scriptType, not(CHROME));
		assertThat(scriptType, not(FIREFOX));
		assertThat(scriptType, not(IE));

		scriptType = dao.getById(3L);
		assertThat(scriptType, not(ALL_BROWSERS));
		assertThat(scriptType, not(ANDROID));
		assertThat(scriptType, is(IOS));
		assertThat(scriptType, not(CHROME));
		assertThat(scriptType, not(FIREFOX));
		assertThat(scriptType, not(IE));
	
		scriptType = dao.getById(4L);
		assertThat(scriptType, not(ALL_BROWSERS));
		assertThat(scriptType, not(ANDROID));
		assertThat(scriptType, not(IOS));
		assertThat(scriptType, is(CHROME));
		assertThat(scriptType, not(FIREFOX));
		assertThat(scriptType, not(IE));
		
		scriptType = dao.getById(5L);
		assertThat(scriptType, not(ALL_BROWSERS));
		assertThat(scriptType, not(ANDROID));
		assertThat(scriptType, not(IOS));
		assertThat(scriptType, not(CHROME));
		assertThat(scriptType, is(FIREFOX));
		assertThat(scriptType, not(IE));
		
		scriptType = dao.getById(6L);
		assertThat(scriptType, not(ALL_BROWSERS));
		assertThat(scriptType, not(ANDROID));
		assertThat(scriptType, not(IOS));
		assertThat(scriptType, not(CHROME));
		assertThat(scriptType, not(FIREFOX));
		assertThat(scriptType, is(IE));
	}

	@Test
	public void testGetAll() {
		ScriptTypeDao dao = context.getBean(ScriptTypeDao.class);
		List<ScriptType> scriptTypes = dao.getAll();
		assertNotNull(scriptTypes);
		
		Set<ScriptType> typeSet = new HashSet<>(scriptTypes);
		assertThat(typeSet.size(), is(6));
		assertThat(typeSet.contains(ALL_BROWSERS), is(true));
		assertThat(typeSet.contains(ANDROID), is(true));
		assertThat(typeSet.contains(IOS), is(true));
		assertThat(typeSet.contains(CHROME), is(true));
		assertThat(typeSet.contains(FIREFOX), is(true));
		assertThat(typeSet.contains(IE), is(true));
		
	}
	
}

package com.compuware.ruxit.synthetic.test.dao;

import static com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlagLevel.SCRIPT;
import static com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlagLevel.TEST_DEFINITION;
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

import com.compuware.ruxit.synthetic.scheduler.core.dao.AbilityFlagDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlagLevel;
import com.compuware.ruxit.synthetic.test.util.AppContextService;

public class TestAbilityFlagDao {
	private static ApplicationContext context;
	private static AbilityFlag IPV4;
	private static AbilityFlag IPV6;
	private static AbilityFlag SMS;
	
	@BeforeClass
	public static void beforeClass () {
		context = AppContextService.getApplicationContext();

		IPV4 = AbilityFlag.create()
				.withId(1L)
				.withLevel(2L)
				.withMask(1L)
				.withDescription("IPv4")
				.build();

		IPV6 = AbilityFlag.create()
				.withId(2L)
				.withLevel(2L)
				.withMask(2L)
				.withDescription("IPv6")
				.build();
	
		SMS = AbilityFlag.create()
				.withId(3L)
				.withLevel(1L)
				.withMask(4L)
				.withDescription("SMS")
				.build();
	}

	@Test
	public void testGetById() {
		AbilityFlagDao dao = context.getBean(AbilityFlagDao.class);
		AbilityFlag abilityFlag = dao.getById(1L);
		assertThat(abilityFlag.getId(), is(1L));
		assertThat(abilityFlag.getLevel(), is(AbilityFlagLevel.TEST_DEFINITION));
		assertThat(abilityFlag.getMask(), is(1L));
		assertThat(abilityFlag.getDescription(), is("IPv4"));
		assertThat(abilityFlag, is(IPV4));
		assertThat(abilityFlag, not(IPV6));
		assertThat(abilityFlag, not(SMS));
		
		abilityFlag = dao.getById(2L);
		assertThat(abilityFlag, not(IPV4));
		assertThat(abilityFlag, is(IPV6));
		assertThat(abilityFlag, not(SMS));

		abilityFlag = dao.getById(3L);
		assertThat(abilityFlag, not(IPV4));
		assertThat(abilityFlag, not(IPV6));
		assertThat(abilityFlag, is(SMS));
	}

	@Test
	public void testGetByLevel() {
		AbilityFlagDao dao = context.getBean(AbilityFlagDao.class);
		List<AbilityFlag> abilityFlags = dao.getByLevel(TEST_DEFINITION);
		assertNotNull(abilityFlags);
		
		Set<AbilityFlag> flagSet = new HashSet<>(abilityFlags);
		assertThat(flagSet.size(), is(2));
		assertThat(flagSet.contains(IPV4), is(true));
		assertThat(flagSet.contains(IPV6), is(true));
		
		abilityFlags = dao.getByLevel(SCRIPT);
		assertNotNull(abilityFlags);
		
		flagSet = new HashSet<>(abilityFlags);
		assertThat(flagSet.size(), is(1));
		assertThat(flagSet.contains(SMS), is(true));
	}
	
	@Test
	public void testGetAll() {
		AbilityFlagDao dao = context.getBean(AbilityFlagDao.class);
		List<AbilityFlag> abilityFlags = dao.getAll();
		assertNotNull(abilityFlags);
		
		Set<AbilityFlag> flagSet = new HashSet<>(abilityFlags);
		assertThat(flagSet.size(), is(3));
		assertThat(flagSet.contains(IPV4), is(true));
		assertThat(flagSet.contains(IPV6), is(true));
		assertThat(flagSet.contains(SMS), is(true));
		
	}
	
}

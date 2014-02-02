package com.compuware.ruxit.synthetic.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.compuware.ruxit.synthetic.test.dao.TestAbilityFlagDao;
import com.compuware.ruxit.synthetic.test.dao.TestScriptDao;
import com.compuware.ruxit.synthetic.test.dao.TestScriptTypeDao;
import com.compuware.ruxit.synthetic.test.dao.model.TestAbilityFlag;
import com.compuware.ruxit.synthetic.test.service.TestTestDefinitionService;

@RunWith(Suite.class)
@SuiteClasses({ TestAbilityFlagDao.class, 
	TestScriptDao.class, TestScriptTypeDao.class,
	TestAbilityFlag.class,
	TestTestDefinitionService.class})
public class AllTests {

}

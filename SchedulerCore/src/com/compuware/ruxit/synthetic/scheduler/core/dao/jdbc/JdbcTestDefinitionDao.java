package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.compuware.ruxit.synthetic.scheduler.core.dao.LcpDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScheduleDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TestDefinitionDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Schedule;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinition;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestDefinitionProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlan;

@Repository
public class JdbcTestDefinitionDao implements TestDefinitionDao {

	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LcpDao lcpDao;
	@Autowired
	private ScheduleDao scheduleDao;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public TestDefinition getById(long id) {
		final String SELECT_SQL = "SELECT test_definition_id, script_id, tenant_id, name, requires_f, suspended, last_modified "
				+ "FROM test_definition "
				+ "WHERE test_definition_id = ?";
		try {
			TestDefinition testDef = this.jdbcTemplate.queryForObject(SELECT_SQL,
			        new Object[]{ id},
			        new TestDefinitionMapper(lcpDao, scheduleDao));
			return testDef;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	public List<TestDefinitionProxy> getByScheduleId(long scheduleId) {
		List<TestDefinitionProxy> testDefinitions = this.jdbcTemplate.query(
		        "SELECT test_definition_id, name, suspended, last_modified FROM schedule_test_definition_vw WHERE schedule_id = ? AND active = true AND deleted = false ORDER BY name ASC",
		        new Object[]{ scheduleId },
		        new TestDefinitionProxyMapper());
		return testDefinitions;
	}

	@Override
	public List<TestDefinitionProxy> getByTenantId(long tenantId) {
		List<TestDefinitionProxy> testDefinitions = this.jdbcTemplate.query(
		        "SELECT test_definition_id, name, suspended, last_modified FROM test_definition WHERE tenant_id = ? AND active = true AND deleted = false ORDER BY name ASC",
		        new Object[]{ tenantId },
		        new TestDefinitionProxyMapper());
		return testDefinitions;
	}

	@Override
	public List<TestDefinitionProxy> getAll(long scriptId) {
		List<TestDefinitionProxy> testDefinitions = this.jdbcTemplate.query(
		        "SELECT test_definition_id, name, suspended, last_modified FROM test_definition WHERE script_id = ? AND active = true AND deleted = false ORDER BY name ASC",
		        new Object[]{ scriptId },
		        new TestDefinitionProxyMapper());
		return testDefinitions;
	}

	@Override
	@Transactional
	public long insert(final TestDefinition testDefinition, List<TestPlan> testPlans) {
		final String SELECT_SCRIPT_SQL = "SELECT requires_f FROM script WHERE script_id = ?";
		final String INSERT_SQL = "INSERT INTO test_definition (script_id, tenant_id, name, requires_f) VALUES (?, ?, ?, ?)";
		final String INSERT_EXEC_SCHEDULE_SQL = "INSERT INTO schedule (tenant_id, name, rrule) VALUES (?, ?, ?)";
		final String INSERT_TEST_DEF_SCHEDULE_SQL = "INSERT INTO test_definition_schedule (test_definition_id, schedule_id) VALUES (?, ?)";
		final String INSERT_TEST_DEF_LCP_SQL = "INSERT INTO test_definition_lcp (test_definition_id, lcp_id) VALUES (?, ?)";
		final String INSERT_TEST_PLAN_SQL = "INSERT INTO test_plan (test_definition_id, lcp_id, requires_f, rrule) VALUES (?, ?, ?, ?)";

		Long script_requires_f = jdbcTemplate.queryForLong(SELECT_SCRIPT_SQL, testDefinition.getScriptId());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"id"});
		            ps.setLong(1, testDefinition.getScriptId());
		            ps.setLong(2, testDefinition.getTenantId());
		            ps.setString(3, testDefinition.getName());
		            ps.setLong(4, testDefinition.getRequiresF());
		            return ps;
		        }
		    },
		    keyHolder);
		Number id = keyHolder.getKey();
		
		final Schedule execSchedule = testDefinition.getExecutionSchedule();
		jdbcTemplate.update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps = connection.prepareStatement(INSERT_EXEC_SCHEDULE_SQL, new String[] {"id"});
			            ps.setLong(1, testDefinition.getTenantId());
			            ps.setString(2, execSchedule.getName());
			            ps.setString(3, execSchedule.getRecurrenceRule());
			            return ps;
			        }
			    },
			    keyHolder);
		Number execScheduleId = keyHolder.getKey();
		
		jdbcTemplate.update(INSERT_TEST_DEF_SCHEDULE_SQL, id, execScheduleId);
		
		for (LcpProxy lcp : testDefinition.getLcps()) {
			jdbcTemplate.update(INSERT_TEST_DEF_LCP_SQL, id, lcp.getId());
		}
		
		for (TestPlan testPlan : testPlans) {
			jdbcTemplate.update(INSERT_TEST_PLAN_SQL, id, testPlan.getLcp().getId(), testDefinition.getRequiresF() | script_requires_f, testPlan.getRecurrenceRule());
		}
		
		return id.longValue();
	}

	@Override
	public void update(final TestDefinition testDefinition, List<TestPlan> testPlans) {
		final String SELECT_SCRIPT_SQL = "SELECT requires_f FROM script WHERE script_id = ?";
		final String UPDATE_SQL = "UPDATE test_definition SET script_id = ?, tenant_id = ?, name = ?, requires_f = ?, last_modified = ? WHERE test_definition_id = ?";
		final String UPDATE_EXEC_SCHEDULE_SQL = "UPDATE schedule SET tenant_id = ?, name = ?, rrule = ?, last_modified = ? WHERE schedule_id = ?";
		final String DELETE_TEST_DEF_LCP_SQL = "DELETE FROM test_definition_lcp WHERE test_definition_id = ?";
		final String INSERT_TEST_DEF_LCP_SQL = "INSERT INTO test_definition_lcp (test_definition_id, lcp_id) VALUES (?, ?)";
		final String DELETE_TEST_PLAN_SQL = "DELETE FROM test_plan WHERE test_definition_id = ?";
		final String INSERT_TEST_PLAN_SQL = "INSERT INTO test_plan (test_definition_id, lcp_id, requires_f, rrule) VALUES (?, ?, ?, ?)";
	
		Timestamp now = new Timestamp(new Date().getTime());
		long id = testDefinition.getTestDefinitionId();
	
		Long script_requires_f = jdbcTemplate.queryForLong(SELECT_SCRIPT_SQL, testDefinition.getScriptId());
		
		jdbcTemplate.update(UPDATE_SQL, testDefinition.getScriptId(),
		            testDefinition.getTenantId(),
		            testDefinition.getName(),
		            testDefinition.getRequiresF(),
		            now,
		            id);
		
		final Schedule execSchedule = testDefinition.getExecutionSchedule();
		jdbcTemplate.update(UPDATE_EXEC_SCHEDULE_SQL, testDefinition.getTenantId(),
			            execSchedule.getName(),
			            execSchedule.getRecurrenceRule(),
			            now,
			            execSchedule.getScheduleId());
		
		
		jdbcTemplate.update(DELETE_TEST_DEF_LCP_SQL, id);
		for (LcpProxy lcp : testDefinition.getLcps()) {
			jdbcTemplate.update(INSERT_TEST_DEF_LCP_SQL, id, lcp.getId());
		}
		
		jdbcTemplate.update(DELETE_TEST_PLAN_SQL, id);
		for (TestPlan testPlan : testPlans) {
			jdbcTemplate.update(INSERT_TEST_PLAN_SQL, id, testPlan.getLcp().getId(), testDefinition.getRequiresF() | script_requires_f, testPlan.getRecurrenceRule());
		}
		
	}

	@Override
	public void deleteById(long id) {
		this.jdbcTemplate.update("call delete_test_definition(?)", id);
	}

	private static final class TestDefinitionProxyMapper implements RowMapper<TestDefinitionProxy> {
	    public TestDefinitionProxy mapRow(ResultSet rs, int rowNum) throws SQLException {
	        TestDefinitionProxy scriptProxy = TestDefinitionProxy.create()
	        		.withId(rs.getLong("test_definition_id"))
	        		.withName(rs.getString("name"))
	        		.withSuspended(rs.getBoolean("suspended"))
	        		.withLastModified(rs.getTimestamp("last_modified").getTime())
	        		.build();
	        return scriptProxy;
	    }
	}

	@Override
	public void suspend(long testDefinitionId, boolean suspended) {
		this.jdbcTemplate.update("UPDATE test_definition SET suspended = ? WHERE test_definition_id = ?",
				suspended, 
				testDefinitionId);
		
	}
	
	@Override
	public void suspendForMaintenance(long testDefinitionId, boolean suspended) {
		this.jdbcTemplate.update("UPDATE test_definition SET maint_suspended = ? WHERE test_definition_id = ?",
				suspended, 
				testDefinitionId);
		
	}

	private static class TestDefinitionMapper implements RowMapper<TestDefinition> {

		private LcpDao lcpDao;
		private ScheduleDao scheduleDao;


		public TestDefinitionMapper(LcpDao lcpDao,
				ScheduleDao scheduleDao) {
			this.lcpDao = lcpDao;
			this.scheduleDao = scheduleDao;
		}

		@Override
		public TestDefinition mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			long testDefinitionId= rs.getLong("test_definition_id");
			TestDefinition.Builder builder = TestDefinition.create()
					.withTestDefinitionId(rs.getLong("test_definition_id"))
					.withTenantId(rs.getLong("tenant_id"))
					.withScriptId(rs.getLong("script_id"))
					.withName(rs.getString("name"))
					.withRequiresF(rs.getLong("requires_f"))
					.withSuspended(rs.getBoolean("suspended"))
					.withLastModified(rs.getTimestamp("last_modified").getTime());
			    
			List<Schedule> schedules = scheduleDao.getByTestDefId(testDefinitionId);
			for (Schedule schedule : schedules) {
				if (!schedule.isMaintenance()) {
					builder.withExecutionSchedule(schedule);
				}
			}
			List<LcpProxy> lcpProxies = lcpDao.getByTestDefId(testDefinitionId);
			for (LcpProxy lcp : lcpProxies) {
				builder.withLcp(lcp);
			}
			TestDefinition testDef = builder.build();
			return testDef;
		}
		
	}

}

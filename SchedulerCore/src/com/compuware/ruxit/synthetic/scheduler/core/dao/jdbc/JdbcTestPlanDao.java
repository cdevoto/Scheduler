package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestPlanDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestPlanView;

@Repository
public class JdbcTestPlanDao implements TestPlanDao {

	private JdbcTemplate jdbcTemplate;
	private GetTestPlans getTestPlans;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.getTestPlans = new GetTestPlans(this.jdbcTemplate);
    }

	@Override
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified) {
		List<TestPlanView> testPlans = getTestPlans.call(totalWorkers, workerNum, maxRows, maxLastModified, null, null, null);
		return testPlans;
	}

	@Override
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified, 
			long minTestDefId, long minTestPlanId) {
		List<TestPlanView> testPlans = getTestPlans.call(totalWorkers, workerNum, maxRows, maxLastModified, minTestDefId, minTestPlanId, null);
		return testPlans;
	}

	@Override
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified,
			long minLastModified) {
		List<TestPlanView> testPlans = getTestPlans.call(totalWorkers, workerNum, maxRows, maxLastModified, null, null, minLastModified);
		return testPlans;
	}

	@Override
	public List<TestPlanView> get(int totalWorkers, int workerNum, int maxRows, long maxLastModified,
			long minTestDefId, long minTestPlanId, long minLastModified) {
		List<TestPlanView> testPlans = getTestPlans.call(totalWorkers, workerNum, maxRows, maxLastModified, minTestDefId, minTestPlanId, minLastModified);
		return testPlans;
	}

	@Override
	public List<TestPlanView> getByTestDefId(long testDefId) {
		final String SELECT_SQL = "SELECT * FROM test_plan_vw WHERE test_definition_id = ?";
		List<TestPlanView> testPlans = jdbcTemplate.query(SELECT_SQL, 
				new Object [] {testDefId}, 
				new TestPlanViewMapper());
		return testPlans;
	}

	private static class GetTestPlans extends StoredProcedure {
		
		public GetTestPlans(JdbcTemplate jdbcTemplate) {
			
			// CREATE PROCEDURE get_test_plans (IN total_workers SMALLINT UNSIGNED, IN worker_num SMALLINT UNSIGNED, IN max_rows SMALLINT UNSIGNED, IN min_test_def_id BIGINT UNSIGNED, IN min_test_plan_id BIGINT UNSIGNED, IN min_last_modified TIMESTAMP) 

			super(jdbcTemplate, "get_test_plans");
			declareParameter(new SqlReturnResultSet("rs", new TestPlanViewMapper()));
			declareParameter(new SqlParameter("total_workers", Types.SMALLINT));
			declareParameter(new SqlParameter("worker_num", Types.SMALLINT));
			declareParameter(new SqlParameter("max_rows", Types.SMALLINT));
			declareParameter(new SqlParameter("max_last_modified", Types.TIMESTAMP));
			declareParameter(new SqlParameter("min_test_def_id", Types.BIGINT));
			declareParameter(new SqlParameter("min_test_plan_id", Types.BIGINT));
			declareParameter(new SqlParameter("min_last_modified", Types.TIMESTAMP));
			
			compile();
		}
		
		public List<TestPlanView> call(int totalWorkers, int workerNum, int maxRows, long maxLastModified, Long minTestDefId, Long minTestPlanId, Long minLastModified) {
			Map<String, Object> inParameters = new HashMap<>();
			Timestamp maxLastMod = new Timestamp(maxLastModified);
			Timestamp minLastMod = null;
			if (minLastModified != null) {
				minLastMod = new Timestamp(minLastModified);
			}
			inParameters.put("total_workers", totalWorkers);
			inParameters.put("worker_num", workerNum);
			inParameters.put("max_rows", maxRows);
			inParameters.put("max_last_modified", maxLastMod);
			inParameters.put("min_test_def_id", minTestDefId);
			inParameters.put("min_test_plan_id", minTestPlanId);
			inParameters.put("min_last_modified", minLastMod);
			
			Map<String, Object> resultMap = execute(inParameters);
			@SuppressWarnings("unchecked")
			List<TestPlanView> lcps = (List<TestPlanView>) resultMap.get("rs");
			return lcps;
		}

	}
	
	private static class TestPlanViewMapper implements RowMapper<TestPlanView> {

		@Override
		public TestPlanView mapRow(ResultSet rs, int rowNum) throws SQLException {
			TestPlanView testPlan = TestPlanView.create()
					.withId(rs.getLong("test_plan_id"))
					.withTestDefinitionId(rs.getLong("test_definition_id"))
					.withLcpId(rs.getLong("lcp_id"))
					.withScriptId(rs.getLong("script_id"))
					.withTenantId(rs.getLong("tenant_id"))
					.withRequiresF(rs.getLong("requires_f"))
					.withRecurrenceRule(rs.getString("rrule"))
					.withActive(rs.getBoolean("active"))
					.withDeleted(rs.getBoolean("deleted"))
					.withLastModified(rs.getTimestamp("last_modified").getTime())
					.build();
			return testPlan;
		}
		
	}

}

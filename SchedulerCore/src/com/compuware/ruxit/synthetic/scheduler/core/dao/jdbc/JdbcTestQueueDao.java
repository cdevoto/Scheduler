package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.compuware.ruxit.synthetic.scheduler.core.dao.TestQueueDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TestView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Test.CancelledStatus;

@Repository
public class JdbcTestQueueDao implements TestQueueDao {

	private JdbcTemplate jdbcTemplate;
	private PollTestQueue pollTestQueue;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.pollTestQueue = new PollTestQueue(jdbcTemplate);
    }
	
	public List<TestView> poll(long vucId, long supportsF, int maxRows) {
		List<TestView> tests = new LinkedList<TestView>();
		int validTestCount = 0;
		while (validTestCount < maxRows) {
			TestView test = poll(vucId, supportsF);
			if (test == null) {
				break;
			} else if (test.getStatus() == Test.Status.DISPATCHED) {
				validTestCount++;
				tests.add(test);
			}
		}
		return tests;
	}

	@Transactional
	private TestView poll(long vucId, long supportsF) {
		final String CANCEL_SQL = "UPDATE test_queue SET status = 3, cancelled_at = ?, cancelled_status = ? WHERE test_queue_id = ?";
		final String DISPATCH_SQL = "UPDATE test_queue SET status = 2, dispatched_at = ?, dispatched_vuc_id = ? WHERE test_queue_id = ?";
		List<TestView> tests = pollTestQueue.call(vucId, supportsF, 1);
		if (tests.isEmpty()) {
			return null;
		}
		TestView test = tests.get(0);
		TestView.Builder builder = TestView.create(test);
		
        Timestamp now = new Timestamp(System.currentTimeMillis());
		if (!test.isActive() || test.isScriptDeleted() || test.isTestDefDeleted() || test.isSuspended() || test.isMaintSuspended()) {
			CancelledStatus cancelledStatus = null;
			if (!test.isActive()) {
				cancelledStatus = CancelledStatus.INACTIVE;
			} else if (test.isScriptDeleted()) {
				cancelledStatus = CancelledStatus.SCRIPT_DELETED;
			} else if (test.isTestDefDeleted()) {
				cancelledStatus = CancelledStatus.TEST_DEF_DELETED;
			} else if (test.isSuspended()) {
				cancelledStatus = CancelledStatus.SUSPENDED;
			} else if (test.isMaintSuspended()) {
				cancelledStatus = CancelledStatus.MAINT_WINDOW;
			}
			jdbcTemplate.update(CANCEL_SQL, now, cancelledStatus.getValue(), test.getId());
			builder.withStatus(Test.Status.CANCELLED)
			       .withCancelledAt(now.getTime());
		} else {
			jdbcTemplate.update(DISPATCH_SQL, now, vucId, test.getId());
			builder.withStatus(Test.Status.DISPATCHED)
		       .withDispatchedAt(now.getTime());
		}
		return builder.build();
	}

	@Override
	public long push(final Test test) {
		
		final String INSERT_SQL = "INSERT INTO test_queue (test_definition_id, script_id, tenant_id, lcp_id, requires_f, priority, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"id"});
		            ps.setLong(1, test.getTestDefinitionId());
		            ps.setLong(2, test.getScriptId());
		            ps.setLong(3, test.getTenantId());
		            ps.setLong(4, test.getLcpId());
		            ps.setLong(5, test.getRequiresF());
		            ps.setInt(6, test.getPriority().getValue());
		            ps.setInt(7, test.getStatus().getValue());
		            return ps;
		        }
		    },
		    keyHolder);
		long id = keyHolder.getKey().longValue();
		return id;
	}
	
	@Override
	public Test getById (long id) {
		final String SELECT_SQL = "SELECT * FROM test_queue WHERE test_queue_id = ?";
		try {
			Test test = this.jdbcTemplate.queryForObject(
			        SELECT_SQL,
			        new Object[]{ id },
			        new TestMapper());
			return test;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	private static class TestMapper implements RowMapper<Test> {

		@Override
		public Test mapRow(ResultSet rs, int rowNum) throws SQLException {
			Test.Priority priority = null;
			Integer priorityValue = rs.getInt("priority");
			if (priorityValue != null) {
				priority = Test.Priority.get(priorityValue);
			}
			Test.Status status = null;
			Integer statusValue = rs.getInt("status");
			if (statusValue != null) {
				status = Test.Status.get(statusValue);
			}
			Test.CompletedStatus completedStatus = null;
			Integer completedStatusValue = rs.getInt("completed_status");
			if (completedStatusValue != null) {
				completedStatus = Test.CompletedStatus.get(completedStatusValue);
			}
			
			Test.CancelledStatus cancelledStatus = null;
			Integer cancelledStatusValue = rs.getInt("cancelled_status");
			if (cancelledStatusValue != null) {
				cancelledStatus = Test.CancelledStatus.get(cancelledStatusValue);
			}

			Long dispatchedAt = null; 
			if (rs.getObject("dispatched_at") != null) {
				dispatchedAt = rs.getTimestamp("dispatched_at").getTime();
			}
			Long cancelledAt = null; 
			if (rs.getObject("cancelled_at") != null) {
				cancelledAt = rs.getTimestamp("cancelled_at").getTime();
			}
			Long completedAt = null; 
			if (rs.getObject("completed_at") != null) {
				completedAt = rs.getTimestamp("completed_at").getTime();
			}
			
			Test test = Test.create()
					.withId(rs.getLong("test_queue_id"))
					.withTestDefinitionId(rs.getLong("test_definition_id"))
					.withScriptId(rs.getLong("script_id"))
					.withTenantId(rs.getLong("tenant_id"))
					.withLcpId(rs.getLong("lcp_id"))
					.withRequiresF(rs.getLong("requires_f"))
					.withPriority(priority)
					.withStatus(status)
					.withEnqueuedAt(rs.getTimestamp("enqueued_at").getTime())
					.withDispatchedAt(dispatchedAt)
					.withDispatchedVucId(rs.getLong("dispatched_vuc_id"))
					.withCancelledAt(cancelledAt)
					.withCancelledStatus(cancelledStatus)
					.withCompletedAt(completedAt)
					.withCompletedStatus(completedStatus)
					.build();
			return test;
		}
		
	}

	private static class TestViewMapper implements RowMapper<TestView> {

		@Override
		public TestView mapRow(ResultSet rs, int rowNum) throws SQLException {
			Test.Priority priority = null;
			Integer priorityValue = rs.getInt("priority");
			if (priorityValue != null) {
				priority = Test.Priority.get(priorityValue);
			}
			Test.Status status = null;
			Integer statusValue = rs.getInt("status");
			if (statusValue != null) {
				status = Test.Status.get(statusValue);
			}
			Test.CompletedStatus completedStatus = null;
			Integer completedStatusValue = rs.getInt("completed_status");
			if (completedStatusValue != null) {
				completedStatus = Test.CompletedStatus.get(completedStatusValue);
			}

			Test.CancelledStatus cancelledStatus = null;
			Integer cancelledStatusValue = rs.getInt("cancelled_status");
			if (cancelledStatusValue != null) {
				cancelledStatus = Test.CancelledStatus.get(cancelledStatusValue);
			}
			
			Long dispatchedAt = null; 
			if (rs.getObject("dispatched_at") != null) {
				dispatchedAt = rs.getTimestamp("dispatched_at").getTime();
			}
			Long cancelledAt = null; 
			if (rs.getObject("cancelled_at") != null) {
				cancelledAt = rs.getTimestamp("cancelled_at").getTime();
			}
			Long completedAt = null; 
			if (rs.getObject("completed_at") != null) {
				completedAt = rs.getTimestamp("completed_at").getTime();
			}
			
			TestView test = TestView.create()
					.withId(rs.getLong("test_queue_id"))
					.withTestDefinitionId(rs.getLong("test_definition_id"))
					.withScriptId(rs.getLong("script_id"))
					.withTenantId(rs.getLong("tenant_id"))
					.withLcpId(rs.getLong("lcp_id"))
					.withRequiresF(rs.getLong("requires_f"))
					.withPriority(priority)
					.withStatus(status)
					.withEnqueuedAt(rs.getTimestamp("enqueued_at").getTime())
					.withDispatchedAt(dispatchedAt)
					.withDispatchedVucId(rs.getLong("dispatched_vuc_id"))
					.withCancelledAt(cancelledAt)
					.withCancelledStatus(cancelledStatus)
					.withCompletedAt(completedAt)
					.withCompletedStatus(completedStatus)
					.withActive(rs.getBoolean("active"))
					.withSuspended(rs.getBoolean("suspended"))
					.withMaintSuspended(rs.getBoolean("maint_suspended"))
					.withTestDefDeleted(rs.getBoolean("test_def_deleted"))
					.withScriptDeleted(rs.getBoolean("script_deleted"))
					.withTestDefLastModified(rs.getTimestamp("test_def_last_modified").getTime())
					.withScriptLastModified(rs.getTimestamp("script_last_modified").getTime())
					.build();
			return test;
		}
		
	}
	
	private static class PollTestQueue extends StoredProcedure {
		
		public PollTestQueue(JdbcTemplate jdbcTemplate) {
			
			// CREATE PROCEDURE get_test_plans (IN total_workers SMALLINT UNSIGNED, IN worker_num SMALLINT UNSIGNED, IN max_rows SMALLINT UNSIGNED, IN min_test_def_id BIGINT UNSIGNED, IN min_test_plan_id BIGINT UNSIGNED, IN min_last_modified TIMESTAMP) 

			super(jdbcTemplate, "poll_test_queue");
			declareParameter(new SqlReturnResultSet("rs", new TestViewMapper()));
			declareParameter(new SqlParameter("vuc_id", Types.BIGINT));
			declareParameter(new SqlParameter("supports_f", Types.BIGINT));
			declareParameter(new SqlParameter("max_rows", Types.SMALLINT));
			compile();
		}
		
		public List<TestView> call(long vucId, long supportsF, int maxRows) {
			Map<String, Object> inParameters = new HashMap<>();
			inParameters.put("vuc_id", vucId);
			inParameters.put("supports_f", supportsF);
			inParameters.put("max_rows", maxRows);
			
			Map<String, Object> resultMap = execute(inParameters);
			@SuppressWarnings("unchecked")
			List<TestView> tests = (List<TestView>) resultMap.get("rs");
			return tests;
		}

	}
	

}

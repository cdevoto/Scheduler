package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
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

import com.compuware.ruxit.synthetic.scheduler.core.dao.ScheduleDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.TimeZoneDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Schedule;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScheduleProxy;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TimeZone;

@Repository
public class JdbcScheduleDao implements ScheduleDao {

	private JdbcTemplate jdbcTemplate;
	private TimeZoneDao timeZoneDao;
	private GetMaintenanceSchedules getMaintenanceSchedules;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        if (this.timeZoneDao != null) {
            getMaintenanceSchedules = new GetMaintenanceSchedules(jdbcTemplate, timeZoneDao);
        }
    }
	
	@Autowired
	public void setTimeZoneDao(TimeZoneDao timeZoneDao) {
		this.timeZoneDao = timeZoneDao;
        if (this.jdbcTemplate != null) {
            getMaintenanceSchedules = new GetMaintenanceSchedules(jdbcTemplate, timeZoneDao);
        }
	}



	@Override
	public List<Schedule> getByTestDefId(long testDefinitionId) {
		final String SELECT_SQL = "SELECT schedule_id, tenant_id, timezone_id, name, rrule, duration, is_maintenance, last_modified "
				+ "FROM test_definition_schedule_vw "
				+ "WHERE test_definition_id = ? AND deleted = false AND active = true";
		List<Schedule> schedules = this.jdbcTemplate.query(SELECT_SQL,
		        new Object[]{ testDefinitionId},
		        new ScheduleMapper(timeZoneDao));
		return schedules;
	}
	
	@Override
	public Schedule getById(long scheduleId) {
		final String SELECT_SQL = "SELECT schedule_id, tenant_id, timezone_id, name, rrule, duration, is_maintenance, last_modified "
				+ "FROM schedule WHERE schedule_id = ?";
		try {
			Schedule schedule = this.jdbcTemplate.queryForObject(
			        SELECT_SQL,
			        new Object[]{ scheduleId },
			        new ScheduleMapper(timeZoneDao));
			return schedule;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	public List<ScheduleProxy> getAll(long tenantId) {
		List<ScheduleProxy> schedules = this.jdbcTemplate.query(
		        "SELECT schedule_id, name, last_modified FROM schedule WHERE tenant_id = ? AND active = true AND deleted = false ORDER BY name ASC",
		        new Object[]{ tenantId },
		        new ScheduleProxyMapper());
		return schedules;
	}
	
	@Override
	public List<ScheduleProxy> getAll(long tenantId, boolean isMaintenance) {
		List<ScheduleProxy> schedules = this.jdbcTemplate.query(
		        "SELECT schedule_id, name, last_modified FROM schedule WHERE tenant_id = ? AND is_maintenance = ? AND active = true AND deleted = false ORDER BY name ASC",
		        new Object[]{ tenantId, isMaintenance },
		        new ScheduleProxyMapper());
		return schedules;
	}

	

	@Override
	@Transactional
	public long insert(final Schedule schedule, List<Long> testDefIds) {
		final String INSERT_MAINT_SCHEDULE_SQL = "INSERT INTO schedule (tenant_id, timezone_id, name, rrule, duration, is_maintenance) VALUES (?, ?, ?, ?, ?, ?)";
		final String INSERT_TEST_DEF_SCHEDULE_SQL = "INSERT INTO test_definition_schedule (test_definition_id, schedule_id) VALUES (?, ?)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps = connection.prepareStatement(INSERT_MAINT_SCHEDULE_SQL, new String[] {"id"});
			            ps.setLong(1, schedule.getTenantId());
			            ps.setLong(2, schedule.getTimeZone().getId());
			            ps.setString(3, schedule.getName());
			            ps.setString(4, schedule.getRecurrenceRule());
			            ps.setInt(5, schedule.getDuration());
			            ps.setBoolean(6, schedule.isMaintenance());
			            return ps;
			        }
			    },
			    keyHolder);
		long maintScheduleId = keyHolder.getKey().longValue();
		
		for (long testDefId : testDefIds) {
			jdbcTemplate.update(INSERT_TEST_DEF_SCHEDULE_SQL, testDefId, maintScheduleId);
		}
	
		return maintScheduleId;
	}

	@Override
	@Transactional
	public void update(final Schedule schedule, List<Long> testDefIds) {
		final String UPDATE_MAINT_SCHEDULE_SQL = "UPDATE schedule SET tenant_id = ?, timezone_id = ?, name = ?, rrule = ?, duration = ?, last_modified = ? WHERE schedule_id = ?";
		final String DELETE_TEST_DEF_SCHEDULE_SQL = "DELETE FROM test_definition_schedule WHERE schedule_id = ?";
		final String INSERT_TEST_DEF_SCHEDULE_SQL = "INSERT INTO test_definition_schedule (test_definition_id, schedule_id) VALUES (?, ?)";
		
		Timestamp now = new Timestamp(new Date().getTime());
		long id = schedule.getScheduleId();

		jdbcTemplate.update(UPDATE_MAINT_SCHEDULE_SQL, schedule.getTenantId(), schedule.getTimeZone().getId(), schedule.getName(), schedule.getRecurrenceRule(), schedule.getDuration(), now, id);
		jdbcTemplate.update(DELETE_TEST_DEF_SCHEDULE_SQL, id);
		
		for (long testDefId : testDefIds) {
			jdbcTemplate.update(INSERT_TEST_DEF_SCHEDULE_SQL, testDefId, id);
		}
	}

	@Override
	public void deleteById(long scheduleId) {
		this.jdbcTemplate.update("call delete_schedule(?)", scheduleId);
	}
	
	@Override
	public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers,
			int workerNum, int maxRows, long maxLastModified) {
		List<MaintScheduleView> schedules = getMaintenanceSchedules.call(totalWorkers, workerNum, maxRows, maxLastModified, null, null, null);
		return schedules;
	}

	@Override
	public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers,
			int workerNum, int maxRows, long maxLastModified, long minScheduleId, long minTestDefId) {
		List<MaintScheduleView> schedules = getMaintenanceSchedules.call(totalWorkers, workerNum, maxRows, maxLastModified, minScheduleId, minTestDefId, null);
		return schedules;
	}

	@Override
	public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers,
			int workerNum, int maxRows, long maxLastModified, long minLastModified) {
		List<MaintScheduleView> schedules = getMaintenanceSchedules.call(totalWorkers, workerNum, maxRows, maxLastModified, null, null, minLastModified);
		return schedules;
	}

	@Override
	public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers,
			int workerNum, int maxRows, long maxLastModified, long minScheduleId, long minTestDefId, long minlastModified) {
		List<MaintScheduleView> schedules = getMaintenanceSchedules.call(totalWorkers, workerNum, maxRows, maxLastModified, minScheduleId, minTestDefId, minlastModified);
		return schedules;
	}

	private static class ScheduleMapper implements RowMapper<Schedule> {
		private TimeZoneDao timeZoneDao;
		
		public ScheduleMapper (TimeZoneDao timeZoneDao) {
			this.timeZoneDao = timeZoneDao;
		}

		@Override
		public Schedule mapRow(ResultSet rs, int numRow) throws SQLException {
			TimeZone timeZone = timeZoneDao.getById(rs.getLong("timezone_id"));
			Schedule schedule = Schedule.create()
					.withScheduleId(rs.getLong("schedule_id"))
					.withTenantId(rs.getLong("tenant_id"))
					.withTimeZone(timeZone)
					.withName(rs.getString("name"))
					.withRecurrenceRule(rs.getString("rrule"))
					.withDuration(rs.getInt("duration"))
					.withMaintenance(rs.getBoolean("is_maintenance"))
					.withLastModified(rs.getTimestamp("last_modified").getTime())
					.build();
			return schedule;
		}
		
	}
	
	private static class GetMaintenanceSchedules extends StoredProcedure {
		
		public GetMaintenanceSchedules(JdbcTemplate jdbcTemplate, TimeZoneDao timeZoneDao) {
			super(jdbcTemplate, "get_maint_schedules");
			
			declareParameter(new SqlReturnResultSet("rs", new MaintScheduleMapper(timeZoneDao)));
			declareParameter(new SqlParameter("total_workers", Types.SMALLINT));
			declareParameter(new SqlParameter("worker_num", Types.SMALLINT));
			declareParameter(new SqlParameter("max_rows", Types.SMALLINT));
			declareParameter(new SqlParameter("max_last_modified", Types.TIMESTAMP));
			declareParameter(new SqlParameter("min_schedule_id", Types.BIGINT));
			declareParameter(new SqlParameter("min_test_def_id", Types.BIGINT));
			declareParameter(new SqlParameter("min_last_modified", Types.TIMESTAMP));
			
			compile();
		}
		
		public synchronized List<MaintScheduleView> call(int totalWorkers, int workerNum, int maxRows, long maxLastModified, Long minScheduleId, Long minTestDefId, Long minLastModified) {
			Map<String, Object> inParameters = new HashMap<>();
			Timestamp maxLastMod = new Timestamp(maxLastModified);
			Timestamp minlastMod = null;
			if (minLastModified != null) {
				minlastMod = new Timestamp(minLastModified);
			}
			inParameters.put("total_workers", totalWorkers);
			inParameters.put("worker_num", workerNum);
			inParameters.put("max_rows", maxRows);
			inParameters.put("max_last_modified", maxLastMod);
			inParameters.put("min_schedule_id", minScheduleId);
			inParameters.put("min_test_def_id", minTestDefId);
			inParameters.put("min_last_modified", minlastMod);
			execute(inParameters);

			Map<String, Object> resultMap = execute(inParameters);
			@SuppressWarnings("unchecked")
			List<MaintScheduleView> schedules = (List<MaintScheduleView>) resultMap.get("rs");

			return schedules;
		}

	}
	
	
	private static class MaintScheduleMapper implements RowMapper<MaintScheduleView> {
		private TimeZoneDao timeZoneDao;
		
		public MaintScheduleMapper (TimeZoneDao timeZoneDao) {
			this.timeZoneDao = timeZoneDao;
		}

		@Override
		public MaintScheduleView mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			TimeZone timeZone = timeZoneDao.getById(rs.getLong("timezone_id"));
			MaintScheduleView schedule = MaintScheduleView.create()
					.withScheduleId(rs.getLong("schedule_id"))
					.withTenantId(rs.getLong("tenant_id"))
					.withTimeZone(timeZone)
					.withRecurrenceRule(rs.getString("rrule"))
					.withDuration(rs.getInt("duration"))
					.withLastModified(rs.getTimestamp("last_modified").getTime())
					.withActive(rs.getBoolean("active"))
					.withDeleted(rs.getBoolean("deleted"))
					.withName(rs.getString("name"))
					.withTestDefinition(rs.getLong("test_definition_id"))
					.build();
				
			return schedule;
		}
	}

	private static final class ScheduleProxyMapper implements RowMapper<ScheduleProxy> {
	    public ScheduleProxy mapRow(ResultSet rs, int rowNum) throws SQLException {
	        ScheduleProxy scriptProxy = ScheduleProxy.create()
	        		.withId(rs.getLong("schedule_id"))
	        		.withName(rs.getString("name"))
	        		.withLastModified(rs.getTimestamp("last_modified").getTime())
	        		.build();
	        return scriptProxy;
	    }
	}
	

}

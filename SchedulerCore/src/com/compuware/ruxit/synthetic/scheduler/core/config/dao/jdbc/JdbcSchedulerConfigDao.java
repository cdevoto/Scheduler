package com.compuware.ruxit.synthetic.scheduler.core.config.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

import com.compuware.ruxit.synthetic.scheduler.core.config.dao.SchedulerConfigDao;
import com.compuware.ruxit.synthetic.scheduler.core.config.dao.model.SchedulerConfig;

@Repository
public class JdbcSchedulerConfigDao implements SchedulerConfigDao {
	
	private SchedulerConfig.Id schedulerId;
	private JdbcTemplate jdbcTemplate;
	private GetScheduler getScheduler;

	private volatile SchedulerConfig schedulerConfig;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.getScheduler = new GetScheduler(jdbcTemplate);
        if (schedulerId != null) {
        	initialize();
        }
    }
	
	@Autowired
	public void setSchedulerId (SchedulerConfig.Id schedulerId) {
		this.schedulerId = schedulerId;
        if (jdbcTemplate != null) {
        	initialize();
        }
	}
	
	private void initialize () {
		this.schedulerConfig = getScheduler.call(schedulerId.get());
	}

	@Override
	public SchedulerConfig get () {
		return this.schedulerConfig;
	}
	
	private static final class SchedulerConfigMapper implements RowMapper<SchedulerConfig> {
        public SchedulerConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
        	SchedulerConfig schedulerConfig = SchedulerConfig.create()
            		.withId(rs.getLong("scheduler_id"))
            		.withWorkerNumber(rs.getInt("worker_num"))
            		.withTotalWorkers(rs.getInt("total_workers"))
            		.build();
            return schedulerConfig;
        }
	}
	
	private static class GetScheduler extends StoredProcedure {
		
		public GetScheduler(JdbcTemplate jdbcTemplate) {
			
			super(jdbcTemplate, "get_scheduler");
			declareParameter(new SqlReturnResultSet("rs", new SchedulerConfigMapper()));
			declareParameter(new SqlParameter("schedulerId", Types.BIGINT));
			
			compile();
		}
		
		public SchedulerConfig call(long schedulerId) {
			Map<String, Object> inParameters = new HashMap<>();
			inParameters.put("schedulerId", schedulerId);
			
			Map<String, Object> resultMap = execute(inParameters);
			@SuppressWarnings("unchecked")
			List<SchedulerConfig> schedulerConfigs = (List<SchedulerConfig>) resultMap.get("rs");
			if (schedulerConfigs.size() != 1) {
				throw new EmptyResultDataAccessException(1);
			}
			return schedulerConfigs.get(0);
		}

	}
	
}

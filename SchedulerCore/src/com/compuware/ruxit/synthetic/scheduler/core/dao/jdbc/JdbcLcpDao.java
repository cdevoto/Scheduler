package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

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

import com.compuware.ruxit.synthetic.scheduler.core.dao.LcpDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.LcpProxy;

@Repository
public class JdbcLcpDao implements LcpDao {

	private JdbcTemplate jdbcTemplate;
	private FilterLcps filterLcps;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.filterLcps = new FilterLcps(this.jdbcTemplate);
    }

	@Override
	public List<LcpProxy> getLcps(long scriptId, long requiresF) {
		List<LcpProxy> lcps = filterLcps.call(scriptId, requiresF);
		return lcps;
	}
	
	@Override
	public LcpProxy getById(long id) {
		try {
			LcpProxy lcpProxy = this.jdbcTemplate.queryForObject(
			        "SELECT lcp_id, lcp_name FROM lcp_vw WHERE lcp_id = ?",
			        new Object[]{ id },
			        new LcpProxyMapper());
			return lcpProxy;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	@Override
	public List<LcpProxy> getByTestDefId(long testDefinitionId) {
		final String SELECT_SQL = "SELECT lcp_id, lcp_name "
				+ "FROM test_definition_lcp_vw "
				+ "WHERE test_definition_id = ?";
		List<LcpProxy> lcps = this.jdbcTemplate.query(SELECT_SQL,
		        new Object[]{ testDefinitionId},
		        new LcpProxyMapper());
		return lcps;
	}
	
	@Override
	public List<LcpProxy> getByVucId(long vucId) {
		final String SELECT_SQL = "SELECT lcp_id, lcp_name "
				+ "FROM vu_controller_lcp_vw "
				+ "WHERE vuc_id = ?";
		List<LcpProxy> lcps = this.jdbcTemplate.query(SELECT_SQL,
		        new Object[]{ vucId},
		        new LcpProxyMapper());
		return lcps;
	}

	private static class FilterLcps extends StoredProcedure {
		
		public FilterLcps(JdbcTemplate jdbcTemplate) {
			super(jdbcTemplate, "filter_lcps");
			declareParameter(new SqlReturnResultSet("rs", new LcpProxyMapper()));
			declareParameter(new SqlParameter("script_id", Types.BIGINT));
			declareParameter(new SqlParameter("requires_f", Types.BIGINT));
			compile();
		}
		
		public List<LcpProxy> call(long scriptId, long requiresF) {
			Map<String, Object> inParameters = new HashMap<>();
			inParameters.put("script_id", scriptId);
			inParameters.put("requires_f", requiresF);
			Map<String, Object> resultMap = execute(inParameters);
			@SuppressWarnings("unchecked")
			List<LcpProxy> lcps = (List<LcpProxy>) resultMap.get("rs");
			return lcps;
		}

	}
	
	private static class LcpProxyMapper implements RowMapper<LcpProxy> {

		@Override
		public LcpProxy mapRow(ResultSet rs, int rowNum) throws SQLException {
			LcpProxy lcp = LcpProxy.create()
					.withId(rs.getLong("lcp_id"))
					.withName(rs.getString("lcp_name"))
					.build();
			return lcp;
		}
		
	}

}

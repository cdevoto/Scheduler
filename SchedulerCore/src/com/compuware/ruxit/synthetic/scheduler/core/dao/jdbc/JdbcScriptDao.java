package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptTypeDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Script;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptProxy;

@Repository
public class JdbcScriptDao implements ScriptDao {

	private JdbcTemplate jdbcTemplate;
	private ScriptTypeDao scriptTypeDao;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Autowired
    public void setScriptTypeDao(ScriptTypeDao scriptTypeDao) {
        this.scriptTypeDao = scriptTypeDao;
    }

	@Override
	public Script getById(long id) {
		try {
			Script script = this.jdbcTemplate.queryForObject(
			        "SELECT script_id, script_type_id, tenant_id, name, requires_f, active, deleted, last_modified FROM script WHERE script_id = ?",
			        new Object[]{ id },
			        new ScriptMapper(scriptTypeDao));
			return script;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	public List<ScriptProxy> getAll(long tenantId) {
		List<ScriptProxy> scripts = this.jdbcTemplate.query(
		        "SELECT script_id, name, last_modified FROM script WHERE tenant_id = ? AND active = true AND deleted = false ORDER BY name ASC",
		        new Object[]{ tenantId },
		        new ScriptProxyMapper());
		return scripts;
	}

	@Override
	public Script insert(final Script script) {
		final String INSERT_SQL = "INSERT INTO script (script_type_id, tenant_id, name, requires_f) VALUES (?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"id"});
		            ps.setLong(1, script.getScriptType().getId());
		            ps.setLong(2, script.getTenantId());
		            ps.setString(3, script.getName());
		            ps.setLong(4, script.getRequiresFlags());
		            return ps;
		        }
		    },
		    keyHolder);
		Number id = keyHolder.getKey();
		Script modifiedScript = getById(id.longValue());
		return modifiedScript;
	}

	@Override
	public void deleteById(long id) {
		this.jdbcTemplate.update("call delete_script(?)", id);
	}

	private static final class ScriptProxyMapper implements RowMapper<ScriptProxy> {
	    public ScriptProxy mapRow(ResultSet rs, int rowNum) throws SQLException {
	        ScriptProxy scriptProxy = ScriptProxy.create()
	        		.withId(rs.getLong("script_id"))
	        		.withName(rs.getString("name"))
	        		.withLastModified(rs.getTimestamp("last_modified").getTime())
	        		.build();
	        return scriptProxy;
	    }
	}
	
	private static final class ScriptMapper implements RowMapper<Script> {
		private ScriptTypeDao scriptTypeDao;
		
		private ScriptMapper (ScriptTypeDao scriptTypeDao) {
			this.scriptTypeDao = scriptTypeDao;
		}
	    public Script mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	long scriptTypeId = rs.getLong("script_type_id");
	        Script script = Script.create()
	        		.withId(rs.getLong("script_id"))
	        		.withScriptType(scriptTypeDao.getById(scriptTypeId))
	        		.withTenantId(rs.getLong("tenant_id"))
	        		.withName(rs.getString("name"))
	        		.withRequiresFlags(rs.getLong("requires_f"))
	        		.withActive(rs.getBoolean("active"))
	        		.withDeleted(rs.getBoolean("deleted"))
	        		.withLastModified(rs.getTimestamp("last_modified").getTime())
	        		.build();
	        return script;
	    }
	}

}

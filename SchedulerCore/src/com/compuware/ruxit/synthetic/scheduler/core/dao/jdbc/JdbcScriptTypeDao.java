package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.compuware.ruxit.synthetic.scheduler.core.dao.ScriptTypeDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptType;

@Repository
public class JdbcScriptTypeDao implements ScriptTypeDao {

	private volatile boolean initialized;
	private Map<Long, ScriptType> scriptTypesById = new TreeMap<>();
	private List<ScriptType> scriptTypes;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public ScriptType getById(long id) {
		initialize();
		return scriptTypesById.get(id);
	}


	@Override
	public List<ScriptType> getAll() {
		initialize();
		List<ScriptType> result = new LinkedList<>(scriptTypes);
		return result;
	}
	
	private void initialize () {
		boolean init = this.initialized;
		if (init == false) {
			synchronized (this) {
				init = this.initialized;
				if (init == false) {
					List<ScriptType> scriptTypes = this.jdbcTemplate.query(
					        "SELECT script_type_id, name FROM script_type ORDER BY name ASC",
					        new Object[]{},
					        new ScriptTypeMapper());
                    this.scriptTypes = Collections.unmodifiableList(scriptTypes);
                    for (ScriptType scriptType : this.scriptTypes) {
                    	this.scriptTypesById.put(scriptType.getId(), scriptType);
                    }
                    this.scriptTypesById = Collections.unmodifiableMap(this.scriptTypesById);
					this.initialized = true;
				}
			}
		}
	}
	
	private static final class ScriptTypeMapper implements RowMapper<ScriptType> {
        public ScriptType mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScriptType scriptType = ScriptType.create()
            		.withId(rs.getLong("script_type_id"))
            		.withName(rs.getString("name"))
            		.build();
            return scriptType;
        }
	}
}

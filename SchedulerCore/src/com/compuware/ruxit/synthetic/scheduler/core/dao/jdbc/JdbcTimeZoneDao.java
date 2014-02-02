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

import com.compuware.ruxit.synthetic.scheduler.core.dao.TimeZoneDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TimeZone;

@Repository
public class JdbcTimeZoneDao implements TimeZoneDao {

	private volatile boolean initialized;
	private Map<Long, TimeZone> timeZonesById = new TreeMap<>();
	private List<TimeZone> timeZones;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public TimeZone getById(long id) {
		initialize();
		return timeZonesById.get(id);
	}

	@Override
	public List<TimeZone> getAll() {
		initialize();
		List<TimeZone> result = new LinkedList<>(timeZones);
		return result;
	}
	
	private void initialize () {
		boolean init = this.initialized;
		if (init == false) {
			synchronized (this) {
				init = this.initialized;
				if (init == false) {
					List<TimeZone> timeZones = this.jdbcTemplate.query(
					        "SELECT timezone_id, name FROM timezone ORDER BY name ASC",
					        new Object[]{},
					        new TimeZoneMapper());
                    this.timeZones = Collections.unmodifiableList(timeZones);
                    for (TimeZone timeZone : this.timeZones) {
                    	this.timeZonesById.put(timeZone.getId(), timeZone);
                    }
                    this.timeZonesById = Collections.unmodifiableMap(this.timeZonesById);
					this.initialized = true;
				}
			}
		}
	}
	
	private static final class TimeZoneMapper implements RowMapper<TimeZone> {
        public TimeZone mapRow(ResultSet rs, int rowNum) throws SQLException {
        	TimeZone timeZone = TimeZone.create()
            		.withId(rs.getLong("timezone_id"))
            		.withName(rs.getString("name"))
            		.build();
            return timeZone;
        }
	}
}

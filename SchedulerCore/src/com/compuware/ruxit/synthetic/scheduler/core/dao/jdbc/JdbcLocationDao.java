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

import com.compuware.ruxit.synthetic.scheduler.core.dao.LocationDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Location;

@Repository
public class JdbcLocationDao implements LocationDao {

	private volatile boolean initialized;
	private Map<Long, Location> locationsById = new TreeMap<>();
	private List<Location> locations;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public Location getById(long id) {
		initialize();
		return locationsById.get(id);
	}


	@Override
	public List<Location> getAll() {
		initialize();
		List<Location> result = new LinkedList<>(locations);
		return result;
	}
	
	private void initialize () {
		boolean init = this.initialized;
		if (init == false) {
			synchronized (this) {
				init = this.initialized;
				if (init == false) {
					List<Location> locations = this.jdbcTemplate.query(
					        "SELECT location_id, name FROM location ORDER BY name ASC",
					        new Object[]{},
					        new LocationMapper());
                    this.locations = Collections.unmodifiableList(locations);
                    for (Location scriptType : this.locations) {
                    	this.locationsById.put(scriptType.getId(), scriptType);
                    }
                    this.locationsById = Collections.unmodifiableMap(this.locationsById);
					this.initialized = true;
				}
			}
		}
	}
	
	private static final class LocationMapper implements RowMapper<Location> {
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location scriptType = Location.create()
            		.withId(rs.getLong("script_type_id"))
            		.withName(rs.getString("name"))
            		.build();
            return scriptType;
        }
	}
}

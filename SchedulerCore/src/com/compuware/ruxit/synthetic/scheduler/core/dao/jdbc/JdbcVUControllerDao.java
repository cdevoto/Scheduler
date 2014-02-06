package com.compuware.ruxit.synthetic.scheduler.core.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.compuware.ruxit.synthetic.scheduler.core.dao.VUControllerDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.VUController;

@Repository
public class JdbcVUControllerDao implements VUControllerDao {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	public List<Long> getAllIds () {
		List<Long> vucIds = jdbcTemplate.query("SELECT vuc_id FROM vu_controller ORDER BY vuc_id", 
				new Object [] {}, 
				new VUControllerIdMapper());
		return vucIds;
	}

	public List<VUController> getAll () {
		List<VUController> vucs = jdbcTemplate.query("SELECT vuc_id, supports_f, location_id FROM vu_controller ORDER BY vuc_id", 
				new Object [] {}, 
				new VUControllerMapper());
		return vucs;
	}

	public VUController getById(final long id) {
		try {
			VUController vuc = jdbcTemplate.queryForObject(
			        "SELECT vuc_id, supports_f, location_id FROM vu_controller WHERE vuc_id = ?",
			        new Object[]{ id },
			        new VUControllerMapper());
			
			return vuc;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
    }
 
	private static class VUControllerMapper implements RowMapper<VUController> {

		@Override
		public VUController mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			VUController vuc = VUController.create()
					.withId(rs.getLong("vuc_id"))
					.withLocationId(rs.getLong("location_id"))
					.withSupportsF(rs.getLong("supports_f"))
					.build();
			return vuc;
		}
		
	}
	
	private static class VUControllerIdMapper implements RowMapper<Long> {

		@Override
		public Long mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			return rs.getLong("vuc_id");
		}
		
	}

}

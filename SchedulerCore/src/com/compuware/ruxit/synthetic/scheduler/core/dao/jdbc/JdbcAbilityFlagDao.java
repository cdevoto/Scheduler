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

import com.compuware.ruxit.synthetic.scheduler.core.dao.AbilityFlagDao;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlagLevel;
import com.compuware.ruxit.synthetic.scheduler.core.util.BitwiseUtil;
import com.compuware.ruxit.synthetic.scheduler.core.util.Pair;

@Repository
public class JdbcAbilityFlagDao implements AbilityFlagDao {

	private volatile boolean initialized;
	private Map<Long, AbilityFlag> abilityFlagById = new TreeMap<>();
	private Map<Long, AbilityFlag> abilityFlagByMask = new TreeMap<>();
	private Map<String, AbilityFlag> abilityFlagByDescription = new TreeMap<>();
	private Map<AbilityFlagLevel, List<AbilityFlag>> abilityFlagsByLevel = new TreeMap<>();
	private Map<Long, List<AbilityFlag>> abilityFlagsByScriptType = new TreeMap<>();
	private List<AbilityFlag> abilityFlags;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public AbilityFlag getById(long id) {
		initialize();
		return abilityFlagById.get(id);
	}

	@Override
	public AbilityFlag getByDescription(String name) {
		initialize();
		return abilityFlagByDescription.get(name);
	}
	
	@Override
	public List<AbilityFlag> getByBitMapField (long bitMap) {
		List<AbilityFlag> result = null;
		if (bitMap == 0L) {
			result = Collections.emptyList();
			return result;
		}
		initialize();
		List<Long> masks = BitwiseUtil.getMasks(bitMap);
		result = new LinkedList<>();
		for (Long mask : masks) {
			AbilityFlag abilityFlag = abilityFlagByMask.get(mask);
			result.add(abilityFlag);
		}
		return result;
	}

	@Override
	public List<AbilityFlag> getByLevel(AbilityFlagLevel level) {
		initialize();
		List<AbilityFlag> result = abilityFlagsByLevel.get(level);
		if (result == null) {
			result = Collections.emptyList();
		} else {
			result = new LinkedList<>(result);
		}
		return result;
	}

	@Override
	public List<AbilityFlag> getAll() {
		initialize();
		List<AbilityFlag> result = new LinkedList<>(abilityFlags);
		return result;
	}
	
	@Override
	public List<AbilityFlag> getByScriptType(long scriptType) {
		initialize();
		List<AbilityFlag> result = abilityFlagsByScriptType.get(scriptType);
		if (result == null) {
			result = Collections.emptyList();
		} else {
			result = new LinkedList<>(result);
		}
		return result;
	}

	private void initialize () {
		boolean init = this.initialized;
		if (init == false) {
			synchronized (this) {
				init = this.initialized;
				if (init == false) {
					List<AbilityFlag> abilityFlags = this.jdbcTemplate.query(
					        "SELECT ability_flag_id, ability_flag_level_id, mask, description FROM ability_flag",
					        new Object[]{},
					        new AbilityFlagMapper());
                    this.abilityFlags = Collections.unmodifiableList(abilityFlags);
                    for (AbilityFlag abilityFlag : this.abilityFlags) {
                    	this.abilityFlagById.put(abilityFlag.getId(), abilityFlag);
                    	this.abilityFlagByMask.put(abilityFlag.getMask(), abilityFlag);
                    	this.abilityFlagByDescription.put(abilityFlag.getDescription(), abilityFlag);
                    	List<AbilityFlag> abilityFlagList = abilityFlagsByLevel.get(abilityFlag.getLevel());
                    	if (abilityFlagList == null) {
                    		abilityFlagList = new LinkedList<>();
                    		abilityFlagsByLevel.put(abilityFlag.getLevel(), abilityFlagList);
                    	}
                    	abilityFlagList.add(abilityFlag);
                    }
                    this.abilityFlagsByLevel = Collections.unmodifiableMap(this.abilityFlagsByLevel);
                    this.abilityFlagByDescription = Collections.unmodifiableMap(this.abilityFlagByDescription);
                    this.abilityFlagById = Collections.unmodifiableMap(this.abilityFlagById);
                    this.abilityFlagByMask = Collections.unmodifiableMap(this.abilityFlagByMask);
                    
					List<Pair<Long>> scriptTypeAbilityFlags = this.jdbcTemplate.query(
					        "SELECT ability_flag_id, script_type_id FROM script_type_ability_flag",
					        new Object[]{},
					        new ScriptTypeAbilityFlagMapper());
					for (Pair<Long> pair : scriptTypeAbilityFlags) {
			        	long abilityFlagId = pair.getFirst();
			        	long scriptTypeId = pair.getLast();
						AbilityFlag abilityFlag = this.abilityFlagById.get(abilityFlagId);
						if (abilityFlag != null) {
							List<AbilityFlag> flagList = abilityFlagsByScriptType.get(scriptTypeId);
							if (flagList == null) {
								flagList = new LinkedList<>();
								abilityFlagsByScriptType.put(scriptTypeId, flagList);
							}
							flagList.add(abilityFlag);
						}
					}
                    this.abilityFlagsByScriptType = Collections.unmodifiableMap(this.abilityFlagsByScriptType);
					this.initialized = true;
				}
			}
		}
	}
	
	private static final class AbilityFlagMapper implements RowMapper<AbilityFlag> {
        public AbilityFlag mapRow(ResultSet rs, int rowNum) throws SQLException {
            AbilityFlag abilityFlag = AbilityFlag.create()
            		.withId(rs.getLong("ability_flag_id"))
            		.withLevel(rs.getLong("ability_flag_level_id"))
            		.withMask(rs.getLong("mask"))
            		.withDescription(rs.getString("description"))
            		.build();
            return abilityFlag;
        }
	}
	
	private static final class ScriptTypeAbilityFlagMapper implements RowMapper<Pair<Long>> {
        public Pair<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
        	long abilityFlagId = rs.getLong("ability_flag_id");
        	long scriptTypeId = rs.getLong("script_type_id");
            return new Pair<Long>(abilityFlagId, scriptTypeId);
        }
	}
	
}

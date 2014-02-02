package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlag;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.AbilityFlagLevel;

public interface AbilityFlagDao {

    public AbilityFlag getById (long id);
	public AbilityFlag getByDescription(String name);
    public List<AbilityFlag> getByLevel (AbilityFlagLevel level);
    public List<AbilityFlag> getAll ();
	public List<AbilityFlag> getByScriptType(long scriptType);
	public List<AbilityFlag> getByBitMapField(long bitMap);

}

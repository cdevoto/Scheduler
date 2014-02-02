package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptType;

public interface ScriptTypeDao {

    public ScriptType getById (long id);
    public List<ScriptType> getAll ();

}

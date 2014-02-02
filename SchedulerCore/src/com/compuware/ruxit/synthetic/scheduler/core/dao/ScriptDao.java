package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Script;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScriptProxy;

public interface ScriptDao {

    public Script getById (long id);
    public List<ScriptProxy> getAll (long tenantId);
    public Script insert(Script script);
    public void deleteById (long id);

}

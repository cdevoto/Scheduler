package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.TimeZone;

public interface TimeZoneDao {

    public TimeZone getById (long id);
    public List<TimeZone> getAll ();

}

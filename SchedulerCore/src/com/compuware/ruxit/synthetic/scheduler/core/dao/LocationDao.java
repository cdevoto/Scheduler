package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Location;

public interface LocationDao {

    public Location getById (long id);
    public List<Location> getAll ();

}

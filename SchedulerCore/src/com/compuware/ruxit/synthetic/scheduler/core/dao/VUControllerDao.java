package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.VUController;

public interface VUControllerDao {

    public VUController getById (long id);
    public List<VUController> getAll ();
    public List<Long> getAllIds ();

}

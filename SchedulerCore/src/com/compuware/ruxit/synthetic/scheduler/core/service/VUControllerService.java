package com.compuware.ruxit.synthetic.scheduler.core.service;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.ui.model.UIVUController;

public interface VUControllerService {

	public List<UIVUController> getVUControllers();
	public UIVUController getVUController(long vucId);
	
}

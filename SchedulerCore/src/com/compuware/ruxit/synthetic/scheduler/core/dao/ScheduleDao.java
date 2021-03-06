package com.compuware.ruxit.synthetic.scheduler.core.dao;

import java.util.List;

import com.compuware.ruxit.synthetic.scheduler.core.dao.model.MaintScheduleView;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.Schedule;
import com.compuware.ruxit.synthetic.scheduler.core.dao.model.ScheduleProxy;

public interface ScheduleDao {

    public List<Schedule> getByTestDefId(long testDefinitionId);
    public void deleteById(long scheduleId);
    public long insert(Schedule schedule, List<Long> testDefIds);
	public void update(Schedule schedule, List<Long> testDefIds);
    
	public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers, int workerNum, int maxRows, long maxLastModified);
    public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers, int workerNum, int maxRows, long maxLastModified, long minScheduleId, long minTestDefId);
	public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers,
			int workerNum, int maxRows, long maxLastModified, long minlastModified);
	public List<MaintScheduleView> getMaintenanceSchedules(int totalWorkers,
			int workerNum, int maxRows, long maxLastModified, long minScheduleId, long minTestDefId,
			long minLastModified);

	public Schedule getById(long scheduleId);
	public List<ScheduleProxy> getAll(long tenantId);
	public List<ScheduleProxy> getAll(long tenantId, boolean isMaintenance);
}

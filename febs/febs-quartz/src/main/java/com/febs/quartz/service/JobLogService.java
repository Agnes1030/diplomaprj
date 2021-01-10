package com.febs.quartz.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.quartz.domain.JobLog;

public interface JobLogService extends IService<JobLog>{

	List<JobLog> findAllJobLogs(JobLog jobLog);

	void saveJobLog(JobLog log);

	void deleteBatch(String jobLogIds);
}

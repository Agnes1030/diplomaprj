package com.febs.system.service;

import java.util.List;

import com.febs.common.service.IService;
import com.febs.system.domain.SysLog;

public interface LogService extends IService<SysLog> {
	
	List<SysLog> findAllLogs(SysLog log);
	
	void deleteLogs(String logIds);
}

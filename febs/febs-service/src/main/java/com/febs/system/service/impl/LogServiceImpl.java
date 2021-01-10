package com.febs.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.febs.common.service.impl.BaseService;
import com.febs.system.dao.LogMapper;
import com.febs.system.domain.SysLog;
import com.febs.system.service.LogService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends BaseService<SysLog> implements LogService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	LogMapper logMapper;

	@Override
	public List<SysLog> findAllLogs(SysLog log) {
		try {
			Example example = new Example(SysLog.class);
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(log.getUsername())) {
				criteria.andCondition("username=", log.getUsername().toLowerCase());
			}
			if (StringUtils.isNotBlank(log.getOperation())) {
				criteria.andCondition("operation like", "%" + log.getOperation() + "%");
			}
			if (StringUtils.isNotBlank(log.getTimeField())) {
				String[] timeArr = log.getTimeField().split("~");
				criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') >=", timeArr[0]);
				criteria.andCondition("date_format(CREATE_TIME,'%Y-%m-%d') <=", timeArr[1]);
			}
			example.setOrderByClause("create_time desc");
			return this.selectByExample(example);
		} catch (Exception e) {
			logger.error("获取系统日志失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void deleteLogs(String logIds) {
		List<String> list = Arrays.asList(logIds.split(","));
		this.batchDelete(list, "id", SysLog.class);
	}

}

package com.febs.quartz.dao;

import java.util.List;

import com.febs.common.config.MyMapper;
import com.febs.quartz.domain.Job;

public interface JobMapper extends MyMapper<Job> {

	List<Job> queryList();
}
package com.febs.quartz.service.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.febs.common.annotation.CronTag;
import com.febs.common.service.impl.BaseService;
import com.febs.quartz.dao.JobMapper;
import com.febs.quartz.domain.Job;
import com.febs.quartz.service.JobService;
import com.febs.quartz.util.ScheduleUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("JobService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl extends BaseService<Job> implements JobService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private JobMapper jobMapper;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init() {
		List<Job> scheduleJobList = this.findAllJobs();
		// 如果不存在，则创建
		scheduleJobList.forEach(scheduleJob -> {
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
			if (cronTrigger == null) {
				ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
			} else {
				ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
			}
		});
	}

	@Override
	public Job findJob(Long jobId) {
		return this.selectByKey(jobId);
	}

	@Override
	public List<Job> findAllJobs() {
		return this.jobMapper.queryList();
	}

	@Override
	public List<Job> findAllJobs(Job job) {
		try {
			Example example = new Example(Job.class);
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(job.getBeanName())) {
				criteria.andCondition("bean_name=", job.getBeanName());
			}
			if (StringUtils.isNotBlank(job.getMethodName())) {
				criteria.andCondition("method_name=", job.getMethodName());
			}
			if (StringUtils.isNotBlank(job.getStatus())) {
				criteria.andCondition("status=", Long.valueOf(job.getStatus()));
			}
			example.setOrderByClause("job_id");
			return this.selectByExample(example);
		} catch (Exception e) {
			log.error("获取任务失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void addJob(Job job) {
		job.setCreateTime(new Date());
		job.setStatus(Job.ScheduleStatus.PAUSE.getValue());
		this.save(job);
		ScheduleUtils.createScheduleJob(scheduler, job);
	}

	@Override
	@Transactional
	public void updateJob(Job job) {
		ScheduleUtils.updateScheduleJob(scheduler, job);
		this.updateNotNull(job);
	}

	@Override
	@Transactional
	public void deleteBatch(String jobIds) {
		List<String> list = Arrays.asList(jobIds.split(","));
		list.forEach(jobId -> ScheduleUtils.deleteScheduleJob(scheduler, Long.valueOf(jobId)));
		this.batchDelete(list, "jobId", Job.class);
	}

	@Override
	@Transactional
	public int updateBatch(String jobIds, String status) {
		List<String> list = Arrays.asList(jobIds.split(","));
		Example example = new Example(Job.class);
		example.createCriteria().andIn("jobId", list);
		Job job = new Job();
		job.setStatus(status);
		return this.jobMapper.updateByExampleSelective(job, example);
	}

	@Override
	@Transactional
	public void run(String jobIds) {
		String[] list = jobIds.split(",");
		Arrays.stream(list).forEach(jobId -> ScheduleUtils.run(scheduler, this.findJob(Long.valueOf(jobId))));
	}

	@Override
	@Transactional
	public void pause(String jobIds) {
		String[] list = jobIds.split(",");
		Arrays.stream(list).forEach(jobId -> ScheduleUtils.pauseJob(scheduler, Long.valueOf(jobId)));
		this.updateBatch(jobIds, Job.ScheduleStatus.PAUSE.getValue());
	}

	@Override
	@Transactional
	public void resume(String jobIds) {
		String[] list = jobIds.split(",");
		Arrays.stream(list).forEach(jobId -> ScheduleUtils.resumeJob(scheduler, Long.valueOf(jobId)));
		this.updateBatch(jobIds, Job.ScheduleStatus.NORMAL.getValue());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Job> getSysCronClazz(Job job) {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().forPackages("cc.febs.quartz.task").addScanners(new SubTypesScanner()) // 添加子类扫描工具
						.addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
						.addScanners(new MethodAnnotationsScanner()) // 添加 方法注解扫描工具
						.addScanners(new MethodParameterScanner()) // 添加方法参数扫描工具
		);
		List<Job> jobList = new ArrayList<>();
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(CronTag.class);

		for (Class cls : annotated) {
			String clsSimpleName = cls.getSimpleName();
			Method[] methods = cls.getDeclaredMethods();
			for (Method method : methods) {
				Job job1 = new Job();
				String methodName = method.getName();
				Parameter[] methodParameters = method.getParameters();
				String params = String.format("%s(%s)", methodName,
						Arrays.stream(methodParameters)
								.map(item -> item.getType().getSimpleName() + " " + item.getName())
								.collect(Collectors.joining(", ")));

				job1.setBeanName(StringUtils.uncapitalize(clsSimpleName));
				job1.setMethodName(methodName);
				job1.setParams(params);
				jobList.add(job1);
			}
		}
		return jobList;
	}

}

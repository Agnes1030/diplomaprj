package com.febs.quartz.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.febs.common.service.IService;
import com.febs.quartz.domain.Job;

import java.util.List;

@CacheConfig(cacheNames = "JobService")
public interface JobService extends IService<Job> {

    Job findJob(Long jobId);

    List<Job> findAllJobs();

    List<Job> findAllJobs(Job job);

    void addJob(Job job);

    void updateJob(Job job);

    void deleteBatch(String jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);

    @Cacheable(key = "#p0")
    List<Job> getSysCronClazz(Job job);
}

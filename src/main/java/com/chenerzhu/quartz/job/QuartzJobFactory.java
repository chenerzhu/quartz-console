package com.chenerzhu.quartz.job;

import com.chenerzhu.quartz.entity.ScheduleJob;
import com.chenerzhu.quartz.executor.IJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class QuartzJobFactory implements Job {

    public static Set<ScheduleJob> jobSet = new TreeSet();
    @Autowired
    private IJobExecutor jobExecutor;

    // simulate data from db
    public static Set<ScheduleJob> getInitAllJobs() {
        return jobSet;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        //log.info("execute job:{}", scheduleJob.getJobName());
        jobExecutor.jobHandle(scheduleJob);
    }

}

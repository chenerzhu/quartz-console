package com.chenerzhu.quartz.service.impl;

import com.chenerzhu.quartz.entity.ScheduleJob;
import com.chenerzhu.quartz.job.QuartzJobFactory;
import com.chenerzhu.quartz.service.IScheduleJobService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * job service
 *
 * @author Jverson
 * @date Dec 9, 2017 1:10:47 PM
 */
@Slf4j
@Service
public class ScheduleJobService implements IScheduleJobService {

    @Resource
    @Qualifier("scheduler")
    private Scheduler scheduler;
    @Override
    public List<ScheduleJob> getAllJobList() {
        List<ScheduleJob> jobList = Lists.newArrayList();
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeySet) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    ScheduleJob scheduleJob = new ScheduleJob();
                    this.wrapScheduleJob(scheduleJob, scheduler, jobKey, trigger);
                    jobList.add(scheduleJob);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    @Override
    public List<ScheduleJob> getRunningJobList() throws SchedulerException {
        List<JobExecutionContext> executingJobList = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJob> jobList = new ArrayList<>(executingJobList.size());
        for (JobExecutionContext executingJob : executingJobList) {
            ScheduleJob scheduleJob = new ScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            this.wrapScheduleJob(scheduleJob, scheduler, jobKey, trigger);
            jobList.add(scheduleJob);
        }
        return jobList;
    }

    @Override
    public void saveOrupdate(ScheduleJob scheduleJob) throws Exception {
        Preconditions.checkNotNull(scheduleJob, "job is null");
        if (StringUtils.isEmpty(scheduleJob.getJobId())) {
            addJob(scheduleJob);
        } else {
            updateJob(scheduleJob);
        }
    }
    @Override
    public void addJob(ScheduleJob scheduleJob) throws Exception {
        checkNotNull(scheduleJob);
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger != null) {
            throw new Exception("job already exists!");
        }

        // simulate job info db persist operation
        scheduleJob.setJobId(String.valueOf(QuartzJobFactory.jobSet.size() + 1));
        QuartzJobFactory.jobSet.add(scheduleJob);

        JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).withSchedule(cronScheduleBuilder).build();

        scheduler.scheduleJob(jobDetail, trigger);

    }

    @Override
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
        checkNotNull(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
        checkNotNull(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        checkNotNull(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);
    }

    @Override
    public void runJobOnce(ScheduleJob scheduleJob) throws SchedulerException {
        checkNotNull(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    @Override
    public void updateJob(ScheduleJob scheduleJob) throws Exception {
        deleteJob(scheduleJob);
        addJob(scheduleJob);
        /*checkNotNull(scheduleJob);
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, cronTrigger);*/
    }

    private void wrapScheduleJob(ScheduleJob scheduleJob, Scheduler scheduler, JobKey jobKey, Trigger trigger) {
        try {
            scheduleJob.setJobName(jobKey.getName());
            scheduleJob.setJobGroup(jobKey.getGroup());

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            ScheduleJob job = (ScheduleJob) jobDetail.getJobDataMap().get("scheduleJob");
            scheduleJob.setDesc(job.getDesc());
            scheduleJob.setJobId(job.getJobId());
            scheduleJob.setJobType(job.getJobType());
            scheduleJob.setContent(job.getContent());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            scheduleJob.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                scheduleJob.setCronExpression(cronExpression);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void checkNotNull(ScheduleJob scheduleJob) {
        Preconditions.checkNotNull(scheduleJob, "job is null");
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobName()), "jobName is null");
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobGroup()), "jobGroup is null");
    }

    @Override
    public SchedulerMetaData getMetaData() throws SchedulerException {
        SchedulerMetaData metaData = scheduler.getMetaData();
        return metaData;
    }


}

package com.chenerzhu.quartz.service;

import com.chenerzhu.quartz.entity.ScheduleJob;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;

import java.util.List;

/**
 * @author chenerzhu
 * @create 2018-10-01 21:38
 **/
public interface IScheduleJobService {
    List<ScheduleJob> getAllJobList();

    List<ScheduleJob> getRunningJobList() throws SchedulerException;

    void saveOrupdate(ScheduleJob scheduleJob) throws Exception;

    void addJob(ScheduleJob scheduleJob) throws Exception;

    void pauseJob(ScheduleJob scheduleJob) throws SchedulerException;

    void resumeJob(ScheduleJob scheduleJob) throws SchedulerException;

    void deleteJob(ScheduleJob scheduleJob) throws SchedulerException;

    void runJobOnce(ScheduleJob scheduleJob) throws SchedulerException;

    void updateJob(ScheduleJob scheduleJob) throws Exception;

    SchedulerMetaData getMetaData() throws SchedulerException;
}
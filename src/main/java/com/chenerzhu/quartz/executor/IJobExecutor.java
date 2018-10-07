package com.chenerzhu.quartz.executor;

import com.chenerzhu.quartz.entity.ScheduleJob;

/**
 * @author chenerzhu
 * @create 2018-10-01 13:34
 **/
public interface IJobExecutor {
    void jobHandle(ScheduleJob scheduleJob);
}

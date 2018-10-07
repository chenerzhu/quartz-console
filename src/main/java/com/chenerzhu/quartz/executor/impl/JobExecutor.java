package com.chenerzhu.quartz.executor.impl;

import com.chenerzhu.quartz.entity.ScheduleJob;
import com.chenerzhu.quartz.executor.IJobExecutor;
import com.chenerzhu.quartz.service.IJavaJobService;
import com.chenerzhu.quartz.service.IDynamicJobService;
import com.chenerzhu.quartz.service.IShellJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenerzhu
 * @create 2018-10-01 13:34
 **/
@Slf4j
@Component
public class JobExecutor implements IJobExecutor {
    @Autowired
    private IJavaJobService javaJobService;
    @Autowired
    private IDynamicJobService dynamicJobService;
    @Autowired
    private IShellJobService shellJobService;

    @Override
    public void jobHandle(ScheduleJob scheduleJob) {
        //0/5 * * * * ?
        switch (scheduleJob.getJobType()) {
            case "java":
                javaJob(scheduleJob);
                break;
            case "dynamic":
                dynamicJob(scheduleJob);
                break;
            case "shell":
                shellJob(scheduleJob);
                break;
            default:
                new Exception("type error!");
        }
    }

    private void dynamicJob(ScheduleJob scheduleJob) {
        log.info("dynamicJob:{}", scheduleJob.getDesc());
        dynamicJobService.dynamicJob(scheduleJob);
    }

    private void javaJob(ScheduleJob scheduleJob) {
        log.info("javaJob:{}", scheduleJob.getDesc());
        if (scheduleJob.getJobName().equalsIgnoreCase("test")) {
            javaJobService.doSomething();
        } else {
            //......
        }
    }

    private void shellJob(ScheduleJob scheduleJob) {
        log.info("shellJob:{}", scheduleJob.getDesc());
        shellJobService.doCommand(scheduleJob);
    }
}
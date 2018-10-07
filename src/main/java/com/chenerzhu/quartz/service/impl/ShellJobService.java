package com.chenerzhu.quartz.service.impl;

import com.chenerzhu.quartz.entity.ExecuteResult;
import com.chenerzhu.quartz.entity.ScheduleJob;
import com.chenerzhu.quartz.executor.IShellExecutor;
import com.chenerzhu.quartz.service.IShellJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenerzhu
 * @create 2018-10-02 0:44
 **/
@Slf4j
@Service
public class ShellJobService implements IShellJobService {
    @Autowired
    private IShellExecutor shellExecutor;

    @Override
    public void doCommand(ScheduleJob scheduleJob) {
        try {
            ExecuteResult executeResult = shellExecutor.executeCommand(scheduleJob.getContent(), 10 * 1000);
            log.info("executeResult:{}", executeResult.toString());
        } catch (Exception e) {
            log.error("shell job error! jobName:{}", scheduleJob.getJobName(), e);
        }
    }
}
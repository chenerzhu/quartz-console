package com.chenerzhu.quartz.service;

import com.chenerzhu.quartz.entity.ScheduleJob;

/**
 * @author chenerzhu
 * @create 2018-10-01 22:00
 **/
public interface IShellJobService {
    void doCommand(ScheduleJob content);
}
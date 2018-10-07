package com.chenerzhu.quartz.controller;

import com.chenerzhu.quartz.entity.Result;
import com.chenerzhu.quartz.entity.ScheduleJob;
import com.chenerzhu.quartz.service.IScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author chenerzhu
 * @create 2018-10-01 21:34
 **/
@RestController
@RequestMapping("/api")
@Slf4j
public class QuartzConsoleController {
    @Autowired
    private IScheduleJobService scheduleJobService;

    @RequestMapping("/metaData")
    public Object metaData() throws SchedulerException {
        SchedulerMetaData metaData = scheduleJobService.getMetaData();
        return metaData;
    }

    @RequestMapping("/getAllJobs")
    public Object getAllJobs() throws SchedulerException {
        List<ScheduleJob> jobList = scheduleJobService.getAllJobList();
        return jobList;
    }

    @RequestMapping("/getRunningJobs")
    public Object getRunningJobs() throws SchedulerException {
        List<ScheduleJob> jobList = scheduleJobService.getRunningJobList();
        return jobList;
    }

    @RequestMapping(value = "/pauseJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Object pauseJob(ScheduleJob job) {
        log.info("params, job = {}", job);
        Result result = Result.failure();
        try {
            scheduleJobService.pauseJob(job);
            result = Result.success();
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            log.error("pauseJob ex:", e);
        }
        return result;
    }

    @RequestMapping(value = "/resumeJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Object resumeJob(ScheduleJob job) {
        log.info("params, job = {}", job);
        Result result = Result.failure();
        try {
            scheduleJobService.resumeJob(job);
            result = Result.success();
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            log.error("resumeJob ex:", e);
        }
        return result;
    }


    @RequestMapping(value = "/deleteJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteJob(ScheduleJob job) {
        log.info("params, job = {}", job);
        Result result = Result.failure();
        try {
            scheduleJobService.deleteJob(job);
            result = Result.success();
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            log.error("deleteJob ex:", e);
        }
        return result;
    }

    @RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
    public Object runJob(ScheduleJob job) {
        log.info("params, job = {}", job);
        Result result = Result.failure();
        try {
            scheduleJobService.runJobOnce(job);
            result = Result.success();
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            log.error("runJob ex:", e);
        }
        return result;
    }


    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public Object saveOrupdate(ScheduleJob job, @RequestPart("classFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("params, job = {}", job);
        Result result = Result.failure();
        try {
            if(!file.isEmpty()){
                job.setClassCode(file.getBytes());
            }
            scheduleJobService.saveOrupdate(job);
            result = Result.success();
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            log.error("updateCron ex:", e);
        }
        response.sendRedirect("/index");
        return result;
    }
}
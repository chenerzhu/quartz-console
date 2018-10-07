package com.chenerzhu.quartz.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ToString
public class ScheduleJob implements Serializable,Comparable{

	private static final long serialVersionUID = 1L;

    private String jobId;

    private String jobType="java";
    
    private String jobName;
    
    private String jobGroup;
    
    private String jobStatus;
    
    private String cronExpression;

    private String desc;

    private String content;

    private byte[] classCode;

    @Override
    public int compareTo(Object obj) {
        ScheduleJob scheduleJob=(ScheduleJob)obj;
        return this.getJobName().compareTo(scheduleJob.getJobName());
    }
}

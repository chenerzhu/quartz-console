package com.chenerzhu.quartz.service.impl;

import com.chenerzhu.quartz.classLoader.MyClassLoader;
import com.chenerzhu.quartz.entity.ScheduleJob;
import com.chenerzhu.quartz.service.IDynamicJobService;
import javassist.ClassPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenerzhu
 * @create 2018-10-02 0:43
 **/
@Slf4j
@Service
public class DynamicJobService implements IDynamicJobService {
    private MyClassLoader myClassLoader=new MyClassLoader();
    private static transient ExecutorService executorService= Executors.newFixedThreadPool(3);
    @Override
    public void dynamicJob(ScheduleJob scheduleJob){
        try{
            /*ClassPool cp = ClassPool.getDefault();
            log.info("do something javassistjob...");
            String className=scheduleJob.getContent();
            byte[] classCode=scheduleJob.getClassCode();
            CtClass cc=null;
            try {
                cc= cp.get(className);
            } catch (NotFoundException e) {
                cp.makeClass(new ByteArrayInputStream(classCode));
                cc= cp.get(className);
            }
            System.out.println(cc.toClass().newInstance());*/
            String className=scheduleJob.getContent();
            byte[] classCode=scheduleJob.getClassCode();

            myClassLoader.addClass(className,classCode);
            Runnable job=(Runnable) myClassLoader.loadClass(className).newInstance();
            executorService.submit(job);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
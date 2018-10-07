package com.chenerzhu.quartz.service.impl;

import com.chenerzhu.quartz.service.IJavaJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chenerzhu
 * @create 2018-10-02 0:42
 **/
@Slf4j
@Service
public class JavaJobService implements IJavaJobService {
    @Override
    public void doSomething(){
      log.info("do something javajob...");
    }
}
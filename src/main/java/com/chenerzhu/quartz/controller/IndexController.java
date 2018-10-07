package com.chenerzhu.quartz.controller;

import com.chenerzhu.quartz.entity.ScheduleJob;
import com.chenerzhu.quartz.service.IScheduleJobService;
import com.chenerzhu.quartz.service.impl.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class IndexController {

	@Autowired
	private IScheduleJobService scheduleJobService;
	
	@RequestMapping("/index")
	public String index(Model model){
		List<ScheduleJob> jobList = scheduleJobService.getAllJobList();
		model.addAttribute("jobs", jobList);
		return "index";
	}
	
}
